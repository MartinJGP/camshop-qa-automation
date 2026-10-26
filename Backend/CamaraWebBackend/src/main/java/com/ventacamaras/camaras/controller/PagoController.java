package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.model.Pago;
import com.ventacamaras.camaras.service.PagoService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    // GET /api/pagos → listar todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    // GET /api/pagos/{id} → obtener pago por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable @Positive Integer id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pagos/orden/{ordenId} → obtener pago de una orden
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<?> obtenerPorOrden(@PathVariable @Positive Integer ordenId) {
        return pagoService.obtenerPorOrden(ordenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pagos/buscar?estado=APROBADO&montoMinimo=100
    @GetMapping("/buscar")
    public ResponseEntity<List<Pago>> buscarPorEstadoYMonto(
            @RequestParam String estado,
            @RequestParam Double montoMinimo) {
        return ResponseEntity.ok(pagoService.buscarPorEstadoYMontoMinimo(estado, montoMinimo));
    }
}

