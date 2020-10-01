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
public class Verso implements Comparable<Verso> {

    /**
     * @return the validaMetro
     */
    public int getValidaMetro() {
        String arrayVerso[] = versoEscandido.split("/");
        int i = 0;
        for (String silaba : arrayVerso) {
            i++;
            if (silaba.contains("#")) {
                validaMetro = i;
            }
        }
        return validaMetro;
    }

    /**
     * @param validaMetro the validaMetro to set
     */
    public void setValidaMetro(int validaMetro) {
        this.validaMetro = validaMetro;
    }

    /**
     * @return the substituicao
     */
    public boolean isSubstituicao() {
        return substituicao;
    }

    /**
     * @param substituicao the substituicao to set
     */
    public void setSubstituicao(boolean substituicao) {
        this.substituicao = substituicao;
    }

    private Versificacao versificacao;
    private String subCategoria;
    private int indiceDeOrigemDaCadeia;
    private String palavras;
    private String versoEscandido; //ver um nome melhor para isso - É o verso com as marcações do programa.
    private String classificacao;//Tipo de verso encontrado.
    private int numeroDeSilabas;
    private String tokensClassificados;
    private int posicionamentoTonicas[];//Posição das tônicas.
    @Deprecated
    private String local;//Apenas para manter a compatibilidade com a classe FraseVerso
    private Integer fraseCorrespondente;//Número da frase correspondente a frase que deu origem ao verso.
    private boolean houveElisao = false;
    private ArrayList<Palavra> palavrasComElisao = new ArrayList<>();
    //Lista de palavras processadas do verso.
    private ArrayList<Palavra> palavrasVerso = new ArrayList<>();
    private int numeroDeDiereses;
    private int numeroDeSinereses;
    private int numeroDeElisoes;
    private int numeroDeElisoesDesfeitas;
    private String versoOriginalmenteEscandido;
    private ArrayList<String> regrasDeElisoesAplicadas = new ArrayList<>();
    private int numeroDeSilabasOriginais;
    private String statusDaEscansao = "Não Modificado";
    private int numeroDeDieresesDesfeitas;
    private int numeroDeSinereseDesfeitas;
    private String versoAnterior;//05/01/2017
    private int quantidadeDeSilabasAnterior;//05/01/2017
    private boolean historicoDeContagem = false;//05/01/2017
    ArrayList<Palavra> historicoPalavrasComElisao = new ArrayList<>();
    //Lista de palavras processadas do verso.
    ArrayList<Palavra> historicoPalavrasVerso = new ArrayList<>();
    int historicoNumeroDeDiereses;
    int historicoNumeroDeSinereses;
    int historicoNumeroDeElisoes;
    private String segmentoFrasico;
    private int numeroDeVersoAntes;
    private int numeroDeVersoDepois;
    private int link;
    private int validaMetro;

    private boolean substituicao = false;
    /**
     * Distância para os versos de mesmo tipo
     */
    private int distanciaACima = 0;

    private VersoPossivel outrasFormas = null;

    public Verso() {
        versificacao = new Versificacao();
    }

    /**
     * @return the palavras
     */
    public String getPalavras() {
        if (palavras.contains("Â")) {
            palavras = palavras.replaceAll("Â", "");
        }

        return palavras;
    }

    /**
     * @param palavras the palavras to set
     */
    public void setPalavras(String palavras) {
        this.palavras = palavras;
    }

    /**
     * @return the versoEscandido
     */
    public String getVersoEscandido() {
        if (versoEscandido.contains("Â")) {
            versoEscandido = versoEscandido.replaceAll("Â", "");
        }
        if (versoEscandido.contains("§")) {
            versoEscandido = versoEscandido.replaceAll("§", "-");
        }
        return versoEscandido;
    }

    /**
     * @param versoEscandido the versoEscandido to set
     */
    public void setVersoEscandido(String versoEscandido) {
        this.versoEscandido = versoEscandido;
    }

    /**
     * @return the classificacao
     */
    public String getClassificacao() {
        return classificacao;
    }

    /**
     * @param classificacao the classificacao to set
     */
    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    /**
     * @return the numeroDeSilabas
     */
    public int getNumeroDeSilabas() {
        return numeroDeSilabas;
    }

