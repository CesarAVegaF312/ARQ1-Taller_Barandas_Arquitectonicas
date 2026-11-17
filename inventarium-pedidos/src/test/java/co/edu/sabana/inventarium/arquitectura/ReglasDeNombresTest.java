package co.edu.sabana.inventarium.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Fitness functions de convenciones de nombres. Un nombre consistente es lo
 * que permite que una regla (y una persona, y un agente) encuentre las piezas.
 *
 * NO MODIFIQUE ESTE ARCHIVO PARA QUE LAS PRUEBAS PASEN.
 */
@AnalyzeClasses(packages = "co.edu.sabana.inventarium", importOptions = ImportOption.DoNotIncludeTests.class)
class ReglasDeNombresTest {

    @ArchTest
    static final ArchRule los_puertos_primarios_se_llaman_caso_de_uso =
            classes().that().resideInAPackage("..application.port.in..").and().areInterfaces()
                    .should().haveSimpleNameEndingWith("CasoDeUso")
                    .as("N1. Los puertos primarios (application.port.in) terminan en CasoDeUso")
                    .because("el puerto primario es la API del caso de uso: su nombre dice que hace el "
                            + "sistema, por ejemplo CrearPedidoCasoDeUso");

    @ArchTest
    static final ArchRule los_puertos_secundarios_son_interfaces =
            classes().that().resideInAPackage("..application.port.out..")
                    .should().beInterfaces()
                    .as("N2. Todo lo que hay en application.port.out es una interfaz")
                    .because("un puerto secundario es un contrato que declara el negocio y que la "
                            + "infraestructura implementa; una clase concreta ahi ya es un adaptador");

    @ArchTest
    static final ArchRule los_puertos_no_nombran_tecnologia =
            noClasses().that().resideInAPackage("..application.port..")
                    .should().haveNameMatching(
                            ".*\\.[^.]*(Jpa|Jdbc|Sql|H2|Mongo|Redis|Kafka|Rabbit|Http|Rest|Smtp|Email|EnMemoria|Spring)[^.]*")
                    .as("N3. Los puertos se nombran con vocabulario de negocio, no de tecnologia")
                    .because("si el nombre de la interfaz menciona una tecnologia (JpaRepository, "
                            + "KafkaPublisher, EmailSender) probablemente es un adaptador disfrazado. Un "
                            + "puerto se llama RepositorioDePedidos, NotificadorDeCliente, PasarelaDePagos");

    @ArchTest
    static final ArchRule los_adaptadores_secundarios_se_llaman_adaptador =
            classes().that().areAssignableTo(resideInAPackage("..application.port.out.."))
                    .and().areNotInterfaces()
                    .should().haveSimpleNameEndingWith("Adaptador")
                    .andShould().resideInAPackage("..infrastructure..")
                    .as("N4. Quien implementa un puerto secundario termina en Adaptador y vive en infrastructure")
                    .because("el adaptador traduce entre el contrato del negocio y una tecnologia concreta; "
                            + "el sufijo permite encontrarlos y el paquete garantiza que el nucleo no los vea");

    @ArchTest
    static final ArchRule los_casos_de_uso_se_llaman_servicio =
            classes().that().resideInAPackage("..application.usecase..")
                    .should().haveSimpleNameEndingWith("Servicio")
                    .andShould().beAssignableTo(resideInAPackage("..application.port.in.."))
                    .as("N5. Las clases de application.usecase terminan en Servicio e implementan un puerto primario")
                    .because("cada implementacion de caso de uso cumple un contrato de port/in; si no lo "
                            + "implementa, nadie fuera de application deberia poder usarla");

    @ArchTest
    static final ArchRule los_controladores_se_llaman_controlador =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().haveSimpleNameEndingWith("Controlador")
                    .as("N6. Los controladores REST terminan en Controlador")
                    .because("la convencion del equipo es nombrar en espanol el rol de cada clase "
                            + "(Controlador, Servicio, Adaptador, CasoDeUso)");
}
