package pe.gob.bcrp.sigir.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_incidente", nullable = false)
    private Incidente incidente;

    @Column(name = "estado_anterior", length = 25)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 25)
    private String estadoNuevo;

    @Column(name = "fecha_transicion", nullable = false)
    private LocalDateTime fechaTransicion;

    @Column(name = "usuario_responsable", nullable = false, length = 80)
    private String usuarioResponsable;

    @Column(name = "comentario_tecnico", nullable = false, columnDefinition = "TEXT")
    private String comentarioTecnico;

    public HistorialEstado() {
    }

    public HistorialEstado(Long idHistorial, Incidente incidente, String estadoAnterior, String estadoNuevo,
                           LocalDateTime fechaTransicion, String usuarioResponsable, String comentarioTecnico) {
        this.idHistorial = idHistorial;
        this.incidente = incidente;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaTransicion = fechaTransicion;
        this.usuarioResponsable = usuarioResponsable;
        this.comentarioTecnico = comentarioTecnico;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaTransicion == null) {
            this.fechaTransicion = LocalDateTime.now();
        }
    }

    public static HistorialEstadoBuilder builder() {
        return new HistorialEstadoBuilder();
    }

    public Long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaTransicion() {
        return fechaTransicion;
    }

    public void setFechaTransicion(LocalDateTime fechaTransicion) {
        this.fechaTransicion = fechaTransicion;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public String getComentarioTecnico() {
        return comentarioTecnico;
    }

    public void setComentarioTecnico(String comentarioTecnico) {
        this.comentarioTecnico = comentarioTecnico;
    }

    public static class HistorialEstadoBuilder {
        private Long idHistorial;
        private Incidente incidente;
        private String estadoAnterior;
        private String estadoNuevo;
        private LocalDateTime fechaTransicion;
        private String usuarioResponsable;
        private String comentarioTecnico;

        HistorialEstadoBuilder() {
        }

        public HistorialEstadoBuilder idHistorial(Long idHistorial) {
            this.idHistorial = idHistorial;
            return this;
        }

        public HistorialEstadoBuilder incidente(Incidente incidente) {
            this.incidente = incidente;
            return this;
        }

        public HistorialEstadoBuilder estadoAnterior(String estadoAnterior) {
            this.estadoAnterior = estadoAnterior;
            return this;
        }

        public HistorialEstadoBuilder estadoNuevo(String estadoNuevo) {
            this.estadoNuevo = estadoNuevo;
            return this;
        }

        public HistorialEstadoBuilder fechaTransicion(LocalDateTime fechaTransicion) {
            this.fechaTransicion = fechaTransicion;
            return this;
        }

        public HistorialEstadoBuilder usuarioResponsable(String usuarioResponsable) {
            this.usuarioResponsable = usuarioResponsable;
            return this;
        }

        public HistorialEstadoBuilder comentarioTecnico(String comentarioTecnico) {
            this.comentarioTecnico = comentarioTecnico;
            return this;
        }

        public HistorialEstado build() {
            return new HistorialEstado(idHistorial, incidente, estadoAnterior, estadoNuevo, fechaTransicion, usuarioResponsable, comentarioTecnico);
        }
    }
}
