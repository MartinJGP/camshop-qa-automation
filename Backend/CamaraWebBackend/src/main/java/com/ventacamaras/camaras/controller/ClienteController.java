package com.ventacamaras.camaras.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ventacamaras.camaras.dto.ClienteRequest;
import com.ventacamaras.camaras.model.Cliente;
import com.ventacamaras.camaras.service.ClienteService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping("/me")
    public ResponseEntity<Cliente> obtenerMiPerfil(Authentication authentication) {
        return clienteService.obtenerPorUsername(authentication.getName())
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<Cliente> guardarMiPerfil(Authentication authentication,
                                                    @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.guardarPerfil(authentication.getName(), request));
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente clienteCreado = clienteService.crearCliente(clienteRequest);
        return ResponseEntity.ok(clienteCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable @Positive Long id) {
        Optional<Cliente> cliente = clienteService.obtenerPorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> obtenerPorEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.obtenerPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cliente> obtenerPorUserId(@PathVariable @Positive Long userId) {
        Optional<Cliente> cliente = clienteService.obtenerPorUserId(userId);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }


    @GetMapping("/buscar/telefono")
    public ResponseEntity<Cliente> buscarPorTelefono(@RequestParam String telefono) {
        Optional<Cliente> cliente = clienteService.buscarPorTelefono(telefono);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable @Positive Long id, @Valid @RequestBody Cliente clienteActualizado) {
        Cliente cliente = clienteService.actualizarCliente(id, clienteActualizado);
        return ResponseEntity.ok(cliente);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable @Positive Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}


