/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.HashMap;

/**
 *
 * @author Ricardo
 */
public class Versificacao {

    //  HashMap<String, String> classificacao;
    HashMap<String, String> tetrassilabos;
    HashMap<String, String> pentassilabos;
    HashMap<String, String> hexassilabo;
    HashMap<String, String> heptassilabo;
    HashMap<String, String> octossilabo;
    HashMap<String, String> eneassilabo;
    HashMap<String, String> decassilabo;
    HashMap<String, String> hendecassilabo;
    HashMap<String, String> dodecassilabo;

    public Versificacao() {
        //classificacao = new HashMap<>();
        tetrassilabos = new HashMap<>();
        pentassilabos = new HashMap<>();
        hexassilabo = new HashMap<>();
        heptassilabo = new HashMap<>();
        octossilabo = new HashMap<>();
        eneassilabo = new HashMap<>();
        decassilabo = new HashMap<>();
        hendecassilabo = new HashMap<>();
        dodecassilabo = new HashMap<>();

        tetrassilabos.put("2 4", "Tetrassílabos");
        tetrassilabos.put("1 4", "Tetrassílabos");
        tetrassilabos.put("4", "Tetrassílabos");

        pentassilabos.put("2 5", "Pentassílabos");
        pentassilabos.put("1 3 5", "Pentassílabos");
        pentassilabos.put("3 5", "Pentassílabos");
        pentassilabos.put("1 5", "Pentassílabos");

        hexassilabo.put("2 4 6", "Hexassílabo");//A
        hexassilabo.put("2 6", "Hexassílabo");//B
        hexassilabo.put("4 6", "Hexassílabo");//C
        hexassilabo.put("1 4 6", "Hexassílabo");//D
        hexassilabo.put("1 3 6", "Hexassílabo");//E
        hexassilabo.put("3 6", "Hexassílabo");//F

        heptassilabo.put("1 3 5 7", "Heptassílabo");//A
        heptassilabo.put("3 5 7", "Heptassílabo");//B
        heptassilabo.put("3 7", "Heptassílabo");//C
        heptassilabo.put("3 7", "Heptassílabo");//D
        heptassilabo.put("4 7", "Heptassílabo");//E
        heptassilabo.put("2 4 7", "Heptassílabo");//F
        heptassilabo.put("1 4 7", "Heptassílabo");//G
        heptassilabo.put("2 5 7", "Heptassílabo");//H

        octossilabo.put("2 4 6 8", "Octossílabo");//A
        octossilabo.put("2 4 8", "Octossílabo");//B
        octossilabo.put("1 4 6 8", "Octossílabo");//C
        octossilabo.put("4 6 8", "Octossílabo");//C
        octossilabo.put("4 8", "Octossílabo");//D
        octossilabo.put("1 4 8", "Octossílabo");//D
        octossilabo.put("2 6 8", "Octossílabo");//E
        octossilabo.put("1 3 5 8", "Octossílabo");//F
        octossilabo.put("3 5 8", "Octossílabo");//G
        octossilabo.put("1 5 8", "Octossílabo");//H
        octossilabo.put("2 5 8", "Octossílabo");//I
        octossilabo.put("3 6 8", "Octossílabo");//J
        octossilabo.put("1 3 6 8", "Octossílabo");//J

        /*
         Note-se que no ENEASSÍLABO ANAPÉSTICO  é tão forte a intensidade da 
         3ª,da 6ª e da 9ª sílaba, que todas as demais, ainda que de natureza
         tônica, nele se obscurecem em benefício daquelas. - PROBLEMA
         */
//        classificacao.put("3 6 9", "Eneassílabo Anapéstico");//A
        eneassilabo.put("3 6 9", "Anapéstico");//A

        eneassilabo.put("4 6 9", "Eneassílabo");//A
        eneassilabo.put("1 4 6 9", "Eneassílabo");//A - Variação
        eneassilabo.put("2 4 6 9", "Eneassílabo");//A - Variação

        eneassilabo.put("4 7 9", "Eneassílabo");//B
        eneassilabo.put("1 4 7 9", "Eneassílabo");//B - Variação
        eneassilabo.put("2 4 7 9", "Eneassílabo");//B - Variação

//        classificacao.put("6 10", "Decassílabo Heroico");//A
//        classificacao.put("8 6 10", "Decassílabo Heroico");//A - Variação
//        classificacao.put("2 6 10", "Decassílabo Heroico");//A - Variação
//        
        decassilabo.put("6 10", "Heroico");//A
        decassilabo.put("8 6 10", "Heroico");//A - Variação
        decassilabo.put("2 6 10", "Heroico");//A - Variação

        hendecassilabo.put("2 5 8 11", "Verso de arte-maior");
        hendecassilabo.put("3 7 11", "Hendecassílabo");

        dodecassilabo.put("3 6 9 12", "Alexandrino Clássico");
        dodecassilabo.put("4 8 12", "Alexandrino Romântico");
    }

    public String getSubTipo(String posicaoTonicas, int quantidadeSilabas) {
        if (quantidadeSilabas == 4) {
            if (tetrassilabos.containsKey(posicaoTonicas)) {
                return tetrassilabos.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 5) {
            if (pentassilabos.containsKey(posicaoTonicas)) {
                return pentassilabos.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }

        }
        if (quantidadeSilabas == 6) {
            if (hexassilabo.containsKey(posicaoTonicas)) {
                return hexassilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 7) {
            if (heptassilabo.containsKey(posicaoTonicas)) {
                return tetrassilabos.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 8) {
            if (octossilabo.containsKey(posicaoTonicas)) {
                return octossilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 9) {
            if (eneassilabo.containsKey(posicaoTonicas)) {
                return eneassilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 10) {
            if (decassilabo.containsKey(posicaoTonicas)) {
                return decassilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 11) {
            if (hendecassilabo.containsKey(posicaoTonicas)) {
                return hendecassilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        if (quantidadeSilabas == 12) {
            if (dodecassilabo.containsKey(posicaoTonicas)) {
                return dodecassilabo.get(posicaoTonicas);
            } else {
                return "Desconhecido";
            }
        }
        return "Desconhecido";
    }

}
