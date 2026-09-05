package pe.gob.bcrp.sigir.web.dto;

import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.OrigenDeteccion;
import pe.gob.bcrp.sigir.domain.enums.Severidad;

import java.time.LocalDateTime;

public class IncidenteResponseDTO {
    private Long idIncidente;
    private String codigoTicket;
    private Long idEntidad;
    private String codigoBcrpEntidad;
    private String nombreEntidad;
    private String codigoCategoria;
    private String nombreCategoria;
    private Severidad severidad;
    private OrigenDeteccion origenDeteccion;
    private EstadoIncidente estadoActual;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraDeteccion;
    private LocalDateTime fechaHoraSolucion;
    private String servicioAfectado;
    private String descripcionDetallada;
    private Integer impactoEstimadoUsuarios;
    private String usuarioCreador;
    private LocalDateTime fechaCreacion;

    public IncidenteResponseDTO() {
    }

    public IncidenteResponseDTO(Long idIncidente, String codigoTicket, Long idEntidad, String codigoBcrpEntidad,
                                String nombreEntidad, String codigoCategoria, String nombreCategoria,
                                Severidad severidad, OrigenDeteccion origenDeteccion, EstadoIncidente estadoActual,
                                LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraDeteccion,
                                LocalDateTime fechaHoraSolucion, String servicioAfectado, String descripcionDetallada,
                                Integer impactoEstimadoUsuarios, String usuarioCreador, LocalDateTime fechaCreacion) {
        this.idIncidente = idIncidente;
        this.codigoTicket = codigoTicket;
        this.idEntidad = idEntidad;
        this.codigoBcrpEntidad = codigoBcrpEntidad;
        this.nombreEntidad = nombreEntidad;
        this.codigoCategoria = codigoCategoria;
        this.nombreCategoria = nombreCategoria;
        this.severidad = severidad;
        this.origenDeteccion = origenDeteccion;
        this.estadoActual = estadoActual;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraDeteccion = fechaHoraDeteccion;
        this.fechaHoraSolucion = fechaHoraSolucion;
        this.servicioAfectado = servicioAfectado;
        this.descripcionDetallada = descripcionDetallada;
        this.impactoEstimadoUsuarios = impactoEstimadoUsuarios;
        this.usuarioCreador = usuarioCreador;
        this.fechaCreacion = fechaCreacion;
    }

    public static IncidenteResponseDTOBuilder builder() {
        return new IncidenteResponseDTOBuilder();
    }

    public Long getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(Long idIncidente) {
        this.idIncidente = idIncidente;
    }

    public String getCodigoTicket() {
        return codigoTicket;
    }

