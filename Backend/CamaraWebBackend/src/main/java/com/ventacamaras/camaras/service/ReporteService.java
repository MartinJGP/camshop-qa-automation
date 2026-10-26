package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.dto.DashboardResponse;
import com.ventacamaras.camaras.dto.ProductoVendidoDTO;
import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.ItemOrdenRepository;
import com.ventacamaras.camaras.repository.OrdenRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final CamaraRepository camaraRepository;
    private final UserRepository userRepository;
    private final OrdenRepository ordenRepository;
    private final ItemOrdenRepository itemOrdenRepository;

    @Transactional(readOnly = true)
    public DashboardResponse resumen() {
        List<Camara> activas = camaraRepository.listarActivas();
        long stock = activas.stream().mapToLong(c -> c.getStock() == null ? 0 : c.getStock()).sum();
        long pedidos = ordenRepository.count();
        double ingresos = ordenRepository.totalIngresos();
        Map<String, Long> porEstado = new LinkedHashMap<>();
        ordenRepository.contarPorEstado().forEach(fila -> porEstado.put((String) fila[0], (Long) fila[1]));
        List<ProductoVendidoDTO> productos = itemOrdenRepository.productosMasVendidos().stream().limit(8)
                .map(f -> new ProductoVendidoDTO((Long) f[0], (String) f[1], (Long) f[2], ((Number) f[3]).doubleValue()))
                .toList();
        return new DashboardResponse(activas.size(), userRepository.count(), pedidos, stock, ingresos,
                pedidos == 0 ? 0 : ingresos / pedidos, porEstado, productos);
    }
}

