/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import mives.model.Livro;

/**
 *
 * @author Ricardo
 */
public class ErroContagem {

    private static ArrayList<ExemplarErro> erros = new ArrayList<>();

    public static void adicionarErro(String cadeia, String posicionamento, int numSilabas) {
        erros.add(new ExemplarErro(cadeia, posicionamento, numSilabas));
    }

    public static void imprimirRelatorio() {
        FileWriter fileWriterDestino;
        BufferedWriter bufferedWriterDestino;
        try {
            String tipoDeBusca = Livro.getInstance().getTipoDeBusca().replace(".", "");
            String nomeDoLivro = Livro.getInstance().getArquivoDeOrigem().getName().replace(".txt", "");

            File arquivoDeDestino = new File(System.getProperty("user.dir") + "/erros/" + nomeDoLivro + tipoDeBusca + System.currentTimeMillis() + ".txt");

            fileWriterDestino = new FileWriter(arquivoDeDestino); //Escrever no novo arquivo
            bufferedWriterDestino = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoDeDestino), StandardCharsets.UTF_8));
            int contador = 1;
            for (ExemplarErro err : erros) {

                bufferedWriterDestino.write(err.numSilabas + " | " + err.cadeias + " | " + err.posicionamento);
                bufferedWriterDestino.newLine();
                contador++;
            }

            bufferedWriterDestino.close();
            bufferedWriterDestino.close();
            fileWriterDestino.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static class ExemplarErro {

        private String cadeias;
        private String posicionamento;
        private int numSilabas;

        public ExemplarErro(String cadeias, String posicionamento, int numSilabas) {
            this.cadeias = cadeias;
            this.posicionamento = posicionamento;
            this.numSilabas = numSilabas;
        }

        /**
         * @return the cadeias
         */
        public String getCadeias() {
            return cadeias;
        }

        /**
         * @param cadeias the cadeias to set
         */
        public void setCadeias(String cadeias) {
            this.cadeias = cadeias;
        }

        /**
         * @return the posicionamento
         */
        public String getPosicionamento() {
            return posicionamento;
        }

        /**
         * @param posicionamento the posicionamento to set
         */
        public void setPosicionamento(String posicionamento) {
            this.posicionamento = posicionamento;
        }

        /**
         * @return the numSilabas
         */
        public int getNumSilabas() {
            return numSilabas;
        }

        /**
         * @param numSilabas the numSilabas to set
         */
        public void setNumSilabas(int numSilabas) {
            this.numSilabas = numSilabas;
        }

    }

}
