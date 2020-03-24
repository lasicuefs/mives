/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Ricardo
 */
public class Frase {

    
    private boolean isReplace = false;
    /**
     * @return the existeVerso
     */
    public boolean isExisteVerso() {
        return existeVerso;
    }

    /**
     * @param existeVerso the existeVerso to set
     */
    public void setExisteVerso(boolean existeVerso) {
        this.existeVerso = existeVerso;
    }

    private String palavras;
    private Integer linhaDeOrigem;
    private Integer paragrafoDeOrigem;
    private Verso verso;
    private ArrayList<Verso> versosExtra = new ArrayList<>();
    private boolean existeVerso = false;
    private int indiceDeOrigemDaCadeiaNaLinha;
    private int numeroDaFrase;
    /**
     * Outras possibilidades de escansão testadas no mesmo segmento frásico.
     */
    private VersoPossivel versosPossiveis = new VersoPossivel();

    public Frase(String palavras) {
        this.palavras = palavras;
        this.verso = null;
    }

    public String getPalavras() {
        return palavras;
    }

    public void setPalavras(String palavras) {
        this.palavras = palavras;
    }

    public Integer getParagrafoDeOrigem() {
        return paragrafoDeOrigem;
    }

    public void setParagrafoDeOrigem(Integer paragrafoDeOrigem) {
        this.paragrafoDeOrigem = paragrafoDeOrigem;
    }

    public Integer getLinhaDeOrigem() {
        return linhaDeOrigem;
    }

    public void setLinhaDeOrigem(Integer linhaDeOrigem) {
        this.linhaDeOrigem = linhaDeOrigem;
    }

    @Override
    public String toString() {
        return palavras;
    }

    public Verso getVerso() {
        return verso;
    }

    public void setVerso(Verso verso) {
        existeVerso = true;
        this.verso = verso;
    }

    public int getIndiceDeOrigemDaCadeiaNaLinha() {
        return indiceDeOrigemDaCadeiaNaLinha;
    }

    public void setIndiceDeOrigemDaCadeiaNaLinha(int indiceDeOrigemDaCadeiaNaLinha) {
        this.indiceDeOrigemDaCadeiaNaLinha = indiceDeOrigemDaCadeiaNaLinha;
    }

    /**
     * Verso base originalmente encontrado. A partir do verso original são
     * testadas outras possiblidades de escansão. As diferentes possibilidades
     * são armazenadas aqui.
     *
     * @return
     */
    public ArrayList<Verso> getVersosExtra() {
        return versosExtra;
    }

    /**
     * Verso base originalmente encontrado. A partir do verso original são
     * testadas outras possiblidades de escansão. As diferentes possibilidades
     * são obtidas aqui.
     *
     * @param versosExtra
     */
    public void setVersosExtra(ArrayList<Verso> versosExtra) {
        this.versosExtra = versosExtra;
    }

    /**
     * Outras possibilidades de escansão testadas no mesmo segmento frásico.
     *
     * @return
     */
    public VersoPossivel getVersosPossiveis() {
        return versosPossiveis;
    }

    public void setVersosPossiveis(VersoPossivel versosPossiveis) {
        this.versosPossiveis = versosPossiveis;
    }

    public boolean jaTemEssaFormaDeEscansao(String formaEscandida) {

        if (verso.getVersoEscandido().equals(formaEscandida)) {
            return true;
        }
        for (Verso v : getVersosExtra()) {
            if (v.getVersoEscandido().equals(formaEscandida)) {
                return true;
            }
        }
        if (versosPossiveis.getVersoBase() != null && versosPossiveis.getVersoBase().getVersoEscandido().equals(formaEscandida)) {
            return true;
        }

        for (Verso v : versosPossiveis.getVersosExtras()) {
            if (v.getVersoEscandido().equals(formaEscandida)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeVerso() {
        return !(verso == null);
    }

    public ArrayList<Integer> getMetricasDeVerso() {
        if (verso == null) {
            return null;
        }
        ArrayList<Integer> metricas = new ArrayList<>();
        metricas.add(new Integer(verso.getNumeroDeSilabas()));
        for (Verso v : versosExtra) {
            metricas.add(new Integer(v.getNumeroDeSilabas()));
        }
        return metricas;
    }

    public int getQuantidadeDePalavras() {
        return new StringTokenizer(palavras).countTokens();
    }

    public int getQuantidadeDeLetras() {
        return palavras.trim().length();
    }

    /**
     * @return the isReplace
     */
    public boolean isIsReplace() {
        return isReplace;
    }

    /**
     * @param isReplace the isReplace to set
     */
    public void setIsReplace(boolean isReplace) {
        this.isReplace = isReplace;
    }

    /**
     * @return the numeroDaFrase
     */
    public int getNumeroDaFrase() {
        return numeroDaFrase;
    }

    /**
     * @param numeroDaFrase the numeroDaFrase to set
     */
    public void setNumeroDaFrase(int numeroDaFrase) {
        this.numeroDaFrase = numeroDaFrase;
    }
}
