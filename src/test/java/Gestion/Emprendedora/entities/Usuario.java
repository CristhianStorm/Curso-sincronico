package Gestion.Emprendedora.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "USUARIOS")
@Data
public class Usuario {

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

    @Column(name = "ID_TUTOR_ASOCIADO")
    private Long idTutorAsociado;
}

