package com.ventacamaras.camaras.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    private Orden orden;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false, length = 50)
    private String metodo;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE";

    @Transient
    public Integer getOrdenId() {
        return orden != null ? orden.getId() : null;
    }
}
