package com.ventacamaras.camaras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_orden")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "camara_id")
    private Long camaraId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Transient
    public Double getSubtotal() {
        return precioUnitario * cantidad;
    }
}
