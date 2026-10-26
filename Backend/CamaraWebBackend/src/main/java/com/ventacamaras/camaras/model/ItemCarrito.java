package com.ventacamaras.camaras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camara_id", nullable = false)
    private Camara camara;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    // @Transient hace que no busque esta columna en la BD, 
    // pero sí la devuelve en el JSON para el frontend.
    @Transient
    public Double getSubtotal() {
        if (this.precioUnitario != null && this.cantidad != null) {
            return this.precioUnitario * this.cantidad;
        }
        return 0.0;
    }
}