package gov.jussantiago.jmulki.autoconsulta.classes;

/**
 * Created by jmulki on 25/04/2019.
 */

public class Notificacion {
    private Integer numero;
    private String remitente;
    private String expediente;
    private String fuero;
    private String fecha;
    private String archivo;

    public Notificacion(Integer numero, String remitente, String expediente, String fuero, String fecha, String archivo) {
        this.numero = numero;
        this.remitente = remitente;
        this.expediente = expediente;
        this.fuero = fuero;
        this.fecha = fecha;
        this.archivo = archivo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getFuero() {
        return fuero;
    }

    public void setFuero(String fuero) {
        this.fuero = fuero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
