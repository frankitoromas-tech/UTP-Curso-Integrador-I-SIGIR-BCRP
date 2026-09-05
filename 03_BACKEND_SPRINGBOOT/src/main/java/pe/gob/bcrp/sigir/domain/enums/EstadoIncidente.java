package pe.gob.bcrp.sigir.domain.enums;

/**
 * Representa los estados finitos del ciclo de vida de un incidente regulatorio
 * según la máquina de estados aprobada en la normativa BCRP.
 */
public enum EstadoIncidente {
    REGISTRADO("Incidente ingresado por sonda o mesa de ayuda"),
    EN_EVALUACION("Asignado a equipo técnico para diagnóstico preliminar"),
    EN_MITIGACION("Protocolos de contingencia y failover en ejecución"),
    RESUELTO("Servicio técnico y flujo transaccional restablecido"),
    CERRADO("Verificación de liquidación y cierre formal para reporte SFT");

    private final String descripcion;

    EstadoIncidente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
