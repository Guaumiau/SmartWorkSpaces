package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import pe.isil.smartworkspaces.model.Sala;
import pe.isil.smartworkspaces.repository.SalaRepository;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    // ----------------------------------------------------------------------
    // 1. LISTAR Y BUSCAR (Index)
    // ----------------------------------------------------------------------
    @GetMapping("")
    String show(Model model,
                @PageableDefault(size = 5, sort = "nombres") Pageable pageable,
                @RequestParam(required = false) String nombres) {

        Page<Sala> salas;

        if (nombres != null && !nombres.trim().isEmpty()) {
            // Búsqueda por nombre (filtrado)
            salas = salaRepository.findByNombresContaining(nombres, pageable);
        } else {
            // Listado general paginado
            salas = salaRepository.findAll(pageable);
        }

        model.addAttribute("salas", salas);
        return "salas/show";
    }

    // ----------------------------------------------------------------------
    // 2. CREAR (Formulario)
    // ----------------------------------------------------------------------
    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("sala", new Sala());
        model.addAttribute("estados", Estado.values()); // Enviar lista de ENUM para el select
        return "salas/create";
    }

    // ----------------------------------------------------------------------
    // 3. GUARDAR (Persistencia Create)
    // ----------------------------------------------------------------------
    @PostMapping("/save")
    public String save(Model model, @Validated Sala sala, BindingResult bindingResult, RedirectAttributes ra) throws IOException {

        // A. Validación de Duplicados (Nombre único)
        // Corregido: Primero verificamos si salaExistente NO es null
        Sala salaExistente = salaRepository.findByNombresIgnoreCase(sala.getNombres());

        if (salaExistente != null) {
            // Si existe, y estamos creando (o el ID es diferente), es duplicado
            bindingResult.rejectValue("nombres", "NombreDuplicado", "Ya existe una sala con ese nombre.");
        }

        // B. Manejo de Errores de Validación
        if (bindingResult.hasErrors()) {
            model.addAttribute("sala", sala);
            model.addAttribute("estados", Estado.values()); // Re-enviar enums si falla
            return "salas/create";
        }

        // C. Guardado Exitoso
        salaRepository.save(sala);
        ra.addFlashAttribute("msgExito", "Sala registrada exitosamente!");

        return "redirect:/salas";
    }

    // ----------------------------------------------------------------------
    // 4. EDITAR (Cargar Formulario)
    // ----------------------------------------------------------------------
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes ra){
        Optional<Sala> optionalSala = salaRepository.findById(id);

        if(optionalSala.isEmpty()){
            ra.addFlashAttribute("msgError", "La sala no existe.");
            return "redirect:/salas";
        }

        Sala sala = optionalSala.get();
        model.addAttribute("sala", sala);
        model.addAttribute("estados", Estado.values()); // Enviar lista de ENUM

        return "salas/edit";
    }

    // ----------------------------------------------------------------------
    // 5. ACTUALIZAR (Persistencia Update)
    // ----------------------------------------------------------------------
    @PostMapping("/update")
    public String update(@Validated Sala sala, BindingResult bindingResult, Model model, RedirectAttributes ra) throws IOException {

        // A. Validación de Duplicados
        Sala salaExistente = salaRepository.findByNombresIgnoreCase(sala.getNombres());

        // Corregido: Solo verificamos el ID si encontramos una sala con ese nombre
        if (salaExistente != null) {
            // Si el ID de la sala encontrada es DIFERENTE al que estamos editando,
            // significa que el nombre pertenece a OTRA sala -> Error.
            if (!sala.getId().equals(salaExistente.getId())) {
                bindingResult.rejectValue("nombres", "NombreDuplicado", "Ya existe otra sala con ese nombre.");
            }
        }

        // B. Manejo de Errores
        if (bindingResult.hasErrors()) {
            model.addAttribute("sala", sala);
            model.addAttribute("estados", Estado.values());
            return "salas/edit";
        }

        // C. Actualización Exitosa
        salaRepository.save(sala);
        ra.addFlashAttribute("msgExito", "Sala actualizada exitosamente!");

        return "redirect:/salas";
    }

    // ----------------------------------------------------------------------
    // 6. ELIMINAR (Delete)
    // ----------------------------------------------------------------------
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra){

        Optional<Sala> optionalSala = salaRepository.findById(id);

        if(optionalSala.isEmpty()){
            ra.addFlashAttribute("msgError", "Error: La sala a eliminar no fue encontrada.");
            return "redirect:/salas";
        }

        try {
            // Intenta eliminar
            salaRepository.deleteById(id);
            ra.addFlashAttribute("msgExito", "Sala eliminada exitosamente!");

        } catch (DataIntegrityViolationException e) {
            // Captura error si la sala tiene reservas (FK constraint)
            ra.addFlashAttribute("msgError",
                    "No se puede eliminar la sala porque tiene reservas asociadas. Elimine las reservas primero.");

        } catch (Exception e) {
            // Captura otros errores
            ra.addFlashAttribute("msgError", "Ocurrió un error inesperado al intentar eliminar.");
        }

        return "redirect:/salas";
    }
}