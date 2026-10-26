package com.ventacamaras.camaras.dto;

import com.ventacamaras.camaras.model.ItemOrden;
import com.ventacamaras.camaras.model.Orden;
import com.ventacamaras.camaras.model.Pago;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrdenResponse {
    private Integer id;
    private Double total;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<ItemOrden> items;
    private Pago pago;

    public static OrdenResponse from(Orden orden, Pago pago) {
        return OrdenResponse.builder()
                .id(orden.getId())
                .total(orden.getTotal())
                .estado(orden.getEstado())
                .fechaCreacion(orden.getFechaCreacion())
                .items(orden.getItems())
                .pago(pago)
                .build();
    }
}
