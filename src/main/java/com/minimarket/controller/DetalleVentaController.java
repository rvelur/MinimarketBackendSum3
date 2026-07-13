package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas")
@Tag(name = "Detalles de Ventas", description = "Endpoints para la gestión y auditoría de los artículos específicos vinculados a cada comprobante de venta")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Operation(summary = "Listar todas las líneas de detalle de venta", description = "Retorna el desglose globalizado de todos los ítems vendidos registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de detalles obtenido exitosamente")
    })
    @GetMapping
    public List<DetalleVenta> listarDetalleVentas() {
        return detalleVentaService.findAll();
    }

    @Operation(summary = "Obtener una línea de detalle por ID", description = "Busca la información específica de un artículo vendido mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de venta localizado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el registro con el ID ingresado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVentaPorId(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        return (detalleVenta != null) ? ResponseEntity.ok(detalleVenta) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar una nueva línea de detalle", description = "Inserta un artículo específico al flujo de una transacción comercial.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle guardado y procesado de forma correcta"),
        @ApiResponse(responseCode = "400", description = "Cuerpo de solicitud inconsistente o erróneo")
    })
    @PostMapping
    public DetalleVenta guardarDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.save(detalleVenta);
    }

    @Operation(summary = "Actualizar una línea de detalle existente", description = "Modifica los valores asociados (cantidad, precios) de un ítem vendido mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro de detalle modificado correctamente"),
        @ApiResponse(responseCode = "404", description = "El detalle de venta solicitado no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        DetalleVenta existente = detalleVentaService.findById(id);
        if (existente != null) {
            detalleVenta.setId(id);
            return ResponseEntity.ok(detalleVentaService.save(detalleVenta));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un registro de detalle de venta", description = "Remueve de manera permanente un desglose de artículo vendido basándose en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registro eliminado con éxito (No Content)"),
        @ApiResponse(responseCode = "404", description = "No se localizó el detalle a eliminar")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        if (detalleVenta != null) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}