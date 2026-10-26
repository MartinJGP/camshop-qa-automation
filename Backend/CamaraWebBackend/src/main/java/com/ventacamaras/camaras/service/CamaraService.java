package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.dto.CamaraRequest;
import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.Categoria;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CamaraService {
    private final CamaraRepository camaraRepository;
    private final CategoriaRepository categoriaRepository;
    private final CamaraImagenService imagenService;

    public List<Camara> listarCamaras() { return camaraRepository.listarActivas(); }
    public List<Camara> listarTodasAdmin() { return camaraRepository.listarTodas(); }
    public List<Camara> filtrar(Long categoriaId, String marca, Double precioMin, Double precioMax, boolean soloStock) {
        if (precioMin != null && precioMax != null && precioMin > precioMax) {
            throw new IllegalArgumentException("El precio mínimo no puede superar al máximo");
        }
        String marcaNormalizada = marca == null || marca.isBlank() ? null : marca.trim();
        return camaraRepository.filtrar(categoriaId, marcaNormalizada, precioMin, precioMax, soloStock);
    }

    public Optional<Camara> obtenerPorId(Long id) {
        return camaraRepository.findById(id).filter(c -> !Boolean.FALSE.equals(c.getActivo()));
    }

    public List<Camara> buscarPorMarca(String marca) { return activas(camaraRepository.findByMarca(marca)); }
    public List<Camara> buscarPorNombre(String nombre) { return activas(camaraRepository.buscarPorNombre(nombre)); }
    public List<Camara> buscarPorRangoPrecio(Double min, Double max) { return activas(camaraRepository.findByPrecioBetween(min, max)); }
    public List<Camara> buscarDisponiblesPorMarca(String marca) { return activas(camaraRepository.buscarDisponiblesPorMarca(marca)); }
    public List<Camara> buscarConStock() { return activas(camaraRepository.findByStockGreaterThan(0)); }

    @Transactional
    public Camara crearCamara(CamaraRequest request) {
        Camara camara = new Camara();
        camara.setActivo(true);
        aplicar(camara, request);
        return camaraRepository.save(camara);
    }

    @Transactional
    public Camara actualizarCamara(Long id, CamaraRequest request) {
        Camara camara = obtenerAdmin(id);
        aplicar(camara, request);
        return camaraRepository.save(camara);
    }

    @Transactional
    public Camara cambiarEstado(Long id, boolean activo) {
        Camara camara = obtenerAdmin(id);
        if (!activo) {
            imagenService.eliminarPorCamara(id);
        }
        camara.setActivo(activo);
        return camaraRepository.save(camara);
    }

    @Transactional
    public Camara registrarEntrada(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) throw new IllegalArgumentException("La entrada debe ser mayor que cero");
        Camara camara = obtenerAdmin(id);
        camara.setStock((camara.getStock() == null ? 0 : camara.getStock()) + cantidad);
        return camaraRepository.save(camara);
    }

    @Transactional
    public Camara establecerStock(Long id, Integer stock) {
        if (stock == null || stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        Camara camara = obtenerAdmin(id);
        camara.setStock(stock);
        return camaraRepository.save(camara);
    }

    @Transactional
    public void descontarStock(Long id, Integer cantidad) {
        Camara camara = obtenerAdmin(id);
        if (cantidad == null || cantidad <= 0 || camara.getStock() < cantidad) {
            throw new IllegalArgumentException("Cantidad inválida o stock insuficiente");
        }
        camara.setStock(camara.getStock() - cantidad);
    }

    private void aplicar(Camara camara, CamaraRequest r) {
        if (r.getNombre() == null || r.getNombre().isBlank() || r.getMarca() == null || r.getMarca().isBlank()
                || r.getModelo() == null || r.getModelo().isBlank() || r.getResolucion() == null || r.getResolucion().isBlank()) {
            throw new IllegalArgumentException("Nombre, marca, modelo y resolución son obligatorios");
        }
        if (r.getPrecio() == null || r.getPrecio() <= 0) throw new IllegalArgumentException("El precio debe ser mayor que cero");
        if (r.getStock() == null || r.getStock() < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        Categoria categoria = r.getCategoriaId() == null ? null : categoriaRepository.findById(r.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        camara.setNombre(r.getNombre().trim()); camara.setMarca(r.getMarca().trim());
        camara.setModelo(r.getModelo().trim()); camara.setResolucion(r.getResolucion().trim());
        camara.setDescripcion(r.getDescripcion()); camara.setPrecio(r.getPrecio());
        camara.setStock(r.getStock()); camara.setCategoria(categoria);
    }

    private Camara obtenerAdmin(Long id) {
        return camaraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cámara no encontrada"));
    }

    private List<Camara> activas(List<Camara> camaras) {
        return camaras.stream().filter(c -> !Boolean.FALSE.equals(c.getActivo())).toList();
    }
}

