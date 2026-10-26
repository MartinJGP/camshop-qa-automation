package com.ventacamaras.camaras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> root() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>CamShop API Portal</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h1>CamShop E-Commerce API</h1>\n" +
                "  <p>Portal de servicios de videovigilancia y comercio electronico.</p>\n" +
                "  <form action=\"/auth/login\" method=\"POST\">\n" +
                "    <label>Usuario: <input type=\"text\" name=\"username\"></label><br>\n" +
                "    <label>Clave: <input type=\"password\" name=\"password\"></label><br>\n" +
                "    <button type=\"submit\">Iniciar Sesion</button>\n" +
                "  </form>\n" +
                "  <ul>\n" +
                "    <li><a href=\"/api/camaras\">Ver Catalogo de Camaras</a></li>\n" +
                "    <li><a href=\"/actuator/health\">Monitoreo de Infraestructura Actuator</a></li>\n" +
                "  </ul>\n" +
                "</body>\n" +
                "</html>";
        return ResponseEntity.ok(html);
    }
}
