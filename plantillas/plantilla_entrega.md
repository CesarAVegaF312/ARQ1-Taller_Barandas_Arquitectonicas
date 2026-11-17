# Entrega: Taller Barandas arquitectónicas

**Grupo:** _número_
**Integrantes (nombre y correo institucional):**

1.
2.
3.
4.

**Enlace al repositorio o nombre del archivo .zip:**

---

## Ronda 1: el atajo

### 1.1 Resultado de `mvn -B clean verify` con el atajo aplicado

Peguen la línea de totales y la lista de pruebas que fallaron (sección `Results:` de Maven).

```text
Tests run: __, Failures: __, Errors: __, Skipped: __
```

### 1.2 Diagnóstico

Una fila por cada regla que falló.

| Regla (ID y nombre) | Clase y línea que la viola | Principio que protege (con el vocabulario de la sesión: puerto primario/secundario, regla de dependencia, inversión) | Consecuencia concreta si esto llega a producción |
|---|---|---|---|
| | | | |
| | | | |
| | | | |
| | | | |

### 1.3 Las pruebas unitarias pasaron

¿Por qué las pruebas unitarias del atajo (`PoliticaDeStockBajoTest`, `StockBajoControllerTest`)
no detectaron nada? Respondan en 3 a 5 líneas.

---

## Ronda 2: refactor a puertos

### 2.1 Qué cambiaron

| Archivo | Cambio | Por qué |
|---|---|---|
| | | |

### 2.2 Evidencia

```text
(peguen aquí la salida final de mvn -B clean verify con BUILD SUCCESS y los totales)
```

- [ ] No modificamos, desactivamos ni borramos ninguna prueba del paquete `arquitectura`.
- [ ] `GET /inventario/stock-bajo` sigue respondiendo lo mismo que con el atajo.
- [ ] Agregamos pruebas unitarias del caso de uso nuevo.

### 2.3 ¿Qué ganaron, además de que las pruebas pasen?

---

## Ronda 3: fitness function del grupo

### 3.1 La regla

- **ID y enunciado** (el texto de `.as(...)`):
- **Riesgo que previene** (qué error real de diseño evita; si lo han visto en un proyecto, cuéntenlo):
- **Archivo:** `src/test/java/co/edu/sabana/inventarium/arquitectura/________Test.java`

```java
// peguen aquí la regla
```

### 3.2 Prueba de la prueba

Clase que agregaron para violar la regla a propósito:

```java
// peguen aquí la violación deliberada
```

Mensaje de falla de ArchUnit:

```text
(peguen aquí el mensaje)
```

Resultado después de borrar la violación (`BUILD SUCCESS`):

```text
(peguen aquí los totales)
```

### 3.3 Límites de la regla

¿Qué violación parecida **no** detectaría su regla? (Toda regla tiene un borde. Nómbrenlo.)

---

## Ronda 4 (opcional): reglas para un agente de IA

**Herramienta y modelo usados:**
**Condición:** [ ] A, sin archivo de reglas  [ ] B, con `AGENTS.md`/`CLAUDE.md`
**Instrucción dada al agente** (textual):

| Medida | Resultado |
|---|---|
| Minutos hasta `BUILD SUCCESS` (o hasta rendirse) | |
| Reglas ArchUnit violadas en el primer intento | |
| ¿Ejecutó `mvn verify` por iniciativa propia? | |
| Número de iteraciones (intentos de corrección) | |
| ¿Intentó modificar o desactivar una regla? | |
| Archivos creados o modificados | |
| ¿La solución final respeta la arquitectura? (`verificar --integridad` + revisión manual) | |

Observaciones (qué hizo bien, dónde se desvió, qué le tuvieron que decir):

---

## Cierre: reflexión del grupo

Entre 200 y 400 palabras. Respondan al menos dos de estas preguntas:

1. En ChargeLedger la versión hexagonal tardó cerca de 38 % más en nueve funcionalidades
   nuevas y 45 % menos al reemplazar la persistencia. ¿Qué tipo de cambio va a dominar en
   Inventarium? ¿Cambia eso su decisión de arquitectura?
2. El autor advierte que su experimento no prueba causalidad. ¿Qué variables confusas
   identifican en ese estudio? ¿Cuáles tiene también su experimento de la ronda 4?
3. Un archivo de instrucciones para el agente y una prueba ArchUnit en el pipeline: ¿qué
   diferencia hay entre los dos como mecanismo de control?
4. ¿Cuál de las reglas de este taller pondrían mañana en el pipeline de su empresa, y cuál no?
   ¿Por qué?
