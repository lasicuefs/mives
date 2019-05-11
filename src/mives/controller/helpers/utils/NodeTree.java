/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers.utils;

/**
 *
 * @author Ricardo
 */
public class NodeTree {

    private String chave;
    private String valor;

    public NodeTree(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;

    }

    @Override
    public String toString() {
        return chave; //To change body of generated methods, choose Tools | Templates.
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
