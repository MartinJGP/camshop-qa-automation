package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.dto.DashboardResponse;
import com.ventacamaras.camaras.service.ReporteService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService service;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResponse> resumen() { return ResponseEntity.ok(service.resumen()); }
}

