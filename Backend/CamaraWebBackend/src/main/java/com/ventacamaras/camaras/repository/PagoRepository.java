package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Query("SELECT p FROM Pago p WHERE p.orden.id = :ordenId")
    Optional<Pago> findByOrdenId(@Param("ordenId") Integer ordenId);

    List<Pago> findByEstado(String estado);

    @Query("SELECT p FROM Pago p WHERE p.estado = :estado AND p.monto >= :montoMinimo")
    List<Pago> buscarPorEstadoYMontoMinimo(@Param("estado") String estado,
            @Param("montoMinimo") Double montoMinimo);
}
