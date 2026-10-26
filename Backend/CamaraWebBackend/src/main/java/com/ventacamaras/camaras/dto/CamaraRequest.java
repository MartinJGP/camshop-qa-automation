package com.ventacamaras.camaras.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class CamaraRequest {
@NotBlank @Size(max = 100)
    private String nombre;
@NotBlank @Size(max = 50)
    private String marca;
@NotBlank @Size(max = 50)
    private String modelo;
@NotBlank @Size(max = 50)
    private String resolucion;
@Size(max = 1000)
    private String descripcion;
@NotNull @Positive
    private Double precio;
@NotNull @PositiveOrZero
    private Integer stock;
@Positive
    private Long categoriaId;
}

