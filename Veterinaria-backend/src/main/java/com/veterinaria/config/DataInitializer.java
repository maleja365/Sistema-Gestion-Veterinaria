package com.veterinaria.config;

import com.veterinaria.model.Usuario;
import com.veterinaria.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    /** El administrador es el único usuario que no se registra por la API: se crea aquí. */
    @Bean
    CommandLineRunner crearAdministrador(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            String correo = "admin@veterinaria.com";

            if (!repository.existsByCorreo(correo)) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setCorreo(correo);
                admin.setPassword(encoder.encode("Admin123*"));
                admin.setRol("ADMIN");
                repository.save(admin);

                System.out.println("Administrador inicial creado correctamente.");
            }
        };
    }
}
