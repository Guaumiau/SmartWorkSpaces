package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sala")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale")
    private Integer id;

    @Column(name = "nombre")
    private String nombres;

    @Column(name = "capacidad")
    private int capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;
}
