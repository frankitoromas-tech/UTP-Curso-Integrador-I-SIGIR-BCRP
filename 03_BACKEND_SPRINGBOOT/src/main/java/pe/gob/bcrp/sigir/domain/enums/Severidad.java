package pe.gob.bcrp.sigir.domain.enums;

/**
 * Grado de severidad e impacto del incidente sobre el sistema nacional de pagos.
 */
public enum Severidad {
    BAJA(1, "Impacto cosmético o latencia leve sin afectación a saldos"),
    MEDIA(2, "Degradación parcial de servicios con canal alternativo"),
    ALTA(3, "Caída de servicio o afectación transaccional considerable"),
    CRITICA(4, "Interrupción total de switch o brecha de seguridad con pérdida de datos");

    private final int nivel;
    private final String detalle;

    Severidad(int nivel, String detalle) {
        this.nivel = nivel;
        this.detalle = detalle;
    }

    public int getNivel() {
        return nivel;
    }

    public String getDetalle() {
        return detalle;
    }
}
