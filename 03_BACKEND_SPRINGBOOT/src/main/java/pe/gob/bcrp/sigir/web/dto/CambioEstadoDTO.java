package pe.gob.bcrp.sigir.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;

import java.time.LocalDateTime;

public class CambioEstadoDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoIncidente nuevoEstado;

    @NotBlank(message = "El comentario técnico que justifica la transición es obligatorio")
    private String comentarioTecnico;

    private LocalDateTime fechaHoraSolucion; // Opcional, requerido al pasar a RESUELTO/CERRADO

    public CambioEstadoDTO() {
    }

    public CambioEstadoDTO(EstadoIncidente nuevoEstado, String comentarioTecnico, LocalDateTime fechaHoraSolucion) {
        this.nuevoEstado = nuevoEstado;
        this.comentarioTecnico = comentarioTecnico;
        this.fechaHoraSolucion = fechaHoraSolucion;
    }

    public static CambioEstadoDTOBuilder builder() {
        return new CambioEstadoDTOBuilder();
    }

    public EstadoIncidente getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(EstadoIncidente nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getComentarioTecnico() {
        return comentarioTecnico;
    }

    public void setComentarioTecnico(String comentarioTecnico) {
        this.comentarioTecnico = comentarioTecnico;
    }

    public LocalDateTime getFechaHoraSolucion() {
        return fechaHoraSolucion;
    }

    public void setFechaHoraSolucion(LocalDateTime fechaHoraSolucion) {
        this.fechaHoraSolucion = fechaHoraSolucion;
    }

    public static class CambioEstadoDTOBuilder {
        private EstadoIncidente nuevoEstado;
        private String comentarioTecnico;
        private LocalDateTime fechaHoraSolucion;

        CambioEstadoDTOBuilder() {
        }

        public CambioEstadoDTOBuilder nuevoEstado(EstadoIncidente nuevoEstado) {
            this.nuevoEstado = nuevoEstado;
            return this;
        }

        public CambioEstadoDTOBuilder comentarioTecnico(String comentarioTecnico) {
            this.comentarioTecnico = comentarioTecnico;
            return this;
        }

        public CambioEstadoDTOBuilder fechaHoraSolucion(LocalDateTime fechaHoraSolucion) {
            this.fechaHoraSolucion = fechaHoraSolucion;
            return this;
        }

        public CambioEstadoDTO build() {
            return new CambioEstadoDTO(nuevoEstado, comentarioTecnico, fechaHoraSolucion);
        }
    }
}
