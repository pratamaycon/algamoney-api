package com.example.algamoney.api.reposiory.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import com.example.algamoney.api.AlgamoneyApiApplication;
import com.example.algamoney.api.storage.S3;
import com.example.algamoney.api.model.Lancamento;

public class LancamentoAnexoLsitener {

	@PostLoad
	public void postLoad(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3 s3 = AlgamoneyApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
		}
	}
}
