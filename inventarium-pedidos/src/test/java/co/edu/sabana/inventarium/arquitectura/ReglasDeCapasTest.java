package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Fitness function de la regla de dependencia: cada capa sabe quien la puede usar.
 *
 *   delivery ──► application ──► domain ◄── infrastructure
 *                     ▲                          │
 *                     └──────────────────────────┘ (implementa los puertos out)
 *   config conoce a todos para cablearlos; nadie conoce a config.
 *
 * NO MODIFIQUE ESTE ARCHIVO PARA QUE LAS PRUEBAS PASEN.
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglasDeCapasTest {

    @ArchTest
    static final ArchRule las_capas_solo_dependen_hacia_adentro =
            layeredArchitecture().consideringOnlyDependenciesInLayers()
                    .layer("Dominio").definedBy("..domain..")
                    .layer("Aplicacion").definedBy("..application..")
                    .layer("Infraestructura").definedBy("..infrastructure..")
                    .layer("Entrega").definedBy("..delivery..")
                    .layer("Configuracion").definedBy("..config..")

                    .whereLayer("Entrega").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Configuracion").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Infraestructura").mayOnlyBeAccessedByLayers("Configuracion")
                    .whereLayer("Aplicacion").mayOnlyBeAccessedByLayers("Entrega", "Infraestructura", "Configuracion")
                    .whereLayer("Dominio").mayOnlyBeAccessedByLayers("Aplicacion", "Infraestructura", "Entrega", "Configuracion")

                    .as("R4. Las capas solo dependen hacia adentro")
                    .because("un adaptador (REST o persistencia) no puede ser usado por otro adaptador ni por "
                            + "el nucleo. La entrega llega al negocio por un puerto primario; la "
                            + "infraestructura solo la conoce config/, que la inyecta detras de un puerto "
                            + "secundario");
}
