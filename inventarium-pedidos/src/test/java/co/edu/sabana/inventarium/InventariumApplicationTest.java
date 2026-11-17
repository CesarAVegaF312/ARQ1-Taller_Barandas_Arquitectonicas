package co.edu.sabana.inventarium;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Arranca el contexto completo y recorre el flujo de punta a punta con los
 * adaptadores en memoria. Si el cableado de config/ se rompe, falla aqui.
 */
@SpringBootTest
@AutoConfigureMockMvc
class InventariumApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void creaUnPedidoContraLosAdaptadoresReales() throws Exception {
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cliente":"Tienda La 80","items":[{"sku":"INV-CEL-A5","cantidad":2}]}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CREADO"))
                .andExpect(jsonPath("$.total").value(2398000));
    }
}
