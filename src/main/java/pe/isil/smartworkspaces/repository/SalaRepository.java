package pe.isil.smartworkspaces.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.smartworkspaces.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
    boolean existsByNombresIgnoreCase(String nombres);
    Sala findByNombresIgnoreCase(String nombres);
    Page<Sala> findByNombresContaining(String nombres, Pageable pageable);
}
