package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.request.UsuarioRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.UsuarioResponse;
import br.com.pitstop.spring_boot_clean_car.entity.Usuario;
import br.com.pitstop.spring_boot_clean_car.exception.ResourceNotFoundException;
import br.com.pitstop.spring_boot_clean_car.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/*
  Gerencia as contas de funcionario/admin. Somente ADMIN acessa esses
  endpoints (ver SecurityConfig: /api/usuarios/** -> hasRole('ADMIN')).
*/
@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UsuarioResponse buscarPorId(UUID id) {
        return toResponse(buscarEntidadePorId(id));
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Ja existe um usuario cadastrado com esse e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(request.role())
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public void deletar(UUID id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarEntidadePorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
