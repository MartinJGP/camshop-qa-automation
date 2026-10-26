package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.Camara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CamaraRepository extends JpaRepository<Camara, Long> {

    @Query("SELECT c FROM Camara c WHERE c.activo IS NULL OR c.activo = true ORDER BY c.nombre")
    List<Camara> listarActivas();

    @Query("SELECT c FROM Camara c ORDER BY c.nombre")
    List<Camara> listarTodas();

    @Query("""
        SELECT c FROM Camara c
        WHERE (c.activo IS NULL OR c.activo = true)
          AND (:categoriaId IS NULL OR c.categoria.id = :categoriaId)
          AND (:marca IS NULL OR LOWER(c.marca) LIKE LOWER(CONCAT('%', :marca, '%')))
          AND (:precioMin IS NULL OR c.precio >= :precioMin)
          AND (:precioMax IS NULL OR c.precio <= :precioMax)
          AND (:soloStock = false OR c.stock > 0)
        ORDER BY c.nombre
        """)
    List<Camara> filtrar(@Param("categoriaId") Long categoriaId,
                         @Param("marca") String marca,
                         @Param("precioMin") Double precioMin,
                         @Param("precioMax") Double precioMax,
                         @Param("soloStock") boolean soloStock);

    List<Camara> findByMarca(String marca);

    List<Camara> findByPrecioBetween(Double min, Double max);

    List<Camara> findByStockGreaterThan(Integer stock);

    @Query("SELECT c FROM Camara c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Camara> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Camara c WHERE c.marca = :marca AND c.stock > 0")
    List<Camara> buscarDisponiblesPorMarca(@Param("marca") String marca);
}
