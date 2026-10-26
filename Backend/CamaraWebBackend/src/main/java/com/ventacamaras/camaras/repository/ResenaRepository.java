package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByCamaraId(Long camaraId);

    List<Resena> findByClienteId(Long clienteId);

    List<Resena> findByCamaraIdAndCalificacionGreaterThanEqual(Long camaraId, Integer calificacion);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.camara.id = :camaraId")
    Double obtenerPromedioCalificacion(@Param("camaraId") Long camaraId);

    @Query("SELECT COUNT(r) > 0 FROM Resena r WHERE r.camara.id = :camaraId AND r.cliente.id = :clienteId")
    boolean existeResenaDeClienteEnCamara(@Param("camaraId") Long camaraId,
                                          @Param("clienteId") Long clienteId);
}