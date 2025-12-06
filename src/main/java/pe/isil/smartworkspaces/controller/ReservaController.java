package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.ReservaRepository;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping("")
    String show(Model model){
        model.addAttribute("reservas", reservaRepository.findAll());
        return "reservations/show";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("reserva", new Reserva());
        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas",salasDisponibles);

        return "reservations/create";
    }

    @PostMapping("/save")
    public String save(Reserva reserva, @RequestParam("sala") Integer idSala) {
        Sala salaSeleccionada = new Sala();
        salaSeleccionada.setId(idSala);

        reserva.setSala(salaSeleccionada);

        reservaRepository.save(reserva);
        return "redirect:/reservas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){

        Optional<Reserva> optionalReserva = reservaRepository.findById(id);

        if (optionalReserva.isEmpty()) {
            return "redirect:/reservas/show";
        }

        Reserva reserva = optionalReserva.get();

        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas", salasDisponibles);

        model.addAttribute("reserva", reserva);

        return "reservations/edit";
    }

    @PostMapping("/update")
    public String update(Reserva reserva) throws IOException {

        reservaRepository.save(reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        sdasdsad
        reservaRepository.deleteById(id);
        return "redirect:/reservas";
    }
}
