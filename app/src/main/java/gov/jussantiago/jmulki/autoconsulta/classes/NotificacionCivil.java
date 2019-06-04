package gov.jussantiago.jmulki.autoconsulta.classes;

/**
 * Created by jmulki on 04/06/2019.
 */

public class NotificacionCivil {
    private Integer numero;
    private String caratula;
    private String fecha;
    private String cgodestino;
    private String observaciones;

    public NotificacionCivil(Integer numero, String caratula, String fecha, String cgodestino, String observaciones) {
        this.numero = numero;
        this.caratula = caratula;
        this.fecha = fecha;
        this.cgodestino = cgodestino;
        this.observaciones = observaciones;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCgodestino() {
        return cgodestino;
    }

    public void setCgodestino(String cgodestino) {
        this.cgodestino = cgodestino;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
