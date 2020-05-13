package com.example.algamoney.api.reposiory.lancamentos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.reposiory.dto.LancamentoDTO;
import com.example.algamoney.api.reposiory.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

	public Page<LancamentoDTO> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

}
