/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.util.HashMap;

/**
 *
 * @author Ricardo
 */
public class EstatisticaTipo {

    private String tipo;
    private int quantidade;
    private HashMap<String, Integer> subtipos = new HashMap<String, Integer>();
    private int quantidadeTotalDeVersoEncontrados;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public HashMap<String, Integer> getSubtipos() {
        return subtipos;
    }

    public void setSubtipos(HashMap<String, Integer> subtipos) {
        this.subtipos = subtipos;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeTotalDeVersoEncontrados() {
        return quantidadeTotalDeVersoEncontrados;
    }

    public void setQuantidadeTotalDeVersoEncontrados(int quantidadeTotalDeVersoEncontrados) {
        this.quantidadeTotalDeVersoEncontrados = quantidadeTotalDeVersoEncontrados;
    }

}
