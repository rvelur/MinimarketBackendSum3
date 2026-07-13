package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
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
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Endpoints para la gestión del carrito de compras (agregar, consultar y eliminar productos) con OpenAPI y HATEOAS")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Listar elementos del carrito", description = "Retorna la lista de todas las asignaciones o ítems cargados en los carritos con sus respectivos hipervínculos dinámicos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado del carrito obtenido exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {
        List<EntityModel<Carrito>> items = carritoService.findAll().stream()
            .map(carrito -> EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(carrito.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito")))
            .collect(Collectors.toList());

        return CollectionModel.of(items,
            linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }

    @Operation(summary = "Obtener un ítem del carrito por ID", description = "Recupera los detalles de un registro específico del carrito de compras mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Elemento del carrito localizado"),
        @ApiResponse(responseCode = "404", description = "No se encontró el elemento del carrito con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Carrito> recurso = EntityModel.of(carrito,
            linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(id)).withSelfRel(),
            linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Agregar producto al carrito", description = "Guarda una nueva relación de producto y cantidad vinculada a un usuario, inyectando la navegación hipermedia de control.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto agregado al carrito con éxito"),
        @ApiResponse(responseCode = "400", description = "Estructura del cuerpo de la solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Carrito>> agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        Carrito nuevoCarrito = carritoService.save(carrito);
        EntityModel<Carrito> recurso = EntityModel.of(nuevoCarrito,
            linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(nuevoCarrito.getId())).withSelfRel(),
            linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar cantidad en el carrito", description = "Modifica las cantidades o propiedades de un ítem existente en el carrito utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito modificado satisfactoriamente"),
        @ApiResponse(responseCode = "404", description = "No existe el ítem del carrito a actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
        Carrito existente = carritoService.findById(id);
        if (existente != null) {
            carrito.setId(id);
            Carrito actualizado = carritoService.save(carrito);
            EntityModel<Carrito> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(id)).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"));
            return ResponseEntity.ok(recurso);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un producto del carrito", description = "Quita de manera definitiva un elemento del carrito de compras basándose en su ID de registro.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto removido del carrito sin contenido de retorno"),
        @ApiResponse(responseCode = "404", description = "El registro del carrito especificado no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}