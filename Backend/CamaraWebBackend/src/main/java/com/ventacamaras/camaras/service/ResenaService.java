package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.Cliente;
import com.ventacamaras.camaras.model.Resena;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.ClienteRepository;
import com.ventacamaras.camaras.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final CamaraRepository camaraRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Resena crearResena(Long camaraId, String username,
                               Integer calificacion, String comentario) {

        Cliente cliente = clienteRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Completa tu perfil antes de dejar una reseña"));

        Camara camara = camaraRepository.findById(camaraId)
                .orElseThrow(() -> new ResourceNotFoundException("Cámara no encontrada con id: " + camaraId));

        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }

        if (resenaRepository.existeResenaDeClienteEnCamara(camaraId, cliente.getId())) {
            throw new IllegalArgumentException("El cliente ya dejó una reseña para esta cámara");
        }

        Resena resena = Resena.builder()
                .camara(camara)
                .cliente(cliente)
                .calificacion(calificacion)
                .comentario(comentario)
                .build();

        return resenaRepository.save(resena);
    }

    @Transactional(readOnly = true)
    public List<Resena> listarPorCamara(Long camaraId) {
        return resenaRepository.findByCamaraId(camaraId);
    }

    @Transactional(readOnly = true)
    public Optional<Resena> obtenerPorId(Long id) {
        return resenaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Resena> listarPorCalificacionMinima(Long camaraId, Integer minCalificacion) {
        return resenaRepository.findByCamaraIdAndCalificacionGreaterThanEqual(camaraId, minCalificacion);
    }

    @Transactional(readOnly = true)
    public Double obtenerPromedio(Long camaraId) {
        Double promedio = resenaRepository.obtenerPromedioCalificacion(camaraId);
        return promedio != null ? promedio : 0.0;
    }

    @Transactional
    public void eliminarResena(Long id) {
        resenaRepository.deleteById(id);
    }
}

