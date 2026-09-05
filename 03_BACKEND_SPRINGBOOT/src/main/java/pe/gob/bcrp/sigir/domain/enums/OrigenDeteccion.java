package pe.gob.bcrp.sigir.domain.enums;

/**
 * Canal u origen mediante el cual se identificó y capturó el incidente.
 */
public enum OrigenDeteccion {
    AUTOMATICO("Capturado por el worker de telemetría y sondeo de salud"),
    MANUAL_OPERADOR("Ingresado formalmente por personal autorizado de la entidad o BCRP");

    private final String descripcion;

    OrigenDeteccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
