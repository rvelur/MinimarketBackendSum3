package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para la administración de usuarios, clientes y roles con soporte OpenAPI y HATEOAS")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista completa de los usuarios registrados junto a enlaces dinámicos de navegación hipermedia.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida de forma exitosa")
    })
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
            .map(usuario -> EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios")))
            .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());
    }

    @Operation(summary = "Obtener un usuario por su ID", description = "Busca la información detallada de un usuario por medio de su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario localizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID ingresado no corresponde a ningún usuario")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        EntityModel<Usuario> recurso = EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Agrega un nuevo usuario al sistema gestionando sus roles y adjuntando sus enlaces dinámicos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "La solicitud contiene parámetros incorrectos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> guardarUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        EntityModel<Usuario> recurso = EntityModel.of(nuevoUsuario,
            linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(nuevoUsuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos de un usuario", description = "Modifica la información de un usuario existente a partir de su ID único y retorna el recurso actualizado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado para ser modificado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            Usuario usuarioActualizado = usuarioService.save(usuario);
            EntityModel<Usuario> recurso = EntityModel.of(usuarioActualizado,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"));
            return ResponseEntity.ok(recurso);
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar un usuario permanentemente", description = "Quita un registro de usuario del ecosistema de Minimarket Plus empleando su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado sin contenido de retorno"),
        @ApiResponse(responseCode = "404", description = "El usuario indicado no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        
        if (usuario.isPresent()) {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}