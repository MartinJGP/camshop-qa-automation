package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.ItemOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemOrdenRepository extends JpaRepository<ItemOrden, Long> {
    @Query("""
        SELECT i.camaraId, i.nombre, SUM(i.cantidad), SUM(i.cantidad * i.precioUnitario)
        FROM ItemOrden i
        WHERE i.orden.estado <> 'CANCELADO'
        GROUP BY i.camaraId, i.nombre
        ORDER BY SUM(i.cantidad) DESC
        """)
    List<Object[]> productosMasVendidos();
}
