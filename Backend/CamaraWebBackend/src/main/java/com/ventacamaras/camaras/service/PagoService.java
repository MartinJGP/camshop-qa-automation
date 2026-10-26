package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Pago;
import com.ventacamaras.camaras.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> obtenerPorId(Integer id) {
        return pagoRepository.findById(id);
    }

    public Optional<Pago> obtenerPorOrden(Integer ordenId) {
        return pagoRepository.findByOrdenId(ordenId);
    }

    public List<Pago> buscarPorEstadoYMontoMinimo(String estado, Double montoMinimo) {
        return pagoRepository.buscarPorEstadoYMontoMinimo(estado, montoMinimo);
    }

    @Transactional
    public Pago registrarPago(Integer ordenId, Double monto, String metodo) {
        if (pagoRepository.findByOrdenId(ordenId).isPresent()) {
            throw new IllegalArgumentException("Ya existe un pago para la orden: " + ordenId);
        }

        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setEstado("COMPLETADO");

        return pagoRepository.save(pago);
    }
}

