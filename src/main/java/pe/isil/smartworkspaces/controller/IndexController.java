package pe.isil.smartworkspaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @GetMapping("") //http://localhost:8080
    String index(Model model)
    {
        return "admin/home";
    }
}
