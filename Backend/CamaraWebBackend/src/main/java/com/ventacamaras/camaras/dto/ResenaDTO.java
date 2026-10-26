package com.ventacamaras.camaras.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResenaDTO {
@NotNull @Min(1) @Max(5)
    private Integer calificacion;
@Size(max = 500)
    private String comentario;
}
