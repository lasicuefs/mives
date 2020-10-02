/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exportacao;

/**
 *
 * @author Ricardo
 */
public class EstruturaVersificacao {

    private int numeroDeSilabas;
    private String posicaoDasTonicas;
    private String sentecaEscandida;
    private String palavrasVerso;
    private int erroMetrica;

    /**
     * @return the numeroDeSilabas
     */
    public int getNumeroDeSilabas() {
        return numeroDeSilabas;
    }

    /**
     * @param numeroDeSilabas the numeroDeSilabas to set
     */
    public void setNumeroDeSilabas(int numeroDeSilabas) {
        this.numeroDeSilabas = numeroDeSilabas;
    }

    /**
     * @return the posicaoDasTonicas
     */
    public String getPosicaoDasTonicas() {
        return posicaoDasTonicas;
    }

    /**
     * @param posicaoDasTonicas the posicaoDasTonicas to set
     */
    public void setPosicaoDasTonicas(String posicaoDasTonicas) {
        this.posicaoDasTonicas = posicaoDasTonicas;
    }

    /**
     * @return the sentecaEscandida
     */
    public String getSentecaEscandida() {
        return sentecaEscandida;
    }

    /**
     * @param sentecaEscandida the sentecaEscandida to set
     */
    public void setSentecaEscandida(String sentecaEscandida) {
        this.sentecaEscandida = sentecaEscandida;
    }

    public String getPalavrasVerso() {
        return palavrasVerso;
    }

    public void setPalavrasVerso(String palavrasVerso) {
        this.palavrasVerso = palavrasVerso;
    }

    /**
     * @return the erroMetrica
     */
    public int getErroMetrica() {
        return erroMetrica;
    }

    /**
     * @param erroMetrica the erroMetrica to set
     */
    public void setErroMetrica(int erroMetrica) {
        this.erroMetrica = erroMetrica;
    }
}
