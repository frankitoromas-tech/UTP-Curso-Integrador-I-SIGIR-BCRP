package pe.gob.bcrp.sigir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SigirApplication {

    private static final Logger log = LoggerFactory.getLogger(SigirApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SigirApplication.class, args);
        log.info("==========================================================================");
        log.info(" SIGIR - BCRP: Sistema de Gestión de Incidentes Regulatorios Iniciado");
        log.info(" Ecosistema: Pagos Digitales Peruano (Yape, Plim, CCE, Tunki)");
        log.info(" Documentación OpenAPI Swagger: http://localhost:8080/swagger-ui.html");
        log.info("==========================================================================");
    }
}
