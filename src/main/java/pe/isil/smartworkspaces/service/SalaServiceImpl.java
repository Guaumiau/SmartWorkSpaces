package pe.isil.smartworkspaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SalaServiceImpl implements SalaService{

    @Autowired
    private SalaRepository salaRepository;

    @Override
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    @Override
    public Optional<Sala> get(Integer id) {
        return salaRepository.findById(id);
    }

    @Override
    public void update(Sala sala) {
        salaRepository.save(sala);
    }

    @Override
    public void delete(Integer id) {
        salaRepository.deleteById(id);
    }

    @Override
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }
}
