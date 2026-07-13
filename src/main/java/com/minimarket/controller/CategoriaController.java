package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "Endpoints para la clasificación y agrupación por categorías de los productos del minimarket")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Listar todas las categorías", description = "Retorna el conjunto completo de categorías configuradas en el catálogo de productos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de categorías obtenido exitosamente")
    })
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.findAll();
    }

    @Operation(summary = "Obtener una categoría por ID", description = "Busca los datos de una clasificación específica utilizando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría localizada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la categoría con el ID ingresado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return (categoria != null) ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva etiqueta o agrupación para clasificar productos en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría creada e insertada de forma correcta"),
        @ApiResponse(responseCode = "400", description = "Cuerpo de solicitud inválido")
    })
    @PostMapping
    public Categoria guardarCategoria(@RequestBody Categoria categoria) {
        return categoriaService.save(categoria);
    }

    @Operation(summary = "Actualizar una categoría existente", description = "Modifica el nombre o atributos de una categoría basándose en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría modificada con éxito"),
        @ApiResponse(responseCode = "404", description = "La categoría solicitada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria categoriaExistente = categoriaService.findById(id);
        if (categoriaExistente != null) {
            categoria.setId(id);
            return ResponseEntity.ok(categoriaService.save(categoria));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una categoría", description = "Quita de manera permanente una categoría de la base de datos por medio de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada satisfactoriamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "No se localizó la categoría a eliminar")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria != null) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}