package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.dto.UserResponse;
import com.ventacamaras.camaras.service.AdminService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.service.CamaraService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CamaraService camaraService;

    @GetMapping("/camaras")
    public ResponseEntity<List<Camara>> listarCamaras() {
        return ResponseEntity.ok(camaraService.listarTodasAdmin());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UserResponse>> listarUsuarios() {
        return ResponseEntity.ok(adminService.listarUsuarios());
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<UserResponse> cambiarEstado(@PathVariable @Positive Long id, @RequestParam boolean enabled) {
        return ResponseEntity.ok(adminService.cambiarEstado(id, enabled));
    }

    @PatchMapping("/usuarios/{id}/rol")
    public ResponseEntity<UserResponse> cambiarRol(@PathVariable @Positive Long id, @RequestParam String rol) {
        return ResponseEntity.ok(adminService.cambiarRol(id, rol));
    }
}

