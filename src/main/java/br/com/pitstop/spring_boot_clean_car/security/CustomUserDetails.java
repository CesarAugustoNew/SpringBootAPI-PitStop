package br.com.pitstop.spring_boot_clean_car.security;

import br.com.pitstop.spring_boot_clean_car.entity.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/*
  Implementação própria de UserDetails (em vez de usar org.springframework
  .security.core.userdetails.User diretamente) para dar acesso fácil à
  entidade Usuario completa (id, nome, role) a partir do
  SecurityContext, sem precisar buscar o usuário de novo no banco em
  cada controller.
*/
@Getter
public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }
}
