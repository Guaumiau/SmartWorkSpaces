package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale")
    private Integer id;

    @NotBlank
    @Column(name = "nombre")
    private String nombres;

    @NotNull
    @Positive
    @Min(1)
    private Integer capacidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

}
