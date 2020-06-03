package com.example.algamoney.api.reposiory;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.reposiory.lancamentos.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

	Lancamento save(Pessoa pessoa);
	
	public List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate date);

}
