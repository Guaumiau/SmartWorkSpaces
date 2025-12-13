package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @Column(name = "nom_completo")
    private String nombreCompleto;

    @NotEmpty
    @Email
    @Column(unique = true) // Agregamos esto para asegurar que el email sea único desde la BD
    private String email;

    // Esta es la contraseña que se guardará ENCRIPTADA en la base de datos
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @NotBlank
    @Transient
    private String password1;

    @NotBlank
    @Transient
    private String password2;

    @PrePersist // Se ejecuta antes de crear
    @PreUpdate  // Se ejecuta antes de editar
    void asignarNombreCompleto() {
        // Concatena automáticamente para llenar el campo 'nombreCompleto'
        this.nombreCompleto = this.nombres + " " + this.apellidos;
    }
}