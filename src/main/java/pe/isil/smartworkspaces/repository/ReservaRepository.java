package pe.isil.smartworkspaces.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.smartworkspaces.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    Page<Reserva> findByUsuarioContaining(String usuario, Pageable pageable);
}
