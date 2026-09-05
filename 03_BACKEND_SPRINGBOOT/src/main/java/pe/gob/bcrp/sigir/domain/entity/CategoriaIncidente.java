package pe.gob.bcrp.sigir.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias_incidentes")
public class CategoriaIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "codigo_normativo", nullable = false, unique = true, length = 20)
    private String codigoNormativo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "es_deteccion_automatica", nullable = false)
    private Boolean esDeteccionAutomatica = false;

    public CategoriaIncidente() {
    }

    public CategoriaIncidente(Long idCategoria, String codigoNormativo, String nombre, String descripcion, Boolean esDeteccionAutomatica) {
        this.idCategoria = idCategoria;
        this.codigoNormativo = codigoNormativo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.esDeteccionAutomatica = esDeteccionAutomatica != null ? esDeteccionAutomatica : false;
    }

    public static CategoriaIncidenteBuilder builder() {
        return new CategoriaIncidenteBuilder();
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCodigoNormativo() {
        return codigoNormativo;
    }

    public void setCodigoNormativo(String codigoNormativo) {
        this.codigoNormativo = codigoNormativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsDeteccionAutomatica() {
        return esDeteccionAutomatica;
    }

    public void setEsDeteccionAutomatica(Boolean esDeteccionAutomatica) {
        this.esDeteccionAutomatica = esDeteccionAutomatica;
    }

    public static class CategoriaIncidenteBuilder {
        private Long idCategoria;
        private String codigoNormativo;
        private String nombre;
        private String descripcion;
        private Boolean esDeteccionAutomatica = false;

        CategoriaIncidenteBuilder() {
        }

        public CategoriaIncidenteBuilder idCategoria(Long idCategoria) {
            this.idCategoria = idCategoria;
            return this;
        }

        public CategoriaIncidenteBuilder codigoNormativo(String codigoNormativo) {
            this.codigoNormativo = codigoNormativo;
            return this;
        }

        public CategoriaIncidenteBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public CategoriaIncidenteBuilder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public CategoriaIncidenteBuilder esDeteccionAutomatica(Boolean esDeteccionAutomatica) {
            this.esDeteccionAutomatica = esDeteccionAutomatica;
            return this;
        }

        public CategoriaIncidente build() {
            return new CategoriaIncidente(idCategoria, codigoNormativo, nombre, descripcion, esDeteccionAutomatica);
        }
    }
}
