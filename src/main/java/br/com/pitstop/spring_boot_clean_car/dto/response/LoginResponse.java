package br.com.pitstop.spring_boot_clean_car.dto.response;

public record LoginResponse(
        String accessToken,
        UsuarioResponse usuario
) {
}
