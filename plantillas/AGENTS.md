# Reglas de arquitectura de inventarium-pedidos

Este archivo es para agentes de código (Claude Code, Codex, Copilot, Cursor u otros).
Cópienlo en la raíz de `inventarium-pedidos/` con el nombre `AGENTS.md` y también como
`CLAUDE.md`: cada herramienta lee uno de los dos.

## Contexto

Servicio de pedidos de Inventarium, una distribuidora de electrónicos. Spring Boot 3,
Java 17, Maven. Arquitectura hexagonal (puertos y adaptadores): las dependencias del
código fuente apuntan hacia adentro, hacia la regla de negocio.

## Mapa de paquetes (`co.edu.sabana.inventarium`)

| Paquete | Qué contiene | Puede depender de |
|---|---|---|
| `domain.model`, `domain.exception` | Entidades, valores y reglas de negocio | Solo `java.*` y el propio dominio |
| `application.port.in` | Puertos primarios: interfaces `*CasoDeUso` y sus comandos | `domain`, `java.*` |
| `application.port.out` | Puertos secundarios: interfaces con nombre de negocio | `domain`, `java.*` |
| `application.usecase` | Clases `*Servicio` que implementan un puerto primario | `domain`, `application`, `java.*` |
| `infrastructure.*` | Clases `*Adaptador` que implementan puertos secundarios | `application.port.out`, `domain`, Spring |
| `delivery.rest` | Clases `*Controlador` (`@RestController`), DTO, manejo de errores | `application.port.in`, `domain`, Spring |
| `config` | Cableado: crea los `*Servicio` con `@Bean` | Todo |

## Reglas obligatorias

1. El dominio no importa nada de Spring, Jakarta, Jackson ni Hibernate. Si necesita un valor
   de configuración, lo recibe por constructor y `config/` lo lee con `@Value`.
2. `application` es Java puro: los casos de uso no llevan `@Service` ni `@Component`; se
   registran como `@Bean` en `config/ConfiguracionCasosDeUso`.
3. Un controlador solo conoce puertos primarios (`application.port.in`). Nunca recibe un
   repositorio, un adaptador ni un `*Servicio` concreto.
4. Si un caso de uso necesita algo del exterior (datos, reloj, correo), se declara un puerto
   en `application.port.out` con nombre de negocio (nada de `Jpa`, `Jdbc`, `Http`, `Email`,
   `EnMemoria` en el nombre) y se implementa en `infrastructure` con sufijo `Adaptador`.
5. No se crean ciclos entre paquetes.
6. Nombres: `*CasoDeUso` (puertos primarios), `*Servicio` (casos de uso), `*Adaptador`
   (adaptadores secundarios), `*Controlador` (controladores REST).

## Cómo se verifica

Las reglas anteriores son pruebas ArchUnit en
`src/test/java/co/edu/sabana/inventarium/arquitectura/`.

- Antes de dar por terminada cualquier tarea, ejecute `mvn -B clean verify` y confirme
  `BUILD SUCCESS`.
- **No modifique, desactive ni borre** ninguna prueba del paquete `arquitectura`. Si una
  regla falla, corrija el código de producción.
- Si cree que una regla es incorrecta, deténgase y explíquelo; no la cambie.
- Escriba pruebas unitarias para la lógica nueva (dominio y casos de uso).
