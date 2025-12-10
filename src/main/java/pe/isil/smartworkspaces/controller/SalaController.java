package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String save(Model model, @Validated  Sala sala, BindingResult bindingResult) throws IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("sala", sala);
            model.addAttribute("estados", Estado.values());
            return "salas/create";
        }
        salaRepository.save(sala);
        return "redirect:/salas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Sala> optionalSala = salaRepository.findById(id);
        if(optionalSala.isEmpty()){
            return "redirect:/salas";
        }


        Sala sala = optionalSala.get();
        model.addAttribute("sala", sala);
        model.addAttribute("estados", Estado.values());
        return "salas/edit";
    }

    @PostMapping("/update")
    public String update(@Validated Sala sala, BindingResult bindingResult, Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("sala", sala);
            model.addAttribute("estados", Estado.values());
            return "salas/edit";
        }

        salaRepository.save(sala);
        return "redirect:/salas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Optional<Sala> optionalSala = salaRepository.findById(id);
        if(optionalSala.isEmpty()){
            return "redirect:/salas";
        }

        Sala sala = optionalSala.get();

//        if(sala.getReservas() != null && !sala.getReservas().isEmpty()){
//            redirectAttributes.addFlashAttribute("error", "No se puede eliminar una sala con reservas asociadas");
//            return "redirect:/salas";
//        }

        salaRepository.deleteById(id);
        return "redirect:/salas";
    }
}
