package pe.gob.bcrp.sigir.domain.entity;

import jakarta.persistence.*;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.OrigenDeteccion;
import pe.gob.bcrp.sigir.domain.enums.Severidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidente")
    private Long idIncidente;

    @Column(name = "codigo_ticket", nullable = false, unique = true, length = 30)
    private String codigoTicket;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_entidad", nullable = false)
    private EntidadFinanciera entidad;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaIncidente categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "severidad", nullable = false, length = 15)
    private Severidad severidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen_deteccion", nullable = false, length = 20)
    private OrigenDeteccion origenDeteccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false, length = 25)
    private EstadoIncidente estadoActual = EstadoIncidente.REGISTRADO;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_deteccion", nullable = false)
    private LocalDateTime fechaHoraDeteccion;

    @Column(name = "fecha_hora_solucion")
    private LocalDateTime fechaHoraSolucion;

    @Column(name = "servicio_afectado", nullable = false, length = 100)
    private String servicioAfectado;

    @Column(name = "descripcion_detallada", nullable = false, columnDefinition = "TEXT")
    private String descripcionDetallada;

    @Column(name = "impacto_estimado_usuarios", nullable = false)
    private Integer impactoEstimadoUsuarios = 0;

    @Column(name = "usuario_creador", nullable = false, length = 80)
    private String usuarioCreador;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistorialEstado> historial = new ArrayList<>();

    public Incidente() {
    }

    public Incidente(Long idIncidente, String codigoTicket, EntidadFinanciera entidad, CategoriaIncidente categoria,
                     Severidad severidad, OrigenDeteccion origenDeteccion, EstadoIncidente estadoActual,
                     LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraDeteccion, LocalDateTime fechaHoraSolucion,
                     String servicioAfectado, String descripcionDetallada, Integer impactoEstimadoUsuarios,
                     String usuarioCreador, LocalDateTime fechaCreacion, List<HistorialEstado> historial) {
        this.idIncidente = idIncidente;
        this.codigoTicket = codigoTicket;
        this.entidad = entidad;
        this.categoria = categoria;
        this.severidad = severidad;
        this.origenDeteccion = origenDeteccion;
        this.estadoActual = estadoActual != null ? estadoActual : EstadoIncidente.REGISTRADO;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraDeteccion = fechaHoraDeteccion;
        this.fechaHoraSolucion = fechaHoraSolucion;
        this.servicioAfectado = servicioAfectado;
        this.descripcionDetallada = descripcionDetallada;
        this.impactoEstimadoUsuarios = impactoEstimadoUsuarios != null ? impactoEstimadoUsuarios : 0;
        this.usuarioCreador = usuarioCreador;
        this.fechaCreacion = fechaCreacion;
        this.historial = historial != null ? historial : new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

    public void agregarHistorial(EstadoIncidente anterior, EstadoIncidente nuevo, String usuario, String motivo) {
        HistorialEstado h = HistorialEstado.builder()
                .incidente(this)
                .estadoAnterior(anterior != null ? anterior.name() : null)
                .estadoNuevo(nuevo.name())
                .fechaTransicion(LocalDateTime.now())
                .usuarioResponsable(usuario)
                .comentarioTecnico(motivo)
                .build();
        this.historial.add(h);
    }

    public static IncidenteBuilder builder() {
        return new IncidenteBuilder();
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

    public EntidadFinanciera getEntidad() {
        return entidad;
    }

    public void setEntidad(EntidadFinanciera entidad) {
        this.entidad = entidad;
    }

    public CategoriaIncidente getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaIncidente categoria) {
        this.categoria = categoria;
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

    public List<HistorialEstado> getHistorial() {
        return historial;
    }

    public void setHistorial(List<HistorialEstado> historial) {
        this.historial = historial;
    }

    public static class IncidenteBuilder {
        private Long idIncidente;
        private String codigoTicket;
        private EntidadFinanciera entidad;
        private CategoriaIncidente categoria;
        private Severidad severidad;
        private OrigenDeteccion origenDeteccion;
        private EstadoIncidente estadoActual = EstadoIncidente.REGISTRADO;
        private LocalDateTime fechaHoraInicio;
        private LocalDateTime fechaHoraDeteccion;
        private LocalDateTime fechaHoraSolucion;
        private String servicioAfectado;
        private String descripcionDetallada;
        private Integer impactoEstimadoUsuarios = 0;
        private String usuarioCreador;
        private LocalDateTime fechaCreacion;
        private List<HistorialEstado> historial = new ArrayList<>();

        IncidenteBuilder() {
        }

        public IncidenteBuilder idIncidente(Long idIncidente) {
            this.idIncidente = idIncidente;
            return this;
        }

        public IncidenteBuilder codigoTicket(String codigoTicket) {
            this.codigoTicket = codigoTicket;
            return this;
        }

        public IncidenteBuilder entidad(EntidadFinanciera entidad) {
            this.entidad = entidad;
            return this;
        }

        public IncidenteBuilder categoria(CategoriaIncidente categoria) {
            this.categoria = categoria;
            return this;
        }

        public IncidenteBuilder severidad(Severidad severidad) {
            this.severidad = severidad;
            return this;
        }

        public IncidenteBuilder origenDeteccion(OrigenDeteccion origenDeteccion) {
            this.origenDeteccion = origenDeteccion;
            return this;
        }

        public IncidenteBuilder estadoActual(EstadoIncidente estadoActual) {
            this.estadoActual = estadoActual;
            return this;
        }

        public IncidenteBuilder fechaHoraInicio(LocalDateTime fechaHoraInicio) {
            this.fechaHoraInicio = fechaHoraInicio;
            return this;
        }

        public IncidenteBuilder fechaHoraDeteccion(LocalDateTime fechaHoraDeteccion) {
            this.fechaHoraDeteccion = fechaHoraDeteccion;
            return this;
        }

        public IncidenteBuilder fechaHoraSolucion(LocalDateTime fechaHoraSolucion) {
            this.fechaHoraSolucion = fechaHoraSolucion;
            return this;
        }

        public IncidenteBuilder servicioAfectado(String servicioAfectado) {
            this.servicioAfectado = servicioAfectado;
            return this;
        }

        public IncidenteBuilder descripcionDetallada(String descripcionDetallada) {
            this.descripcionDetallada = descripcionDetallada;
            return this;
        }

        public IncidenteBuilder impactoEstimadoUsuarios(Integer impactoEstimadoUsuarios) {
            this.impactoEstimadoUsuarios = impactoEstimadoUsuarios;
            return this;
        }

        public IncidenteBuilder usuarioCreador(String usuarioCreador) {
            this.usuarioCreador = usuarioCreador;
            return this;
        }

        public IncidenteBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public IncidenteBuilder historial(List<HistorialEstado> historial) {
            this.historial = historial;
            return this;
        }

        public Incidente build() {
            return new Incidente(idIncidente, codigoTicket, entidad, categoria, severidad, origenDeteccion,
                    estadoActual, fechaHoraInicio, fechaHoraDeteccion, fechaHoraSolucion, servicioAfectado,
                    descripcionDetallada, impactoEstimadoUsuarios, usuarioCreador, fechaCreacion, historial);
        }
    }
}
