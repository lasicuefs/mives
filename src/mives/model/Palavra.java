/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.ArrayList;

/**
 *
 * @author Ricardo
 */
public class Palavra {

    private String palavra;
    private String palavraSeparada;
    private int qtdSilabasPoeticas;
    private boolean sinerese = false;//Verdadeiro se houver o fenômeno
    private boolean dierese = false;//Verdadeiro se houver o fenômeno
    private ArrayList<String> silabasSinerese = new ArrayList<>();
    private ArrayList<String> silabasDirese = new ArrayList<>();
    private ArrayList<String> novaSilabaComDierese = new ArrayList<>();
    private ArrayList<String> regrasAplicadasDirese = new ArrayList<>();
    private ArrayList<String> novaSilabaComSinerese = new ArrayList<>();
    private ArrayList<String> regrasAplicadasSinerese = new ArrayList<>();
    private String palavraOrigialEscandida;
    private boolean possuiDiereseNaTonica = false;

    /**
     * @return the sinerese
     */
    public boolean isSinerese() {
        return sinerese;
    }

    /**
     * @param sinerese the sinerese to set
     */
    public void setSinerese(boolean sinerese) {
        this.sinerese = sinerese;
    }

    /**
     * @return the dierese
     */
    public boolean isDierese() {
        return dierese;
    }

    /**
     * @param dierese the dierese to set
     */
    public void setDierese(boolean dierese) {
        this.dierese = dierese;
    }

    /**
     * @return the silabasSinerese
     */
    public ArrayList<String> getSilabasSinerese() {
        return silabasSinerese;
    }

    /**
     * @param silabasSinerese the silabasSinerese to set
     */
    public void setSilabasSinerese(ArrayList<String> silabasSinerese) {
        this.silabasSinerese = silabasSinerese;
    }

    /**
     * Retorna a sílaba original, antes mesmo de ter o efeito aplicado sobre a
     * palavra
     *
     * @return the silabasDirese
     */
    public ArrayList<String> getSilabasDirese() {
        return silabasDirese;
    }

    /**
     * @param silabasDirese the silabasDirese to set
     */
    public void setSilabasDirese(ArrayList<String> silabasDirese) {
        this.silabasDirese = silabasDirese;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getPalavraSeparada() {
        return palavraSeparada;
    }

    public void setPalavraSeparada(String palavraSeparada) {
        this.palavraSeparada = palavraSeparada;
    }

    public int getQtdSilabasPoeticas() {
        return qtdSilabasPoeticas;
    }

    public void setQtdSilabasPoeticas(int qtdSilabasPoeticas) {
        this.qtdSilabasPoeticas = qtdSilabasPoeticas;
    }

    public ArrayList<String> getNovaSilabaComDierese() {
        return novaSilabaComDierese;
    }

    public void setNovaSilabaComDierese(ArrayList<String> novaSilabaComDierese) {
        this.novaSilabaComDierese = novaSilabaComDierese;
    }

    public ArrayList<String> getNovaSilabaComSinerese() {
        return novaSilabaComSinerese;
    }

    public void setNovaSilabaComSinerese(ArrayList<String> novaSilabaComSinerese) {
        this.novaSilabaComSinerese = novaSilabaComSinerese;
    }

    public ArrayList<String> getRegrasAplicadasDirese() {
        return regrasAplicadasDirese;
    }

    public void setRegrasAplicadasDirese(ArrayList<String> regrasAplicadasDirese) {
        this.regrasAplicadasDirese = regrasAplicadasDirese;
    }

    public ArrayList<String> getRegrasAplicadasSinerese() {
        return regrasAplicadasSinerese;
    }

    public void setRegrasAplicadasSinerese(ArrayList<String> regrasAplicadasSinerese) {
        this.regrasAplicadasSinerese = regrasAplicadasSinerese;
    }

    public String getPalavraOrigialEscandida() {
        return palavraOrigialEscandida;
    }

    public void setPalavraOrigialEscandida(String palavraOrigialEscandida) {
        this.palavraOrigialEscandida = palavraOrigialEscandida;
    }

    public boolean isPossuiDiereseNaTonica() {
        return possuiDiereseNaTonica;
    }

    public void setPossuiDiereseNaTonica(boolean possuiDiereseNaTonica) {
        this.possuiDiereseNaTonica = possuiDiereseNaTonica;
    }

}
