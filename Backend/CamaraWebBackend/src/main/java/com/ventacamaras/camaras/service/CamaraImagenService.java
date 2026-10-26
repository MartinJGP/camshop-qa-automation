package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.CamaraImagen;
import com.ventacamaras.camaras.repository.CamaraImagenRepository;
import com.ventacamaras.camaras.repository.CamaraRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CamaraImagenService {

    private final CamaraImagenRepository imagenRepository;
    private final CamaraRepository camaraRepository;
    private final ImageStorageService storageService;

    public List<CamaraImagen> listarPorCamara(Long camaraId) {
        return imagenRepository.findByCamaraIdOrderByNumImagenAsc(camaraId);
    }

    public Optional<CamaraImagen> obtenerPrincipal(Long camaraId) {
        return imagenRepository.buscarImagenPrincipal(camaraId);
    }

    @Transactional
    public CamaraImagen agregarImagen(Long camaraId, CamaraImagen imagen) {
        Camara camara = camaraRepository.findById(camaraId)
                .orElseThrow(() -> new ResourceNotFoundException("Cámara no encontrada con id: " + camaraId));
        imagen.setCamara(camara);
        if (Boolean.TRUE.equals(imagen.getEsPrincipal())) {
            imagenRepository.findByCamaraIdOrderByNumImagenAsc(camaraId).forEach(actual -> {
                if (Boolean.TRUE.equals(actual.getEsPrincipal())) {
                    actual.setEsPrincipal(false);
                    imagenRepository.save(actual);
                }
            });
        }
        return imagenRepository.save(imagen);
    }

    @Transactional
    public void eliminarImagen(Long id) {
        CamaraImagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));
        String url = imagen.getUrl();
        Long camaraId = imagen.getCamara().getId();
        boolean eraPrincipal = Boolean.TRUE.equals(imagen.getEsPrincipal());
        imagenRepository.delete(imagen);
        imagenRepository.flush();
        if (eraPrincipal) {
            imagenRepository.findByCamaraIdOrderByNumImagenAsc(camaraId).stream().findFirst()
                    .ifPresent(siguiente -> siguiente.setEsPrincipal(true));
        }
        storageService.eliminar(url);
    }

    @Transactional
    public void eliminarPorCamara(Long camaraId) {
        List<CamaraImagen> imagenes = imagenRepository.findByCamaraIdOrderByNumImagenAsc(camaraId);
        if (imagenes.isEmpty()) return;
        List<String> urls = imagenes.stream().map(CamaraImagen::getUrl).toList();
        imagenRepository.deleteAll(imagenes);
        imagenRepository.flush();
        urls.forEach(storageService::eliminar);
    }

    @Transactional
    public CamaraImagen marcarPrincipal(Long id) {
        CamaraImagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));
        imagenRepository.findByCamaraIdOrderByNumImagenAsc(imagen.getCamara().getId())
                .forEach(actual -> actual.setEsPrincipal(actual.getId().equals(id)));
        return imagen;
    }

    @Transactional
    public CamaraImagen actualizar(Long id, CamaraImagen datos) {
        CamaraImagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));
        if (datos.getUrl() != null && !datos.getUrl().isBlank()) imagen.setUrl(datos.getUrl().trim());
        if (datos.getNumImagen() != null && datos.getNumImagen() > 0) imagen.setNumImagen(datos.getNumImagen());
        if (Boolean.TRUE.equals(datos.getEsPrincipal())) return marcarPrincipal(id);
        return imagenRepository.save(imagen);
    }
}

