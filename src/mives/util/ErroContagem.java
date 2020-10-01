/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import mives.model.Verso;

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

        FileChooser fileChooser = new FileChooser();
        File file = null;
        fileChooser.setTitle("Erros Encontrados");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            file = fileChooser.showSaveDialog(null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FileWriter fileWriterDestino = new FileWriter(file); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            int contador = 1;
            for (ExemplarErro err : erros) {

                bufferedWriterDestino.write(err.numSilabas + " | " + err.cadeias + " | " + err.posicionamento);
                bufferedWriterDestino.newLine();
                contador++;
            }

            bufferedWriterDestino.flush();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

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
