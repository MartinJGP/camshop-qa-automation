package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.CamaraImagen;
import com.ventacamaras.camaras.repository.CamaraImagenRepository;
import com.ventacamaras.camaras.repository.CamaraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
class CamaraImagenServiceTest {
    @Autowired private CamaraRepository camaraRepository;
    @Autowired private CamaraImagenRepository imagenRepository;
    @Autowired private CamaraImagenService imagenService;

    @Test
    void eliminaLaUnicaImagenPrincipal() {
        Camara camara = new Camara();
        camara.setNombre("Cámara de prueba");
        camara.setMarca("Marca");
        camara.setModelo("Modelo");
        camara.setResolucion("1080p");
        camara.setPrecio(100.0);
        camara.setStock(1);
        camara.setActivo(true);
        camara = camaraRepository.saveAndFlush(camara);

        CamaraImagen imagen = new CamaraImagen();
        imagen.setCamara(camara);
        imagen.setUrl("https://example.com/imagen-prueba.jpg");
        imagen.setNumImagen(1);
        imagen.setEsPrincipal(true);
        imagen = imagenRepository.saveAndFlush(imagen);

        imagenService.eliminarImagen(imagen.getId());

        assertFalse(imagenRepository.existsById(imagen.getId()));
    }
}
