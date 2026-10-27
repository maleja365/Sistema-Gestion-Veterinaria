
package com.veterinaria.service;

import com.veterinaria.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {

        var usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                        "Usuario no encontrado"));

        return User.withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .build();
    }
}
