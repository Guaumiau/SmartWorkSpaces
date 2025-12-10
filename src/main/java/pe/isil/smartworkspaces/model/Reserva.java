package pe.isil.smartworkspaces.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id;

    @NotBlank
    @Column(name = "usuario")
    private String usuario;

    @NotNull
    @FutureOrPresent
    @Column(name = "fecha", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull
    @Column(name = "horaInicio", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "horaFin", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_sale", referencedColumnName = "id_sale")
    private Sala sala;
}
