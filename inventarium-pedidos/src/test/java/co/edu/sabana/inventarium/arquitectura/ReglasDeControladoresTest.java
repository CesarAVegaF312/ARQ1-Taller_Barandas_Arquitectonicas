package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Fitness functions del adaptador primario (REST).
 *
 * NO MODIFIQUE ESTE ARCHIVO PARA QUE LAS PRUEBAS PASEN.
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglasDeControladoresTest {

    @ArchTest
    static final ArchRule los_controladores_solo_usan_puertos_primarios =
            noClasses().that().resideInAPackage("..delivery..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..infrastructure..", "..application.port.out..", "..application.usecase..")
                    .as("R5. Los controladores no acceden a repositorios, adaptadores ni implementaciones de casos de uso")
                    .because("un controlador solo conoce puertos primarios (application/port/in). Si el "
                            + "controlador consulta el repositorio directamente, la regla de negocio queda "
                            + "en la capa HTTP y ninguna otra entrada (un job, un evento, una prueba) puede "
                            + "reutilizarla");

    @ArchTest
    static final ArchRule los_controladores_viven_en_delivery =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().resideInAPackage("..delivery.rest..")
                    .as("R6. Toda clase @RestController vive en delivery.rest")
                    .because("los adaptadores primarios HTTP se agrupan en un solo lugar; un controlador "
                            + "dentro de domain o application arrastra el framework web al nucleo");
}
