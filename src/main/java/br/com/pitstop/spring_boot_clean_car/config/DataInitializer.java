package br.com.pitstop.spring_boot_clean_car.config;

import br.com.pitstop.spring_boot_clean_car.entity.Usuario;
import br.com.pitstop.spring_boot_clean_car.enums.Role;
import br.com.pitstop.spring_boot_clean_car.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
  Cria o usuario ADMIN inicial automaticamente na primeira execucao,
  para que sempre exista alguem com permissao de gerenciar as contas
  de funcionario (ver SecurityConfig: /api/usuarios/** e somente ADMIN).
*/
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.nome}")
    private String adminNome;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByEmail(adminEmail)) {
            return;
        }

        Usuario admin = Usuario.builder()
                .nome(adminNome)
                .email(adminEmail)
                .senha(passwordEncoder.encode(adminSenha))
                .role(Role.ADMIN)
                .build();

        usuarioRepository.save(admin);
    }
}
