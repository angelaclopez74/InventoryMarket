package com.InventoryManagmentUniandes.InventoryManagmentUniandes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/hola")
    public String hola() {
        return "Hola desde Spring Boot!";
    }
}