package pe.gob.bcrp.sigir.web.dto;

public class IndisponibilidadDTO {
    private Long idEntidad;
    private String codigoBcrp;
    private String nombreEntidad;
    private long totalIncidentes;
    private long incidentesCriticos;
    private Double mttrMinutos;
    private Double mttdMinutos;
    private long minutosIndisponibles;
    private Double uptimePct;

    public IndisponibilidadDTO() {}

    public Long getIdEntidad() { return idEntidad; }
    public void setIdEntidad(Long v) { this.idEntidad = v; }
    public String getCodigoBcrp() { return codigoBcrp; }
    public void setCodigoBcrp(String v) { this.codigoBcrp = v; }
    public String getNombreEntidad() { return nombreEntidad; }
    public void setNombreEntidad(String v) { this.nombreEntidad = v; }
    public long getTotalIncidentes() { return totalIncidentes; }
    public void setTotalIncidentes(long v) { this.totalIncidentes = v; }
    public long getIncidentesCriticos() { return incidentesCriticos; }
    public void setIncidentesCriticos(long v) { this.incidentesCriticos = v; }
    public Double getMttrMinutos() { return mttrMinutos; }
    public void setMttrMinutos(Double v) { this.mttrMinutos = v; }
    public Double getMttdMinutos() { return mttdMinutos; }
    public void setMttdMinutos(Double v) { this.mttdMinutos = v; }
    public long getMinutosIndisponibles() { return minutosIndisponibles; }
    public void setMinutosIndisponibles(long v) { this.minutosIndisponibles = v; }
    public Double getUptimePct() { return uptimePct; }
    public void setUptimePct(Double v) { this.uptimePct = v; }
}
