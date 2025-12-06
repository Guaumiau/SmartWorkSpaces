package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.isil.smartworkspaces.model.Estado;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping("")
    String show(Model model){
        model.addAttribute("salas", salaRepository.findAll());
        return "salas/list";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("sala", new Sala());
        model.addAttribute("estados", Estado.values());
        return "salas/form";
    }

    @PostMapping("/save")
    public String save(Sala sala) throws IOException {
        salaRepository.save(sala);
        return "redirect:/salas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Sala sala = new Sala();
        Optional<Sala> optionalSala = salaRepository.findById(id);
        sala = optionalSala.get();

        model.addAttribute("estados", Estado.values());

        model.addAttribute("sala", sala);

        return "salas/edit";
    }

    @PostMapping("/update")
    public String update(Sala sala) throws IOException {
        salaRepository.save(sala);
        return "redirect:/salas";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        salaRepository.deleteById(id);
        return "redirect:/salas";
    }
}