    /**
     * @param numeroDeSilabas the numeroDeSilabas to set
     */
    public void setNumeroDeSilabas(int numeroDeSilabas) {
        this.numeroDeSilabas = numeroDeSilabas;
    }

    /**
     * @return the posicionamentoTonicas
     */
    public int[] getPosicionamentoTonicas() {
        return posicionamentoTonicas;
    }

    /**
     * @param posicionamentoTonicas the posicionamentoTonicas to set
     */
    public void setPosicionamentoTonicas(int[] posicionamentoTonicas) {
        this.posicionamentoTonicas = posicionamentoTonicas;
    }

    public void gerarPosicionamento() {
        //    System.out.println("TOKENS: olha olha olha " + versoEscandido);

        String t[] = versoEscandido.split("/");
        posicionamentoTonicas = new int[t.length];
//        System.out.println("TAMANHO DO VETOR: " + posicionamentoTonicas.length);
        String temp = null;
        int i = 0;
        for (String p : t) {
            if (p.contains("#")) {
                posicionamentoTonicas[i++] = 1;
            } else {
                posicionamentoTonicas[i++] = 0;
            }

        }
    }

    /**
     * @return the local
     */
    public String getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getFraseCorrespondente() {
        return fraseCorrespondente;
    }

    public void setFraseCorrespondente(Integer fraseCorrespondente) {
        this.fraseCorrespondente = fraseCorrespondente;
    }

    @Override
    public String toString() {
        return palavras;
    }

    public String getTokensClassificados() {
        return tokensClassificados;
    }

    public void setTokensClassificados(String tokensClassificados) {
        this.tokensClassificados = tokensClassificados;
    }

    public String getPosicionamentoDasTonicas() {
//        System.out.println("GERAR POSICIONAMENTO TÔNICAS: " + versoEscandido);
        String arrayVerso[] = versoEscandido.split("/");
        int i = 0;
        StringBuilder posicionamento = new StringBuilder();
        for (String silaba : arrayVerso) {
            i++;
            if (silaba.contains("#")) {
                posicionamento.append(i + " ");
                validaMetro = i;
            }
        }
        return posicionamento.toString().trim();
    }

