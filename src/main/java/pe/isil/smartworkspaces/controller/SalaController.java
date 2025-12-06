package pe.isil.smartworkspaces.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.isil.smartworkspaces.model.Estado;
import pe.isil.smartworkspaces.model.Reserva;
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.service.SalaService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/salas")
public class SalaController {

    private final Logger LOGGER = LoggerFactory.getLogger(SalaController.class);

    @Autowired
    private SalaService salaService;

    @GetMapping("")
    String show(Model model){
        model.addAttribute("salas", salaService.findAll());
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
        salaService.save(sala);
        return "redirect:/salas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Sala sala = new Sala();
        Optional<Sala> optionalSala = salaService.get(id);
        sala = optionalSala.get();

        model.addAttribute("estados", Estado.values());

        LOGGER.info("Sala buscada: {}", sala);
        model.addAttribute("sala", sala);

        return "salas/edit";
    }

    @PostMapping("/update")
    public String update(Sala sala) throws IOException {
        salaService.update(sala);
        return "redirect:/salas";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){

        salaService.delete(id);
        return "redirect:/salas";
    }
}
