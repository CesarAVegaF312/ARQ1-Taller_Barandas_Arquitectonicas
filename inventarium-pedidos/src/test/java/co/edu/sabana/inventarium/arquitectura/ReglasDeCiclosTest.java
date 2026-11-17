package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Fitness function de ciclos: cada paquete es una "rebanada" y no puede haber
 * un camino de dependencias que salga de un paquete y vuelva a el.
 *
 * NO MODIFIQUE ESTE ARCHIVO PARA QUE LAS PRUEBAS PASEN.
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglasDeCiclosTest {

    @ArchTest
    static final ArchRule no_hay_ciclos_entre_paquetes =
            slices().matching("co.edu.sabana.inventarium.(**)")
                    .should().beFreeOfCycles()
                    .as("R7. No hay ciclos de dependencias entre paquetes")
                    .because("un ciclo convierte dos paquetes en uno solo: no se pueden entender, probar ni "
                            + "reemplazar por separado. Rompa el ciclo con un puerto (inversion de "
                            + "dependencias) o moviendo la clase al paquete que realmente la usa");
}
