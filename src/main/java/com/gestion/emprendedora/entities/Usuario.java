package com.gestion.emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Entidad que representa la tabla USUARIOS en la base de datos.
 * Implementa UserDetails para integrarse directamente con Spring Security.
 */
@Entity
@Table(name = "USUARIOS")
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIOS_SEQ")
    @SequenceGenerator(name = "USUARIOS_SEQ", sequenceName = "USUARIOS_SEQ", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOMBRE_COMPLETO", nullable = false)
    private String nombreCompleto;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String password;

    @Column(name = "EDAD")
    private Integer edad;

    @Column(name = "CEDULA")
    private String cedula;

    @Column(name = "ROL", nullable = false)
    private String rol;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REGISTRO", updatable = false)
    @ColumnDefault("SYSDATE")
    private Date fechaRegistro;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "TOKEN_VERIFICACION")
    private String tokenVerificacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_EXPIRACION_TOKEN")
    private Date fechaExpiracionToken;
    
    @Column(name = "PUNTOS")
    private Integer puntos = 0;

    // --- MÉTODOS REQUERIDOS POR LA INTERFAZ UserDetails DE SPRING SECURITY ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rol.toUpperCase()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado. En nuestro flujo, permitimos el login
     * incluso si la cuenta no está "activa" para poder redirigirlo a la página correcta
     * (ej: "esperando verificación" o "pendiente de pago"). La lógica de bloqueo real
     * la maneja el frontend basándose en el campo 'estado'.
     */
    @Override
    public boolean isEnabled() {
        return true; 
    }
}

