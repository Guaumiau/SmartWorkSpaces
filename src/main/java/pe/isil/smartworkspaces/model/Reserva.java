package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "horaInicio")
    private LocalDateTime horaInicio;

    @Column(name = "horaFin")
    private LocalDateTime horaFin;

    @ManyToOne
    @JoinColumn(name = "id_sale", referencedColumnName = "id_sale")
    private Sala sala;
}
