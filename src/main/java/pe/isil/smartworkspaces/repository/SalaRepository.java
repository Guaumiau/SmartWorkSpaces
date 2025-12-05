package pe.isil.smartworkspaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.smartworkspaces.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
}
