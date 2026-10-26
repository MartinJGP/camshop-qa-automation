package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.Carrito;
import com.ventacamaras.camaras.model.ItemCarrito;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.CarritoRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CamaraRepository camaraRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CarritoService carritoService;

    private User mockUser;
    private Camara mockCamara;

    @BeforeEach
    void setUp() {
        // Preparamos datos simulados antes de cada prueba
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test@correo.com");

        mockCamara = new Camara();
        mockCamara.setId(10L);
        mockCamara.setPrecio(200.0);
        mockCamara.setStock(15); // Hay 15 cámaras en stock
    }

    @Test
    void agregarItemAlCarrito_ConExito() {
        // 1.Configurar el comportamiento de los repositorios simulados (Mocks)
        when(userRepository.findByUsername("test@correo.com")).thenReturn(Optional.of(mockUser));
        when(camaraRepository.findById(10L)).thenReturn(Optional.of(mockCamara));
        
        // Simulamos que el usuario aún no tiene un carrito guardado en la BD
        when(carritoRepository.findByUsuarioUsernameConItems("test@correo.com")).thenReturn(Optional.empty());
        
        // Simulamos el guardado de un carrito nuevo
        Carrito carritoNuevo = Carrito.builder().usuario(mockUser).items(new ArrayList<>()).build();
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoNuevo);

        // 2.Ejecutamos el método de tu servicio pidiendo 2 cámaras
        Carrito resultado = carritoService.agregarItemAlCarrito("test@correo.com", 10L, 2);

        // 3.Verificamos que la lógica hizo lo correcto
        assertNotNull(resultado);
        assertEquals(1, resultado.getItems().size()); // Debe haber 1 item en el carrito
        
        ItemCarrito itemAgregado = resultado.getItems().get(0);
        assertEquals(2, itemAgregado.getCantidad());
        assertEquals(200.0, itemAgregado.getPrecioUnitario());
        assertEquals(400.0, itemAgregado.getSubtotal()); // 2 * 200 = 400
        
        assertEquals(400.0, resultado.getTotal()); // El total del carrito debe ser 400

        // Verificamos que el repositorio guardó el carrito al final de la transacción
        verify(carritoRepository, times(2)).save(any(Carrito.class)); 
    }

    @Test
    void agregarItemAlCarrito_StockInsuficiente_LanzaExcepcion() {
        // 1.El usuario pide 20 cámaras, pero solo hay 15 en stock
        when(userRepository.findByUsername("test@correo.com")).thenReturn(Optional.of(mockUser));
        when(camaraRepository.findById(10L)).thenReturn(Optional.of(mockCamara));

        // 2.Verificamos que explote con RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carritoService.agregarItemAlCarrito("test@correo.com", 10L, 20);
        });

        assertEquals("Stock insuficiente para esta cámara. Stock actual: 15", exception.getMessage());
        
        // Verificamos que NUNCA se intentó guardar nada en la base de datos para proteger la integridad
        verify(carritoRepository, never()).save(any(Carrito.class));
    }
}