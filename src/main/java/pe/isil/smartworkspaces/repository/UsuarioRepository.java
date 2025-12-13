package pe.isil.smartworkspaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.smartworkspaces.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar un usuario por email
    Optional<Usuario> findByEmail(String email);

    // Retorna true si existe el email
    Boolean existsByEmail(String email);

}