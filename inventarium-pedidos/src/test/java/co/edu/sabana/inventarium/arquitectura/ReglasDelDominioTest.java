package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Fitness functions del nucleo: el dominio y los casos de uso son Java puro.
 *
 * NO MODIFIQUE ESTE ARCHIVO PARA QUE LAS PRUEBAS PASEN. Si una regla falla,
 * lo que se corrige es el codigo de produccion.
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglasDelDominioTest {

    @ArchTest
    static final ArchRule el_dominio_no_depende_de_frameworks =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("org.springframework..", "jakarta..", "com.fasterxml..", "org.hibernate..")
                    .as("R1. El dominio no depende de Spring, Jakarta, Jackson ni Hibernate")
                    .because("la regla de negocio no debe conocer la tecnologia que la entrega ni la que la "
                            + "persiste. Si el dominio necesita un valor de configuracion, recibalo por "
                            + "constructor y cablee el valor en config/");

    @ArchTest
    static final ArchRule el_dominio_no_depende_de_capas_externas =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..application..", "..infrastructure..", "..delivery..", "..config..")
                    .as("R2. El dominio no depende de application, infrastructure, delivery ni config")
                    .because("las dependencias del codigo fuente apuntan hacia adentro: el dominio es el "
                            + "circulo mas interno y no puede referenciar nada de un circulo externo");

    @ArchTest
    static final ArchRule la_aplicacion_solo_depende_del_nucleo =
            classes().that().resideInAPackage("..application..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage("..domain..", "..application..", "java..")
                    .as("R3. Los casos de uso solo dependen del dominio, de sus puertos y de java.*")
                    .because("un caso de uso habla con el mundo exterior solo a traves de puertos. Si "
                            + "necesita una base de datos, un correo o un reloj, declare un puerto en "
                            + "application/port/out y deje que infrastructure lo implemente. Los casos de uso "
                            + "no llevan @Service: se cablean en config/");
}
