/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Ricardo
 */
public class MapaConfiguracao {

    /**
     * Tipo 1 - Padrão Tipo 2 - Modificado
     */
    private int tipo = 1;

    private HashSet<String> sinerese;
    private String[] vetorSinerese;
    private HashSet< String> dierese;
    private String[] vetorDierese;
    private HashSet<String> elisao;
    private String[] vetorElisao;

    //Início dos atributos de Exceções
    private HashSet<String> excecoesSinerese;
    private String[] vetorExcecoesSinerese;
    private HashSet<String> excecoesElisao;
    private String[] vetorExcecoesElisao;
    private HashSet<String> excecoesDierese;
    private String[] vetorExcecoesDierese;
    //Fim dos atributos de Exceções

    //Início dos atributos de Exclusão
    private HashSet<String> excluirSinerese;
    //private String[] vetorExcecoesSinerese;
    private HashSet<String> excluirElisao;
    //   private String[] vetorExcecoesElisao;
    private HashSet<String> excluirDierese;
    //   private String[] vetorExcecoesDierese;
    //Fim dos atributos de Exclusão

    private int tipoDeElisao;
    private boolean considerarH;
    private boolean considerarAtona = true;
    private boolean considerarTonica = true;
    private int tipoDeVerso;
    private static MapaConfiguracao mapaConfiguracao = null;
    private boolean versoLivre = false;
    private boolean versoContado = false;
    private boolean utilizar = false;

    private MapaConfiguracao() {
        sinerese = new HashSet<>();
        dierese = new HashSet<>();
        elisao = new HashSet<>();
        System.out.println("Inicalizando lista de exceções");
        excecoesSinerese = new HashSet<>();
        excecoesElisao = new HashSet<>();
        excecoesDierese = new HashSet<>();
        System.out.println("Finalizando inicialização da lista de exceções");

        System.out.println("Inicalizando lista de Exclusão");
        excluirSinerese = new HashSet<>();
        excluirElisao = new HashSet<>();
        excluirDierese = new HashSet<>();

    }

    public static MapaConfiguracao getInstacia() {
        if (getMapaConfiguracao() == null) {
            setMapaConfiguracao(new MapaConfiguracao());
        }
        return getMapaConfiguracao();
    }

    /**
     * @param vetorSinerese the vetorSinerese to set
     */
    public void setVetorSinerese(String[] vetorSinerese) {
        this.vetorSinerese = vetorSinerese;
    }

    /**
     * @param vetorDierese the vetorDierese to set
     */
    public void setVetorDierese(String[] vetorDierese) {
        this.vetorDierese = vetorDierese;
    }

    /**
     * @param vetorElisao the vetorElisao to set
     */
    public void setVetorElisao(String[] vetorElisao) {
        this.vetorElisao = vetorElisao;
    }

    /**
     * @return the excecoesSinerese
     */
    public HashSet<String> getExcecoesSinerese() {
        if (excecoesSinerese == null) {
            excecoesSinerese = new HashSet<>();
        }
        return excecoesSinerese;
    }

    /**
     * @param excecoesSinerese the excecoesSinerese to set
     */
    public void setExcecoesSinerese(HashSet<String> excecoesSinerese) {
        this.excecoesSinerese = excecoesSinerese;
    }

    /**
     * @return the vetorExcecoesSinerese
     */
    public String[] getVetorExcecoesSinerese() {
        return vetorExcecoesSinerese;
    }

    /**
     * @param vetorExcecoesSinerese the vetorExcecoesSinerese to set
     */
    public void setVetorExcecoesSinerese(String[] vetorExcecoesSinerese) {
        this.vetorExcecoesSinerese = vetorExcecoesSinerese;
    }

    /**
     * @return the excecoesElisao
     */
    public HashSet<String> getExcecoesElisao() {
        if (excecoesElisao == null) {
            excecoesElisao = new HashSet<>();
        }
        return excecoesElisao;
    }

    /**
     * @param excecoesElisao the excecoesElisao to set
     */
    public void setExcecoesElisao(HashSet<String> excecoesElisao) {
        this.excecoesElisao = excecoesElisao;
    }

    /**
     * @return the vetorExcecoesElisao
     */
    public String[] getVetorExcecoesElisao() {
        return vetorExcecoesElisao;
    }

    /**
     * @param vetorExcecoesElisao the vetorExcecoesElisao to set
     */
    public void setVetorExcecoesElisao(String[] vetorExcecoesElisao) {
        this.vetorExcecoesElisao = vetorExcecoesElisao;
    }

    /**
     * @return the excecoesDierese
     */
    public HashSet<String> getExcecoesDierese() {
        if (excecoesDierese == null) {
            excecoesDierese = new HashSet<>();
        }
        return excecoesDierese;
    }

    /**
     * @param excecoesDierese the excecoesDierese to set
     */
    public void setExcecoesDierese(HashSet<String> excecoesDierese) {
        this.excecoesDierese = excecoesDierese;
    }

