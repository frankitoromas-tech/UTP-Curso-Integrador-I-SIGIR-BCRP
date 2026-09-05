package pe.gob.bcrp.sigir.web.dto;

public class EntidadSaludDTO {
    private Long idEntidad;
    private String codigoBcrp;
    private String nombreComercial;
    private String canalInteroperable;
    private String estadoSalud;      // 'SALUDABLE', 'DEGRADADO', 'CAIDO'
    private Integer latenciaMs;
    private Integer incidentesActivos;
    private String ultimoSondeo;

    public EntidadSaludDTO() {
    }

    public EntidadSaludDTO(Long idEntidad, String codigoBcrp, String nombreComercial, String canalInteroperable,
                           String estadoSalud, Integer latenciaMs, Integer incidentesActivos, String ultimoSondeo) {
        this.idEntidad = idEntidad;
        this.codigoBcrp = codigoBcrp;
        this.nombreComercial = nombreComercial;
        this.canalInteroperable = canalInteroperable;
        this.estadoSalud = estadoSalud;
        this.latenciaMs = latenciaMs;
        this.incidentesActivos = incidentesActivos;
        this.ultimoSondeo = ultimoSondeo;
    }

    public static EntidadSaludDTOBuilder builder() {
        return new EntidadSaludDTOBuilder();
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

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public Integer getLatenciaMs() {
        return latenciaMs;
    }

    public void setLatenciaMs(Integer latenciaMs) {
        this.latenciaMs = latenciaMs;
    }

    public Integer getIncidentesActivos() {
        return incidentesActivos;
    }

    public void setIncidentesActivos(Integer incidentesActivos) {
        this.incidentesActivos = incidentesActivos;
    }

    public String getUltimoSondeo() {
        return ultimoSondeo;
    }

    public void setUltimoSondeo(String ultimoSondeo) {
        this.ultimoSondeo = ultimoSondeo;
    }

    public static class EntidadSaludDTOBuilder {
        private Long idEntidad;
        private String codigoBcrp;
        private String nombreComercial;
        private String canalInteroperable;
        private String estadoSalud;
        private Integer latenciaMs;
        private Integer incidentesActivos;
        private String ultimoSondeo;

        EntidadSaludDTOBuilder() {
        }

        public EntidadSaludDTOBuilder idEntidad(Long idEntidad) {
            this.idEntidad = idEntidad;
            return this;
        }

        public EntidadSaludDTOBuilder codigoBcrp(String codigoBcrp) {
            this.codigoBcrp = codigoBcrp;
            return this;
        }

        public EntidadSaludDTOBuilder nombreComercial(String nombreComercial) {
            this.nombreComercial = nombreComercial;
            return this;
        }

        public EntidadSaludDTOBuilder canalInteroperable(String canalInteroperable) {
            this.canalInteroperable = canalInteroperable;
            return this;
        }

        public EntidadSaludDTOBuilder estadoSalud(String estadoSalud) {
            this.estadoSalud = estadoSalud;
            return this;
        }

        public EntidadSaludDTOBuilder latenciaMs(Integer latenciaMs) {
            this.latenciaMs = latenciaMs;
            return this;
        }

        public EntidadSaludDTOBuilder incidentesActivos(Integer incidentesActivos) {
            this.incidentesActivos = incidentesActivos;
            return this;
        }

        public EntidadSaludDTOBuilder ultimoSondeo(String ultimoSondeo) {
            this.ultimoSondeo = ultimoSondeo;
            return this;
        }

        public EntidadSaludDTO build() {
            return new EntidadSaludDTO(idEntidad, codigoBcrp, nombreComercial, canalInteroperable,
                    estadoSalud, latenciaMs, incidentesActivos, ultimoSondeo);
        }
    }
}
