package com.minimarket.service;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl; // Ajusta si tu implementación está en otro paquete
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService; // La clase que implementa la lógica de negocio

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        // Configuramos un producto de prueba antes de cada test
        productoEjemplo = new Producto();
        productoEjemplo.setId(1L);
        productoEjemplo.setNombre("Bebida Cola 3L");
        productoEjemplo.setPrecio(2500.0);
        productoEjemplo.setStock(50);
    }

    @Test
    @DisplayName("Debería retornar un producto cuando el ID existe")
    void obtenerProductoPorIdExitoso() {
        // 1. Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        // 2. Act (Usando el método real de tu servicio)
        Producto resultado = productoService.findById(1L);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals("Bebida Cola 3L", resultado.getNombre());
        assertEquals(2500.0, resultado.getPrecio());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar null cuando el ID del producto no existe")
    void obtenerProductoPorIdNoEncontrado() {
        // 1. Arrange
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // 2. Act
        Producto resultado = productoService.findById(99L);

        // 3. Assert (Validamos que retorne null tal como dicta tu lógica orElse(null))
        assertNull(resultado);
        verify(productoRepository, times(1)).findById(99L);
    }
}
