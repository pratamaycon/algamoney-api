package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.reposiory.LancamentoRepository;
import com.example.algamoney.api.reposiory.PessoaRepository;
import com.example.algamoney.api.service.exceptions.PessoaInexistenteOuInativoException;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired 
	private LancamentoRepository lancamentoRepository; 
	
	
	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoaOptional = verificaSeENull(lancamento);
		
		Pessoa pessoa = pessoaOptional.get();
		
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

	private Optional<Pessoa> verificaSeENull(Lancamento lancamento) {
		Optional<Pessoa> pessoaOptional = Optional.ofNullable( pessoaRepository.findById(lancamento.getPessoa().getCodigo())
				.  map(Optional::of).
                orElseThrow(() -> new PessoaInexistenteOuInativoException()))
		.get();
		return pessoaOptional;
	}
	
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = bucarPorLancamentoExistente(codigo);
		
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
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
		
		if (!pessoaOptional.isPresent() == pessoa.isInativo()) {
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
