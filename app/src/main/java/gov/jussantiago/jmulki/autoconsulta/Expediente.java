package gov.jussantiago.jmulki.autoconsulta;

/**
 * Created by jmulki on 17/04/2019.
 */

public class Expediente {
    private int numero;
    private String actor;
    private String demandado;
    private String causa;
    private String cgodestino;

    public Expediente(int numero, String actor, String demandado, String causa, String cgodestino) {
        this.numero = numero;
        this.actor = actor;
        this.demandado = demandado;
        this.causa = causa;
        this.cgodestino = cgodestino;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDemandado() {
        return demandado;
    }

    public void setDemandado(String demandado) {
        this.demandado = demandado;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getCgodestino() {
        return cgodestino;
    }

    public void setCgodestino(String cgodestino) {
        this.cgodestino = cgodestino;
    }
}
