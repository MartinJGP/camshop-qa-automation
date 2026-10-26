package com.ventacamaras.camaras.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "camaras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camara {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String resolucion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 1000)
    private String descripcion;

    private Boolean activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "camara", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CamaraImagen> imagenes;

    @Transient
    public boolean isAgotada() {
        return stock == null || stock <= 0;
    }
}
