package com.ventacamaras.camaras.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "camara_imagenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CamaraImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "camara_id", nullable = false)
    private Camara camara;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "num_imagen", nullable = false)
    private Integer numImagen;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;
}
