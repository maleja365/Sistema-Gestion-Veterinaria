package com.veterinaria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI veterinariaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Sistema de Gestión de Mascotas - VetCare")
                .version("1.0")
                .description("""
                        Roles: ADMIN, VETERINARIO y PROPIETARIO.

                        Para probar: ejecuta primero POST /api/auth/login (por ejemplo con
                        admin@veterinaria.com / Admin123*). La sesión queda guardada en el
                        navegador y desde ese momento puedes probar los demás endpoints
                        según el rol del usuario."""));
    }
}
