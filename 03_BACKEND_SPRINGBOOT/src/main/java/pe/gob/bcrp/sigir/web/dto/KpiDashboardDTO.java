package pe.gob.bcrp.sigir.web.dto;

import java.util.List;

public class KpiDashboardDTO {
    private long totalIncidentes;
    private long activos;
    private long cerrados;
    private Double mttrGlobalMinutos;
    private Double mttdGlobalMinutos;
    private Double uptimeGlobalPct;
    private List<IndisponibilidadDTO> porEntidad;

    public long getTotalIncidentes() { return totalIncidentes; }
    public void setTotalIncidentes(long v) { this.totalIncidentes = v; }
    public long getActivos() { return activos; }
    public void setActivos(long v) { this.activos = v; }
    public long getCerrados() { return cerrados; }
    public void setCerrados(long v) { this.cerrados = v; }
    public Double getMttrGlobalMinutos() { return mttrGlobalMinutos; }
    public void setMttrGlobalMinutos(Double v) { this.mttrGlobalMinutos = v; }
    public Double getMttdGlobalMinutos() { return mttdGlobalMinutos; }
    public void setMttdGlobalMinutos(Double v) { this.mttdGlobalMinutos = v; }
    public Double getUptimeGlobalPct() { return uptimeGlobalPct; }
    public void setUptimeGlobalPct(Double v) { this.uptimeGlobalPct = v; }
    public List<IndisponibilidadDTO> getPorEntidad() { return porEntidad; }
    public void setPorEntidad(List<IndisponibilidadDTO> v) { this.porEntidad = v; }
}
