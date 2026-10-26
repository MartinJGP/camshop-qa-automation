package com.ventacamaras.camaras.dto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
        long camarasActivas,
        long usuarios,
        long pedidos,
        long unidadesStock,
        double ingresos,
        double ticketPromedio,
        Map<String, Long> pedidosPorEstado,
        List<ProductoVendidoDTO> masVendidas) {}
