package com.ventacamaras.camaras.repository;

import com.ventacamaras.camaras.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    

    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.usuario.username = :username")
    Optional<Carrito> findByUsuarioUsernameConItems(@Param("username") String username);
}