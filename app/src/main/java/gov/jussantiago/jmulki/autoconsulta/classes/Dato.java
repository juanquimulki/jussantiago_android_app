package gov.jussantiago.jmulki.autoconsulta.classes;

/**
 * Created by jmulki on 08/05/2019.
 */

public class Dato {
    private String atributo;
    private String valor;

    public Dato(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
