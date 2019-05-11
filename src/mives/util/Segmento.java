/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

/**
 *
 * @author Ricardo
 *
 * Classe criada para dar suporte a criação do gráfico de densidade
 *
 */
public class Segmento {

    private long numero;
    private int valor;

    public Segmento(long numero, int valor) {
        this.numero = numero;
        this.valor = valor;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

}