    public void setCodigoTicket(String codigoTicket) {
        this.codigoTicket = codigoTicket;
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Long idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getCodigoBcrpEntidad() {
        return codigoBcrpEntidad;
    }

    public void setCodigoBcrpEntidad(String codigoBcrpEntidad) {
        this.codigoBcrpEntidad = codigoBcrpEntidad;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public void setSeveridad(Severidad severidad) {
        this.severidad = severidad;
    }

    public OrigenDeteccion getOrigenDeteccion() {
        return origenDeteccion;
    }

    public void setOrigenDeteccion(OrigenDeteccion origenDeteccion) {
        this.origenDeteccion = origenDeteccion;
    }

    public EstadoIncidente getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoIncidente estadoActual) {
        this.estadoActual = estadoActual;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraDeteccion() {
        return fechaHoraDeteccion;
    }

    public void setFechaHoraDeteccion(LocalDateTime fechaHoraDeteccion) {
        this.fechaHoraDeteccion = fechaHoraDeteccion;
    }

    public LocalDateTime getFechaHoraSolucion() {
        return fechaHoraSolucion;
    }

    public void setFechaHoraSolucion(LocalDateTime fechaHoraSolucion) {
        this.fechaHoraSolucion = fechaHoraSolucion;
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

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static class IncidenteResponseDTOBuilder {
        private Long idIncidente;
        private String codigoTicket;
        private Long idEntidad;
        private String codigoBcrpEntidad;
        private String nombreEntidad;
        private String codigoCategoria;
        private String nombreCategoria;
        private Severidad severidad;
        private OrigenDeteccion origenDeteccion;
        private EstadoIncidente estadoActual;
        private LocalDateTime fechaHoraInicio;
        private LocalDateTime fechaHoraDeteccion;
        private LocalDateTime fechaHoraSolucion;
        private String servicioAfectado;
        private String descripcionDetallada;
        private Integer impactoEstimadoUsuarios;
        private String usuarioCreador;
        private LocalDateTime fechaCreacion;

        IncidenteResponseDTOBuilder() {
        }

        public IncidenteResponseDTOBuilder idIncidente(Long idIncidente) {
            this.idIncidente = idIncidente;
            return this;
        }

        public IncidenteResponseDTOBuilder codigoTicket(String codigoTicket) {
            this.codigoTicket = codigoTicket;
            return this;
        }

        public IncidenteResponseDTOBuilder idEntidad(Long idEntidad) {
            this.idEntidad = idEntidad;
            return this;
        }

        public IncidenteResponseDTOBuilder codigoBcrpEntidad(String codigoBcrpEntidad) {
            this.codigoBcrpEntidad = codigoBcrpEntidad;
            return this;
        }

        public IncidenteResponseDTOBuilder nombreEntidad(String nombreEntidad) {
            this.nombreEntidad = nombreEntidad;
            return this;
        }

        public IncidenteResponseDTOBuilder codigoCategoria(String codigoCategoria) {
            this.codigoCategoria = codigoCategoria;
            return this;
        }

        public IncidenteResponseDTOBuilder nombreCategoria(String nombreCategoria) {
            this.nombreCategoria = nombreCategoria;
            return this;
        }

        public IncidenteResponseDTOBuilder severidad(Severidad severidad) {
            this.severidad = severidad;
            return this;
        }

        public IncidenteResponseDTOBuilder origenDeteccion(OrigenDeteccion origenDeteccion) {
            this.origenDeteccion = origenDeteccion;
            return this;
        }

        public IncidenteResponseDTOBuilder estadoActual(EstadoIncidente estadoActual) {
            this.estadoActual = estadoActual;
            return this;
        }

        public IncidenteResponseDTOBuilder fechaHoraInicio(LocalDateTime fechaHoraInicio) {
            this.fechaHoraInicio = fechaHoraInicio;
            return this;
        }

        public IncidenteResponseDTOBuilder fechaHoraDeteccion(LocalDateTime fechaHoraDeteccion) {
            this.fechaHoraDeteccion = fechaHoraDeteccion;
            return this;
        }

        public IncidenteResponseDTOBuilder fechaHoraSolucion(LocalDateTime fechaHoraSolucion) {
            this.fechaHoraSolucion = fechaHoraSolucion;
            return this;
        }

        public IncidenteResponseDTOBuilder servicioAfectado(String servicioAfectado) {
            this.servicioAfectado = servicioAfectado;
            return this;
        }

        public IncidenteResponseDTOBuilder descripcionDetallada(String descripcionDetallada) {
            this.descripcionDetallada = descripcionDetallada;
            return this;
        }

        public IncidenteResponseDTOBuilder impactoEstimadoUsuarios(Integer impactoEstimadoUsuarios) {
            this.impactoEstimadoUsuarios = impactoEstimadoUsuarios;
            return this;
        }

        public IncidenteResponseDTOBuilder usuarioCreador(String usuarioCreador) {
            this.usuarioCreador = usuarioCreador;
            return this;
        }

        public IncidenteResponseDTOBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public IncidenteResponseDTO build() {
            return new IncidenteResponseDTO(idIncidente, codigoTicket, idEntidad, codigoBcrpEntidad,
                    nombreEntidad, codigoCategoria, nombreCategoria, severidad, origenDeteccion,
                    estadoActual, fechaHoraInicio, fechaHoraDeteccion, fechaHoraSolucion,
                    servicioAfectado, descripcionDetallada, impactoEstimadoUsuarios, usuarioCreador, fechaCreacion);
        }
    }
}