    /**
     * @return the vetorExcecoesDierese
     */
    public String[] getVetorExcecoesDierese() {
        return vetorExcecoesDierese;
    }

    /**
     * @param vetorExcecoesDierese the vetorExcecoesDierese to set
     */
    public void setVetorExcecoesDierese(String[] vetorExcecoesDierese) {
        this.vetorExcecoesDierese = vetorExcecoesDierese;
    }

    public static MapaConfiguracao getMapaConfiguracao() {
        return mapaConfiguracao;
    }

    public static void setMapaConfiguracao(MapaConfiguracao aMapaConfiguracao) {
        mapaConfiguracao = aMapaConfiguracao;
    }

    public boolean isUtilizar() {
        return utilizar;
    }

    public void setUtilizar(boolean utilizar) {
        this.utilizar = utilizar;
    }

    public HashSet<String> getSinerese() {
        return sinerese;
    }

    public void setSinerese(HashSet<String> sinerese) {
        this.sinerese = sinerese;
    }

    public String[] getVetorSinerese() {
        return vetorSinerese;
    }

    public HashSet<String> getDierese() {
        return dierese;
    }

    public void setDierese(HashSet<String> dierese) {
        this.dierese = dierese;
    }

    public String[] getVetorDierese() {
        return vetorDierese;
    }

    public HashSet<String> getElisao() {
        return elisao;
    }

    public void setElisao(HashSet<String> elisao) {
        this.elisao = elisao;
    }

    public String[] getVetorElisao() {
        return vetorElisao;
    }

    public int getTipoDeElisao() {
        return tipoDeElisao;
    }

    public void setTipoDeElisao(int tipoDeElisao) {
        this.tipoDeElisao = tipoDeElisao;
    }

    public boolean isConsiderarH() {
        return considerarH;
    }

    public void setConsiderarH(boolean considerarH) {
        this.considerarH = considerarH;
    }

    public int getTipoDeVerso() {
        return tipoDeVerso;
    }

    public void setTipoDeVerso(int tipoDeVerso) {
        this.tipoDeVerso = tipoDeVerso;
    }

    public boolean isVersoLivre() {
        return versoLivre;
    }

    public void setVersoLivre(boolean versoLivre) {
        this.versoLivre = versoLivre;
    }

    public boolean isVersoContado() {
        return versoContado;
    }

    public void setVersoContado(boolean versoContado) {
        this.versoContado = versoContado;
    }

    public boolean isConsiderarAtona() {
        return considerarAtona;
    }

    public void setConsiderarAtona(boolean considerarAtona) {
        this.considerarAtona = considerarAtona;
    }

    public boolean isConsiderarTonica() {
        return considerarTonica;
    }

    public void setConsiderarTonica(boolean considerarTonica) {
        this.considerarTonica = considerarTonica;
    }

    public void imprimirMapa() {
        Iterator<String> elisoes = getElisao().iterator();
        String chave;
        System.out.println("-----------------Mapa de Conguração-----------------");
        System.out.println("Elisões:");
        while (elisoes.hasNext()) {
            chave = elisoes.next();
            System.out.println(chave);
        }
        System.out.println("----------------------------------------------------");
        Iterator<String> sinereses = getSinerese().iterator();
        System.out.println("Sinérese:");
        while (sinereses.hasNext()) {
            chave = sinereses.next();
            System.out.println(chave);
        }
        System.out.println("----------------------------------------------------");
        Iterator<String> diereses = getDierese().iterator();
        System.out.println("Diérese:");
        while (diereses.hasNext()) {
            chave = diereses.next();
            System.out.println(chave);
        }
        System.out.println("----------------------------------------------------");
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the excluirSinerese
     */
    public HashSet<String> getExcluirSinerese() {
        if (excluirSinerese == null) {
            excluirSinerese = new HashSet<>();
        }
        return excluirSinerese;
    }

    /**
     * @param excluirSinerese the excluirSinerese to set
     */
    public void setExcluirSinerese(HashSet<String> excluirSinerese) {
        this.excluirSinerese = excluirSinerese;
    }

    /**
     * @return the excluirElisao
     */
    public HashSet<String> getExcluirElisao() {
        if (excluirElisao == null) {
            excluirElisao = new HashSet<>();
        }
        return excluirElisao;
    }

    /**
     * @param excluirElisao the excluirElisao to set
     */
    public void setExcluirElisao(HashSet<String> excluirElisao) {
        this.excluirElisao = excluirElisao;
    }

    /**
     * @return the excluirDierese
     */
    public HashSet<String> getExcluirDierese() {
        if (excluirDierese == null) {
            excluirDierese = new HashSet<>();
        }
        return excluirDierese;
    }

    /**
     * @param excluirDierese the excluirDierese to set
     */
    public void setExcluirDierese(HashSet<String> excluirDierese) {
        this.excluirDierese = excluirDierese;
    }
}
