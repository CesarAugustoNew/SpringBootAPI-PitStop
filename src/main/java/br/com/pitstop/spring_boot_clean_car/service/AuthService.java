package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.request.LoginRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.LoginResponse;
import br.com.pitstop.spring_boot_clean_car.dto.response.UsuarioResponse;
import br.com.pitstop.spring_boot_clean_car.entity.Usuario;
import br.com.pitstop.spring_boot_clean_car.security.CustomUserDetails;
import br.com.pitstop.spring_boot_clean_car.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        String token = jwtService.gerarToken(userDetails);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole()
        );

        return new LoginResponse(token, usuarioResponse);
    }
}
