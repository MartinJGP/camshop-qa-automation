package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.CamaraImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CamaraImagenRepository extends JpaRepository<CamaraImagen, Long> {
    List<CamaraImagen> findByCamaraIdOrderByNumImagenAsc(Long camaraId);

    @Query("SELECT ci FROM CamaraImagen ci WHERE ci.camara.id = :camaraId AND ci.esPrincipal = true")
    Optional<CamaraImagen> buscarImagenPrincipal(@Param("camaraId") Long camaraId);
}
