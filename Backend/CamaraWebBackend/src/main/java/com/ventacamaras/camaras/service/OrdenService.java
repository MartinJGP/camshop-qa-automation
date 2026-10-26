package com.ventacamaras.camaras.service;

import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ventacamaras.camaras.model.Carrito;
import com.ventacamaras.camaras.model.Orden;
import com.ventacamaras.camaras.model.Pago;
import com.ventacamaras.camaras.model.ItemOrden;
import com.ventacamaras.camaras.dto.OrdenResponse;
import com.ventacamaras.camaras.repository.CarritoRepository;
import com.ventacamaras.camaras.repository.OrdenRepository;
import com.ventacamaras.camaras.repository.PagoRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
@RequiredArgsConstructor
@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;

    private final CarritoRepository carritoRepository;

    private final PagoRepository pagoRepository;

    @Transactional
    public OrdenResponse checkout(String username, String metodoPago){

        Carrito carrito =
                carritoRepository
                .findByUsuarioUsernameConItems(username)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Carrito no encontrado"));

        if (carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        carrito.getItems().forEach(item -> {
            if (Boolean.FALSE.equals(item.getCamara().getActivo())) {
                throw new IllegalArgumentException(item.getCamara().getNombre() + " ya no está disponible");
            }
            if (item.getCantidad() <= 0 || item.getCantidad() > item.getCamara().getStock()) {
                throw new IllegalArgumentException("Stock insuficiente para " + item.getCamara().getNombre());
            }
        });

        double total = carrito.getItems().stream()
                .mapToDouble(i -> i.getCantidad() * i.getPrecioUnitario()).sum();

        Orden orden = new Orden();

        orden.setUser(carrito.getUsuario());
        orden.setTotal(total);
        // El pago de este proyecto es simulado y se aprueba dentro de la misma
        // transacción, por lo que la orden nace directamente como PAGADO.
        orden.setEstado("PAGADO");

        carrito.getItems().forEach(item -> {
            item.getCamara().setStock(item.getCamara().getStock() - item.getCantidad());
            ItemOrden snapshot = ItemOrden.builder()
                    .orden(orden)
                    .camaraId(item.getCamara().getId())
                    .nombre(item.getCamara().getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .build();
            orden.getItems().add(snapshot);
        });

        ordenRepository.save(orden);

        Pago pago = new Pago();

        pago.setOrden(orden);
        pago.setMetodo(metodoPago);
        pago.setMonto(total);
        pago.setEstado("APROBADO");

        pagoRepository.save(pago);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return OrdenResponse.from(orden, pago);
    }

    @Transactional(readOnly = true)
    public java.util.List<OrdenResponse> listarDelUsuario(String username) {
        return ordenRepository.findByUserUsernameOrderByIdDesc(username).stream()
                .map(o -> OrdenResponse.from(o, pagoRepository.findByOrdenId(o.getId()).orElse(null)))
                .toList();
    }

    @Transactional(readOnly = true)
    public java.util.List<OrdenResponse> listarTodas() {
        return ordenRepository.findAllByOrderByIdDesc().stream()
                .map(o -> OrdenResponse.from(o, pagoRepository.findByOrdenId(o.getId()).orElse(null)))
                .toList();
    }

    @Transactional
    public OrdenResponse actualizarEstado(Integer id, String estado) {
        String nuevoEstado = estado == null ? "" : estado.trim().toUpperCase();
        if (!Set.of("PENDIENTE", "PROCESANDO", "ENVIADO", "ENTREGADO", "CANCELADO", "PAGADO").contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado de pedido no permitido");
        }
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
        orden.setEstado(nuevoEstado);
        ordenRepository.save(orden);
        return OrdenResponse.from(orden, pagoRepository.findByOrdenId(id).orElse(null));
    }
}

