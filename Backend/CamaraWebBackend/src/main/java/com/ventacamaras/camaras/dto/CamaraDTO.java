package com.ventacamaras.camaras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CamaraDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String modelo;
    private String resolucion;
    private Double precio;
    private Integer stock;
}
