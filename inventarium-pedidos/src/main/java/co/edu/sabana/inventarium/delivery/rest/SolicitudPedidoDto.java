package co.edu.sabana.inventarium.delivery.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Cuerpo JSON de POST /pedidos. Las anotaciones de validacion viven aqui, no en el dominio. */
public record SolicitudPedidoDto(
        @NotBlank String cliente,
        @NotEmpty List<@Valid ItemDto> items) {

    public record ItemDto(@NotBlank String sku, @Min(1) int cantidad) {
    }
}
