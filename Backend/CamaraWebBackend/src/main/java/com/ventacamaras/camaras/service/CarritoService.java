package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.Carrito;
import com.ventacamaras.camaras.model.ItemCarrito;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.CarritoRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CamaraRepository camaraRepository;
    private final UserRepository userRepository;

    @Transactional
    public Carrito agregarItemAlCarrito(String username, Long camaraId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        
        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Camara camara = camaraRepository.findById(camaraId)
                .orElseThrow(() -> new ResourceNotFoundException("Cámara no encontrada"));

        if (Boolean.FALSE.equals(camara.getActivo())) {
            throw new IllegalArgumentException("Esta cámara ya no está disponible");
        }

        if (camara.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para esta cámara. Stock actual: " + camara.getStock());
        }

        Carrito carrito = carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = Carrito.builder().usuario(usuario).build();
                    return carritoRepository.save(nuevoCarrito);
                });

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getCamara().getId().equals(camaraId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            if (item.getCantidad() + cantidad > camara.getStock()) {
                throw new IllegalArgumentException("La cantidad total supera el stock disponible: " + camara.getStock());
            }
            // Ya no seteamos el subtotal porque se calcula solo
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .camara(camara)
                    .cantidad(cantidad)
                    .precioUnitario(camara.getPrecio())
                    // Tampoco seteamos el subtotal aquí
                    .build();
            carrito.getItems().add(nuevoItem);
        }

        // Ya no seteamos el total del carrito, @Transient hace el trabajo
        return carritoRepository.save(carrito);
    }

    public Carrito obtenerCarritoPorUsername(String username) {
        return carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseGet(() -> {
                    User usuario = userRepository.findByUsername(username)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
                    // No persistimos aquí: devolvemos un carrito vacío asociado al usuario
                    return Carrito.builder().usuario(usuario).items(new ArrayList<>()).build();
                });
    }

    @Transactional
    public Carrito incrementarCantidadItem(String username, Long itemId) {
        Carrito carrito = carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado en el carrito"));

        if (item.getCantidad() >= item.getCamara().getStock()) {
            throw new IllegalArgumentException("No hay más stock disponible");
        }
        item.setCantidad(item.getCantidad() + 1);

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito decrementarCantidadItem(String username, Long itemId) {
        Carrito carrito = carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado en el carrito"));

        if (item.getCantidad() <= 1) {
            // eliminar item
            carrito.getItems().remove(item);
        } else {
            item.setCantidad(item.getCantidad() - 1);
        }

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarItemDelCarrito(String username, Long itemId) {
        Carrito carrito = carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        boolean removed = carrito.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new ResourceNotFoundException("Item no encontrado en el carrito");
        }

        return carritoRepository.save(carrito);
    }

    @Transactional
    public void vaciarCarrito(String username) {
        Carrito carrito = carritoRepository.findByUsuarioUsernameConItems(username)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}

