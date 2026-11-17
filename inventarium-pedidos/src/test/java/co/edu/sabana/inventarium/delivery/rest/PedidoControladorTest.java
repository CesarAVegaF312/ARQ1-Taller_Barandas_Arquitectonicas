package co.edu.sabana.inventarium.delivery.rest;

import co.edu.sabana.inventarium.application.port.in.ConsultarPedidoCasoDeUso;
import co.edu.sabana.inventarium.application.port.in.CrearPedidoCasoDeUso;
import co.edu.sabana.inventarium.domain.exception.PedidoNoEncontradoException;
import co.edu.sabana.inventarium.domain.exception.StockInsuficienteException;
import co.edu.sabana.inventarium.domain.model.LineaDePedido;
import co.edu.sabana.inventarium.domain.model.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba del adaptador primario. Los puertos primarios se reemplazan por
 * dobles: se prueba solo la traduccion HTTP <-> caso de uso.
 */
@WebMvcTest(PedidoControlador.class)
class PedidoControladorTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CrearPedidoCasoDeUso crearPedido;

    @MockitoBean
    private ConsultarPedidoCasoDeUso consultarPedido;

    @Test
    void creaUnPedidoYResponde201() throws Exception {
        Pedido pedido = Pedido.crear("P-1", "Tienda La 80",
                List.of(new LineaDePedido("INV-TV-55", 2, new BigDecimal("2899000"))));
        when(crearPedido.crear(any())).thenReturn(pedido);

        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cliente":"Tienda La 80","items":[{"sku":"INV-TV-55","cantidad":2}]}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("P-1"))
                .andExpect(jsonPath("$.total").value(5798000));
    }

    @Test
    void respondeConflictoCuandoNoHayStock() throws Exception {
        when(crearPedido.crear(any())).thenThrow(new StockInsuficienteException("INV-AUD-01", 5, 3));

        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cliente":"Tienda La 80","items":[{"sku":"INV-AUD-01","cantidad":5}]}
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    void rechazaUnaSolicitudSinItems() throws Exception {
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cliente":"Tienda La 80","items":[]}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void respondeNoEncontrado() throws Exception {
        when(consultarPedido.porId("X")).thenThrow(new PedidoNoEncontradoException("X"));

        mvc.perform(get("/pedidos/X")).andExpect(status().isNotFound());
    }
}
