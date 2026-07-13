package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
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
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Endpoints para la gestión y consulta de Productos con soporte OpenAPI y HATEOAS")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Listar todos los productos", description = "Retorna una colección con todos los productos disponibles en el minimarket e inyecta enlaces dinámicos HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
            .map(producto -> EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos")))
            .collect(Collectors.toList());

        return CollectionModel.of(productos,
            linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }

    @Operation(summary = "Obtener un producto por ID", description = "Busca un producto específico mediante su identificador único y retorna sus enlaces de navegación HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado con el ID especificado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        // Paréntesis corregidos aquí
        EntityModel<Producto> recurso = EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Guardar un nuevo producto", description = "Registra un producto en el sistema minimarket y asocia de forma dinámica sus hipervínculos de control.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o erróneos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> guardarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.save(producto);
        EntityModel<Producto> recurso = EntityModel.of(nuevoProducto,
            linkTo(methodOn(ProductoController.class).obtenerProductoPorId(nuevoProducto.getId())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un producto existente", description = "Modifica los atributos de un producto existente y devuelve el recurso con su respectiva estructura HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado para actualización")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoExistente = productoService.findById(id);
        if (productoExistente != null) {
            producto.setId(id);
            Producto productoActualizado = productoService.save(producto);
            EntityModel<Producto> recurso = EntityModel.of(productoActualizado,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));
            return ResponseEntity.ok(recurso);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un producto", description = "Remueve permanentemente un producto del catálogo del minimarket a través de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}