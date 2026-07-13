package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Endpoints para el registro y auditoría de movimientos de stock con soporte OpenAPI y HATEOAS")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Listar los movimientos de inventario", description = "Retorna el historial completo de entradas y salidas de stock inyectando enlaces hipermedia HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial de inventario obtenido exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {
        List<EntityModel<Inventario>> movimientos = inventarioService.findAll().stream()
            .map(inventario -> EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario")))
            .collect(Collectors.toList());

        return CollectionModel.of(movimientos,
            linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withSelfRel());
    }

    @Operation(summary = "Obtener un movimiento por ID", description = "Busca la traza de un movimiento de stock específico a través de su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento de inventario localizado"),
        @ApiResponse(responseCode = "404", description = "No se encontró el movimiento con el ID especificado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Inventario> recurso = EntityModel.of(inventario,
            linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(id)).withSelfRel(),
            linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Registrar un movimiento de stock", description = "Crea un nuevo asiento de flujo de inventario (Entrada/Salida) y devuelve el recurso con su navegación RESTful.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros del cuerpo de la solicitud erróneos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> registrarMovimiento(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.save(inventario);
        EntityModel<Inventario> recurso = EntityModel.of(nuevoInventario,
            linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(nuevoInventario.getId())).withSelfRel(),
            linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una traza de inventario", description = "Modifica las propiedades de una auditoría de stock existente usando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro de inventario actualizado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la traza de inventario para modificar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(@PathVariable Long id, @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            Inventario actualizado = inventarioService.save(inventario);
            EntityModel<Inventario> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(id)).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario"));
            return ResponseEntity.ok(recurso);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un registro de inventario", description = "Remueve una transacción histórica de stock de la base de datos basándose en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Asiento eliminado satisfactoriamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "El registro de inventario no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}