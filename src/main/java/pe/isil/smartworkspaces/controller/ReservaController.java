package pe.isil.smartworkspaces.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.ReservaRepository;
import pe.isil.smartworkspaces.service.ReservaService;
import pe.isil.smartworkspaces.service.SalaService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private SalaService salaService;

    @GetMapping("")
    String show(Model model){
        model.addAttribute("reservas", reservaService.findAll());
        return "reservations/show";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("reserva", new Reserva());
        List<Sala> salasDisponibles = salaService.findAll();
        model.addAttribute("salas",salasDisponibles);

        return "reservations/create";
    }

    @PostMapping("/save")
    public String save(Reserva reserva) {
        reservaService.save(reserva);
        return "redirect:/reservas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){

        Optional<Reserva> optionalReserva = reservaService.get(id);

        if (optionalReserva.isEmpty()) {

            LOGGER.warn("Intento de edición fallido: No se encontró la Reserva con ID {}", id);
            return "redirect:/reservas/show";
        }

        Reserva reserva = optionalReserva.get();

        List<Sala> salasDisponibles = salaService.findAll();
        model.addAttribute("salas", salasDisponibles);

        model.addAttribute("reserva", reserva);

        LOGGER.info("Reserva encontrada para edición: {}", reserva);

        return "reservations/edit";
    }

    @PostMapping("/update")
    public String update(Reserva reserva) throws IOException {

        reservaService.update(reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        reservaService.delete(id);
        return "redirect:/reservas";
    }
}
