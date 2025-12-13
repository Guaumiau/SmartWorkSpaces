package pe.isil.smartworkspaces.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    Page<Reserva> findByUsuarioContaining(String usuario, Pageable pageable);
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r " +
            "WHERE r.sala = :sala " +
            "AND r.fecha = :fecha " +
            "AND r.horaInicio < :horaFin " +
            "AND r.horaFin > :horaInicio")
    boolean existeCruceDeHorarios(@Param("sala") Sala sala,
                                  @Param("fecha") LocalDate fecha,
                                  @Param("horaInicio") LocalTime horaInicio,
                                  @Param("horaFin") LocalTime horaFin);
}
