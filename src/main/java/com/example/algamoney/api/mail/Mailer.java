package com.example.algamoney.api.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-lancamentos-vencidos";
//		
//		List<Lancamento> lista = repo.findAll();
//		
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("lancamentos", lista);
//		
//		this.enviarEmail("testemayconprata@gmail.com", 
//				Arrays.asList("mayconprata90@gmail.com"), 
//				"Testando", 
//				template, variaveis);
//		System.out.println("Terminado o envio de e-mail....");
//	}
	
	public void avisarSobreLancamentosVencidos(
			List<Lancamento> vencidos, List<Usuario> destinatarios) {
		String template = "mail/aviso-lancamentos-vencidos";
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", vencidos);
		
		List<String> emails = destinatarios.stream()
				.map(u -> u.getEmail())
				.collect(Collectors.toList());
		
		this.enviarEmail("testemayconprata@gmail.com", 
				emails, 
				"Lançamentos Vencidos", 
				template, 
				variaveis);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, 
			String template, Map<String, Object> variaveis ) {
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> {
			context.setVariable(e.getKey(), e.getValue());
		});
		
		String mensagem = thymeleaf.process(template, context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
		
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, 
			String messagem ) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray( new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(messagem, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}
	
}
