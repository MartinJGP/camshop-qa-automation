package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.dto.ResenaDTO;
import com.ventacamaras.camaras.model.Resena;
import com.ventacamaras.camaras.service.ResenaService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @PostMapping("/camara/{camaraId}")
    public ResponseEntity<Resena> crearResena(@PathVariable @Positive Long camaraId,
                                               @Valid @RequestBody ResenaDTO resenaDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = auth.getName();
        Resena resena = resenaService.crearResena(camaraId, emailUsuario,
                resenaDTO.getCalificacion(), resenaDTO.getComentario());
        return ResponseEntity.ok(resena);
    }

    @GetMapping("/camara/{camaraId}")
    public ResponseEntity<List<Resena>> listarPorCamara(@PathVariable @Positive Long camaraId) {
        List<Resena> resenas = resenaService.listarPorCamara(camaraId);
        return ResponseEntity.ok(resenas);
    }

    // GET: Obtener una reseña por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerPorId(@PathVariable @Positive Long id) {
        Optional<Resena> resena = resenaService.obtenerPorId(id);
        return resena.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/camara/{camaraId}/promedio")
    public ResponseEntity<Double> obtenerPromedio(@PathVariable @Positive Long camaraId) {
        Double promedio = resenaService.obtenerPromedio(camaraId);
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/camara/{camaraId}/calificacion")
    public ResponseEntity<List<Resena>> listarPorCalificacion(@PathVariable @Positive Long camaraId,
                                                               @RequestParam Integer minCalificacion) {
        List<Resena> resenas = resenaService.listarPorCalificacionMinima(camaraId, minCalificacion);
        return ResponseEntity.ok(resenas);
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable @Positive Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}
