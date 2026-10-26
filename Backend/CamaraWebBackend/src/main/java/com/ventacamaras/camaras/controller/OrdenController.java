package com.ventacamaras.camaras.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ventacamaras.camaras.service.OrdenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ordenes")
public class OrdenController {


    private final OrdenService ordenService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            Authentication auth,
            @RequestParam String metodo){

        String username = auth.getName();

        return ResponseEntity.ok(
            ordenService.checkout(
                username,
                metodo
            )
        );
    }

    @GetMapping("/mias")
    public ResponseEntity<?> listarMias(Authentication auth) {
        return ResponseEntity.ok(ordenService.listarDelUsuario(auth.getName()));
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable @Positive Integer id, @RequestParam String estado) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, estado));
    }
}

