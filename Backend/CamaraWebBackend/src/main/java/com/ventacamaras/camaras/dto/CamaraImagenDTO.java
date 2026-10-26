package com.ventacamaras.camaras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CamaraImagenDTO {
    private Long id;
    private Long camaraId;
    private String url;
    private Integer numImagen;
    private Boolean esPrincipal;
}
