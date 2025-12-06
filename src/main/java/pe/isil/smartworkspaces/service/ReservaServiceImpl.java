package pe.isil.smartworkspaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.repository.ReservaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaServiceImpl implements ReservaService{

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public Optional<Reserva> get(Integer id) {
        return reservaRepository.findById(id);
    }

    @Override
    public void update(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    @Override
    public void delete(Integer id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }
}
