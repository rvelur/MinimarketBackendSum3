package com.minimarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pruebas Base", description = "Endpoints de verificación pública y comprobación de estado de la aplicación")
public class HolaMundoController {

    @Operation(summary = "Endpoint de bienvenida pública", description = "Retorna un mensaje de texto simple para comprobar el estado operativo del servicio sin requerir autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio activo y respondiendo correctamente")
    })
    @GetMapping("/public/hola")
    public String holaMundo() {
        return "¡Hola Mundo!";
    }
}