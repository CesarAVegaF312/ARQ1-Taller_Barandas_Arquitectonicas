# Ronda 1: el atajo

## Lo que se le pidió al agente

> Necesitamos una alerta de stock bajo. Agrega un endpoint `GET /inventario/stock-bajo`
> que devuelva los productos con stock menor o igual a un umbral configurable (por defecto 5),
> ordenados de menor a mayor stock. Incluye pruebas. Es para hoy.

## Lo que entregó

Una implementación que funciona, con pruebas que pasan:

| Archivo | Qué hizo |
|---|---|
| `src/main/java/.../domain/model/PoliticaDeStockBajo.java` | Regla nueva en el dominio, anotada con `@Component` y con el umbral leído con `@Value`. |
| `src/main/java/.../infrastructure/persistence/CatalogoEnMemoriaAdaptador.java` | Le agregó un método público `todos()` que no está en el puerto. |
| `src/main/java/.../delivery/rest/StockBajoController.java` | Controlador nuevo que recibe el adaptador concreto y filtra él mismo. |
| `src/test/java/.../domain/model/PoliticaDeStockBajoTest.java` | Prueba unitaria de la regla. |
| `src/test/java/.../delivery/rest/StockBajoControllerTest.java` | Prueba unitaria del controlador. |

Esta carpeta replica la estructura de `inventarium-pedidos/src`. Aplicarla es copiar
encima: `scripts/aplicar-atajo.ps1` (Windows) o `scripts/aplicar-atajo.sh` (bash).

No corrijan nada todavía. En la ronda 1 se trata de leer lo que dicen las pruebas.
