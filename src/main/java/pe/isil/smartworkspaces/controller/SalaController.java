package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.isil.smartworkspaces.model.Estado;
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
    String show(Model model,
                @PageableDefault(size = 5, sort = "nombres") Pageable pageable,
                @RequestParam(required = false) String nombres) {
        
        Page<Sala> salas;

        if (nombres != null && !nombres.trim().isEmpty()) {
            salas = salaRepository.findByNombresContaining(nombres, pageable);
        } else {
            salas = salaRepository.findAll(pageable);
        }

        model.addAttribute("salas", salas);
        return "salas/show";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("sala", new Sala());
        model.addAttribute("estados", Estado.values());
        return "salas/create";
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

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        salaRepository.deleteById(id);
        return "redirect:/salas";
    }
}
