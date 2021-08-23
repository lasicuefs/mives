/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import mives.observable.Observer;
import mives.observable.ProcessarLivroObservable;
import mives.util.ErroContagem;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo
 */
public class Mineracao implements ProcessarLivroObservable {

    ArrayList<Observer> observables = new ArrayList<>();
    HashSet<String> numeros = new HashSet<>();

    private Livro livro;
    private File arquivoDestino;//Para o caso de arquivos intermediários.
    private int tipoDeVersoInicio;// Quantidade de Sílabas Poéticas que devem ser buscadas.
    private int tipoDeVersoFim;
    @Deprecated
    HashMap<String, String> artigos;
    @Deprecated
    HashSet<String> pontuacoes;
    private boolean considerarSinerese;
    private boolean considerarDierese;
    private boolean considerarElisao;
    private boolean buscarFraseInteira;
    private boolean buscaNoFinal;
    private boolean buscaNoInicio;
    private boolean considerarHiato;
    private boolean considerarElisão;
    private boolean considerarPosicionamentoTonica;
    EscansaoCustomizada2 escansaoCustomizada2;
    Separador separador;
    private static Mineracao mineracao;

    public static Mineracao getInstance() {
        if (mineracao == null) {
            mineracao = new Mineracao();

        }
        return mineracao;
    }

    public void definirParametros(Livro livro, boolean buscaInicio, boolean buscarNoFim, boolean buscarFraseInteira,
            int tipoDeVersoInicio, int tipoDeVersoFim, boolean considerarPosicionamentoTonica, boolean considerarSinerese, boolean considerarDierese,
            boolean considerarHiato, boolean considerarElisao) {
        this.livro = livro;
        this.buscaNoInicio = buscaInicio;
        this.buscaNoFinal = buscarNoFim;
        this.buscarFraseInteira = buscarFraseInteira;
//        this.tipoDeVersoInicio = Integer.parseInt(tipoDeVersoInicio.split("-")[0].trim());
//        this.tipoDeVersoFim = Integer.parseInt(tipoDeVersoFim.split("-")[0].trim());
        this.tipoDeVersoInicio = tipoDeVersoInicio;
        this.tipoDeVersoFim = tipoDeVersoFim;
        this.considerarPosicionamentoTonica = considerarPosicionamentoTonica;
        this.considerarSinerese = considerarSinerese;
        this.considerarDierese = considerarDierese;
        this.considerarHiato = considerarHiato;
        this.considerarElisao = considerarElisao;
        escansaoCustomizada2 = new EscansaoCustomizada2(considerarSinerese, considerarDierese, considerarElisao);
    }

    private Mineracao() {
        this.separador = new Separador();
        this.carregarArtigos();
        this.carregarPontuacoes();
        this.carregarNumeros();
    }

    private Mineracao(Livro livro) {
        this();
        this.livro = livro;
    }

    public void gerarCombinacaoDeEscansao(Verso vTemp, Frase frase, int fraseCorrespondente, String local) throws Exception {
        int vBase = vTemp.getNumeroDeSilabas();
        Verso vTempAux;
        for (int i = tipoDeVersoInicio; i <= tipoDeVersoFim; i++) {
            if (vBase != i) {
                vTempAux = this.extrairPalavrasFraseInteiraCustomizada2(vTemp.getPalavras(), i, i);
                if (vTempAux != null) {
                    vTempAux.setLocal(local);
                    vTempAux.setIndiceDeOrigemDaCadeia(frase.getIndiceDeOrigemDaCadeiaNaLinha());
                    vTempAux.setTokensClassificados(vTempAux.getVersoEscandido() + "||" + vTempAux.getNumeroDeSilabas() + "||");
                    vTempAux.setFraseCorrespondente(fraseCorrespondente);
                    frase.getVersosExtra().add(vTempAux);
                }
            }
        }

    }

