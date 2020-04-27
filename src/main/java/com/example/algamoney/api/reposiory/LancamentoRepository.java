package com.example.algamoney.api.reposiory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.reposiory.lancamentos.LancamentoRepositoryQuery;



public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

	Lancamento save(Pessoa pessoa);

}