    public String getSubCategoria() {
        return versificacao.getSubTipo(getPosicionamentoDasTonicas(), getNumeroDeSilabas());
//        if (versificacao.classificacao.get(getPosicionamentoDasTonicas()) != null) {
//            return versificacao.classificacao.get(getPosicionamentoDasTonicas());
//        }
//        return "Desconhecida";
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Versificacao getVersificacao() {
        return versificacao;
    }

    public void setVersificacao(Versificacao versificacao) {
        this.versificacao = versificacao;
    }

    public int getIndiceDeOrigemDaCadeia() {
        return indiceDeOrigemDaCadeia;
    }

    public void setIndiceDeOrigemDaCadeia(int indiceDeOrigemDaCadeia) {
        this.indiceDeOrigemDaCadeia = indiceDeOrigemDaCadeia;
    }

    public boolean isHouveElisao() {
        return houveElisao;
    }

    public void setHouveElisao(boolean houveElisao) {
        this.houveElisao = houveElisao;
    }

    public ArrayList<Palavra> getPalavrasComElisao() {
        return palavrasComElisao;
    }

    public void setPalavrasComElisao(ArrayList<Palavra> palavrasComElisao) {
        this.palavrasComElisao = palavrasComElisao;
    }

    /**
     * Uma lista de Palavras encontradas durante o processamento do verso. É
     * conjunto é importante quando for necessário reverter algum tipo de de
     * efeito intravocabular aplicado
     *
     * @return
     */
    public ArrayList<Palavra> getPalavrasVerso() {
        return palavrasVerso;
    }

    public void setPalavrasVerso(ArrayList<Palavra> palavrasVerso) {
        this.palavrasVerso = palavrasVerso;
    }

    public int getNumeroDeDiereses() {
        return numeroDeDiereses;
    }

    public void setNumeroDeDiereses(int numeroDeDiereses) {
        this.numeroDeDiereses = numeroDeDiereses;
    }

    public int getNumeroDeSinereses() {
        return numeroDeSinereses;
    }

    public void setNumeroDeSinereses(int numeroDeSinereses) {
        this.numeroDeSinereses = numeroDeSinereses;
    }

    public int getNumeroDeElisoes() {
        return palavrasComElisao.size() / 2;
    }

    public void setNumeroDeElisoes(int numeroDeElisoes) {
        this.numeroDeElisoes = numeroDeElisoes;
    }

    public int getNumeroDeElisoesDesfeitas() {
        return numeroDeElisoesDesfeitas;
    }

    public void setNumeroDeElisoesDesfeitas(int numeroDeElisoesDesfeitas) {
        this.numeroDeElisoesDesfeitas = numeroDeElisoesDesfeitas;
    }

    public String getVersoOriginalmenteEscandido() {
        return versoOriginalmenteEscandido;
    }

    public void setVersoOriginalmenteEscandido(String versoOriginalmenteEscandido) {
        this.versoOriginalmenteEscandido = versoOriginalmenteEscandido;
    }

    public ArrayList<String> getRegrasDeElisoesAplicadas() {
        return regrasDeElisoesAplicadas;
    }

    public void setRegrasDeElisoesAplicadas(ArrayList<String> regrasDeElisoesAplicadas) {
        this.regrasDeElisoesAplicadas = regrasDeElisoesAplicadas;
    }

    public int getNumeroDeSilabasOriginais() {
        return numeroDeSilabasOriginais;
    }

    public void setNumeroDeSilabasOriginais(int numeroDeSilabasOriginais) {
        this.numeroDeSilabasOriginais = numeroDeSilabasOriginais;
    }

    public String getStatusDaEscansao() {
        return statusDaEscansao;
    }

    public void setStatusDaEscansao(String statusDaEscansao) {
        this.statusDaEscansao = statusDaEscansao;
    }

    public int getNumeroDeDieresesDesfeitas() {
        return numeroDeDieresesDesfeitas;
    }

    public void setNumeroDeDieresesDesfeitas(int numeroDeDieresesDesfeitas) {
        this.numeroDeDieresesDesfeitas = numeroDeDieresesDesfeitas;
    }

    public int getNumeroDeSinereseDesfeitas() {
        return numeroDeSinereseDesfeitas;
    }

    public void setNumeroDeSinereseDesfeitas(int numeroDeSinereseDesfeitas) {
        this.numeroDeSinereseDesfeitas = numeroDeSinereseDesfeitas;
    }

    public String getVersoAnterior() {
        return versoAnterior;
    }

    public void setVersoAnterior(String versoAnterior) {
        this.versoAnterior = versoAnterior;
    }

    public int getQuantidadeDeSilabasAnterior() {
        return quantidadeDeSilabasAnterior;
    }

    public void setQuantidadeDeSilabasAnterior(int quantidadeDeSilabasAnterior) {
        this.quantidadeDeSilabasAnterior = quantidadeDeSilabasAnterior;
    }

    public boolean isHistoricoDeContagem() {
        return historicoDeContagem;
    }

    public void setHistoricoDeContagem(boolean historicoDeContagem) {
        this.historicoDeContagem = historicoDeContagem;
    }

    public void guardarHistorico() {
//        System.out.println("Guardando as palavras do verso");
        historicoPalavrasVerso.clear();
        for (Palavra palavra : getPalavrasVerso()) {
            historicoPalavrasVerso.add(palavra);
        }
//        System.out.println("Guardando as palavras com elisão do verso");
        historicoPalavrasComElisao.clear();
        for (Palavra palavra : getPalavrasComElisao()) {
            historicoPalavrasComElisao.add(palavra);
        }
        historicoNumeroDeDiereses = numeroDeDiereses;
        historicoNumeroDeSinereses = numeroDeSinereses;
        historicoNumeroDeElisoes = numeroDeElisoes;

    }

    public void restaurarHistorico() {
//        System.out.println("Guardando as palavras do verso");
        palavrasVerso.clear();
        for (Palavra palavra : historicoPalavrasVerso) {

            palavrasVerso.add(palavra);
        }
//        System.out.println("Guardando as palavras com elisão do verso");
        palavrasComElisao.clear();
        for (Palavra palavra : historicoPalavrasComElisao) {
            palavrasComElisao.add(palavra);
        }
        numeroDeDiereses = historicoNumeroDeDiereses;
        numeroDeSinereses = historicoNumeroDeSinereses;
        numeroDeElisoes = historicoNumeroDeElisoes;
    }

    /**
     * No método de busca versos no final de frases as palavra das elisões são
     * inseridas na ordem inversa. É necessário reorganizar para aproveitar os
     * métodos que reorganizam as situações que as sinéres, elisões e diéreses
     * fazem com que o verso ultrapasse ou fique abaixo do número de sílibas
     * informadas como limites para o verso que esta sendo procurado.
     */
    public void reordenarElisoes() {
//        System.out.println("----------------------------------------------------------------------------------");
//        System.out.println("REORDENANDO ELISÕES.....");
//        System.out.println("NÚMERO DE ELIÕES ATUAIS: " + getNumeroDeElisoes());

        if (palavrasComElisao.size() > 0) {
            ArrayList<Palavra> palavrasComElisaoTemp = new ArrayList<>();
//            System.out.println("Palavras como elisões atuais: ");
            for (Palavra pt : palavrasComElisao) {
//                System.out.print(pt.getPalavra() + " ");
                palavrasComElisaoTemp.add(pt);
            }
//            System.out.println("\npalavrasComElisaoTemp: " + palavrasComElisaoTemp.size());
            palavrasComElisao.clear();
            int indiceElisoes = palavrasComElisaoTemp.size() - 1;
            while (indiceElisoes >= 0) {
//                System.out.println("RETIRANDO A ELISÃO: " + indiceElisoes);
//                System.out.println("ADICIONANDO PALAVRA: " + palavrasComElisaoTemp.get(indiceElisoes).getPalavra());
                palavrasComElisao.add(palavrasComElisaoTemp.get(indiceElisoes));
                indiceElisoes--;
            }
        }
//        System.out.println("NOVA SEQUÊNCIA DE PALAVRAS");
//        for (Palavra pt : palavrasComElisao) {
//            System.out.print(pt.getPalavra() + " ");
//
//        }
//        System.out.println("\n----------------------------------------------------------------------------------");
    }

    public int compareTo(Verso verso) {
        //  return getClassificacao().compareToIgnoreCase(verso.getClassificacao());
        if (this.getNumeroDeSilabas() < verso.getNumeroDeSilabas()) {
            return -1;
        } else if (this.getNumeroDeSilabas() > verso.getNumeroDeSilabas()) {
            return 1;
        }
        return 0;
    }

    public VersoPossivel getOutrasFormas() {
        return outrasFormas;
    }

    public void setOutrasFormas(VersoPossivel outrasFormas) {
        this.outrasFormas = outrasFormas;
    }

    public int getNumeroDeVersoAntes() {
        return numeroDeVersoAntes;
    }

    public void setNumeroDeVersoAntes(int numeroDeVersoAntes) {
        this.numeroDeVersoAntes = numeroDeVersoAntes;
    }

    public int getNumeroDeVersoDepois() {
        return numeroDeVersoDepois;
    }

    public void setNumeroDeVersoDepois(int numeroDeVersoDepois) {
        this.numeroDeVersoDepois = numeroDeVersoDepois;
    }

    public int getDistanciaACima() {
        return distanciaACima;
    }

    public void setDistanciaACima(int distanciaACima) {
        this.distanciaACima = distanciaACima;
    }

    public String getSegmentoFrasico() {
        return segmentoFrasico;
    }

    public void setSegmentoFrasico(String segmentoFrasico) {
        this.segmentoFrasico = segmentoFrasico;
    }

    public int getQuantidadeDePalavras() {
        StringTokenizer st = new StringTokenizer(this.getPalavras());
        return st.countTokens();
    }

    public int getQuantidadeDeLetras() {
        return this.palavras.length();
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public String getTempVerso() {
        StringBuilder st = new StringBuilder();
        for (Palavra pal : palavrasVerso) {
            st.append(pal.getPalavra() + " ");
        }

        return st.toString();
    }
}
