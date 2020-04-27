package com.example.algamoney.api.service;

import java.util.Optional;

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
		Optional<Pessoa> pessoaOptional = Optional.ofNullable( pessoaRepository.findById(lancamento.getPessoa().getCodigo())
				.  map(Optional::of).
                orElseThrow(() -> new PessoaInexistenteOuInativoException()))
		.get();
		
		Pessoa pessoa = pessoaOptional.get();
		
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

}
