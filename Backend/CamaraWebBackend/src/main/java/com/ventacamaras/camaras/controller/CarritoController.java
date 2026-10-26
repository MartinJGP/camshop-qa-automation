package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.model.Carrito;
import com.ventacamaras.camaras.service.CarritoService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;

    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregarAlCarrito(Authentication auth,
            @RequestParam @Positive Long camaraId, @RequestParam @Positive Integer cantidad) {
        return ResponseEntity.ok(carritoService.agregarItemAlCarrito(auth.getName(), camaraId, cantidad));
    }

    @GetMapping
    public ResponseEntity<Carrito> verCarrito(Authentication auth) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsername(auth.getName()));
    }

    @PostMapping("/item/{itemId}/incrementar")
    public ResponseEntity<Carrito> incrementarItem(Authentication auth, @PathVariable @Positive Long itemId) {
        return ResponseEntity.ok(carritoService.incrementarCantidadItem(auth.getName(), itemId));
    }

    @PostMapping("/item/{itemId}/decrementar")
    public ResponseEntity<Carrito> decrementarItem(Authentication auth, @PathVariable @Positive Long itemId) {
        return ResponseEntity.ok(carritoService.decrementarCantidadItem(auth.getName(), itemId));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Carrito> eliminarItem(Authentication auth, @PathVariable @Positive Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarItemDelCarrito(auth.getName(), itemId));
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciarCarrito(Authentication auth) {
        carritoService.vaciarCarrito(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
