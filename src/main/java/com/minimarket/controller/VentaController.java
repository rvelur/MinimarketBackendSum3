package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Endpoints para la gestión, consulta y registro de transacciones comerciales en el minimarket")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Operation(summary = "Listar todas las ventas", description = "Retorna el historial completo de comprobantes de ventas emitidos por el establecimiento comercial.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial de ventas obtenido exitosamente")
    })
    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.findAll();
    }

    @Operation(summary = "Obtener una venta por ID", description = "Busca el encabezado de una venta específica mediante su identificador numérico único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta localizada de forma correcta"),
        @ApiResponse(responseCode = "404", description = "No se registró ninguna venta con el ID ingresado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return (venta != null) ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar una nueva venta", description = "Consolida y almacena una nueva transacción de venta dentro del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta guardada y procesada correctamente"),
        @ApiResponse(responseCode = "400", description = "La petición contiene datos de venta inconsistentes")
    })
    @PostMapping
    public Venta guardarVenta(@RequestBody Venta venta) {
        return ventaService.save(venta);
    }
}