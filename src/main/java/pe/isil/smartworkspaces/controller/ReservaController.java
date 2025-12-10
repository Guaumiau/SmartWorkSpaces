package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.time.LocalDate;
import org.springframework.validation.BindingResult;
import pe.isil.smartworkspaces.model.Estado;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.ReservaRepository;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.io.IOException;
import java.time.LocalTime;
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
    String show(Model model,
                @PageableDefault(size = 5, sort = "usuario") Pageable pageable,
                @RequestParam(required = false) String usuario) {
        
        Page<Reserva> reservas;

        if (usuario != null && !usuario.trim().isEmpty()) {
            reservas = reservaRepository.findByUsuarioContaining(usuario, pageable);
        } else {
            reservas = reservaRepository.findAll(pageable);
        }

        model.addAttribute("reservas", reservas);
        return "reservas/show";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("reserva", new Reserva());
        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas",salasDisponibles);

        return "reservas/create";
    }

    @PostMapping("/save")
    public String save(@Validated Reserva reserva,
                       BindingResult bindingResult,
                       Model model) {

        if (reserva.getFecha() != null && reserva.getHoraInicio() != null &&
                reserva.getFecha().isEqual(LocalDate.now()) &&
                reserva.getHoraInicio().isBefore(LocalTime.now())) {

            bindingResult.rejectValue("horaInicio", "HoraInvalida");
        }
        if (reserva.getHoraInicio() != null && reserva.getHoraFin() != null &&
                !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {

            bindingResult.rejectValue("horaFin", "HoraInvalida");
        }
        Sala salaSeleccionada=salaRepository.findById(reserva.getSala());
        if (salaSeleccionada.getEstado() == Estado.INACTIVA) {
            bindingResult.rejectValue("sala", "SalaInactiva");
        }

        // Si hay errores, regresar al formulario
        if (bindingResult.hasErrors()) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("salas", salaRepository.findAll());

            return "reservas/create";
        }

        reservaRepository.save(reserva);
        return "redirect:/reservas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);
        if (optionalReserva.isEmpty()) {
            return "redirect:/reservas/edit";
        }

        Reserva reserva = optionalReserva.get();

        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas", salasDisponibles);

        model.addAttribute("reserva", reserva);

        return "reservas/edit";
    }

    @PostMapping("/update")
    public String update(@Validated Reserva reserva, BindingResult bindingResult, Model model){


        if (reserva.getFecha() != null && reserva.getHoraInicio() != null &&
                reserva.getFecha().isEqual(LocalDate.now()) &&
                reserva.getHoraInicio().isBefore(LocalTime.now())) {

            bindingResult.rejectValue("horaInicio", "HoraInvalida");
        }
        if (reserva.getHoraInicio() != null && reserva.getHoraFin() != null &&
                !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {

            bindingResult.rejectValue("horaFin", "HoraInvalida");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("salas", salaRepository.findAll());
            return "reservas/edit";
        }


        reservaRepository.save(reserva);
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        reservaRepository.deleteById(id);
        return "redirect:/reservas";
    }
}
