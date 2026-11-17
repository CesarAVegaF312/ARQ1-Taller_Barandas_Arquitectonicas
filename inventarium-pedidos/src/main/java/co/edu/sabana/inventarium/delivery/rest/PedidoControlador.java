package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.application.port.in.ComandoCrearPedido;
import co.edu.sabana.inventarium.application.port.in.ConsultarPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.in.CrearPedidoCasoDeUso;
import co.edu.sabana.inventarium.domain.model.Pedido;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador primario (driving). Solo conoce puertos primarios: no sabe si los
 * pedidos se guardan en memoria, en H2 o en PostgreSQL.
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoControlador {

    private final CrearPedidoCasoDeUso crearPedido;
    private final ConsultarPedidoCasoDeUso consultarPedido;

    public PedidoControlador(CrearPedidoCasoDeUso crearPedido, ConsultarPedidoCasoDeUso consultarPedido) {
        this.crearPedido = crearPedido;
        this.consultarPedido = consultarPedido;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto crear(@Valid @RequestBody SolicitudPedidoDto solicitud) {
        ComandoCrearPedido comando = new ComandoCrearPedido(
                solicitud.cliente(),
                solicitud.items().stream()
                        .map(i -> new ComandoCrearPedido.Item(i.sku(), i.cantidad()))
                        .toList());
        Pedido pedido = crearPedido.crear(comando);
        return PedidoDto.desde(pedido);
    }

    @GetMapping("/{id}")
    public PedidoDto consultar(@PathVariable String id) {
        return PedidoDto.desde(consultarPedido.porId(id));
    }
}
