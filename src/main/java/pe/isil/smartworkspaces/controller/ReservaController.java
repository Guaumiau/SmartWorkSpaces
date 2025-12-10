package pe.isil.smartworkspaces.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.smartworkspaces.model.Estado;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.ReservaRepository;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private SalaRepository salaRepository;

    // --- LISTAR ---
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
        return "reservas/show"; // CORREGIDO: antes reservations/show
    }

    // --- FORMULARIO NUEVO ---
    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("reserva", new Reserva());
        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas",salasDisponibles);

        return "reservas/create"; // CORREGIDO: antes reservations/create
    }

    // --- GUARDAR (SAVE) ---
    @PostMapping("/save")
    public String save(@Validated Reserva reserva,
                       BindingResult bindingResult,
                       @RequestParam(name = "sala", required = false) Integer idSala,
                       Model model,
                       RedirectAttributes ra) {

        // Validaciones de Negocio
        if (reserva.getFecha() != null && reserva.getHoraInicio() != null &&
                reserva.getFecha().isEqual(LocalDate.now()) &&
                reserva.getHoraInicio().isBefore(LocalTime.now())) {
            bindingResult.rejectValue("horaInicio", "HoraInvalida", "La hora de inicio no puede ser en el pasado.");
        }
        if (reserva.getHoraInicio() != null && reserva.getHoraFin() != null &&
                !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            bindingResult.rejectValue("horaFin", "HoraInvalida", "La hora final debe ser posterior a la inicial.");
        }

        if (idSala != null) {
            Sala salaSeleccionada = salaRepository.findById(idSala).orElse(null);
            if (salaSeleccionada != null) {
                if (salaSeleccionada.getEstado() == Estado.INACTIVA) {
                    bindingResult.rejectValue("sala", "SalaInactiva", "No se puede reservar una sala inactiva.");
                } else {
                    reserva.setSala(salaSeleccionada);
                }
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("salas", salaRepository.findAll());
            return "reservas/create"; // CORREGIDO: antes reservations/create
        }

        reservaRepository.save(reserva);
        ra.addFlashAttribute("msgExito", "¡Reserva registrada exitosamente!");

        return "redirect:/reservas";
    }

    // --- FORMULARIO EDITAR ---
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes ra){
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);
        if (optionalReserva.isEmpty()) {
            ra.addFlashAttribute("msgError", "La reserva no existe.");
            return "redirect:/reservas";
        }

        Reserva reserva = optionalReserva.get();

        List<Sala> salasDisponibles = salaRepository.findAll();
        model.addAttribute("salas", salasDisponibles);
        model.addAttribute("reserva", reserva);

        return "reservas/edit"; // CORREGIDO: antes reservations/edit
    }

    // --- ACTUALIZAR (UPDATE) ---
    @PostMapping("/update")
    public String update(@Validated Reserva reserva, BindingResult bindingResult,
                         @RequestParam(name = "sala", required = false) Integer idSala,
                         Model model,
                         RedirectAttributes ra){

        if (reserva.getFecha() != null && reserva.getHoraInicio() != null &&
                reserva.getFecha().isEqual(LocalDate.now()) &&
                reserva.getHoraInicio().isBefore(LocalTime.now())) {
            bindingResult.rejectValue("horaInicio", "HoraInvalida", "La hora de inicio no puede ser en el pasado.");
        }
        if (reserva.getHoraInicio() != null && reserva.getHoraFin() != null &&
                !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            bindingResult.rejectValue("horaFin", "HoraInvalida", "La hora final debe ser posterior a la inicial.");
        }
        if (idSala != null) {
            Sala salaSeleccionada = salaRepository.findById(idSala).orElse(null);
            if (salaSeleccionada != null) {
                if (salaSeleccionada.getEstado() == Estado.INACTIVA) {
                    bindingResult.rejectValue("sala", "SalaInactiva", "No se puede reservar una sala inactiva.");
                } else {
                    reserva.setSala(salaSeleccionada);
                }
            }
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("salas", salaRepository.findAll());
            return "reservas/edit"; // CORREGIDO: antes reservations/edit
        }

        reservaRepository.save(reserva);
        ra.addFlashAttribute("msgExito", "¡Reserva actualizada exitosamente!");

        return "redirect:/reservas";
    }

    // --- ELIMINAR ---
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra){
        try {
            reservaRepository.deleteById(id);
            ra.addFlashAttribute("msgExito", "Reserva eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("msgError", "No se pudo eliminar la reserva.");
        }
        return "redirect:/reservas";
    }
}