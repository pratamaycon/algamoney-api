package com.example.algamoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.mail.Mailer;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.reposiory.LancamentoRepository;
import com.example.algamoney.api.reposiory.PessoaRepository;
import com.example.algamoney.api.reposiory.UsuarioRepository;
import com.example.algamoney.api.service.exceptions.PessoaInexistenteOuInativoException;
import com.example.algamoney.api.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;
	
	@Autowired
	private S3 s3;

	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("\n\n\nPreparando envio de " + 
					"e-mails de aviso de lançamentos vencidos.\n\n\n");
		}

		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (vencidos.isEmpty()) {
			logger.info("\n\n\nSem lançamentos vencidos para aviso.\n\n\n");

			return;
		}

		logger.info("\n\n\nExitem {} lançamentos vencidos.\n\n\n", vencidos.size());

		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

		if (destinatarios.isEmpty()) {
			logger.warn("\n\n\nExistem lançamentos vencidos, mas o " + 
					"sistema não encontrou destinatários.\n\n\n");

			return;
		}

		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

		logger.info("\n\n\nEnvio de e-mail de aviso concluído.\n\n\n");
	}

	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public Lancamento salvar(Lancamento lancamento) {
		validarPessoa(lancamento);
		
		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}

		return lancamentoRepository.save(lancamento);
	}

	private Optional<Pessoa> verificaSeENull(Lancamento lancamento) {
		Optional<Pessoa> pessoaOptional = Optional
				.ofNullable(pessoaRepository.findById(lancamento.getPessoa().getCodigo()).map(Optional::of)
						.orElseThrow(() -> new PessoaInexistenteOuInativoException()))
				.get();
		return pessoaOptional;
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = bucarPorLancamentoExistente(codigo);

		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		if (StringUtils.isEmpty(lancamento.getAnexo())
				&& StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else if (StringUtils.hasText(lancamento.getAnexo())
				&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		Optional<Pessoa> pessoaOptional = verificaSeENull(lancamento);

		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaOptional.get();
		}

		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}

	}

	private Lancamento bucarPorLancamentoExistente(Long codigo) {
		Optional<Lancamento> lancamentoOptional = lancamentoRepository.findById(codigo);

		if (!lancamentoOptional.isPresent()) {
			throw new IllegalArgumentException();
		}

		Lancamento lancamento = lancamentoOptional.get();

		return lancamento;

	}

}
