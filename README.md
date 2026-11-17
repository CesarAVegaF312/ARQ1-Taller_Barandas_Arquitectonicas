# Taller: Barandas arquitectónicas

**Fitness functions con ArchUnit cuando el código lo escribe (también) un agente de IA**

Diplomado en Diseño y Arquitectura de Software · Módulo 5: Arquitectura limpia y hexagonal
Modalidad virtual · Duración: 2 horas · Grupos de 3 a 4 personas

---

## Objetivo general

Convertir las reglas de la arquitectura hexagonal en **pruebas ejecutables** (fitness functions)
que fallen cuando alguien, una persona o un agente de código, toma un atajo. Al final de la sesión
ustedes habrán leído una violación real, la habrán corregido refactorizando a puertos, habrán
escrito una regla propia y habrán observado si un agente respeta las reglas cuando se las dan por
escrito.

La idea de fondo cabe en una línea: **una instrucción en un archivo para el agente es una
petición; una prueba en el pipeline es una baranda.**

---

## Índice

- [Conceptos clave](#conceptos-clave)
- [Requisitos de instalación](#requisitos-de-instalación)
- [Complemento visual](#complemento-visual)
- [Conozcan el proyecto](#conozcan-el-proyecto)
- [Las reglas que ya están en el pipeline](#las-reglas-que-ya-están-en-el-pipeline)
- [Agenda](#agenda)
- [Ronda 1: el atajo](#ronda-1-el-atajo-20-min)
- [Ronda 2: refactor a puertos](#ronda-2-refactor-a-puertos-30-min)
- [Ronda 3: su propia fitness function](#ronda-3-su-propia-fitness-function-25-min)
- [Ronda 4 (opcional): reglas para un agente](#ronda-4-opcional-reglas-para-un-agente-15-min)
- [Cierre: el experimento ChargeLedger](#cierre-el-experimento-chargeledger-15-min)
- [Comandos](#comandos)
- [Para entregar](#para-entregar)
- [Rúbrica](#rúbrica)
- [Recursos](#recursos)

---

## Conceptos clave

- **Fitness function (función de aptitud arquitectónica).** Cualquier mecanismo que evalúa de
  forma objetiva si el sistema conserva una característica arquitectónica. El término viene de
  *Building Evolutionary Architectures* (Ford, Parsons, Kua y Sadalage). En este taller son
  pruebas JUnit que corren con `mvn verify`, igual que las unitarias.
- **ArchUnit.** Biblioteca Java que analiza el bytecode compilado y permite escribir reglas como
  "ninguna clase de `domain` depende de `org.springframework`". Si la regla se viola, la prueba
  falla y dice qué clase, qué línea y por qué.
- **Puerto primario (driving).** La API del caso de uso. Lo invoca quien inicia la interacción:
  un controlador, un job, una prueba. Aquí, las interfaces `*CasoDeUso`.
- **Puerto secundario (driven).** Lo que el negocio necesita del mundo, declarado con
  vocabulario de negocio (`RepositorioDePedidos`, `CatalogoDeProductos`) e implementado desde
  afuera por un adaptador.
- **Regla de dependencia.** Las dependencias del código fuente apuntan hacia adentro. Si una
  clase del dominio importa `org.springframework...`, la arquitectura ya está incumplida, sin
  importar cuántos círculos tenga el diagrama.

### Por qué esto importa más con agentes de código

Un agente produce en minutos un cambio que antes tomaba una tarde. Por eso el cuello de botella
se mueve: deja de estar en escribir el código y pasa a estar en revisarlo. Un agente apurado, igual
que una persona apurada, tiende al camino más corto: inyectar el repositorio en el controlador,
poner `@Component` donde haga falta y nombrar las clases como en el último tutorial que vio. Las
pruebas unitarias no lo detectan, porque el código *funciona*. Lo que se degrada es la estructura,
y eso solo se ve si hay una prueba que la mire.

Un archivo de instrucciones (`AGENTS.md`, `CLAUDE.md`) mejora la probabilidad de que el agente
respete las reglas. Una prueba ArchUnit en el pipeline hace que no pueda integrar el cambio si no
las respeta. Son mecanismos complementarios, no equivalentes.

---

## Requisitos de instalación

Instálenlos **antes** de la sesión. No se usa Docker.

| Herramienta | Versión | Verificación |
|---|---|---|
| JDK | 17 o superior (Temurin recomendado: <https://adoptium.net>) | `java -version` |
| Maven | 3.9 o superior (<https://maven.apache.org/download.cgi>) | `mvn -v` |
| Git | cualquiera reciente | `git --version` |
| Editor | IntelliJ IDEA, VS Code con Extension Pack for Java, o Eclipse | |
| Agente de código (solo ronda 4, opcional) | El que tengan: Claude Code, Codex, GitHub Copilot en modo agente, Cursor, Gemini CLI… | |

`mvn -v` debe mostrar en la línea `Java version` un JDK 17 o superior. Si muestra otro, ajusten
`JAVA_HOME`.

Prueba de humo, también antes de la sesión (descarga las dependencias, unos 2 minutos la primera vez):

```powershell
cd inventarium-pedidos
mvn -B clean verify
```

Deben ver `Tests run: 29, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS`.

---

## Complemento visual

En línea: <https://cesaravegaf312.github.io/ARQ1-Taller_Barandas_Arquitectonicas/barandas_visual.html>.
Sin conexión: [`barandas_visual.html`](barandas_visual.html) se abre con doble clic, sin servidor.
Muestra el grafo de dependencias del proyecto en dos estados (la base y con el atajo), marca en rojo las dependencias que violan una regla y, al hacer clic, presenta el
mensaje real de ArchUnit que produce cada una.

---

## Conozcan el proyecto

`inventarium-pedidos` es el servicio de pedidos de Inventarium, la distribuidora de electrónicos
del módulo 4. Permite crear un pedido (valida stock, descuenta inventario y calcula el total) y
consultarlo.

```text
src/main/java/co/edu/sabana/inventarium/
 ├─ InventariumApplication.java
 ├─ domain/                              ← Java puro. Ni Spring ni Jakarta.
 │   ├─ model/        Pedido, LineaDePedido, Producto, EstadoPedido
 │   └─ exception/    ReglaDeNegocioException y derivadas
 ├─ application/                         ← Java puro. Sin @Service.
 │   ├─ port/in/      CrearPedidoCasoDeUso, ConsultarPedidoCasoDeUso   (puertos primarios)
 │   ├─ port/out/     RepositorioDePedidos, CatalogoDeProductos        (puertos secundarios)
 │   └─ usecase/      CrearPedidoServicio, ConsultarPedidoServicio
 ├─ infrastructure/persistence/          ← Adaptadores secundarios en memoria
 │                    PedidosEnMemoriaAdaptador, CatalogoEnMemoriaAdaptador
 ├─ delivery/rest/                       ← Adaptador primario HTTP
 │                    PedidoControlador, DTO, ManejadorDeErrores
 └─ config/           ConfiguracionCasosDeUso  ← raíz de composición: el único que conoce todo

src/test/java/co/edu/sabana/inventarium/
 ├─ domain/model/          PedidoTest, ProductoTest                  (unitarias, Java puro)
 ├─ application/usecase/   CrearPedidoServicioTest                   (unitarias, Mockito)
 ├─ delivery/rest/         PedidoControladorTest                     (@WebMvcTest)
 ├─ InventariumApplicationTest                                       (contexto completo)
 └─ arquitectura/          Reglas*Test                               (ArchUnit: las barandas)
```

> **Vocabulario.** En la presentación del módulo los paquetes se llaman `dominio`, `aplicacion`
> e `infraestructura`. Aquí se usan los nombres en inglés que verán en la mayoría de proyectos
> (`domain`, `application`, `infrastructure`, `delivery`). Las reglas son las mismas; solo cambia
> el texto del paquete.

Endpoints:

| Método | Ruta | Respuesta |
|---|---|---|
| `POST` | `/pedidos` | `201` con el pedido; `404` si el SKU no existe; `409` sin stock; `400` si la solicitud es inválida |
| `GET` | `/pedidos/{id}` | `200` con el pedido; `404` si no existe |

Catálogo inicial (en memoria): `INV-TV-55` (12 u.), `INV-LAP-14` (4 u.), `INV-CEL-A5` (30 u.),
`INV-AUD-01` (3 u.), `INV-CBL-HD` (120 u.).

### Antes de empezar: una línea base en Git

Dentro de `inventarium-pedidos`, creen un repositorio y un primer commit. Así `git diff` les
mostrará exactamente qué cambia el atajo y podrán volver a la base cuando quieran.

```powershell
git init
git add .
git commit -m "Base del taller"
```

---

## Las reglas que ya están en el pipeline

Están en `src/test/java/co/edu/sabana/inventarium/arquitectura/`. Cada una lleva `.as(...)` con
un identificador y `.because(...)` con la razón en español, de modo que el mensaje de falla
explica la regla sin tener que abrir el archivo.

| ID | Regla | Archivo |
|---|---|---|
| R1 | El dominio no depende de Spring, Jakarta, Jackson ni Hibernate | `ReglasDelDominioTest` |
| R2 | El dominio no depende de `application`, `infrastructure`, `delivery` ni `config` | `ReglasDelDominioTest` |
| R3 | Los casos de uso solo dependen del dominio, de sus puertos y de `java.*` | `ReglasDelDominioTest` |
| R4 | Las capas solo dependen hacia adentro (arquitectura por capas de ArchUnit) | `ReglasDeCapasTest` |
| R5 | Los controladores no acceden a repositorios, adaptadores ni implementaciones de casos de uso | `ReglasDeControladoresTest` |
| R6 | Toda clase `@RestController` vive en `delivery.rest` | `ReglasDeControladoresTest` |
| R7 | No hay ciclos de dependencias entre paquetes | `ReglasDeCiclosTest` |
| N1 | Los puertos primarios terminan en `CasoDeUso` | `ReglasDeNombresTest` |
| N2 | Todo lo que hay en `application.port.out` es una interfaz | `ReglasDeNombresTest` |
| N3 | Los puertos no llevan nombres de tecnología (`Jpa`, `Jdbc`, `Http`, `Email`, `EnMemoria`…) | `ReglasDeNombresTest` |
| N4 | Quien implementa un puerto secundario termina en `Adaptador` y vive en `infrastructure` | `ReglasDeNombresTest` |
| N5 | Las clases de `application.usecase` terminan en `Servicio` e implementan un puerto primario | `ReglasDeNombresTest` |
| N6 | Los controladores REST terminan en `Controlador` | `ReglasDeNombresTest` |

Los textos de `.as` y `.because` van sin tildes a propósito: en una consola de Windows con otra
página de códigos, Maven mostraría los caracteres acentuados corruptos, justo en el mensaje que se
quiere leer.

**Regla del taller: las clases del paquete `arquitectura` no se modifican.** Si una regla falla,
se corrige el código de producción. El docente verifica la integridad de esas clases en cada
entrega.

---

## Agenda

Sesión virtual sincrónica de 120 minutos. Los grupos trabajan en salas separadas (*breakout rooms*).

| Minuto | Duración | Actividad | Modalidad |
|---|---|---|---|
| 0–10 | 10 | Encuadre: qué es una fitness function y por qué importa con agentes. Verificación rápida del entorno. | Plenaria |
| 10–30 | 20 | **Ronda 1: el atajo.** Aplicar, ejecutar, diagnosticar. | Salas + 5 min de puesta en común |
| 30–60 | 30 | **Ronda 2: refactor a puertos** hasta volver al verde. | Salas |
| 60–65 | 5 | Pausa | |
| 65–90 | 25 | **Ronda 3: su propia fitness function**, con prueba de la prueba. | Salas + 5 min de puesta en común |
| 90–105 | 15 | **Ronda 4 (opcional): reglas para un agente.** Si no se hace, esos 15 minutos se suman a la ronda 3. | Salas |
| 105–120 | 15 | **Cierre:** debate sobre el experimento ChargeLedger. | Plenaria |

---

## Ronda 1: el atajo (20 min)

**Situación.** Ventas pidió para hoy una alerta de stock bajo: `GET /inventario/stock-bajo`
debe devolver los productos con stock menor o igual a un umbral configurable (5 por defecto). Un
agente de código la implementó en cinco minutos. El resultado está en la carpeta
[`atajo/`](atajo/LEEME.md): funciona y trae sus pruebas.

**Qué hacen ustedes:**

1. Apliquen el atajo desde la carpeta raíz del taller:

   ```powershell
   powershell -ExecutionPolicy Bypass -File scripts\aplicar-atajo.ps1
   ```

   En Git Bash, macOS o Linux: `./scripts/aplicar-atajo.sh`.

2. Miren qué cambió: `git status` y `git diff` dentro de `inventarium-pedidos`.
3. Ejecuten `mvn -B clean verify`. Lean la sección `Results:` de Maven.
4. Llenen la tabla de diagnóstico de la [plantilla de entrega](plantillas/plantilla_entrega.md):
   para cada regla que falla, qué clase la viola, qué principio protege y qué pasaría en
   producción.
5. Respondan: ¿por qué las pruebas unitarias del atajo pasaron?

**Qué deben observar.** De 33 pruebas, fallan 4, todas de arquitectura (R1, R4, R5, N6). Las 18
unitarias y la de contexto pasan. Un mensaje se ve así:

```text
Rule 'R5. Los controladores no acceden a repositorios, adaptadores ni implementaciones de casos
de uso, because un controlador solo conoce puertos primarios (application/port/in). Si el
controlador consulta el repositorio directamente, la regla de negocio queda en la capa HTTP y
ninguna otra entrada (un job, un evento, una prueba) puede reutilizarla' was violated (3 times):
Constructor <...StockBajoController.<init>(...CatalogoEnMemoriaAdaptador, ...)> has parameter of
type <...CatalogoEnMemoriaAdaptador> in (StockBajoController.java:0)
Field <...StockBajoController.catalogo> has type <...CatalogoEnMemoriaAdaptador> ...
Method <...StockBajoController.stockBajo()> calls method <...CatalogoEnMemoriaAdaptador.todos()>
in (StockBajoController.java:31)
```

---

## Ronda 2: refactor a puertos (30 min)

**Objetivo.** Que `mvn -B clean verify` vuelva a `BUILD SUCCESS` **sin tocar las reglas** y
manteniendo el mismo endpoint con el mismo comportamiento.

Lo que tienen que resolver (en el orden que prefieran):

1. La regla de stock bajo debe quedar en el dominio, pero como Java puro. ¿Dónde se lee entonces
   el umbral de `application.properties`?
2. El caso de uso necesita recorrer el catálogo. Ese método todavía no está en el contrato.
3. El controlador debe hablar con un puerto primario, no con un adaptador.
4. Los nombres deben seguir la convención (N1 a N6).
5. Las pruebas del atajo prueban el diseño viejo: ajústenlas o reemplácenlas por pruebas del
   caso de uso nuevo.

Verifiquen también que el endpoint responde:

```powershell
mvn spring-boot:run
# en otra terminal:
Invoke-RestMethod http://localhost:8080/inventario/stock-bajo
```

Debe listar `INV-AUD-01` (3) e `INV-LAP-14` (4), en ese orden.

---

## Ronda 3: su propia fitness function (25 min)

**Objetivo.** Cada grupo propone **una regla nueva** que proteja algo del diseño que les importe,
la escribe en ArchUnit y demuestra que puede fallar.

1. Copien [`plantillas/ReglaDelGrupoTest.java`](plantillas/ReglaDelGrupoTest.java) a
   `src/test/java/co/edu/sabana/inventarium/arquitectura/` con el nombre `ReglaGrupoNTest`.
2. Escriban la regla con `.as("G1. ...")` y `.because("...")` en español.
3. **Prueba de la prueba:** agreguen en `src/main` una clase que la viole a propósito y ejecuten

   ```powershell
   mvn -B test "-Dtest=Regla*Test"
   ```

   Copien el mensaje de falla en la plantilla. Luego borren la violación y muestren el verde.
4. Escriban qué violación parecida **no** detectaría su regla.

Ideas, si les sirven de punto de partida (mejor si la regla sale de un problema que hayan visto
en un proyecto real):

- El dominio no representa dinero con `double` ni `float`.
- El dominio no usa `LocalDateTime.now()` ni `System.currentTimeMillis()`: si necesita la hora,
  la pide a un puerto `Reloj`.
- Ningún método público de un `@RestController` devuelve una entidad del dominio (solo DTO).
- Las excepciones del dominio extienden `ReglaDeNegocioException`.
- Nadie usa inyección por campo (`@Autowired` sobre un atributo). ArchUnit trae
  `GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION`.
- Los DTO de `delivery` no se usan fuera de `delivery`.
- Solo `config` declara métodos `@Bean`.

---

## Ronda 4 (opcional): reglas para un agente (15 min)

**Pregunta.** Si al agente le damos las reglas por escrito, ¿las respeta?

**Diseño del experimento.** El docente asigna una condición a cada grupo:

- **Condición A, sin reglas:** el agente recibe solo la instrucción.
- **Condición B, con reglas:** antes de empezar, copien [`plantillas/AGENTS.md`](plantillas/AGENTS.md)
  en la raíz de `inventarium-pedidos/` como `AGENTS.md` y como `CLAUDE.md`. Claude Code lee
  `CLAUDE.md`; Codex, Copilot, Cursor y otros leen `AGENTS.md`.

Partan de su solución de la ronda 2 ya en verde, con un commit hecho. Den al agente
**exactamente** esta instrucción, en una sesión nueva:

> Agrega descuento por volumen a los pedidos: si una línea tiene 10 o más unidades, esa línea
> tiene 5 % de descuento; si tiene 50 o más, 10 %. El total del pedido y la respuesta de
> POST /pedidos deben reflejar el descuento. Incluye pruebas.

No le ayuden con la arquitectura. Si pregunta algo de negocio, respondan; si pregunta dónde
poner el código, contesten "decide tú". Registren en la plantilla: minutos hasta el verde,
reglas violadas en el primer intento, si ejecutó `mvn verify` por iniciativa propia, número de
iteraciones y, sobre todo, **si intentó modificar o desactivar alguna regla**.

---

## Cierre: el experimento ChargeLedger (15 min)

Kristiyan Stoyanov construyó ChargeLedger, un servicio de facturación de carga de vehículos
eléctricos, en dos versiones: una hexagonal (puertos, adaptadores y pruebas de arquitectura) y
otra sin estructura prescrita. Le pidió a un agente (OpenCode con un modelo local) las mismas
tareas en ambas y midió el tiempo hasta que el cambio pasaba la aceptación.

Cifras verificadas en las publicaciones originales (septiembre de 2026):

| Campaña | Resultado de la versión hexagonal frente a la plana |
|---|---|
| F1–F9: nueve funcionalidades nuevas acumuladas | **37,8 % más tiempo** hasta la aceptación (228,57 frente a 165,93 min); 70,9 % más tokens de entrada |
| C1–C6: seis retos más difíciles | Cerca de **8 % más tiempo** (174,24 frente a 161,55 min); ganó en dos de los seis |
| S01–S15: segunda serie de cambios | 30,3 % más tiempo |
| S16: reemplazar la persistencia por PostgreSQL | **45,4 % menos tiempo** (38,92 frente a 71,25 min) |
| S01–S16 acumulado | 15,7 % más tiempo; S16 recuperó el 35,7 % de la diferencia |

Las advertencias del propio autor: un solo servicio, un solo modelo y un solo entorno, cada tarea
corrida **una vez** por condición; las dos versiones diferían en más que la arquitectura
(guías y pruebas internas distintas); el tiempo de preparar la base hexagonal no se contó. Él
mismo dice que el estudio no permite afirmar que la arquitectura hexagonal *causó* nada, ni cuál
enfoque cuesta menos en todo el ciclo de vida.

**Preguntas para el debate** (el docente elige dos o tres):

1. La hexagonal fue más lenta para funcionalidades nuevas y más rápida para un cambio
   estructural. ¿Qué tipo de cambio va a dominar en Inventarium en los próximos dos años?
2. ¿Qué variables confusas tiene el estudio? ¿Cuáles tiene nuestro experimento de la ronda 4?
3. Si la arquitectura no hace más rápido al agente, ¿qué la justifica cuando el código lo
   escribe un agente? ¿Cambia la respuesta si hay pruebas de arquitectura en el pipeline?
4. ¿Qué regla de este taller pondrían mañana en el pipeline de su empresa, y cuál no?

---

## Comandos

Todos se ejecutan dentro de `inventarium-pedidos/`, salvo los scripts.

| Para qué | Comando |
|---|---|
| Todo: compilar, pruebas unitarias y de arquitectura | `mvn -B clean verify` |
| Solo las reglas de arquitectura | `mvn -B test "-Dtest=Reglas*Test"` |
| Solo una clase de pruebas | `mvn -B test "-Dtest=CrearPedidoServicioTest"` |
| Levantar el servicio en el puerto 8080 | `mvn spring-boot:run` |
| Aplicar el atajo (ronda 1), desde la raíz del taller | `powershell -ExecutionPolicy Bypass -File scripts\aplicar-atajo.ps1` |
| Ver qué cambió | `git status` y `git diff` |
| Volver a la base | `git restore .` y luego `git clean -fd src` |

Crear un pedido desde PowerShell:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/pedidos -ContentType "application/json" -Body '{"cliente":"Tienda La 80","items":[{"sku":"INV-TV-55","cantidad":2}]}'
```

Con `curl` (Git Bash, macOS, Linux):

```bash
curl -s -X POST localhost:8080/pedidos -H 'Content-Type: application/json' \
  -d '{"cliente":"Tienda La 80","items":[{"sku":"INV-TV-55","cantidad":2}]}'
```

En PowerShell, escriban `"-Dtest=..."` entre comillas: sin ellas, PowerShell puede partir el
argumento antes de pasarlo a Maven.

---

## Para entregar

Una entrega por grupo, en el espacio que el docente habilite en el aula virtual:

1. **El proyecto** `inventarium-pedidos` (enlace a un repositorio o un `.zip` **sin** la carpeta
   `target/`) con:
   - la alerta de stock bajo refactorizada (ronda 2), con `mvn -B clean verify` en verde;
   - la regla del grupo (ronda 3) en `src/test/java/.../arquitectura/`;
   - las cinco clases `Reglas*Test` originales sin modificar.
2. **La plantilla** [`plantillas/plantilla_entrega.md`](plantillas/plantilla_entrega.md)
   diligenciada, en la raíz del proyecto, con el nombre `ENTREGA.md`.

---

## Rúbrica

Cada criterio se califica de 0.0 a 5.0. La nota es la suma ponderada:
**Nota = 0,20 × C1 + 0,30 × C2 + 0,30 × C3 + 0,20 × C4**.

| Criterio | Peso | 5.0 | 4.0 | 3.0 | 1.5 | 0.0 |
|---|---|---|---|---|---|---|
| **C1. Diagnóstico del atajo** (ronda 1) | 20 % | Las 4 reglas que fallan quedan explicadas: clase infractora, principio que protege y una consecuencia concreta. Explica por qué las unitarias pasaron. | 4 reglas explicadas; alguna consecuencia es genérica. | 2 o 3 reglas bien explicadas, o explicaciones que repiten el mensaje sin interpretarlo. | Solo enumera las fallas. | No entrega. |
| **C2. Refactor a puertos** (ronda 2) | 30 % | `mvn verify` en verde con las reglas intactas. El endpoint conserva su comportamiento. Puerto primario, puerto secundario ampliado, umbral cableado en `config` y pruebas unitarias del caso de uso nuevo. | Verde y reglas intactas; falta una de las piezas (por ejemplo, las pruebas del caso de uso). | Verde, pero con restos del atajo (un `Map` como respuesta, lógica en el controlador) o sin pruebas nuevas. | No llega al verde, aunque el avance va en la dirección correcta. | No entrega, o llega al verde **modificando, desactivando o borrando reglas**. |
| **C3. Fitness function propia** (ronda 3) | 30 % | La regla protege un riesgo real y explicado. Lleva `.as` y `.because` claros en español. Tiene prueba de la prueba (violación, mensaje y verde) y nombra su propio límite. | Regla válida con prueba de la prueba; el riesgo o el límite quedan poco argumentados. | Regla válida sin demostrar que puede fallar, o regla casi trivial. | La regla no compila, no se ejecuta o pasa en vacío. | No entrega. |
| **C4. Experimento y reflexión** (ronda 4 y cierre) | 20 % | Reflexión de 200 a 400 palabras que usa las cifras de ChargeLedger con sus límites, distingue correlación de causalidad y, si hicieron la ronda 4, se apoya en su propia evidencia. | Reflexión sólida con una de las dimensiones débil. | Reflexión correcta pero genérica. | Opinión sin argumentos ni datos. | No entrega. |

La ronda 4 es opcional. Quien no la haga puede obtener 5.0 en C4 con la reflexión.

---

## Recursos

- Ford, N., Parsons, R., Kua, P. y Sadalage, P. *Building Evolutionary Architectures*, 2.ª ed.
  O'Reilly, 2022. Capítulos 2 y 4: fitness functions y su automatización.
- ArchUnit, guía de usuario: <https://www.archunit.org/userguide/html/000_Index.html>
- Cockburn, A. *Hexagonal Architecture*, 2005: <https://alistair.cockburn.us/hexagonal-architecture/>
- Martin, R. C. *The Clean Architecture*, 2012:
  <https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html>
- Stoyanov, K. *I expected hexagonal architecture to help my coding agent. It didn't.*, DEV,
  2026: <https://dev.to/kstoyanovai/i-expected-hexagonal-architecture-to-help-my-coding-agent-it-didnt-3blg>
- Stoyanov, K. *How does clean architecture impact token costs and execution times?*, DEV, 2026:
  <https://dev.to/kstoyanovai/how-does-clean-architecture-impact-token-costs-and-execution-times-5g1g>
- Formato `AGENTS.md`: <https://agents.md>
- Presentación del módulo: *Arquitectura limpia y hexagonal*, sección 6, "Cómo verificar que la
  arquitectura se cumple".

---

## Créditos y uso académico

**Autor:** César Augusto Vega Fernández
**Programa:** Diplomado en Diseño y Arquitectura de Software, Universidad de La Sabana
**Año:** 2026

Material académico diseñado por el profesor **César Augusto Vega Fernández** para el módulo de
arquitectura limpia y hexagonal. Su propósito es exclusivamente educativo.

### Licencia de uso

Este material se distribuye bajo la licencia
[Creative Commons Atribución-NoComercial-CompartirIgual 4.0 Internacional (CC BY-NC-SA 4.0)](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es).
