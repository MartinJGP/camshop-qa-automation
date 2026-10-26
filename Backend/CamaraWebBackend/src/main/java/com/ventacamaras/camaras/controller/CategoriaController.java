package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.model.Categoria;
import com.ventacamaras.camaras.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService service;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() { return ResponseEntity.ok(service.listar()); }

    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.guardar(null, categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable @Positive Long id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.guardar(id, categoria));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Categoria> estado(@PathVariable @Positive Long id, @RequestParam boolean activo) {
        return ResponseEntity.ok(service.cambiarEstado(id, activo));
    }
}

