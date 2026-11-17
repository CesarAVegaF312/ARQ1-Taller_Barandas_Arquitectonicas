package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * RONDA 3 - Fitness function del grupo.
 *
 * 1. Copien este archivo a
 *    inventarium-pedidos/src/test/java/co/edu/sabana/inventarium/arquitectura/
 *    y renombren la clase (por ejemplo ReglaGrupo3Test).
 * 2. Escriban UNA regla nueva que proteja algo que les importe del diseño.
 *    Debe llevar .as("G1. ...") y .because("...") en español.
 * 3. Prueba de la prueba: agreguen en src/main una clase que viole la regla a
 *    propósito, ejecuten mvn -B test "-Dtest=Regla*Test" y copien el mensaje
 *    de falla en la plantilla de entrega. Luego borren la violación y muestren
 *    el verde.
 *
 * Una regla que nunca puede fallar no protege nada. ArchUnit ayuda: si el
 * "that()" no selecciona ninguna clase, la regla falla con
 * "failed to check any classes" (propiedad archRule.failOnEmptyShould).
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglaDelGrupoTest {

    // Forma general:
    //
    // @ArchTest
    // static final ArchRule nombre_en_minusculas =
    //         noClasses().that().resideInAPackage("..paquete..")
    //                 .should().dependOnClassesThat().resideInAnyPackage("..otro..")
    //                 .as("G1. Enunciado corto de la regla")
    //                 .because("por qué importa, en una o dos frases, y qué hacer si falla");

    // Ejemplo con una condición propia (para reglas que el DSL no expresa directamente):
    //
    // static final ArchCondition<JavaClass> no_tener_campos_double =
    //         new ArchCondition<>("no tener campos de tipo double") {
    //             @Override
    //             public void check(JavaClass clase, ConditionEvents eventos) {
    //                 clase.getFields().stream()
    //                         .filter(campo -> campo.getRawType().isEquivalentTo(double.class))
    //                         .forEach(campo -> eventos.add(SimpleConditionEvent.violated(
    //                                 campo, campo.getFullName() + " es double")));
    //             }
    //         };
    //
    // @ArchTest
    // static final ArchRule el_dinero_no_es_double =
    //         classes().that().resideInAPackage("..domain..")
    //                 .should(no_tener_campos_double)
    //                 .as("G1. El dominio no representa valores con double")
    //                 .because("...");
    //
    // (requiere importar JavaClass, ArchCondition, ConditionEvents y SimpleConditionEvent)
}
