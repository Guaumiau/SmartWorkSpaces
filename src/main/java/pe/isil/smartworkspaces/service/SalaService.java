package pe.isil.smartworkspaces.service;


import pe.isil.smartworkspaces.model.Sala;

import java.util.List;
import java.util.Optional;

public interface SalaService {
    public Sala save(Sala sala);
    public Optional<Sala> get(Integer id);
    public void update(Sala reserva);
    public void delete(Integer id);
    public List<Sala> findAll();
}
