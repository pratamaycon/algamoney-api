package com.example.algamoney.api.reposiory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	public List<Cidade> findByEstadoCodigo(Long codigo);
	
}
