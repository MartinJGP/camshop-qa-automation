package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.dto.CamaraRequest;
import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.service.CamaraService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/camaras")
@RequiredArgsConstructor
public class CamaraController {
    private final CamaraService service;

    @GetMapping public ResponseEntity<List<Camara>> listar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(defaultValue = "false") boolean soloStock) {
        return ResponseEntity.ok(service.filtrar(categoriaId, marca, precioMin, precioMax, soloStock));
    }
    @GetMapping("/{id}") public ResponseEntity<Camara> obtener(@PathVariable @Positive Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/marca/{marca}") public ResponseEntity<List<Camara>> marca(@PathVariable String marca) { return ResponseEntity.ok(service.buscarPorMarca(marca)); }
    @GetMapping("/buscar/nombre") public ResponseEntity<List<Camara>> nombre(@RequestParam String nombre) { return ResponseEntity.ok(service.buscarPorNombre(nombre)); }
    @GetMapping("/buscar/rango-precio") public ResponseEntity<List<Camara>> precio(@RequestParam Double min, @RequestParam Double max) { return ResponseEntity.ok(service.buscarPorRangoPrecio(min, max)); }
    @GetMapping("/con-stock") public ResponseEntity<List<Camara>> stock() { return ResponseEntity.ok(service.buscarConStock()); }

    @PostMapping public ResponseEntity<Camara> crear(@Valid @RequestBody CamaraRequest request) { return ResponseEntity.ok(service.crearCamara(request)); }
    @PutMapping("/{id}") public ResponseEntity<Camara> actualizar(@PathVariable @Positive Long id, @Valid @RequestBody CamaraRequest request) { return ResponseEntity.ok(service.actualizarCamara(id, request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Camara> baja(@PathVariable @Positive Long id) { return ResponseEntity.ok(service.cambiarEstado(id, false)); }
    @PatchMapping("/{id}/estado") public ResponseEntity<Camara> estado(@PathVariable @Positive Long id, @RequestParam boolean activo) { return ResponseEntity.ok(service.cambiarEstado(id, activo)); }
    @PatchMapping("/{id}/stock/entrada") public ResponseEntity<Camara> entrada(@PathVariable @Positive Long id, @RequestParam Integer cantidad) { return ResponseEntity.ok(service.registrarEntrada(id, cantidad)); }
    @PutMapping("/{id}/stock") public ResponseEntity<Camara> establecerStock(@PathVariable @Positive Long id, @RequestParam Integer cantidad) { return ResponseEntity.ok(service.establecerStock(id, cantidad)); }
}

