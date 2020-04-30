package com.example.algamoney.api.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.reposiory.UsuarioRepository;

@Service()
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
		Usuario usuario = optionalUsuario.orElseThrow(() -> {
			throw new UsernameNotFoundException("Usuário e/ou senha incorretos");
		});
		return new User(email, usuario.getSenha(), getPermissoes(usuario));
	}

	private Set<SimpleGrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(p -> authorities.
				add(new SimpleGrantedAuthority(
						p.getDescricao().toUpperCase())));
		return authorities;
	}
}
