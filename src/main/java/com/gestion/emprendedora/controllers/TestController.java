package com.gestion.emprendedora.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protegido")
    public String rutaProtegida() {
        return "¡Acceso concedido! Si ves este mensaje, es porque tu token JWT es válido.";
    }
}
