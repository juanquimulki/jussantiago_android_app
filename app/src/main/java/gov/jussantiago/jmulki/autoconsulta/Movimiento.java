package gov.jussantiago.jmulki.autoconsulta;

/**
 * Created by jmulki on 22/04/2019.
 */

public class Movimiento {
    private Integer nroexpediente;
    private String fecha;
    private String estado;
    private String observaciones;
    private Integer cgoestado;
    private Integer idvar;
    private String cgodestino;
    private Integer anio;

    public Movimiento(Integer nroexpediente, String fecha, String estado, String observaciones, Integer cgoestado, Integer idvar, String cgodestino, Integer anio) {
        this.nroexpediente = nroexpediente;
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
        this.cgoestado = cgoestado;
        this.idvar = idvar;
        this.cgodestino = cgodestino;
        this.anio = anio;
    }

    public Integer getNroexpediente() {
        return nroexpediente;
    }

    public void setNroexpediente(Integer nroexpediente) {
        this.nroexpediente = nroexpediente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCgoestado() {
        return cgoestado;
    }

    public void setCgoestado(Integer cgoestado) {
        this.cgoestado = cgoestado;
    }

    public Integer getIdvar() {
        return idvar;
    }

    public void setIdvar(Integer idvar) {
        this.idvar = idvar;
    }

    public String getCgodestino() {
        return cgodestino;
    }

    public void setCgodestino(String cgodestino) {
        this.cgodestino = cgodestino;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}
