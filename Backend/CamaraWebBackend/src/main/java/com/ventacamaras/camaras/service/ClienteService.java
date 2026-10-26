package com.ventacamaras.camaras.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ventacamaras.camaras.dto.ClienteRequest;
import com.ventacamaras.camaras.model.Cliente;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.ClienteRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;

    /**
     * Crear un nuevo cliente
     */
    public Cliente crearCliente(ClienteRequest clienteRequest) {
        validarDocumentos(clienteRequest.getDni(), clienteRequest.getRuc());
        if (clienteRepository.findByEmail(clienteRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (clienteRequest.getUserId() == null) {
            throw new IllegalArgumentException("El cliente necesita un userId");
        }

        User user = userRepository.findById(clienteRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cliente cliente = Cliente.builder()
                .nombre(clienteRequest.getNombre())
                .email(clienteRequest.getEmail())
                .direccion(clienteRequest.getDireccion())
                .telefono(clienteRequest.getTelefono())
                .dni(normalizar(clienteRequest.getDni()))
                .ruc(normalizar(clienteRequest.getRuc()))
                .user(user)
                .build();

        return clienteRepository.save(cliente);
    }


    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }


    public Optional<Cliente> obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }


    public Optional<Cliente> obtenerPorUserId(Long userId) {
        return clienteRepository.findByUserId(userId);
    }

    public Optional<Cliente> obtenerPorUsername(String username) {
        return clienteRepository.findByUserUsername(username);
    }

    @Transactional
    public Cliente guardarPerfil(String username, ClienteRequest request) {
        validarDocumentos(request.getDni(), request.getRuc());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Cliente cliente = clienteRepository.findByUserUsername(username)
                .orElseGet(() -> Cliente.builder().user(user).email(username).build());
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setDni(normalizar(request.getDni()));
        cliente.setRuc(normalizar(request.getRuc()));
        return clienteRepository.save(cliente);
    }

    /**
     * Buscar clientes por nombre (con query personalizado)
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.buscarPorNombre(nombre);
    }

    /**
     * Buscar cliente por teléfono (con query personalizado)
     */
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.buscarPorTelefono(telefono);
    }


    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }


    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        validarDocumentos(clienteActualizado.getDni(), clienteActualizado.getRuc());
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setDni(normalizar(clienteActualizado.getDni()));
        cliente.setRuc(normalizar(clienteActualizado.getRuc()));

        return clienteRepository.save(cliente);
    }

    private void validarDocumentos(String dni, String ruc) {
        String dniNormalizado = normalizar(dni);
        String rucNormalizado = normalizar(ruc);
        if (dniNormalizado != null && !dniNormalizado.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos");
        }
        if (rucNormalizado != null && !rucNormalizado.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe contener exactamente 11 dígitos");
        }
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) return null;
        return valor.trim();
    }

    /**
     * Eliminar cliente y deshabilitar su usuario asociado
     * Transacción para garantizar integridad: si falla algo, se revierte todo
     */
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        // Obtener el usuario asociado al cliente
        Long userId = cliente.getUser().getId();

        // Deshabilitar el usuario
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });

        // Eliminar el cliente
        clienteRepository.deleteById(id);
    }
}