    /**
     * Busca por outras possibilidades de escansão a partir da faixa de metros
     * definida pelo usuário.
     *
     * @param frase - Onde as possibilidades devem ser testadas
     * @param fraseCorrespondente - Número da frase correspondente.
     * @param tipoDoversoBase - O que não deve ser testado pelo fato de já ter
     * sido catalogado como tipo de verso principal.
     */
    public void gerarOutrasPossibilidadesDeEscansaoIniciFrase(Frase frase, int fraseCorrespondente, int tipoDoversoBase) throws Exception {
        Verso vTemp;
        String linhaBase = frase.toString();
        for (int i = tipoDeVersoInicio; i <= tipoDeVersoFim; i++) {
            if (tipoDoversoBase != i) {
                vTemp = this.extrairPalavrasFraseInicialCustomizada2(frase.toString(), i, i);
                if (vTemp != null && (!frase.jaTemEssaFormaDeEscansao(vTemp.getVersoEscandido()))) {
                    vTemp.setLocal("Início de frase.");
                    vTemp.setIndiceDeOrigemDaCadeia(frase.getIndiceDeOrigemDaCadeiaNaLinha());
                    vTemp.setTokensClassificados(vTemp.getVersoEscandido() + "||" + vTemp.getNumeroDeSilabas() + "||");
                    vTemp.setFraseCorrespondente(fraseCorrespondente);
                    frase.getVersosPossiveis().setVersoBase(vTemp);

                    int vBase = vTemp.getNumeroDeSilabas();
                    Verso vTempAux;
                    for (int iAux = tipoDeVersoInicio; iAux <= tipoDeVersoFim; iAux++) {
                        if (vBase != iAux) {
                            vTempAux = this.extrairPalavrasFraseInteiraCustomizada2(vTemp.getPalavras(), iAux, iAux);
                            if (vTempAux != null && (!frase.jaTemEssaFormaDeEscansao(vTempAux.getVersoEscandido()))) {
                                vTempAux.setLocal("Início de frase.");
                                vTempAux.setIndiceDeOrigemDaCadeia(frase.getIndiceDeOrigemDaCadeiaNaLinha());
                                vTempAux.setTokensClassificados(vTempAux.getVersoEscandido() + "||" + vTempAux.getNumeroDeSilabas() + "||");
                                vTempAux.setFraseCorrespondente(fraseCorrespondente);
                                frase.getVersosPossiveis().getVersosExtras().add(vTempAux);
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Busca por outras possibilidades de escansão a partir da faixa de metros
     * definida pelo usuário.
     *
     * @param frase - Onde as possibilidades devem ser testadas
     * @param fraseCorrespondente - Número da frase correspondente.
     * @param tipoDoversoBase - O que não deve ser testado pelo fato de já ter
     * sido catalogado como tipo de verso principal.
     */
    public void gerarOutrasPossibilidadesDeEscansaoFinalFrase(Frase frase, int fraseCorrespondente, int tipoDoversoBase) throws Exception {
//        System.out.println("Frase/Verso recebido: " + frase);
        Verso vTemp;
        String linhaBase = frase.toString();
        for (int i = tipoDeVersoInicio; i <= tipoDeVersoFim; i++) {
            if (tipoDoversoBase != i) {
                vTemp = this.extrairPalavrasFraseFinalCustomizadaNew(frase.toString(), i, i);
                if (vTemp != null && (!frase.jaTemEssaFormaDeEscansao(vTemp.getVersoEscandido()))) {
                    vTemp.setLocal("Final de Frase.");
                    vTemp.setIndiceDeOrigemDaCadeia(frase.getIndiceDeOrigemDaCadeiaNaLinha());
                    vTemp.setTokensClassificados(vTemp.getVersoEscandido() + "||" + vTemp.getNumeroDeSilabas() + "||");
                    vTemp.setFraseCorrespondente(fraseCorrespondente);
                    frase.getVersosPossiveis().setVersoBase(vTemp);

                    int vBase = vTemp.getNumeroDeSilabas();
                    Verso vTempAux;
                    for (int iAux = tipoDeVersoInicio; iAux <= tipoDeVersoFim; iAux++) {
                        if (vBase != iAux) {
                            vTempAux = this.extrairPalavrasFraseInteiraCustomizada2(vTemp.getPalavras(), iAux, iAux);
                            if (vTempAux != null && (!frase.jaTemEssaFormaDeEscansao(vTempAux.getVersoEscandido()))) {
                                vTempAux.setLocal("Final de Frase.");
                                vTempAux.setIndiceDeOrigemDaCadeia(frase.getIndiceDeOrigemDaCadeiaNaLinha());
                                vTempAux.setTokensClassificados(vTempAux.getVersoEscandido() + "||" + vTempAux.getNumeroDeSilabas() + "||");
                                vTempAux.setFraseCorrespondente(fraseCorrespondente);
                                frase.getVersosPossiveis().getVersosExtras().add(vTempAux);
                            }
                        }
                    }
                }
            }
        }

    }

    public static int total;
    public static int valorInicial;

    /**
     * Aqui eu devo receber o mecanismo para poder atualizar a barra de
     * progresso Fazer isso por último. Agora é prover a visualização.
     */
    public void minerarVersos() {
        Verso vTemp;
        String linha;
        livro.setTipoDeVersoInicio(tipoDeVersoInicio);
        livro.setTipoDeVersoFim(tipoDeVersoFim);
        if (buscaNoInicio) {
            livro.setTipoDeBusca("Início de Frase.");
        } else if (buscaNoFinal) {
            livro.setTipoDeBusca("Final de Frase.");
        } else {
            livro.setTipoDeBusca("Frases Completas.");
        }
        total = livro.getPaginas().size();
        int progresso = 0;
        for (Integer frase : livro.getPaginas().keySet()) {
            valorInicial++;
            Pagina pagina = livro.getPaginas().get(frase);
            //ArrayList<String> frases = pagina.getFraseEncontrada();//15-01-2016
            ArrayList<Frase> frases = pagina.getFrases();
            int fraseCorrespondente = -1;
            for (Frase f : frases) {
                progresso++;
                String linhaBase = f.toString();
                fraseCorrespondente++;
                linha = "";
                if (buscaNoInicio) {
                    // vTemp = this.extrairPalavrasFraseInicialCustomizada2(linhaBase, tipoDeVersoInicio);
                    vTemp = this.extrairPalavrasFraseInicialCustomizada2(linhaBase, tipoDeVersoInicio, tipoDeVersoFim);
                    if (vTemp != null && !containsNumber(vTemp.getPalavras())) {
                        vTemp.setLocal("Início de frase.");
                        vTemp.setSegmentoFrasico(linhaBase);
                        vTemp.setIndiceDeOrigemDaCadeia(f.getIndiceDeOrigemDaCadeiaNaLinha());
                        vTemp.setFraseCorrespondente(fraseCorrespondente);
                        pagina.getVersos().add(vTemp);
                        f.setVerso(vTemp);
                        //Existe um verso, Ok. Tente escandir com metros diferentes dentro da faixa informada pelo usuário.
                        try {
                            gerarCombinacaoDeEscansao(vTemp, f, fraseCorrespondente, "Início de frase.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            gerarOutrasPossibilidadesDeEscansaoIniciFrase(f, fraseCorrespondente, vTemp.getNumeroDeSilabas());
                            //  vTemp.setOutrasFormas(f.getVersosPossiveis());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (f.getVersosPossiveis().getVersoBase() != null) {
                            f.getVerso().setOutrasFormas(f.getVersosPossiveis());//Muito ruim isso aqui pensar em uma alternativa melhor
                        }
                    }

                } else if (buscaNoFinal) {
                    vTemp = this.extrairPalavrasFraseFinalCustomizadaNew(linhaBase, tipoDeVersoInicio, tipoDeVersoFim);
                    if (vTemp != null && !containsNumber(vTemp.getPalavras())) {
                        vTemp.setLocal("Final de Frase.");
                        vTemp.setSegmentoFrasico(linhaBase);
                        vTemp.setFraseCorrespondente(fraseCorrespondente);
                        pagina.getVersos().add(vTemp);
                        f.setVerso(vTemp);

                        try {
                            gerarCombinacaoDeEscansao(vTemp, f, fraseCorrespondente, "Final de Frase.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            gerarOutrasPossibilidadesDeEscansaoFinalFrase(f, fraseCorrespondente, vTemp.getNumeroDeSilabas());
                            //  vTemp.setOutrasFormas(f.getVersosPossiveis());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (f.getVersosPossiveis().getVersoBase() != null) {
                            f.getVerso().setOutrasFormas(f.getVersosPossiveis());//Muito ruim isso aqui pensar em uma alternativa melhor

                        }

                    }
                } else if (buscarFraseInteira) {
                    //vTemp = this.extrairPalavrasFraseInteira(linhaBase, tipoDeVerso);
                    vTemp = this.extrairPalavrasFraseInteiraCustomizada2(linhaBase, tipoDeVersoInicio, tipoDeVersoFim);

                    if (vTemp != null && !containsNumber(vTemp.getPalavras())) {

                        vTemp.setLocal("Frases completas.");
                        vTemp.setSegmentoFrasico(linhaBase);
                        vTemp.setIndiceDeOrigemDaCadeia(f.getIndiceDeOrigemDaCadeiaNaLinha());
                        vTemp.setTokensClassificados(vTemp.getVersoEscandido() + "||" + vTemp.getNumeroDeSilabas() + "||");
                        vTemp.setFraseCorrespondente(fraseCorrespondente);
                        pagina.getVersos().add(vTemp);
                        f.setVerso(vTemp);
                        int vBase = vTemp.getNumeroDeSilabas();
                        for (int i = tipoDeVersoInicio; i <= tipoDeVersoFim; i++) {
                            if (vBase != i) {
                                vTemp = this.extrairPalavrasFraseInteiraCustomizada2(linhaBase, i, i);
                                if (vTemp != null) {
                                    vTemp.setLocal("Frases completas.");
                                    vTemp.setIndiceDeOrigemDaCadeia(f.getIndiceDeOrigemDaCadeiaNaLinha());
                                    vTemp.setTokensClassificados(vTemp.getVersoEscandido() + "||" + vTemp.getNumeroDeSilabas() + "||");
                                    vTemp.setFraseCorrespondente(fraseCorrespondente);
                                    f.getVersosExtra().add(vTemp);
                                }
                            }
                        }
                    }
                    //                    }
                }
                vTemp = null;
                //    System.gc();
                notifyObservers(progresso);
            }
        }
    }

    private String sequencia() {
        return "";
    }

    public int numeroSilabasFrase(String frase) {
        int aux = 0;
        StringTokenizer st = new StringTokenizer(frase);
        String temp = "";
        while (st.hasMoreTokens()) {
            temp = st.nextToken();
            if (temp.contains("-")) {
                aux += temp.split("-").length;
            } else {
                aux++;
            }
        }
        return aux;
    }

    public int getNumeroTokens(String frase, int numeroDeSilabas) {
        int numeroDeTokens = 0;
        StringTokenizer st = new StringTokenizer(frase);
        String temp = "";
        while (st.hasMoreTokens() && numeroDeTokens < numeroDeSilabas) {
            temp = st.nextToken();
            if (!pontuacoes.contains(temp.trim())) {
                numeroDeTokens++;
            }
        }
        return 0;
    }

    private void carregarArtigos() {
        artigos = new HashMap<>();
        artigos.put("o", "o");
        artigos.put("a", "a");
        artigos.put("os", "os");
        artigos.put("as", "as");
        artigos.put("um", "um");
        artigos.put("uma", "uma");
        artigos.put("uns", "uns");
        artigos.put("umas", "umas");
    }

    private void carregarPontuacoes() {
        pontuacoes = new HashSet<>();
        pontuacoes.add(".");
        pontuacoes.add(",");
        pontuacoes.add(";");
        pontuacoes.add(":");
        pontuacoes.add("?");
        pontuacoes.add("!");
        pontuacoes.add("...");
        pontuacoes.add("\"");
        pontuacoes.add("(");
        pontuacoes.add(")");
        pontuacoes.add("—");
        pontuacoes.add("–");
        pontuacoes.add("—");
        pontuacoes.add("[");
        pontuacoes.add("]");
        pontuacoes.add("{");
        pontuacoes.add("}");
        pontuacoes.add("*");
        pontuacoes.add("/");
        pontuacoes.add("−");
    }

    public boolean isArtigo(String palavra) {
        return artigos.containsKey(palavra.toLowerCase());
    }

    //Coloquei esse método na classe escansão customizada
    private String getLinhaComSeparacao(String[] palavras, int quantidadeMinima) {
        StringBuilder linha = new StringBuilder("");
        int quantidade = 0;
        StringBuilder temp = new StringBuilder();

        for (String palavra : palavras) {
            temp = new StringBuilder(separador.separarSilabasTexto(palavra));
            // quantidade += contarSilabasDaPalavra(temp.toString()); - 13/02/2016
            quantidade++;
            linha.append(temp.toString() + " ");
            if (quantidade >= quantidadeMinima && temp.toString().contains("#")) {
                break;
            }
        }
        return linha.toString();
    }

    public int contarSilabasDaPalavra(String palavra) {
        return palavra.split("-").length;
    }

    private Verso extrairPalavrasFraseInteiraCustomizada2(String frase, int numeroSilabasInicio) {
        StringTokenizer f = new StringTokenizer(frase.toString());
        Verso verso = new Verso();
        verso = escansaoCustomizada2.contarSilabasPoeticas(frase, numeroSilabasInicio, 12, false);
        if (verso != null
                && new StringTokenizer(frase).countTokens() == new StringTokenizer(verso.getVersoEscandido()).countTokens()
                //                && verso.getNumeroDeSilabas() >= numeroSilabas) {
                && ((verso.getNumeroDeSilabas() >= numeroSilabasInicio) && (verso.getNumeroDeSilabas() <= 12))) {
            verso.setPalavras(verso.getVersoEscandido().toString().replaceAll("#", "").replaceAll("§", "-").replaceAll("/", ""));
            verso.gerarPosicionamento();
            return verso;
        }
        return null;
    }

    //Modificado em 30/12/2016
    private Verso extrairPalavrasFraseInteiraCustomizada2(String frase, int numeroSilabasInicio, int numeroSilabasFim) {
        StringTokenizer f = new StringTokenizer(frase.toString());
        Verso verso = new Verso();
//        verso = escansaoCustomizada2.contarSilabasPoeticasNew(frase, numeroSilabasFim, numeroSilabasInicio, false); 
        verso = escansaoCustomizada2.contarSilabasPoeticasCompleta(frase, numeroSilabasFim, numeroSilabasInicio, false);//21/05/2017

        /**
         * Exclusão forçada em fazer aqui, se autorizada...
         */
        if (verso != null
                && new StringTokenizer(frase).countTokens() == new StringTokenizer(verso.getVersoEscandido()).countTokens()
                && ((verso.getNumeroDeSilabas() >= numeroSilabasInicio) && (verso.getNumeroDeSilabas() <= numeroSilabasFim))) {

            verso.setPalavras(verso.getVersoEscandido().toString().replaceAll("#", "").replaceAll("§", "-").replaceAll("/", ""));
            verso.gerarPosicionamento();
            if (verso.getNumeroDeSilabas() != verso.getValidaMetro()) {
                ErroContagem.adicionarErro(verso.getVersoEscandido(), verso.getPosicionamentoDasTonicas(), verso.getNumeroDeSilabas());
            }
            return verso;
        }
        return null;
    }

    // private Verso extrairPalavrasFraseInicialCustomizada2(String frase, int numeroSilabas) {
    private Verso extrairPalavrasFraseInicialCustomizada2(String frase, int numeroSilabasInicio, int numeroSilabasFim) {
        StringTokenizer f = new StringTokenizer(frase.toString());

        Verso verso = escansaoCustomizada2.contarSilabasPoeticasNew(frase, numeroSilabasFim, numeroSilabasInicio, true);
        if (verso == null) {
            return null;
        }

        if ((verso.getNumeroDeSilabas() >= numeroSilabasInicio) && (verso.getNumeroDeSilabas() <= numeroSilabasFim)) {

            verso.setPalavras(verso.getVersoEscandido().toString().replaceAll("#", "").replaceAll("§", "-").replaceAll("/", ""));
            String ultimaPalavra[] = Utilitario.preencherVetorTokenV2(new StringTokenizer(verso.getPalavras()));
            boolean possuiPontuacao = escansaoCustomizada2.verifcarSimboloPontuacao(ultimaPalavra[ultimaPalavra.length - 1]);
            if (!possuiPontuacao) {

                return null;
            }

            verso.gerarPosicionamento();
            return verso;
        }
        return null;
    }

    private Verso extrairPalavrasFraseFinalCustomizadaNew(String frase, int minimo, int maximo) {
        StringTokenizer f = new StringTokenizer(frase.toString());
//        System.out.println("FRASE RECEBIDA - DOMINGO..............." + frase);
        Verso verso = escansaoCustomizada2.contarSilabasPoeticasFinalNew(frase, minimo, maximo, true);
        if (verso == null) {
            return null;
        }

        if (verso.getVersoEscandido().toString().matches(".*\\d.*")) {

            return null;
        }
        if ((verso.getNumeroDeSilabas() >= minimo) && (verso.getNumeroDeSilabas() <= maximo)) {

            String auxFrase[] = Utilitario.preencherVetorToken(f);
            String auxVerso[] = new String[new StringTokenizer(verso.getVersoEscandido().toString().replaceAll("-", "").replaceAll("#", "").replaceAll("§", "-").replaceAll("/", "")).countTokens()];
            StringBuilder versoExtraido = new StringBuilder();

            int in = auxFrase.length - auxVerso.length;

            for (int inicio = in; inicio < auxFrase.length; inicio++) {
                versoExtraido.append(auxFrase[inicio] + " ");
            }
            verso.setPalavras(versoExtraido.toString().replace("\"", "\"").trim());//IFBA

            verso.gerarPosicionamento();
            return verso;
        }
        return null;
    }

    @Deprecated
    private Verso extrairPalavrasFraseFinalCustomizada2(String frase, int numeroSilabas) {
        StringTokenizer f = new StringTokenizer(frase.toString());
        Verso verso = escansaoCustomizada2.contarSilabasPoeticasFinal(frase, numeroSilabas, true);
        if (verso == null) {
            return null;
        }
        if (verso.getNumeroDeSilabas() >= numeroSilabas) {

            verso.setPalavras(verso.getVersoEscandido().toString().replaceAll("#", "").replaceAll("§", "-").replaceAll("/", ""));
            verso.gerarPosicionamento();
            return verso;
        }
        return null;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public File getArquivoDestino() {
        return arquivoDestino;
    }

    public void setArquivoDestino(File arquivoDestino) {
        this.arquivoDestino = arquivoDestino;
    }

    public int getTipoDeVerso() {
        return tipoDeVersoInicio;
    }

    public void setTipoDeVerso(int tipoDeVerso) {
        this.tipoDeVersoInicio = tipoDeVerso;
    }

    public boolean isConsiderarSinerese() {
        return considerarSinerese;
    }

    public void setConsiderarSinerese(boolean considerarSinerese) {
        this.considerarSinerese = considerarSinerese;
    }

    public boolean isConsiderarDierese() {
        return considerarDierese;
    }

    public void setConsiderarDierese(boolean considerarDierese) {
        this.considerarDierese = considerarDierese;
    }

    public boolean isConsiderarElisao() {
        return considerarElisao;
    }

    public void setConsiderarElisao(boolean considerarElisao) {
        this.considerarElisao = considerarElisao;
    }

    public boolean isBuscarFraseInteira() {
        return buscarFraseInteira;
    }

    public void setBuscarFraseInteira(boolean buscarFraseInteira) {
        this.buscarFraseInteira = buscarFraseInteira;
    }

    public boolean isBuscaNoFinal() {
        return buscaNoFinal;
    }

    public void setBuscaNoFinal(boolean buscaNoFinal) {
        this.buscaNoFinal = buscaNoFinal;
    }

    public boolean isBuscaNoInicio() {
        return buscaNoInicio;
    }

    public void setBuscaNoInicio(boolean buscaNoInicio) {
        this.buscaNoInicio = buscaNoInicio;
    }

    public boolean isConsiderarHiato() {
        return considerarHiato;
    }

    public void setConsiderarHiato(boolean considerarHiato) {
        this.considerarHiato = considerarHiato;
    }

    public boolean isConsiderarElisão() {
        return considerarElisão;
    }

    public void setConsiderarElisão(boolean considerarElisão) {
        this.considerarElisão = considerarElisão;
    }

    public boolean isConsiderarPosicionamentoTonica() {
        return considerarPosicionamentoTonica;
    }

    public void setConsiderarPosicionamentoTonica(boolean considerarPosicionamentoTonica) {
        this.considerarPosicionamentoTonica = considerarPosicionamentoTonica;
    }

    @Override
    public void registerObserver(Observer observer) {
        observables.add(observer);
    }

    @Override
    public void notifyObservers(int progresso) {
        for (Observer observer : observables) {
            observer.update(progresso);
        }
    }

    private void carregarNumeros() {
        numeros.add("0");
        numeros.add("1");
        numeros.add("2");
        numeros.add("3");
        numeros.add("4");
        numeros.add("5");
        numeros.add("6");
        numeros.add("7");
        numeros.add("8");
        numeros.add("9");
    }

    private boolean containsNumber(String verso) {
        for (String string : numeros) {
            if (verso.contains(string)) {
                return true;
            }
        }
        return false;
    }
}
