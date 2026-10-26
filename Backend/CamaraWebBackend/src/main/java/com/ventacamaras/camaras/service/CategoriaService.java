package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Categoria;
import com.ventacamaras.camaras.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repository;

    public List<Categoria> listar() { return repository.findAllByOrderByNombreAsc(); }

    @Transactional
    public Categoria guardar(Long id, Categoria datos) {
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        Categoria categoria = id == null ? new Categoria() : repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        repository.findByNombreIgnoreCase(datos.getNombre().trim()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) throw new IllegalArgumentException("La categoría ya existe");
        });
        categoria.setNombre(datos.getNombre().trim());
        categoria.setDescripcion(datos.getDescripcion());
        categoria.setActivo(datos.getActivo() == null || datos.getActivo());
        return repository.save(categoria);
    }

    @Transactional
    public Categoria cambiarEstado(Long id, boolean activo) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        categoria.setActivo(activo);
        return repository.save(categoria);
    }
}

