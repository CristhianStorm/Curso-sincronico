package com.gestion.emprendedora.services;

import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Este servicio es utilizado por Spring Security para cargar los detalles de un usuario
 * desde la base de datos durante el proceso de autenticación.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Busca un usuario por su email (que Spring Security trata como "username").
     * @param email el email del usuario que intenta iniciar sesión.
     * @return un objeto UserDetails (nuestra propia clase Usuario) con la información del usuario.
     * @throws UsernameNotFoundException si no se encuentra ningún usuario con ese email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // Busca el usuario en la base de datos de Oracle usando nuestro repositorio.
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró un usuario con el email: " + email));

        // ¡Este es el cambio clave que soluciona el problema!
        // Devolvemos nuestro objeto 'usuario' directamente.
        // Como nuestra clase 'Usuario' ahora implementa UserDetails,
        // Spring Security llamará a su método isEnabled() para verificar el estado de la cuenta.
        return usuario;
    }
}

