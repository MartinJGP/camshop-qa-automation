package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.model.CamaraImagen;
import com.ventacamaras.camaras.service.CamaraImagenService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/camaras")
@RequiredArgsConstructor
public class CamaraImagenController {

    private final CamaraImagenService imagenService;
    private final com.ventacamaras.camaras.service.ImageStorageService storageService;

    @PostMapping("/{camaraId}/imagenes")
    public ResponseEntity<CamaraImagen> agregarImagen(@PathVariable @Positive Long camaraId,
                                                       @Valid @RequestBody CamaraImagen imagen) {
        CamaraImagen nueva = imagenService.agregarImagen(camaraId, imagen);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/{camaraId}/imagenes")
    public ResponseEntity<List<CamaraImagen>> listarImagenes(@PathVariable @Positive Long camaraId) {
        List<CamaraImagen> imagenes = imagenService.listarPorCamara(camaraId);
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/{camaraId}/imagenes/principal")
    public ResponseEntity<CamaraImagen> obtenerPrincipal(@PathVariable @Positive Long camaraId) {
        Optional<CamaraImagen> imagen = imagenService.obtenerPrincipal(camaraId);
        return imagen.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/imagenes/{id}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable @Positive Long id) {
        imagenService.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{camaraId}/imagenes/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CamaraImagen> subirImagen(@PathVariable @Positive Long camaraId,
            @RequestPart("archivo") org.springframework.web.multipart.MultipartFile archivo,
            @RequestParam(defaultValue = "1") Integer numImagen,
            @RequestParam(defaultValue = "false") Boolean esPrincipal) {
        CamaraImagen imagen = new CamaraImagen();
        String url = storageService.guardar(archivo);
        imagen.setUrl(url);
        imagen.setNumImagen(numImagen);
        imagen.setEsPrincipal(esPrincipal);
        try {
            return ResponseEntity.ok(imagenService.agregarImagen(camaraId, imagen));
        } catch (RuntimeException ex) {
            storageService.eliminar(url);
            throw ex;
        }
    }

    @PatchMapping("/imagenes/{id}/principal")
    public ResponseEntity<CamaraImagen> marcarPrincipal(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(imagenService.marcarPrincipal(id));
    }

    @PutMapping("/imagenes/{id}")
    public ResponseEntity<CamaraImagen> actualizarImagen(@PathVariable @Positive Long id, @Valid @RequestBody CamaraImagen imagen) {
        return ResponseEntity.ok(imagenService.actualizar(id, imagen));
    }
}

