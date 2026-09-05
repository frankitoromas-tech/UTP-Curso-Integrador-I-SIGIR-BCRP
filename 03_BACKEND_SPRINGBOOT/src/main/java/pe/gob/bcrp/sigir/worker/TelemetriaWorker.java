package pe.gob.bcrp.sigir.worker;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;
import pe.gob.bcrp.sigir.repository.EntidadFinancieraRepository;
import pe.gob.bcrp.sigir.service.IIncidenteService;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(prefix = "sigir.telemetria", name = "habilitada", havingValue = "true", matchIfMissing = true)
public class TelemetriaWorker {

    private static final Logger log = LoggerFactory.getLogger(TelemetriaWorker.class);

    private final EntidadFinancieraRepository entidadRepository;
    private final IIncidenteService incidenteService;
    private final RestTemplate restTemplate;
    private final ExecutorService executor;

    public TelemetriaWorker(EntidadFinancieraRepository entidadRepository,
                            IIncidenteService incidenteService,
                            RestTemplate restTemplate) {
        this.entidadRepository = entidadRepository;
        this.incidenteService = incidenteService;
        this.restTemplate = restTemplate;
        this.executor = Executors.newFixedThreadPool(8, r -> {
            Thread t = new Thread(r);
            t.setName("sigir-telemetria-worker-" + t.getId());
            t.setDaemon(true);
            return t;
        });
    }

    @Scheduled(fixedDelayString = "${sigir.telemetria.intervalo-milisegundos:30000}")
    public void ejecutarCicloSondeoTelemetria() {
        List<EntidadFinanciera> activas = entidadRepository.findByActivoTrue();
        log.debug("Iniciando sondeo paralelo de salud sobre {} entidades financieras...", activas.size());

        List<CompletableFuture<Void>> tareas = activas.stream()
                .filter(entidad -> entidad.getUrlHealthcheck() != null && !entidad.getUrlHealthcheck().trim().isEmpty())
                .map(entidad -> CompletableFuture.runAsync(() -> sondeoIndividual(entidad), executor))
                .toList();

        try {
            CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0]))
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.warn("El ciclo de sondeo paralelo finalizó con timeouts o excepciones parciales: {}", ex.getMessage());
        }
    }

    private void sondeoIndividual(EntidadFinanciera entidad) {
        String url = entidad.getUrlHealthcheck();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is5xxServerError()) {
                procesarFallaDetectada(entidad, "HTTP 5xx Server Error (" + response.getStatusCode().value() + ")", response.getBody());
            } else {
                log.debug("Entidad {} saludable. Status: {}", entidad.getNombreComercial(), response.getStatusCode());
            }
        } catch (ResourceAccessException ex) {
            procesarFallaDetectada(entidad, "Timeout o Conexión Rechazada en API", ex.getMessage());
        } catch (Exception ex) {
            procesarFallaDetectada(entidad, "Excepción de Red Inesperada", ex.getMessage());
        }
    }

    private void procesarFallaDetectada(EntidadFinanciera entidad, String motivo, String traza) {
        log.warn("¡ALERTA BCRP! Falla detectada en entidad: {} [{}] - Motivo: {}",
                entidad.getNombreComercial(), entidad.getCodigoBcrp(), motivo);

        boolean yaExisteIncidenteAbierto = incidenteService.existeIncidenteAbiertoParaEntidad(entidad.getIdEntidad());

        if (!yaExisteIncidenteAbierto) {
            log.info("Creando incidente automático para {} debido a caída de servicio.", entidad.getNombreComercial());
            incidenteService.registrarIncidenteAutomatico(entidad, motivo, traza);
        } else {
            log.debug("La entidad {} ya cuenta con un incidente abierto en curso. Omitiendo duplicado.", entidad.getNombreComercial());
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Cerrando pool de telemetría de forma ordenada...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Configuration
    public static class RestTemplateConfig {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder
                    .setConnectTimeout(Duration.ofMillis(3000))
                    .setReadTimeout(Duration.ofMillis(3000))
                    .build();
        }
    }
}
