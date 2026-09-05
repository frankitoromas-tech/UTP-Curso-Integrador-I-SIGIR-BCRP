package pe.gob.bcrp.sigir.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;
import pe.gob.bcrp.sigir.service.EntidadFinancieraService;
import pe.gob.bcrp.sigir.web.dto.EntidadSaludDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entidades")
@Tag(name = "Entidades Financieras", description = "Catálogo de bancos, billeteras y switches del sistema de pagos")
public class EntidadFinancieraController {

    private final EntidadFinancieraService entidadService;

    public EntidadFinancieraController(EntidadFinancieraService entidadService) {
        this.entidadService = entidadService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las entidades financieras registradas")
    public ResponseEntity<List<EntidadFinanciera>> listarTodas() {
        return ResponseEntity.ok(entidadService.listarTodas());
    }

    @GetMapping("/salud")
    @Operation(summary = "Obtener el estado de salud y semáforos de telemetría en tiempo real")
    public ResponseEntity<List<EntidadSaludDTO>> obtenerSalud() {
        return ResponseEntity.ok(entidadService.obtenerMonitoreoSalud());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entidad por ID")
    public ResponseEntity<EntidadFinanciera> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entidadService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva entidad financiera en el sistema")
    public ResponseEntity<EntidadFinanciera> crear(@Valid @RequestBody EntidadFinanciera entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadService.guardar(entidad));
    }
}
