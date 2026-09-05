package pe.gob.bcrp.sigir.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.gob.bcrp.sigir.domain.enums.Severidad;

import java.time.LocalDateTime;

public class IncidenteRegistroDTO {

    @NotNull(message = "El identificador de la entidad es obligatorio")
    private Long idEntidad;

    @NotBlank(message = "El código normativo de la categoría es obligatorio")
    private String codigoCategoria; // 'DISP_SERV', 'FRAUDE_FIN', 'BRECHA_DATOS'

    @NotNull(message = "La severidad es obligatoria")
    private Severidad severidad;

    @NotNull(message = "La fecha y hora de inicio del incidente es obligatoria")
    private LocalDateTime fechaHoraInicio;

    @NotBlank(message = "El servicio o componente afectado es obligatorio")
    private String servicioAfectado;

    @NotBlank(message = "La descripción técnica del incidente es obligatoria")
    private String descripcionDetallada;

    private Integer impactoEstimadoUsuarios = 0;

    public IncidenteRegistroDTO() {
    }

    public IncidenteRegistroDTO(Long idEntidad, String codigoCategoria, Severidad severidad,
                                LocalDateTime fechaHoraInicio, String servicioAfectado,
                                String descripcionDetallada, Integer impactoEstimadoUsuarios) {
        this.idEntidad = idEntidad;
        this.codigoCategoria = codigoCategoria;
        this.severidad = severidad;
        this.fechaHoraInicio = fechaHoraInicio;
        this.servicioAfectado = servicioAfectado;
        this.descripcionDetallada = descripcionDetallada;
        this.impactoEstimadoUsuarios = impactoEstimadoUsuarios != null ? impactoEstimadoUsuarios : 0;
    }

    public static IncidenteRegistroDTOBuilder builder() {
        return new IncidenteRegistroDTOBuilder();
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Long idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public void setSeveridad(Severidad severidad) {
        this.severidad = severidad;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getServicioAfectado() {
        return servicioAfectado;
    }

    public void setServicioAfectado(String servicioAfectado) {
        this.servicioAfectado = servicioAfectado;
    }

    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }

    public void setDescripcionDetallada(String descripcionDetallada) {
        this.descripcionDetallada = descripcionDetallada;
    }

    public Integer getImpactoEstimadoUsuarios() {
        return impactoEstimadoUsuarios;
    }

    public void setImpactoEstimadoUsuarios(Integer impactoEstimadoUsuarios) {
        this.impactoEstimadoUsuarios = impactoEstimadoUsuarios;
    }

    public static class IncidenteRegistroDTOBuilder {
        private Long idEntidad;
        private String codigoCategoria;
        private Severidad severidad;
        private LocalDateTime fechaHoraInicio;
        private String servicioAfectado;
        private String descripcionDetallada;
        private Integer impactoEstimadoUsuarios = 0;

        IncidenteRegistroDTOBuilder() {
        }

        public IncidenteRegistroDTOBuilder idEntidad(Long idEntidad) {
            this.idEntidad = idEntidad;
            return this;
        }

        public IncidenteRegistroDTOBuilder codigoCategoria(String codigoCategoria) {
            this.codigoCategoria = codigoCategoria;
            return this;
        }

        public IncidenteRegistroDTOBuilder severidad(Severidad severidad) {
            this.severidad = severidad;
            return this;
        }

        public IncidenteRegistroDTOBuilder fechaHoraInicio(LocalDateTime fechaHoraInicio) {
            this.fechaHoraInicio = fechaHoraInicio;
            return this;
        }

        public IncidenteRegistroDTOBuilder servicioAfectado(String servicioAfectado) {
            this.servicioAfectado = servicioAfectado;
            return this;
        }

        public IncidenteRegistroDTOBuilder descripcionDetallada(String descripcionDetallada) {
            this.descripcionDetallada = descripcionDetallada;
            return this;
        }

        public IncidenteRegistroDTOBuilder impactoEstimadoUsuarios(Integer impactoEstimadoUsuarios) {
            this.impactoEstimadoUsuarios = impactoEstimadoUsuarios;
            return this;
        }

        public IncidenteRegistroDTO build() {
            return new IncidenteRegistroDTO(idEntidad, codigoCategoria, severidad, fechaHoraInicio,
                    servicioAfectado, descripcionDetallada, impactoEstimadoUsuarios);
        }
    }
}
