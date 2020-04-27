package com.example.algamoney.api.reposiory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Pessoa;



public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	void save(Optional<Pessoa> pessoaSalva);

}
