package pe.gob.bcrp.sigir.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_regulatorios_bcrp")
public class ReporteBCRP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @Column(name = "numero_envio", nullable = false, unique = true, length = 40)
    private String numeroEnvio;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "nombre_archivo", nullable = false, length = 120)
    private String nombreArchivo;

    @Column(name = "total_registros", nullable = false)
    private Integer totalRegistros;

    @Column(name = "hash_sha256", nullable = false, length = 64)
    private String hashSha256;

    @Column(name = "estado_envio", nullable = false, length = 20)
    private String estadoEnvio = "PENDIENTE";

    @Column(name = "contenido_plano", nullable = false, columnDefinition = "TEXT")
    private String contenidoPlano;

    @Column(name = "usuario_generador", nullable = false, length = 80)
    private String usuarioGenerador;

    public ReporteBCRP() {
    }

    public ReporteBCRP(Long idReporte, String numeroEnvio, LocalDateTime fechaGeneracion, String nombreArchivo,
                       Integer totalRegistros, String hashSha256, String estadoEnvio, String contenidoPlano,
                       String usuarioGenerador) {
        this.idReporte = idReporte;
        this.numeroEnvio = numeroEnvio;
        this.fechaGeneracion = fechaGeneracion;
        this.nombreArchivo = nombreArchivo;
        this.totalRegistros = totalRegistros;
        this.hashSha256 = hashSha256;
        this.estadoEnvio = estadoEnvio != null ? estadoEnvio : "PENDIENTE";
        this.contenidoPlano = contenidoPlano;
        this.usuarioGenerador = usuarioGenerador;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaGeneracion == null) {
            this.fechaGeneracion = LocalDateTime.now();
        }
    }

    public static ReporteBCRPBuilder builder() {
        return new ReporteBCRPBuilder();
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getNumeroEnvio() {
        return numeroEnvio;
    }

    public void setNumeroEnvio(String numeroEnvio) {
        this.numeroEnvio = numeroEnvio;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getContenidoPlano() {
        return contenidoPlano;
    }

    public void setContenidoPlano(String contenidoPlano) {
        this.contenidoPlano = contenidoPlano;
    }

    public String getUsuarioGenerador() {
        return usuarioGenerador;
    }

    public void setUsuarioGenerador(String usuarioGenerador) {
        this.usuarioGenerador = usuarioGenerador;
    }

    public static class ReporteBCRPBuilder {
        private Long idReporte;
        private String numeroEnvio;
        private LocalDateTime fechaGeneracion;
        private String nombreArchivo;
        private Integer totalRegistros;
        private String hashSha256;
        private String estadoEnvio = "PENDIENTE";
        private String contenidoPlano;
        private String usuarioGenerador;

        ReporteBCRPBuilder() {
        }

        public ReporteBCRPBuilder idReporte(Long idReporte) {
            this.idReporte = idReporte;
            return this;
        }

        public ReporteBCRPBuilder numeroEnvio(String numeroEnvio) {
            this.numeroEnvio = numeroEnvio;
            return this;
        }

        public ReporteBCRPBuilder fechaGeneracion(LocalDateTime fechaGeneracion) {
            this.fechaGeneracion = fechaGeneracion;
            return this;
        }

        public ReporteBCRPBuilder nombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
            return this;
        }

        public ReporteBCRPBuilder totalRegistros(Integer totalRegistros) {
            this.totalRegistros = totalRegistros;
            return this;
        }

        public ReporteBCRPBuilder hashSha256(String hashSha256) {
            this.hashSha256 = hashSha256;
            return this;
        }

        public ReporteBCRPBuilder estadoEnvio(String estadoEnvio) {
            this.estadoEnvio = estadoEnvio;
            return this;
        }

        public ReporteBCRPBuilder contenidoPlano(String contenidoPlano) {
            this.contenidoPlano = contenidoPlano;
            return this;
        }

        public ReporteBCRPBuilder usuarioGenerador(String usuarioGenerador) {
            this.usuarioGenerador = usuarioGenerador;
            return this;
        }

        public ReporteBCRP build() {
            return new ReporteBCRP(idReporte, numeroEnvio, fechaGeneracion, nombreArchivo,
                    totalRegistros, hashSha256, estadoEnvio, contenidoPlano, usuarioGenerador);
        }
    }
}
