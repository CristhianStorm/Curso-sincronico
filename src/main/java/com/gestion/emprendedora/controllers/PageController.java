package com.gestion.emprendedora.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * Mapea la raíz del sitio ("/") para que devuelva la página principal.
     * El "forward" le dice a Spring que busque el archivo dentro de sus recursos estáticos.
     * @return La ruta al archivo HTML principal.
     */
    @GetMapping("/")
    public String homePage() {
        return "forward:/pagina_principal.html";
    }
}
