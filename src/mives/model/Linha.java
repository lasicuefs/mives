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
public class Linha {

    private String linha;//Cadeia de Caracteres
    private String linhaComEscansao;
    private int indiceDeFraseAssociada;
    private ArrayList<Integer> frasesAssociadas;
    private boolean substituicao = false;
    private int diferencaDeInserir = 0;

    public Linha() {
        frasesAssociadas = new ArrayList<>();
    }

    public Linha(String linha) {
        this();
        this.linha = linha;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public int getIndiceDeFraseAssociada() {
        return indiceDeFraseAssociada;
    }

    public void setIndiceDeFraseAssociada(int indiceDeFraseAssociada) {
        this.indiceDeFraseAssociada = indiceDeFraseAssociada;
    }

    public String getLinhaComEscansao() {
        return linhaComEscansao;
    }

    public void setLinhaComEscansao(String linhaComEscansao) {
        this.linhaComEscansao = linhaComEscansao;
    }

    public ArrayList<Integer> getFrasesAssociadas() {
        return frasesAssociadas;
    }

    public void setFrasesAssociadas(ArrayList<Integer> frasesAssociadas) {
        this.frasesAssociadas = frasesAssociadas;
    }

    public boolean isSubstituicao() {
        return substituicao;
    }

    public void setSubstituicao(boolean substituicao) {
        this.substituicao = substituicao;
    }

    public int getDiferencaDeInserir() {
        return diferencaDeInserir;
    }

    public void setDiferencaDeInserir(int diferencaDeInserir) {
        this.diferencaDeInserir = diferencaDeInserir;
    }

}
