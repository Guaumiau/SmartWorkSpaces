package pe.isil.smartworkspaces.service;

import pe.isil.smartworkspaces.model.Reserva;

import java.util.List;
import java.util.Optional;

public interface ReservaService {
    public Reserva save(Reserva reserva);
    public Optional<Reserva> get(Integer id);
    public void update(Reserva reserva);
    public void delete(Integer id);
    public List<Reserva> findAll();
}
