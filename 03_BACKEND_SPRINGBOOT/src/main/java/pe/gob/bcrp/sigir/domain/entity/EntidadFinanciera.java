package pe.gob.bcrp.sigir.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entidades_financieras")
public class EntidadFinanciera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad")
    private Long idEntidad;

    @Column(name = "codigo_bcrp", nullable = false, unique = true, length = 10)
    private String codigoBcrp;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "nombre_comercial", nullable = false, length = 80)
    private String nombreComercial;

    @Column(name = "canal_interoperable", nullable = false, length = 60)
    private String canalInteroperable;

    @Column(name = "url_healthcheck", length = 255)
    private String urlHealthcheck;

    @Column(name = "frecuencia_monitoreo_seg", nullable = false)
    private Integer frecuenciaMonitoreoSeg = 30;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public EntidadFinanciera() {
    }

    public EntidadFinanciera(Long idEntidad, String codigoBcrp, String razonSocial, String nombreComercial,
                            String canalInteroperable, String urlHealthcheck, Integer frecuenciaMonitoreoSeg,
                            Boolean activo, LocalDateTime fechaCreacion) {
        this.idEntidad = idEntidad;
        this.codigoBcrp = codigoBcrp;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.canalInteroperable = canalInteroperable;
        this.urlHealthcheck = urlHealthcheck;
        this.frecuenciaMonitoreoSeg = frecuenciaMonitoreoSeg != null ? frecuenciaMonitoreoSeg : 30;
        this.activo = activo != null ? activo : true;
        this.fechaCreacion = fechaCreacion;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

    public static EntidadFinancieraBuilder builder() {
        return new EntidadFinancieraBuilder();
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Long idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getCodigoBcrp() {
        return codigoBcrp;
    }

    public void setCodigoBcrp(String codigoBcrp) {
        this.codigoBcrp = codigoBcrp;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getCanalInteroperable() {
        return canalInteroperable;
    }

    public void setCanalInteroperable(String canalInteroperable) {
        this.canalInteroperable = canalInteroperable;
    }

    public String getUrlHealthcheck() {
        return urlHealthcheck;
    }

    public void setUrlHealthcheck(String urlHealthcheck) {
        this.urlHealthcheck = urlHealthcheck;
    }

    public Integer getFrecuenciaMonitoreoSeg() {
        return frecuenciaMonitoreoSeg;
    }

    public void setFrecuenciaMonitoreoSeg(Integer frecuenciaMonitoreoSeg) {
        this.frecuenciaMonitoreoSeg = frecuenciaMonitoreoSeg;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static class EntidadFinancieraBuilder {
        private Long idEntidad;
        private String codigoBcrp;
        private String razonSocial;
        private String nombreComercial;
        private String canalInteroperable;
        private String urlHealthcheck;
        private Integer frecuenciaMonitoreoSeg = 30;
        private Boolean activo = true;
        private LocalDateTime fechaCreacion;

        EntidadFinancieraBuilder() {
        }

        public EntidadFinancieraBuilder idEntidad(Long idEntidad) {
            this.idEntidad = idEntidad;
            return this;
        }

        public EntidadFinancieraBuilder codigoBcrp(String codigoBcrp) {
            this.codigoBcrp = codigoBcrp;
            return this;
        }

        public EntidadFinancieraBuilder razonSocial(String razonSocial) {
            this.razonSocial = razonSocial;
            return this;
        }

        public EntidadFinancieraBuilder nombreComercial(String nombreComercial) {
            this.nombreComercial = nombreComercial;
            return this;
        }

        public EntidadFinancieraBuilder canalInteroperable(String canalInteroperable) {
            this.canalInteroperable = canalInteroperable;
            return this;
        }

        public EntidadFinancieraBuilder urlHealthcheck(String urlHealthcheck) {
            this.urlHealthcheck = urlHealthcheck;
            return this;
        }

        public EntidadFinancieraBuilder frecuenciaMonitoreoSeg(Integer frecuenciaMonitoreoSeg) {
            this.frecuenciaMonitoreoSeg = frecuenciaMonitoreoSeg;
            return this;
        }

        public EntidadFinancieraBuilder activo(Boolean activo) {
            this.activo = activo;
            return this;
        }

        public EntidadFinancieraBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public EntidadFinanciera build() {
            return new EntidadFinanciera(idEntidad, codigoBcrp, razonSocial, nombreComercial,
                    canalInteroperable, urlHealthcheck, frecuenciaMonitoreoSeg, activo, fechaCreacion);
        }
    }
}
