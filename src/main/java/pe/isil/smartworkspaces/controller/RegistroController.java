package pe.isil.smartworkspaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.isil.smartworkspaces.model.Usuario;
import pe.isil.smartworkspaces.model.Rol;
import pe.isil.smartworkspaces.repository.UsuarioRepository;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Mostrar el formulario
    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Procesar el registro
    @PostMapping("/registro")
    public String registrar(@Validated Usuario usuario,
                            BindingResult bindingResult,
                            RedirectAttributes ra,
                            Model model) {

        // 1. Validar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            bindingResult.rejectValue("email", "EmailExists", "El correo electrónico ya está registrado.");
        }

        // 2. Validar si las contraseñas coinciden (usamos password1 y password2)
        if (!usuario.getPassword1().equals(usuario.getPassword2())) {
            bindingResult.rejectValue("password2", "PasswordMismatch", "Las contraseñas no coinciden.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        // 3. Registrar al usuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1())); // Encriptamos

        usuarioRepository.save(usuario);
        ra.addFlashAttribute("msgExito", "¡Registro exitoso! Ahora puedes iniciar sesión.");

        return "redirect:/login";
    }
}