package co.edu.sabana.inventarium.domain.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Regla de stock bajo. El umbral se lee de application.properties.
 */
@Component
public class PoliticaDeStockBajo {

    private final int umbral;

    public PoliticaDeStockBajo(@Value("${inventarium.stock-bajo.umbral:5}") int umbral) {
        this.umbral = umbral;
    }

    public boolean aplicaA(Producto producto) {
        return producto.stock() <= umbral;
    }

    public int umbral() {
        return umbral;
    }
}
