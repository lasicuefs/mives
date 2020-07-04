/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import mives.util.EstatisticaTipo;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo
 */
public class Pagina {

    private int numero;
    private ArrayList<String> linhasOriginais;
    private ArrayList<Linha> linhas;
    //  private ArrayList<String> linhasEscandidas;
    @Deprecated
    private ArrayList<String> fraseEncontrada;
    private ArrayList<Frase> frases;
    private StringBuilder frase;
    HashSet<String> pronomesDeTratamento;
    HashSet<String> alfabeto;
    private HashSet<String> algarismosRomanos;
    private ArrayList<Verso> versos;
    int lendoLinha;
    private int numeroDeVersos = 0;
    private HashMap<String, EstatisticaTipo> estatisticaPagina = new HashMap<>();
    boolean exiteResto = false;//dá suporte a criação do HTML para os finais de sentença
    String restoInserir;//dá suporte a criação do HTML para os finais de sentença
    int quantidadeDoResto = 0;

    public Pagina() {
        // linhasEscandidas = new ArrayList<>();
        linhas = new ArrayList<>();
        algarismosRomanos = new HashSet<>();
        versos = new ArrayList<>();
        carregarAlgarismos();
        linhasOriginais = new ArrayList<>();
        fraseEncontrada = new ArrayList<>();
        frases = new ArrayList<>();
        alfabeto = new HashSet();
        gerarAlfabeto();
        pronomesDeTratamento = new HashSet();
        this.carregarPronomes();
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the linhasOriginais
     */
    public ArrayList<String> getLinhasOriginais() {
        return linhasOriginais;
    }

    /**
     * @param linhasOriginais the linhasOriginais to set
     */
    public void setLinhasOriginais(ArrayList<String> linhasOriginais) {
        this.linhasOriginais = linhasOriginais;
    }

    public void processarFrase() {
        frase = new StringBuilder();
        StringBuilder stringAux = new StringBuilder();
        StringBuilder resto = new StringBuilder();
        boolean houveResto = false;
        int indiceDalinha = -1;
        int linhaDeOrigemDaFrase = -1;
        int indiceDeOrigemDaCadeiaNaLinha = 0; //Armazena em que ponto da linha se originou a frase
        int tamanhoDaLinhaOriginal = 0;
        StringBuilder ultimaLinha = new StringBuilder();
        inicio:
        for (String linha : linhasOriginais) {
//            System.out.println("Processando linha: " + linha);
            tamanhoDaLinhaOriginal = linha.length();
            indiceDalinha++;
            lendoLinha = indiceDalinha;//utilizada por outros métodos.
            if (linha.trim().length() == 0) {
                continue inicio;
            }
            if ((linha.length() != 0) && (!linha.contains("Página: >> "))) {
                if (isTitulo(indiceDalinha)) {
//                    System.out.println("isTitulo: " + linha);
                    Frase f = new Frase(linha);
                    f.setIndiceDeOrigemDaCadeiaNaLinha(0);//27/01
                    indiceDeOrigemDaCadeiaNaLinha = 0;//27/01
                    linhaDeOrigemDaFrase = indiceDalinha;
                    f.setLinhaDeOrigem(linhaDeOrigemDaFrase);
                    linhaDeOrigemDaFrase = -1;
                    resto = new StringBuilder("");
                    houveResto = false;
                    frases.add(f);
                    linhas.get(f.getLinhaDeOrigem()).getFrasesAssociadas().add(new Integer(frases.size() - 1));
                    continue inicio;
                }
                //Se contém ponto e não contém reticências adicionar a frase.
                if (houveResto) {
                    if (!frase.toString().contains(resto)) {
                        linha = new String(resto + " " + linha);
//                        System.out.println("Nova linha do início: " + linha);
                    }
                    resto = new StringBuilder();
                    houveResto = false;
                }
                if (!linha.contains(".")
                        && !linha.contains("...")) {
                    if (frase.length() == 0) {
                        frase.append(linha.trim());
                        ultimaLinha = new StringBuilder(frase);
                        if (linhaDeOrigemDaFrase == -1) {
                            linhaDeOrigemDaFrase = indiceDalinha;
                        }
                    } else {
                        frase.append(" " + linha.trim());
                        ultimaLinha = new StringBuilder(frase);
                        if (linhaDeOrigemDaFrase == -1) {
                            linhaDeOrigemDaFrase = indiceDalinha;
                        }
                    }
                    houveResto = false;
                    continue inicio;
                } else if (linha.contains(".") && isPontoValido(linha)) { //Se existir ponto, iterar até o ponto pegando os caracteres e guardar em um string auxiliar.
                    int numeroDeCaracteres = linha.length();
//                    System.out.println("NÚMERO DE CARACTERES::::::: " + numeroDeCaracteres);
                    for (int n = 0; n < numeroDeCaracteres; n++) {
//                        System.out.println("LINHA DO FOR:::::::::::::::::::::::::::::::::::::: " + linha);
                        if (linha.charAt(n) != '.') {
                            stringAux.append(linha.charAt(n));
//                            System.out.println("stringAux.append(linha.charAt(n)); ======= " + stringAux);
                        } else {
                            if (linha.charAt(n) == '.') {
                                if (n < numeroDeCaracteres
                                        //&& !isReticncias(linha.substring(n)) 
                                        && !isAlgarismoRomano(new StringTokenizer(stringAux.toString()))
                                        && (!isOrdinal(linha, n))
                                        && (!isPontoAbreviacao(linha, n))) {
//                                    System.out.println("Cai aqui com 1: " + stringAux.toString());
                                    if (isReticncias(linha.substring(n))) {
                                        stringAux.append(linha.charAt(++n));
                                        stringAux.append(linha.charAt(++n));
                                    }

                                    stringAux.append(linha.charAt(n));
                                    if (n + 1 <= numeroDeCaracteres) {
                                        frase.append(" " + stringAux.toString().trim());
                                        stringAux = new StringBuilder();
                                        if (linhaDeOrigemDaFrase == -1) {
                                            linhaDeOrigemDaFrase = indiceDalinha;
                                        }

                                        Frase f = new Frase(frase.toString().trim());
                                        f.setLinhaDeOrigem(linhaDeOrigemDaFrase);

                                        frases.add(f);
                                        linhas.get(f.getLinhaDeOrigem()).setIndiceDeFraseAssociada(frases.lastIndexOf(f));
                                        linhas.get(f.getLinhaDeOrigem()).getFrasesAssociadas().add(new Integer(frases.lastIndexOf(f)));
                                        frase = new StringBuilder();
//                                        resto = new StringBuilder(linha.substring(n + 1).trim());
                                        resto = new StringBuilder(linha.substring(n).trim());

                                        f.setIndiceDeOrigemDaCadeiaNaLinha(indiceDeOrigemDaCadeiaNaLinha);
//                                        System.out.println("1 - Linha: " + linha);
//                                        System.out.println("1 - indice de origem: " + indiceDeOrigemDaCadeiaNaLinha);
                                        indiceDeOrigemDaCadeiaNaLinha = tamanhoDaLinhaOriginal - resto.length();
                                        if (resto.toString().trim() == "\"") {
                                            resto = new StringBuilder("");
                                        }

                                        if (resto.toString().trim().length() == 0) {
                                            indiceDeOrigemDaCadeiaNaLinha = 0;
                                            linhaDeOrigemDaFrase = -1;
                                            houveResto = false;
                                            continue inicio;
                                        }

                                        houveResto = true;
                                        if (resto.toString().contains(".") && isPontoValido(resto.toString())) {
                                            resto = new StringBuilder(tratarResto(new StringBuilder(linha.substring(n + 1)), indiceDalinha, indiceDeOrigemDaCadeiaNaLinha, tamanhoDaLinhaOriginal));
                                            if (resto.toString().trim().length() == 0) {
                                                houveResto = false;
                                                linhaDeOrigemDaFrase = -1;
                                                indiceDeOrigemDaCadeiaNaLinha = 0;
                                            } else {
                                                linhaDeOrigemDaFrase = indiceDalinha;
                                                houveResto = true;
                                                indiceDeOrigemDaCadeiaNaLinha = tamanhoDaLinhaOriginal - resto.length();
                                            }
                                        } else {
                                            linhaDeOrigemDaFrase = indiceDalinha;
                                        }
                                    }
                                    continue inicio;
                                } else {

                                    if (n + 1 < numeroDeCaracteres && isReticncias(linha.substring(n + 1))) {//Se for Reticencias - Tratar
//                                        System.out.println("Ricardo 1: " + linha);
                                        stringAux.append(linha.charAt(++n));
                                        stringAux.append(linha.charAt(++n));
                                        frase.append(" " + stringAux.toString().trim());
                                        stringAux = new StringBuilder();
                                    }
                                    if (n + 1 < numeroDeCaracteres && isPontoAbreviacao(linha, n)) {
//                                        System.out.println("Ricardo 2: " + linha);
                                        stringAux.append(linha.charAt(n));
                                        stringAux.append(linha.charAt(++n));
                                        frase.append(" " + stringAux.toString().trim());
                                        stringAux = new StringBuilder();
                                    }

                                    if (n + 1 < numeroDeCaracteres && isAlgarismoRomano(new StringTokenizer(stringAux.toString()))) {//Se for Reticencias - Tratar

//                                        System.out.println("Ricardo 3: " + linha);
//                                        System.out.println("Frase atual: " + frase);
                                        stringAux.append(linha.charAt(n));
                                        stringAux.append(linha.charAt(++n));//Inserido em 26/12/2016 para resolver problema de duplicidade da palavra Capitulo quando aparece com algorismo Romano
                                        if (!frase.toString().contains(stringAux)) {
                                            frase.append(" " + stringAux.toString().trim());
//                                            System.out.println("Adicionando 2: " + stringAux.toString().trim());
                                            stringAux = new StringBuilder();
//                                            System.out.println("Valor de N: " + n);
                                        }

                                    }
                                    if (n + 1 < numeroDeCaracteres && isOrdinal(linha, n)) {//Se for Reticencias - Tratar
//                                        System.out.println("Ricardo 4: " + linha);
                                        stringAux.append(linha.charAt(n));
                                        stringAux.append(linha.charAt(++n));
                                        if (n + 1 < numeroDeCaracteres && linha.charAt(n + 1) == ',') {
                                            stringAux.append(linha.charAt(++n));
                                        }
                                        frase.append(" " + stringAux.toString().trim());
                                        stringAux = new StringBuilder();
                                    }

                                }
                            }
                        }

                    }
                    System.gc();
                } else if (linha.contains("...")) {
                    if (frase.length() == 0) {
                        frase.append(linha.trim());
                        ultimaLinha = new StringBuilder(frase);
                    } else {
                        frase.append(" " + linha.trim());
                        ultimaLinha = new StringBuilder(frase);
                    }
                    continue inicio;
                }
                houveResto = true;

                resto.append(linha);
                stringAux = new StringBuilder();
//                System.out.println("Chamadando daqui o continue");
                continue inicio;
            }
            System.gc();
            frase = new StringBuilder();
            frase.append(" " + stringAux.toString().trim());
            stringAux = new StringBuilder();
        }
        //Se a última frase da página contiver letras e não conviver ponto - Melhorar isso aqui!!!!
        if (!resto.toString().contains(".") && !isPontoValido(resto.toString().trim())) {
            Frase f = new Frase(frase.toString());
            if (linhaDeOrigemDaFrase == -1) {
                linhaDeOrigemDaFrase = indiceDalinha;
            }
            f.setLinhaDeOrigem(linhaDeOrigemDaFrase);
//            System.out.println("2 - Linha: " + frase.toString());
//            System.out.println("2 - indice de origem: " + indiceDeOrigemDaCadeiaNaLinha);
            f.setIndiceDeOrigemDaCadeiaNaLinha(indiceDeOrigemDaCadeiaNaLinha);
            linhaDeOrigemDaFrase = -1;
            frases.add(f);
            linhas.get(f.getLinhaDeOrigem()).setIndiceDeFraseAssociada(frases.lastIndexOf(f));
            linhas.get(f.getLinhaDeOrigem()).getFrasesAssociadas().add(new Integer(frases.lastIndexOf(f)));

        }
    }

    private boolean isReticncias(String frase) {
        if (frase.length() >= 3) {
            if (frase.charAt(0) == '.'
                    && frase.charAt(1) == '.'
                    && frase.charAt(2) == '.') {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se a linha é um título. IMPORTANTE rever esté método pois ele
     * serve apenas para título de apenas uma linnha.
     *
     * @param linha
     * @param indiceDalinha
     * @return
     */
    private boolean isTitulo(int indiceDalinha) {

        String linhaBase = linhasOriginais.get(indiceDalinha);
//        System.out.print("É um título? " + linhaBase);
        int comprimento = linhaBase.trim().length() - 1;
        if (indiceDalinha == linhasOriginais.size() - 1) {//Verifica se é a última linha da Página
//            System.out.println(" Não é um título");
//            System.out.println("Retornei daqui 1");
            return false;//Não é um título
        }
        int numeroDaLinhaSeguinte = indiceDalinha + 1;
        String linhaSeguinte = null;
        //Encontra o índice da linha seguinte
        boolean pulouLinha = false;
        try {
            while (numeroDaLinhaSeguinte < linhasOriginais.size() - 1 && linhasOriginais.get(numeroDaLinhaSeguinte).trim().length() == 0) {
                numeroDaLinhaSeguinte++;
                pulouLinha = true;
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("O erro é aqui!!!");
        }
        linhaSeguinte = linhasOriginais.get(numeroDaLinhaSeguinte);
        if (numeroDaLinhaSeguinte == linhasOriginais.size() - 1 || linhaSeguinte.toString().trim().length() == 0) {
//            System.out.println("numeroDaLinhaSeguinte == linhasOriginais.size() - 1 >>>>> " + (numeroDaLinhaSeguinte == linhasOriginais.size() - 1));
//            System.out.println("linhaSeguinte.toString().trim().length() == 0) >>>>>>> " + (linhaSeguinte.toString().trim().length() == 0));
//            System.out.println("Retornei daqui 2");
//            System.out.println(" Não é um título");
            //return false;
            if (pulouLinha) {
                return true;
            }
            return false;
        }
        if (alfabeto.contains("" + linhaBase.trim().charAt(0))//Se começar com letra maiúscula
                && linhaBase.trim().charAt(comprimento) != '.' //Se não terminar com ponto.
                && alfabeto.contains("" + linhaSeguinte.trim().charAt(0))) {//Se a primeira letra da linha seguinte for Maiúscula
            //Então é um título
//            System.out.println("Linha base: " + linhaBase + " >>> É um título.");
//            System.out.println("Retornei daqui 3");
            return true;
        } else {
//            System.out.println("Linha base: " + linhaBase + " >>> Não é um título.");
//            System.out.println("Retornei daqui 4");
            return false;

        }
    }

    public void gerarAlfabeto() {
        alfabeto.add("A");
        alfabeto.add("B");
        alfabeto.add("C");
        alfabeto.add("D");
        alfabeto.add("E");
        alfabeto.add("F");
        alfabeto.add("G");
        alfabeto.add("H");
        alfabeto.add("I");
        alfabeto.add("J");
        alfabeto.add("L");
        alfabeto.add("M");
        alfabeto.add("N");
        alfabeto.add("O");
        alfabeto.add("P");
        alfabeto.add("Q");
        alfabeto.add("R");
        alfabeto.add("S");
        alfabeto.add("T");
        alfabeto.add("U");
        alfabeto.add("V");
        alfabeto.add("W");
        alfabeto.add("X");
        alfabeto.add("Y");
        alfabeto.add("Z");
    }

    private String[] preencherVetorToken(StringTokenizer st) {
        int tamanho = st.countTokens();
        String palavras[] = new String[tamanho];
        int i = 0;
        while (st.hasMoreTokens()) {
            palavras[i++] = st.nextToken();
        }
        return palavras;
    }

    //Melhorar esse método
    private boolean isPontoValido(String frase) {
//        System.out.println("Existe um ponto válido: " + frase);
        StringTokenizer st = new StringTokenizer(frase);
        int tamanho = st.countTokens();
        String palavras[] = preencherVetorToken(new StringTokenizer(frase));
        String temp = "";
        int posicaoToken = -1;
        inicio:
        while (st.hasMoreTokens()) {
            posicaoToken++;
            temp = st.nextToken();
            if (isPronomeTratamento(temp)) {//Se for encontrado um pronome de tratamento, continua a busca
                continue inicio;
            } else if (temp.matches(".*\\d.*")) {//Se for encontrado um número continua a pesquisa.
                if (temp.charAt(temp.length() - 1) == '.' && (palavras.length - 1 < posicaoToken)) {
                    if (alfabeto.contains("" + palavras[posicaoToken + 1].charAt(0))) {
//                        System.out.println("Retornando: " + true);
                        return true;
                    }
                }
                if (temp.charAt(temp.length() - 1) == '.' && lendoLinha < linhasOriginais.size() - 1) {

                    if (considerarLinhaSeguinte(lendoLinha)) {
//                        System.out.println("Retornando: " + true);
                        return true;
                    }
                }

                if (temp.charAt(temp.length() - 1) == '.') {
//                    System.out.println("Retornando: " + true);
                    return true;
                }
                continue inicio;

            } else if (temp.contains("°") || temp.contains("°") || temp.contains("ª") || temp.contains("ª")) {
                continue inicio;
            } else if (temp.contains(".,")) {//Se for encontrado um número continua a pesquisa.
                continue inicio;
            } else if (temp.contains("pág.")) {
                continue inicio;
            }

            if (temp.contains(".")) {
//                System.out.println("Retornando: (temp.contains(\".\"))" + true);
                return true;
            }

        }

        return false;
    }

    /**
     * Retorna verdadeiro se o caracter da linha seguinte for um início válido
     * de paragráfo.
     *
     * @param linhaAtual
     * @return
     */
    private boolean considerarLinhaSeguinte(int linhaAtual) {
        linhaAtual++;
        while (linhasOriginais.get(linhaAtual).length() == 0) {
            linhaAtual++;
            if (linhaAtual > linhasOriginais.size() - 1) {
                return false;
            }
        }
        if (alfabeto.contains("" + linhasOriginais.get(linhaAtual).trim().charAt(0))) {
            return true;
        }
        return false;
    }

    @Deprecated
    public void extrairFrasesInterando() {
        int numeroDeCaracteres = Livro.getStringLivro().length();
        for (int n = 0; n < numeroDeCaracteres; n++) {
            if (Livro.getStringLivro().charAt(n) != '\\' && Livro.getStringLivro().charAt(n + 1) != 'n') {

                //System.out.print(Livro.getLivro().charAt(n));
                if (Livro.getStringLivro().charAt(n) == '.') {
                    //System.out.println();
                }
            }
        }
        System.exit(0);
    }

    public boolean isPronomeTratamento(String token) {
        return pronomesDeTratamento.contains(token);
    }

    private void carregarPronomes() {
        String[] pronomes = {"V.", "VV.", "Sr.", "Sr.ª", "Srs.", "Srª.s", "S.ª", "Sª.s", "Ex.ª", "Ex.ªs", "Em.ª", "Em.ªs", "A.", "V.S.", "Rev.mª", "Rev.mªs", "Mag.ª", "Mag.ªs", "PP.", "M.", "P.", "S.", "N."};
        for (String p : pronomes) {
            pronomesDeTratamento.add(p);
        }
    }

    public void imprimirFrases() {
        for (String nf : getFraseEncontrada()) {
        }
    }

    /**
     * @return the fraseEncontrada
     */
    public ArrayList<String> getFraseEncontrada() {
        return fraseEncontrada;
    }

    /**
     * @param fraseEncontrada the fraseEncontrada to set
     */
    public void setFraseEncontrada(ArrayList<String> fraseEncontrada) {
        this.fraseEncontrada = fraseEncontrada;
    }

    private StringBuilder tratarResto(StringBuilder linha, int linhaDeOrigemDaFrase, int indiceDeOrigemDaCadeiaNaLinha, int tamanhoDaLinhaOriginal) {
        if (!linha.toString().contains(".") || !isPontoValido(linha.toString()) || linha.toString().trim().length() == 0) {

            return linha;
        } else {
            StringBuilder resto = new StringBuilder();
            StringBuilder frase = new StringBuilder();
            StringBuilder stringAux = new StringBuilder();
            int numeroDeCaracteres = linha.length();
            for (int n = 0; n < numeroDeCaracteres; n++) {
                if (linha.charAt(n) != '.') {
                    stringAux.append(linha.charAt(n));
                } else {
                    if (linha.charAt(n) == '.') {
                        if (n < numeroDeCaracteres //                                && !isReticncias(linha.substring(n))
                                ) {
                            if (isReticncias(linha.substring(n))) {
                                stringAux.append(linha.charAt(++n));
                                stringAux.append(linha.charAt(++n));
                            }

                            stringAux.append(linha.charAt(n));
                            if (n + 1 <= numeroDeCaracteres) {
                                resto = new StringBuilder(linha.substring(n + 1).trim());
                                frase.append(" " + stringAux.toString().trim());

                                stringAux = new StringBuilder();

                                Frase f = new Frase(frase.toString().trim());
                                f.setLinhaDeOrigem(linhaDeOrigemDaFrase);
                                f.setIndiceDeOrigemDaCadeiaNaLinha(indiceDeOrigemDaCadeiaNaLinha);
                                indiceDeOrigemDaCadeiaNaLinha = tamanhoDaLinhaOriginal - resto.length();
                                frases.add(f);
                                linhas.get(f.getLinhaDeOrigem()).setIndiceDeFraseAssociada(frases.lastIndexOf(f));
                                linhas.get(f.getLinhaDeOrigem()).getFrasesAssociadas().add(new Integer(frases.lastIndexOf(f)));
                                frase = new StringBuilder();
                                break;
                            }
                        }

                    }
                }
            }

            if (resto.toString().contains(".") && isPontoValido(resto.toString())) {
                return resto = tratarResto(resto, linhaDeOrigemDaFrase, indiceDeOrigemDaCadeiaNaLinha, tamanhoDaLinhaOriginal);
            }
            return resto;
        }

    }

    private boolean isAlgarismoRomano(StringTokenizer palavra) {
        String algarismo = null;
        //  System.out.println("Verificando algarismo Romano: " + palavra);
        while (palavra.hasMoreTokens()) {
            algarismo = palavra.nextToken();
        }
//        System.out.println("Verificando algarismo romano: " + algarismo);
//        System.out.println("É um algarismo: " + isAlgarismoRomano(algarismo));
        return isAlgarismoRomano(algarismo);
    }

    private boolean isAlgarismoRomano(String token) {
//        System.out.println("Verificando algarismo Romano: " + token);
        return algarismosRomanos.contains(token);
    }

    private void carregarAlgarismos() {
        algarismosRomanos.addAll(Arrays.asList("I", "II", "III", "IV", "V", "VI",
                "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI",
                "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV",
                "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI",
                "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII",
                "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV",
                "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII", "LIII",
                "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX", "LXI", "LXII",
                "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
                "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII",
                "LXXVIII", "LXXIX", "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV",
                "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC", "XCI",
                "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"));
    }

    /**
     * Avalia um cadeia de caracteres e retorna vedadeiro se o caracter indicado
     * é um caracter de ordenação.
     *
     * @param linha - Cadeia de caracteres que deve ser analisada.
     * @param posicao - Posição do caracter que deve ser analisado.
     * @return
     */
    private boolean isOrdinal(String linha, int posicao) {
        int tamanho = linha.length() - 1;
        if (posicao < tamanho
                && (linha.charAt(posicao + 1) == '°' || linha.charAt(posicao + 1) == 'ª' || linha.charAt(posicao + 1) == 'º' || linha.charAt(posicao + 1) == 'ª')) {
            return true;
        }
        return false;
    }

    private boolean isPontoAbreviacao(String linha, int posicao) {
        int tamanho = linha.length() - 1;
        if (getUltimaPalavraDaLinha(linha.substring(0, posicao)).contains("pág")) {
            return true;
        }
        String tokens[] = preencherVetorToken(new StringTokenizer(linha.substring(1, posicao + 1)));
        if (tokens.length > 0 && isPronomeTratamento(tokens[tokens.length - 1])) {
            return true;
        }
        if (posicao < tamanho
                && (linha.charAt(posicao + 1) == ',')) {
            return true;
        }
        return false;
    }

    private String getUltimaPalavraDaLinha(String linha) {
        String palavras[] = preencherVetorToken(new StringTokenizer(linha));
        if (palavras.length == 0) {
            return "";
        }
        return palavras[palavras.length - 1];
    }

    public void imprimeVersos() {
        System.out.println("Pagina: " + this.numero);
        for (Frase f : frases) {
            if (f.getVerso() != null) {
                System.out.println("Linha Correspondente: " + getLinhasOriginais().get(f.getLinhaDeOrigem()));
                System.out.println("Verso: " + f.getVerso().toString());
                System.out.println("Verso marcado: " + f.getVerso().getVersoEscandido());
                System.out.println("Quantidade: " + f.getVerso().getNumeroDeSilabas());
                System.out.println("Tipo: " + f.getVerso().getClassificacao());
                System.out.println("Posicionamento das Tônicas: " + f.getVerso().getPosicionamentoDasTonicas());
                System.out.println("Classificação Sub-categoria: " + f.getVerso().getSubCategoria());
                System.out.println("Indice de Origem da Cadeia: " + f.getIndiceDeOrigemDaCadeiaNaLinha());
//                System.out.println("");
            }

        }
    }

    public ArrayList<Verso> getVersos() {
        return versos;
    }

    public void setVersos(ArrayList<Verso> versos) {
        this.versos = versos;
    }

    public ArrayList<Frase> getFrases() {
        return frases;
    }

    public void setFrases(ArrayList<Frase> frases) {
        this.frases = frases;
    }

    /**
     * 26 de junho de 2020 O objetivo deste método é realizar a marcação dos
     * versos encontrados pelo sistema nas frases extraídas na etapa de
     * pré-processamento feito pelo mives. Dessa forma, a problemática de lidar
     * com as inconsistências do texto original, produzidas no momento da
     * conversão do PDF para o TXT, podem ser contornadas.
     *
     * @param livro
     */
    public void gerarLinhasEscandidasV2() {
        int contador = 1;

        ArrayList<Frase> frases = getFrases();
        int indexFrase = -1;
        for (Frase frase : frases) {
            indexFrase++;
            if (frase.toString().length() != 0) {//Existem uma frase?
                if (Utilitario.existeUmaPalavra(frase.toString())) {//Não é apenas um número de página.
                    if (frase.getVerso() != null) {//existe verso associado a frase?  
                        marcarVerso(contador, frase);
                    } else {
                        frase.setFraseSaida(contador + ": " + frase.toString());
                    }
                    contador++;
                }

            }
        }

    }

    /**
     * Insere marcações HTML na frase em que foi identificado um verso no final.
     *
     * @param contador
     * @param frase
     */
    private void marcarVerso(int contador, Frase frase) {

        //Analisar remover essa linha
        if (frase.getVerso().isSubstituicao()) {
            return;
        }
        String fraseSaida;
        frase.getVerso().setLink(link);
        String verso = frase.getVerso().toString();
        System.out.println("frase.getVerso().toString()" + verso);
        System.out.println("Palavras do verso: " + frase.getVerso().getPalavras());
        fraseSaida = frase.toString().replace(verso, "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + verso + "</span>");
        frase.getVerso().setSubstituicao(true);
        frase.setFraseSaida(contador + ": " + fraseSaida);
        link++;
    }

    public void gerarLinhasEscandidas() {
        int numeroDeLinhasDaPagina = linhas.size();
        inicio:
        for (int indiceDaLinha = 0; indiceDaLinha < numeroDeLinhasDaPagina; indiceDaLinha++) {
            //Toda linha tem uma frase ou várias frases associadas;
            //Cada frase pode ter um verso associado;
            Linha linha = linhas.get(indiceDaLinha);
            if (linha.getLinha().length() == 0) {
                linha.setLinhaComEscansao("");
                continue inicio;
            }
            for (Integer i : linha.getFrasesAssociadas()) {
                System.out.println("Linha: " + linha.getLinha());
                System.out.println("Número de frases Associadas: " + linha.getFrasesAssociadas().size());
                if (frases.get(i).getVerso() != null) {
                    //  substituirCaracteres(indiceDaLinha);
                    if (!Livro.getInstance().getTipoDeBusca().equals("Frases Completas.") && !Livro.getInstance().getTipoDeBusca().equals("Início de Frase.")) {
                        if (linha.getLinha().replaceAll("-", "").toLowerCase().contains(frases.get(i).getVerso().getPalavras().toLowerCase())) {
                            marcarVersosFinalV2(indiceDaLinha, 0, i);
//                            System.out.println("Entrei aqui 1: " + frases.get(i).getVerso().getPalavras());
                        } else {
//                            System.out.println("Entrei aqui 2: " + frases.get(i).getVerso().getPalavras());
//                            System.out.println("Enviando linha para bucar origem: " + linha.getLinha());
//                            System.out.println("Verso: " + frases.get(i).getVerso().getPalavras());
                            int indiceComeca = comecaNestaLinha(indiceDaLinha, linha, frases.get(i));
//                            System.out.println("LINHA DE ORIGEM: " + indiceDaLinha);
//                            System.out.println("ESTA NESTA LINHA: " + indiceComeca);
                            int aux = indiceDaLinha;
                            while (indiceComeca == 0 && aux > 0) {
//                                System.out.println("Procuranto origem");
                                aux--;
                                indiceComeca = comecaNestaLinha(aux, linha, frases.get(i));
                            }
                            if (indiceComeca != 0) {
//                                System.out.println("Entrei aqui 3: " + frases.get(i).getVerso().getPalavras());
                                marcarVersosFinalV2(indiceComeca, comeca, i);

                            } else {
//                                System.out.println("Entrei aqui 4: " + frases.get(i).getVerso().getPalavras());
                                linha.setLinhaComEscansao(linha.getLinha());
                            }
                            comeca = 0;
                        }
//                        System.out.println("Não consegui marcar: " + frases.get(i).getVerso().getPalavras());
//                        System.out.println("Marcando verso final");
                    } else {
//                        System.out.println("LOCAL: " + frases.get(i).getVerso().getLocal());
//                        if (frases.get(i).getVerso().getLocal().toLowerCase().equals("Início de frase.".toLowerCase())) {
//                            System.out.println("ENVIANDO PARA MARCAR NO INÍCIO....");
//                            marcarVersosInicio(indiceDaLinha);
//                        } else {
                        marcarVersos(indiceDaLinha);
//                        }

                    }

                } else {
                    if (!linha.isSubstituicao()) {
                        linha.setLinhaComEscansao(linha.getLinha());
                    }

                }

            }
            if (!linha.isSubstituicao()) {
                linha.setLinhaComEscansao(linha.getLinha());
            }

        }
    }
    int comeca = 0;

    private int comecaNestaLinha(Integer indice, Linha linha, Frase frase) {

        System.out.println("ComecaNestaLinha.........................");
        System.out.println("Recebendo para procurar na linha: " + linhas.get(indice).getLinha());
        System.out.println("O verso: " + frase.getVerso().getPalavras());

        StringTokenizer v = new StringTokenizer(frase.getVerso().getPalavras());
        String vetorVerso[] = new String[v.countTokens()];
        int i = 0;

        i = 0;

        while (v.hasMoreTokens()) {
            vetorVerso[i] = v.nextToken();
            i++;
        }

        for (String s : vetorVerso) {
            System.out.println(s);
        }

        //    int comecaAqui = indice;
        String primeiraPalavraVerso = vetorVerso[0];

        int t;

//        for (t = indice; t < linhas.size(); t++) {
//            indice = t;
        StringTokenizer l = new StringTokenizer(linhas.get(indice).getLinha());
        String vetorLinha[] = new String[l.countTokens()];

        i = 0;
        while (l.hasMoreTokens()) {
            vetorLinha[i] = l.nextToken();
            i++;
        }

//        for (String s : vetorLinha) {
//            System.out.println(s);
//        }
        i = 0;

        for (String g : vetorLinha) {
            i++;
//            System.out.println("Valor de g: " + g);
//            System.out.println("Valord e i: " + i);
//            System.out.println("Tamanho do vetor: " + vetorLinha.length);

            if (i == vetorLinha.length && g.replace("-", "").equals(primeiraPalavraVerso.replace("-", ""))) {//A última palavra da linha dá início ao verso

//                System.out.println("ENTREI...ENTREI...ENTREI...ENTREI...ENTREI===> " + g);
                if (i == vetorLinha.length) {
                    //      comecaAqui = indice;
//                    System.out.println("ESTOU NA ÚLTIMA PALAVRA");
//                    System.out.println("ÚLTIMA PALAVRA: " + g);
//                    System.out.println("Linha de início: " + linhas.get(indice).getLinha());
                    if (linhas.size() > indice) {
                        int linhaAtual = 0;
                        indice++;
                        while (linhas.get(indice).getLinha().length() == 0) {
                            indice++;
                            linhaAtual++;
                        }
                        comeca = vetorLinha.length - 1;
                        StringTokenizer token = new StringTokenizer(linhas.get(indice).getLinha());
                        String aux = token.nextToken();
//                        System.out.println("PRIMEIRA PALAVRA DA PRÓXIMA LINHA: " + aux);
                        if (aux.replaceAll("-", "").toLowerCase().equals(vetorVerso[1].toLowerCase().replace("-", ""))) {
                            return indice - linhaAtual - 1;
                        }
                    }

                }
            }
            if (g.replace("-", "").equals(primeiraPalavraVerso.replace("-", ""))) {//O verso começa no meio da linha
                int indiceLinha = i;
                int indiceVerso = 1;
//                System.out.println("Achei:............ ");
//                System.out.println("Primeira palavra encontrada: " + g);
//                System.out.println("Próxima palavra do verso a ser analisada: " + vetorVerso[indiceVerso]);

                // System.out.println("Proxima palavra da linha a ser analisada: " + vetorLinha[indiceLinha]);
                while (indiceVerso < vetorVerso.length && indiceLinha < vetorLinha.length
                        && removerCaracteres(vetorLinha[indiceLinha]).equals(removerCaracteres(vetorVerso[indiceVerso]))) {
//                    System.out.println("É igual: " + vetorLinha[indiceLinha]);
                    indiceLinha++;
                    indiceVerso++;
                }

//                System.out.println("indiceLinha == vetorLinha.length? " + (indiceLinha == vetorLinha.length));
                if (linhas.get(indice).getLinha().toLowerCase().contains(frase.getVerso().getPalavras().toLowerCase())) {
//                    System.out.println("RICARDO");
//                    System.out.println("RICARDO: " + linhas.get(indice).getLinha());
//                    System.out.println("RICARDO: " + frase.getVerso().getPalavras());
                    comeca = i;
                    return indice;
                }
                if (indiceLinha == vetorLinha.length) {
                    if (indice < linhas.size() - 1) {
                        int linhaAtual = 0;
                        indice++;
                        while (indice < linhas.size() && linhas.get(indice).getLinha().length() == 0) {
                            indice++;
                            linhaAtual++;
                        }

//                        System.out.println("Linha: " + linha.getLinha());
//                        System.out.println("Verso procurado: " + frase.getVerso().getPalavras());
                        if (indice < linhas.size()) {
//                            System.out.println("Entrei.....");
//                            System.out.println("Linha: " + linha.getLinha());
//                            System.out.println("Verso procurado: " + frase.getVerso().getPalavras());
                            comeca = i;
                            StringTokenizer token = new StringTokenizer(linhas.get(indice).getLinha());
                            String aux = token.nextToken();
//                            System.out.println("PRIMEIRA PALAVRA DA PRÓXIMA LINHA: " + aux);
//                            System.out.println("Começa aqui? " + (removerCaracteres(aux).equals(removerCaracteres(vetorVerso[indiceVerso]))));
                            if (removerCaracteres(aux).replace("-", "").equals(removerCaracteres(vetorVerso[indiceVerso].replace("-", "")))) {

                                return indice - linhaAtual - 1;
                            }
                        }
                    }
                }
            }
        }
        //Não está nesta linha procure na próxima
        if (indice < linhas.size() - 1) {
//            System.out.println("Chamada recursiva.....................................");
//            System.out.println("Verso: " + frase.getVerso().getPalavras());
//            System.out.println("Linha: " + linhas.get(indice + 1).getLinha());
//            System.out.println("Índice linha: " + indice);

            if (linhas.get(indice + 1).getLinha().toLowerCase().replaceAll("-", "").contains(frase.getVerso().getPalavras().toLowerCase())) {
                comeca = posicaoInicialDoVerso(linhas.get(indice + 1).getLinha().toLowerCase(), frase.getVerso().getPalavras().toLowerCase());
//                System.out.println("Começa na linha: " + comeca);
                return indice + 1;
            }

            int retorno = comecaNestaLinha(indice + 1, linhas.get(indice + 1), frase);
//            System.out.println("Retorno: " + retorno);
            return retorno;
        } else {

            return 0;
        }

    }

    private int posicaoInicialDoVerso(String linha, String verso) {

        int inicio = linha.toLowerCase().indexOf(verso.toLowerCase());

        StringBuilder st = new StringBuilder();
        for (int b = 0; b < inicio; b++) {
            st.append(linha.charAt(b));
        }
        StringTokenizer v = new StringTokenizer(st.toString());
        return v.countTokens() + 1;

    }

    private String removerCaracteres(String palavra) {
        if (palavra.contains(".")) {
            return palavra.replaceAll(".", "");
        }

        if (palavra.contains(",")) {
            return palavra.replace(",", "");
        }

        if (palavra.contains("!")) {
            return palavra.replace("!", "");
        }
        if (palavra.contains("”")) {
            return palavra.replace("”", "");
        }

        return palavra;

    }

    public void imprimeFrasesComVersos() {
        int numeroDeLinhasDaPagina = linhas.size();
        int indiceDaFrase = 0;
        for (Frase f : frases) {
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("Indice da frase: " + indiceDaFrase);
            System.out.println("Frase: " + f.getPalavras());
            if (f.getVerso() != null) {
                System.out.println("Verso: " + f.getVerso().toString());
            }
            indiceDaFrase++;
        }
        imprimeLinhasFrasesAssociadas();
    }

    public void imprimeLinhasFrasesAssociadas() {
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><>LINHAS E FRASES<><><><><><><><><><><><><><><><><><><><><><>");
        for (Linha l : linhas) {
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("Linha: " + l.getLinha());
            System.out.println("Número de Frases Associadas: " + l.getFrasesAssociadas().size());
            for (Integer i : l.getFrasesAssociadas()) {
                System.out.println("Frase: " + i + " " + frases.get(i).toString() + " Possui verso? " + (frases.get(i).getVerso() == null ? false : true));
            }
        }
    }

//    /**
//     * Versão: Nova versão MIVES Marca no HTML o trecho de texto onde o verso
//     * foi encontrado em 21/05/2018
//     *
//     * @param indiceDaLinha
//     */
//    private void marcarVersosFinal(int indiceDaLinha, int origem, int indiceVerso) {
//
//        StringBuilder retorno = new StringBuilder();
//        Linha linhaBase = linhas.get(indiceDaLinha);
//
//        for (Integer nf : linhaBase.getFrasesAssociadas()) {
//            if (exiteResto) {
//                retorno.append("<span style=\"background-color: #8CC5F4\">");
//                retorno.append(restoInserir);
//                exiteResto = false;
//                retorno.append("</span> ");
//            }
//
//            if (frases.get(nf).getVerso() != null) {
//                link++;
//                String verso = frases.get(nf).getVerso().getPalavras();
//                System.out.println("Linha base: " + linhaBase.getLinha());
//                System.out.println("Frase: " + frases.get(nf).getPalavras());
//
//                System.out.println("Verso: " + verso);
//
//                if (linhaBase.getLinha().contains(verso)) {
//                    retorno.append(linhaBase.getLinha().replace(verso, "<a id=\"" + link + "\"><span style=\"background-color: #8CC5F4\">" + verso + "</span>"));
//                    linhaBase.setSubstituicao(true);
//                    linhaBase.setLinhaComEscansao(retorno.toString());
//                } else {
//
//                }
//
////                    StringTokenizer st = new StringTokenizer(verso);
////                    String arrayVerso[] = new String[st.countTokens()];
////                    int i = 0;
////                    while (st.hasMoreTokens()) {
////                        arrayVerso[i] = st.nextToken();
////                        i++;
////                    }
////
////                    StringTokenizer stLinha = new StringTokenizer(verso);
////                    String arrayLinha[] = new String[stLinha.countTokens()];
////                    i = 0;
////                    while (stLinha.hasMoreTokens()) {
////                        arrayLinha[i] = stLinha.nextToken();
////                        i++;
////                    }
////
////                    i = arrayVerso.length - 1;
////                    int j = arrayLinha.length - 1;
////                    int quantidadeLinha = 0;
////                    while (i >= 0 && arrayVerso[i].equals(arrayLinha[j])) {
////                        i--;
////                        j--;
////                        quantidadeLinha++;
////                    }
////                    int a = 0;
////                    j++;
////
////                    for (a = 0; a < j; a++) {
////                        retorno.append(arrayLinha[a] + " ");
////                    }
////
////                    retorno.append("<a id=\"" + link + "\"><span style=\"background-color: #8CC5F4\">");
////
////                    StringBuilder versoAux = new StringBuilder();
////                    for (i = 0; i < quantidadeLinha; i++) {
////                        retorno.append(arrayVerso[i] + " ");
////                    }
////                    retorno.append("</span>");
////
////                    StringBuilder resto = new StringBuilder();
////                    for (int b = i; b < arrayVerso.length; b++) {
////                        this.exiteResto = true;
////                        resto.append(arrayVerso[b] + " ");
////                    }
////                    quantidadeDoResto = new StringTokenizer(resto.toString()).countTokens();
////                    restoInserir = resto.toString();
////                    linhaBase.setSubstituicao(true);
////                    linhaBase.setLinhaComEscansao(retorno.toString());
//            }
//
//        }
//    }
    /**
     * Versão: Nova versão MIVES Marca no HTML o trecho de texto onde o verso
     * foi encontrado em 21/05/2018
     *
     * @param indiceDaLinha
     */
    private void marcarVersosFinalV2(int indiceDaLinha, int origem, int indiceVerso) {

        StringBuilder retorno = new StringBuilder();

        Linha linhaBase = linhas.get(indiceDaLinha);

        if (frases.get(indiceVerso).getVerso().isSubstituicao()) {
            return;
        }
        // link++; 07/09/2018
        String verso = frases.get(indiceVerso).getVerso().toString();
        System.out.println("Linha base: " + linhaBase.getLinha());
        System.out.println("Frase: " + frases.get(indiceVerso).getPalavras());

        frases.get(indiceVerso).getVerso().setLink(link);//Inserido em: 09/09/2018

        System.out.println("Verso: " + verso);

        if (linhaBase.getLinha().contains(verso)) {
            System.out.println("Tentativa 01");
            if (linhaBase.isSubstituicao()) {
                retorno.append(linhaBase.getLinhaComEscansao().replace(verso, "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + verso + "</span>"));
            } else {
                retorno.append(linhaBase.getLinha().replace(verso, "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + verso + "</span>"));
            }

            String aux = "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">";
            StringTokenizer auxToken = new StringTokenizer(aux);
            linhaBase.setDiferencaDeInserir(auxToken.countTokens() - 1);
            System.out.println("Retorno: " + retorno.toString());
            frases.get(indiceVerso).getVerso().setSubstituicao(true);
            linhaBase.setSubstituicao(true);
            linhaBase.setLinhaComEscansao(retorno.toString());
            link++;
            return;
        }
        if (linhaBase.getLinha().toLowerCase().contains(verso.toLowerCase())) {
            System.out.println("Consegui agora vai");
            int inicio = linhaBase.getLinha().toLowerCase().indexOf(verso.toLowerCase());
            retorno.append(linhaBase.getLinha());
            retorno.replace(inicio, inicio + verso.length(), "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + verso + "</span>");
//            retorno.append(linhaBase.getLinha().replace(verso.toLowerCase(), "<a id=\"" + link + "\"><span style=\"background-color: #8CC5F4\">" + verso + "</span>"));
//            retorno.append(linhaBase.getLinha().replace(verso.toLowerCase(), "<a id=\"" + link + "\"><span style=\"background-color: #8CC5F4\">" + verso + "</span>"));
            frases.get(indiceVerso).getVerso().setSubstituicao(true);
            linhaBase.setSubstituicao(true);
            linhaBase.setLinhaComEscansao(retorno.toString());
            link++;
            return;
        } else {
            origem--;
            StringTokenizer l;// = new StringTokenizer(linhaBase.getLinha());
            if (linhaBase.isSubstituicao()) {
                l = new StringTokenizer(linhaBase.getLinhaComEscansao());
                System.out.println("Valor de Origem 1 - Antes: " + origem);
                origem += linhaBase.getDiferencaDeInserir();
                System.out.println("Valor de Origem 1 - Depois: " + origem);
            } else {
                l = new StringTokenizer(linhaBase.getLinha());
                origem++;
                System.out.println("Valor de Origem 2: " + origem);
            }
            //  StringTokenizer l = new StringTokenizer(linhaBase.getLinha());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Linha base: " + linhaBase.getLinha());
            System.out.println("Origem na palavra: " + comeca);
            System.out.println("Verso: " + verso);

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            int i = 0;
            while (i < origem && l.hasMoreTokens()) {
                System.out.println("Origem: " + origem);
                System.out.println("Linha Base: " + linhaBase.getLinha());
                i++;
                String add = l.nextToken() + " ";
                System.out.println("ADICIONANDO: " + add);
                retorno.append(add);
            }
            System.out.println("COMEÇANDO A INSERIR....");
            retorno.append("<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + l.nextToken() + " ");
            System.out.println("Retorno: " + retorno.toString());
            int indiceInserido = 0;
            //     System.out.println("Existem um próximo??? -> " + l.hasMoreTokens());

            while (l.hasMoreTokens() && indiceInserido <= new StringTokenizer(verso).countTokens()) {
                retorno.append(l.nextToken() + " ");
                System.out.println("Retorno: " + retorno.toString());
                System.out.println("Indice inserido: " + indiceInserido);
                indiceInserido++;
            }
            if (indiceInserido == 0) {
                indiceInserido = 1;
            }
            retorno.append("</span>");

            while (l.hasMoreTokens()) {
                retorno.append(l.nextToken() + " ");

            }
            linhaBase.setSubstituicao(true);
            System.out.println("retorno com linha de escansão: " + retorno.toString());
            linhaBase.setLinhaComEscansao(retorno.toString());
            frases.get(indiceVerso).getVerso().setSubstituicao(true);
            indiceDaLinha++;
            while (linhas.size() < indiceDaLinha && linhas.get(indiceDaLinha).getLinha().trim().length() == 0) {
                indiceDaLinha++;
            }

            StringTokenizer st = new StringTokenizer(verso.toLowerCase());
            String arrayVerso[] = new String[st.countTokens()];
            i = 0;
            while (st.hasMoreTokens()) {
                arrayVerso[i] = st.nextToken();
                i++;
            }
//
//            StringTokenizer stLinha = new StringTokenizer(linhas.get(indiceDaLinha).getLinha());
//            String arrayLinha[] = new String[stLinha.countTokens()];
//            i = 0;
//            while (stLinha.hasMoreTokens()) {
//                arrayLinha[i] = stLinha.nextToken();
//                i++;
//            }
//
            StringBuilder resto = new StringBuilder();
//            if (indiceInserido < arrayVerso.length && arrayVerso[indiceInserido].equalsIgnoreCase(auxTemp)) {
//                System.out.println("vamos vamos vamos vamos vamos vamos vamos vamos vamos");
//            indiceInserido++;
//            }

            while (indiceInserido < arrayVerso.length) {
                resto.append(arrayVerso[indiceInserido] + " ");
                indiceInserido++;
            }

            // StringBuilder resto = new StringBuilder();
            System.out.println("OLHA O RESTO AQUI: " + resto.toString());

            //  String novaLinha = linhas.get(indiceDaLinha).getLinha().replace(linhas.get(indiceDaLinha).getLinha(), "<span style=\"background-color: #8CC5F4\">" + resto.toString() + "</span> ");
            String novaLinha = linhas.get(indiceDaLinha).getLinha().replace(resto.toString().trim(), "<span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + resto.toString().trim() + "</span> ");
            System.out.println("SUBSTITUINDO NA LINHA: " + linhas.get(indiceDaLinha).getLinha());
            System.out.println("NOVA LINHA: " + novaLinha);
            linhas.get(indiceDaLinha).setLinhaComEscansao(novaLinha);

            linhas.get(indiceDaLinha).setSubstituicao(true);

        }
        link++;

    }

    boolean acrescentarUm = false;

    /**
     * Versão: Nova versão MIVES Marca no HTML o trecho de texto onde o verso
     * foi encontrado em 13/02/2018
     *
     * @param indiceDaLinha
     */
    private void marcarVersos(int indiceDaLinha) {
        Linha linhaBase = linhas.get(indiceDaLinha);
//        int incrementoDaSubstituicao = 0;
        for (Integer nf : linhaBase.getFrasesAssociadas()) {
            int auxLink;
            if (frases.get(nf).getVerso() != null && !(frases.get(nf).isIsReplace())) {
                String verso = frases.get(nf).getVerso().getPalavras();

                String linha = null;
                if (!linhaBase.isSubstituicao()) {
                    linha = linhaBase.getLinha();
                } else {
                    linha = linhaBase.getLinhaComEscansao();
                }
//                frases.get(nf).getIndiceDeOrigemDaCadeiaNaLinha();
                int posicaoInicial = frases.get(nf).getIndiceDeOrigemDaCadeiaNaLinha();
//                System.out.println("Valor de nf: " + nf);
//                System.out.println("linha de origem da frase: " + frases.get(nf).getLinhaDeOrigem());
                while (frases.get(nf).getVerso().getLocal().equals("Início de frase.") && indiceDaLinha != frases.get(nf).getLinhaDeOrigem()) {
                    System.out.println("Indice da linha: " + indiceDaLinha);
                    System.out.println("Indice da linha de origem: " + (frases.get(nf).getLinhaDeOrigem()));
                    System.out.println("Linha obtida: " + linhas.get((frases.get(nf).getLinhaDeOrigem())).getLinha());
                    indiceDaLinha++;
                    linhaBase = linhas.get(indiceDaLinha);
                    //Ajustar isso aqui redundante
                    if (!linhaBase.isSubstituicao()) {
                        linha = linhaBase.getLinha();
                    } else {
                        linha = linhaBase.getLinhaComEscansao();
                    }
                }

                if (posicaoInicial + (verso.length() + linhaBase.getDiferencaDeInserir()) <= linha.length()) {
//                    System.out.println("Esta na mesma linha/Linha base: " + linhaBase.getLinha());
//                    System.out.println("Numero da linha de origem: " + frases.get(nf).getLinhaDeOrigem());
//                    System.out.println("Linha selecionada: " + linhas.get(frases.get(nf).getLinhaDeOrigem()).getLinha());
//                    System.out.println("Verso: " + verso);
//                    System.out.println("Posição Inicial: " + posicaoInicial);

                    //O problema da substituição errada no caso de exitir dois versos na mesma linha está aqui neste ponto
//                    auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial + incrementoDaSubstituicao, verso, true);
//                    frases.get(nf).getVerso().setLink(auxLink);
//                    //10.09.2018
//                    incrementoDaSubstituicao += linhaBase.getLinhaComEscansao().length() - linha.length();
                    if (posicaoInicial != 0) {
                        auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial + 2, verso, true);
                    } else {
                        auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial, verso, true);
                    }
                    frases.get(nf).getVerso().setLink(auxLink);
                    frases.get(nf).getVerso().setSubstituicao(true);//Em 27/09 - Objetivo marcar o verso que foi substituíto e não produzir equivócos no na geração das estruturas de versificação
                    frases.get(nf).setIsReplace(true);
                    if (frases.get(nf).getVerso().getLocal().equals("Início de frase.")) {
                        return;
                    }
                } else {//Não está na mesma linha
                    System.out.println("Não está na mesma linha");
                    System.out.println("Linha: " + linha);
                    System.out.println("Verso: " + verso);
                    System.out.println("Posição inicial : " + posicaoInicial);
                    System.out.println("Linha base: " + linhaBase.getLinha());
                    int oQueJaFoiInserido = linhaBase.getLinha().length() - posicaoInicial;
                    System.out.println("O que já foi inserido");
                    System.out.println(" 1- Enviando para marcar: " + verso.substring(0, oQueJaFoiInserido));
                    auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial, verso.substring(0, oQueJaFoiInserido), false);//O que deve ser inserido até onde a linha permitir
                    frases.get(nf).getVerso().setLink(auxLink);
                    frases.get(nf).getVerso().setSubstituicao(true);//Em 27/09 - Objetivo marcar o verso que foi substituíto e não produzir equivócos no na geração das estruturas de versificação
                    verso = verso.substring(oQueJaFoiInserido + 1, verso.length());//Ainda falta inserir isso aqui...
                    int indiceDaLinhaAux = indiceDaLinha + 1;//Pegar a próxima linha
                    posicaoInicial = 0;//Na próxima linha, começcar pelo índice 0.
                    while (verso.length() > 0) {//Enquanto não terminar de inserir tudo
                        // oQueJaFoiInserido = linhaBase.getLinha().length() - posicaoInicial;
                        linhaBase = linhas.get(indiceDaLinhaAux);//Pegue a próxima linha
                        linha = linhaBase.getLinha();
                        if (verso.length() > linha.length()) {
                            System.out.println(" 2- Enviando para marcar: " + verso.substring(0, linha.length()));
                            auxLink = marcarNaMesmaLista(linhaBase.getLinha(), linhaBase, posicaoInicial, verso.substring(0, linha.length()), true);//O que deve ser inserido até onde a linha permitir 
                            frases.get(nf).getVerso().setLink(auxLink);
                            frases.get(nf).getVerso().setSubstituicao(true);//Em 27/09 - Objetivo marcar o verso que foi substituíto e não produzir equivócos no na geração das estruturas de versificação
                            verso = verso.substring(0, linha.length());
                            indiceDaLinhaAux++;
                            frases.get(nf).setIsReplace(true);
                        } else {
                            System.out.println(" 3- Enviando para marcar: " + verso);
                            auxLink = marcarNaMesmaLista(linhaBase.getLinha(), linhaBase, posicaoInicial, verso, true);//O que deve ser inserido até onde a linha permitir 
                            frases.get(nf).getVerso().setLink(auxLink);
                            frases.get(nf).getVerso().setSubstituicao(true);//Em 27/09 - Objetivo marcar o verso que foi substituíto e não produzir equivócos no na geração das estruturas de versificação
                            verso = "";
                            frases.get(nf).setIsReplace(true);
                        }

                    }

                }
            }
        }
    }

    /**
     * Versão: Nova versão MIVES Marca no HTML o trecho de texto onde o verso
     * foi encontrado em 13/02/2018
     *
     * @param indiceDaLinha
     */
    private void marcarVersosInicio(int indiceDaLinha) {
        Linha linhaBase = linhas.get(indiceDaLinha);
        for (Integer nf : linhaBase.getFrasesAssociadas()) {
            int auxLink;
            if (frases.get(nf).getVerso() != null && !(frases.get(nf).isIsReplace())) {
                String verso = frases.get(nf).getVerso().getPalavras();

                String linha = null;
                if (!linhaBase.isSubstituicao()) {
                    linha = linhaBase.getLinha();
                } else {
                    linha = linhaBase.getLinhaComEscansao();
                }

                System.out.println("getLinhaDeOrigem: " + linhas.get((frases.get(nf).getLinhaDeOrigem())).getLinha());
                System.out.println("getLinhaDeOrigem: " + frases.get(nf).getLinhaDeOrigem());
                System.out.println("indiceDaLinha: " + indiceDaLinha);

                int posicaoInicial = frases.get(nf).getIndiceDeOrigemDaCadeiaNaLinha();
                while (frases.get(nf).getVerso().getLocal().toLowerCase().equals("Início de frase.".toLowerCase()) && indiceDaLinha != frases.get(nf).getLinhaDeOrigem()) {
                    System.out.println("Indice da linha: " + indiceDaLinha);
                    System.out.println("Indice da linha de origem: " + (frases.get(nf).getLinhaDeOrigem()));
                    System.out.println("Linha obtida: " + linhas.get((frases.get(nf).getLinhaDeOrigem())).getLinha());
                    indiceDaLinha++;
                    linhaBase = linhas.get(indiceDaLinha);
                    //Ajustar isso aqui redundante
                    if (!linhaBase.isSubstituicao()) {
                        linha = linhaBase.getLinha();
                    } else {
                        linha = linhaBase.getLinhaComEscansao();
                    }
                }

                if (posicaoInicial + (verso.length() + linhaBase.getDiferencaDeInserir()) <= linha.length()) {
//                    System.out.println("Esta na mesma linha/Linha base: " + linhaBase.getLinha());
//                    System.out.println("Numero da linha de origem: " + frases.get(nf).getLinhaDeOrigem());
//                    System.out.println("Linha selecionada: " + linhas.get(frases.get(nf).getLinhaDeOrigem()).getLinha());
//                    System.out.println("Verso: " + verso);
//                    System.out.println("Posição Inicial: " + posicaoInicial);

                    if (posicaoInicial != 0) {
                        if (frases.get(nf).getVerso().getLocal().toLowerCase().equals("Início de frase.".toLowerCase())) {
                            auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial + 1, verso, true);
                        } else {
                            auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial + 2, verso, true);
                        }
                    } else {
                        auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial, verso, true);
                    }
                    frases.get(nf).getVerso().setLink(auxLink);
                    frases.get(nf).setIsReplace(true);
                    if (frases.get(nf).getVerso().getLocal().toLowerCase().equals("Início de frase.".toLowerCase())) {
                        return;
                    }
                } else {//Não está na mesma linha
                    System.out.println("Não está na mesma linha");
                    System.out.println("Linha: " + linha);
                    System.out.println("Verso: " + verso);
                    System.out.println("Posição inicial : " + posicaoInicial);
                    System.out.println("Linha base: " + linhaBase.getLinha());
                    int oQueJaFoiInserido = linhaBase.getLinha().length() - posicaoInicial;
                    System.out.println("O que já foi inserido");
                    System.out.println(" 1- Enviando para marcar: " + verso.substring(0, oQueJaFoiInserido));
                    auxLink = marcarNaMesmaLista(linha, linhaBase, posicaoInicial, verso.substring(0, oQueJaFoiInserido), false);//O que deve ser inserido até onde a linha permitir
                    frases.get(nf).getVerso().setLink(auxLink);
                    verso = verso.substring(oQueJaFoiInserido + 1, verso.length());//Ainda falta inserir isso aqui...
                    int indiceDaLinhaAux = indiceDaLinha + 1;//Pegar a próxima linha
                    posicaoInicial = 0;//Na próxima linha, começcar pelo índice 0.
                    while (verso.length() > 0) {//Enquanto não terminar de inserir tudo

                        linhaBase = linhas.get(indiceDaLinhaAux);//Pegue a próxima linha
                        linha = linhaBase.getLinha();
                        if (verso.length() > linha.length()) {
                            System.out.println(" 2- Enviando para marcar: " + verso.substring(0, linha.length()));
                            auxLink = marcarNaMesmaLista(linhaBase.getLinha(), linhaBase, posicaoInicial, verso.substring(0, linha.length()), true);//O que deve ser inserido até onde a linha permitir 
                            frases.get(nf).getVerso().setLink(auxLink);
                            verso = verso.substring(0, linha.length());
                            indiceDaLinhaAux++;
                            frases.get(nf).setIsReplace(true);
                        } else {
                            System.out.println(" 3- Enviando para marcar: " + verso);
                            auxLink = marcarNaMesmaLista(linhaBase.getLinha(), linhaBase, posicaoInicial, verso, true);//O que deve ser inserido até onde a linha permitir 
                            frases.get(nf).getVerso().setLink(auxLink);
                            verso = "";
                            frases.get(nf).setIsReplace(true);
                        }

                    }

                }
            }
        }
    }

    public static int link = 0;

    private int marcarNaMesmaLista(String linha, Linha linhaBase, int posicaoInicial, String verso, boolean incrementarLink) {
        StringBuilder retorno = new StringBuilder(linha);
        if (linhaBase.isSubstituicao()) {
            if (acrescentarUm) {
                posicaoInicial += linhaBase.getDiferencaDeInserir() + 1;
                acrescentarUm = false;
            } else {
                posicaoInicial += linhaBase.getDiferencaDeInserir() + 1;
            }
        }
//        System.out.println("id=\"" + link + ": " + verso);
//        System.out.println("MarcarNaMesmaLinha....");
//        System.out.println("Diferenca de inserir: " + linhaBase.getDiferencaDeInserir());
//        System.out.println("Linha recebida: " + linha);
//        System.out.println("Posiccao inicial: " + posicaoInicial);
//        System.out.println("Ponto de partida: " + retorno.substring(0, posicaoInicial));
//        System.out.println("Posição final: " + (posicaoInicial + (verso.length() - 1)));
//        System.out.println("Comprimento do retorno: " + (retorno.length() - 1));

        try {
            retorno.replace(posicaoInicial, posicaoInicial + (verso.length()), "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\">" + verso + "</span>");
        } catch (StringIndexOutOfBoundsException se) {
            System.out.println("Erro ao tentar substituir: " + verso);
        }

        String incremento = "<a id=\"" + link + "\"><span id=\"" + link + "\" style=\"background-color: #8CC5F4\"></span>";

        int linkRetorno = link;
        if (incrementarLink) {
            link++;

        }
//        System.out.println("Novo Valor de Link: " + link);

        linhaBase.setSubstituicao(true);
        //linhaBase.setDiferencaDeInserir(verso.length() - verso.length());//Rever isso aqui
        linhaBase.setDiferencaDeInserir(linhaBase.getDiferencaDeInserir() + incremento.length() - 1);//Rever isso aqui
        linhaBase.setLinhaComEscansao(retorno.toString());
        return linkRetorno;
    }

    private String extrairSegmentoInseridoFim(String segmento) {
        StringBuilder verso = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(segmento);
        String vetorDeTokens[] = Utilitario.preencherVetorToken(tokens);
        int posicaoInicial = 0;
        for (posicaoInicial = 0; !(vetorDeTokens[posicaoInicial].contains("</span>")); posicaoInicial++);
        int i = posicaoInicial;
        vetorDeTokens[posicaoInicial] = vetorDeTokens[posicaoInicial].replace("</span>", "");
        for (i = 0; i <= posicaoInicial; i++) {
            if (!(i == vetorDeTokens.length - 1)) {
                verso.append(vetorDeTokens[i] + " ");
            } else {
                verso.append(vetorDeTokens[i]);
            }
        }

        return verso.toString();
    }

    private String inserirNegritoNasTonicasInicio(String texto) {
        StringBuilder novoTexto = new StringBuilder();
        String segmento = extrairSegmentoInseridoFim(texto);
        // novoTexto.append("<html>");
        String segs[] = segmento.split("/");
        int aux = -1;
        for (String seg : segs) {
            aux++;
            if (seg.contains("#")) {
                if (!(seg.trim().split(" ").length > 1)) {
                    novoTexto.append("<b><font color = \"#FF0000\">");
                    novoTexto.append(seg.replace("#", ""));
                    novoTexto.append("</font></b>");
                } else {
                    novoTexto.append(inserirNegritoNasTonicasTrantoEspaco(seg));
                }
                if (!(aux == segs.length - 1)) {
                    novoTexto.append("/");
                }

            } else if (aux == segs.length - 1) {
                novoTexto.append(seg);
            } else {
                novoTexto.append(seg + "/");
            }
        }
        novoTexto = new StringBuilder(novoTexto.toString().replaceAll("§", "-"));
        return texto.replace(segmento, novoTexto.toString());
    }

    private String extrairSegmentoInserido(String segmento) {
        StringBuilder verso = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(segmento);
        String vetorDeTokens[] = Utilitario.preencherVetorToken(tokens);
        int posicaoInicial = 0;
        for (posicaoInicial = vetorDeTokens.length - 1; !(vetorDeTokens[posicaoInicial].contains("#8CC5F4\">")); posicaoInicial--);
        int i = posicaoInicial;
        vetorDeTokens[posicaoInicial] = vetorDeTokens[posicaoInicial].replace("#8CC5F4\">", "");
        for (i = posicaoInicial; i < vetorDeTokens.length; i++) {
            if (!(i == vetorDeTokens.length - 1)) {
                verso.append(vetorDeTokens[i] + " ");
            } else {
                verso.append(vetorDeTokens[i]);
            }
        }

        return verso.toString();
    }

    private String inserirNegritoNasTonicasFim(String texto) {
        StringBuilder novoTexto = new StringBuilder();
        String segmento = extrairSegmentoInserido(texto);
        // novoTexto.append("<html>");
        String segs[] = segmento.split("/");
        int aux = -1;
        for (String seg : segs) {
            aux++;
            if (seg.contains("#")) {
                if (!(seg.trim().split(" ").length > 1)) {
                    novoTexto.append("<b><font color = \"#FF0000\">");
                    novoTexto.append(seg.replace("#", ""));
                    novoTexto.append("</font></b>");
                } else {
                    novoTexto.append(inserirNegritoNasTonicasTrantoEspaco(seg));
                }
                if (!(aux == segs.length - 1)) {
                    novoTexto.append("/");
                }

            } else if (aux == segs.length - 1) {
                novoTexto.append(seg);
            } else {
                novoTexto.append(seg + "/");
            }
        }
        novoTexto = new StringBuilder(novoTexto.toString().replaceAll("§", "-"));
        return texto.replace(segmento, novoTexto.toString());
    }

    private String inserirNegritoNasTonicas(String texto) {
        StringBuilder novoTexto = new StringBuilder();
        // novoTexto.append("<html>");
        String segs[] = texto.split("/");
        int aux = -1;
        for (String seg : segs) {
            aux++;
            if (seg.contains("#")) {
                if (!(seg.trim().split(" ").length > 1)) {
                    novoTexto.append("<b><font color = \"#FF0000\">");
                    novoTexto.append(seg.replace("#", ""));
                    novoTexto.append("</font></b>");
                } else {
                    novoTexto.append(inserirNegritoNasTonicasTrantoEspaco(seg));
                }
                if (!(aux == segs.length - 1)) {
                    novoTexto.append("/");
                }

            } else if (aux == segs.length - 1) {
                novoTexto.append(seg);
            } else {
                novoTexto.append(seg + "/");
            }
        }

        return novoTexto.toString().replaceAll("§", "-");
    }

    private String inserirNegritoNasTonicasTrantoEspaco(String texto) {
        StringBuilder novoTexto = new StringBuilder();
        String segs[] = texto.split(" ");
        for (String seg : segs) {
            if (seg.contains("#")) {
                novoTexto.append("<b>");
                novoTexto.append(seg.replace("#", ""));
                novoTexto.append("</b> ");
            } else {
                novoTexto.append(seg);
            }
        }
        return novoTexto.toString();

    }

    public void imprimeLinhasEscadidadas(BufferedWriter bufferedWriterDestino) {
        try {
            for (Linha linha : linhas) {
                bufferedWriterDestino.write(linha.getLinhaComEscansao());
                bufferedWriterDestino.newLine();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void imprimeLinhasEscadidadas() {

        try {

            for (Linha linha : linhas) {
                System.out.println(linha.getLinhaComEscansao());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private static String[] preencherVetorTokenSubstituir(StringTokenizer st, int quantidade) {
        int tamanho = st.countTokens();
        ArrayList<String> aux = new ArrayList<>();

        int i = 0;
        String temp = "";
        while (st.hasMoreTokens() && (i + 1) <= quantidade) {
            aux.add(st.nextToken());
            i++;
        }
        String palavras[] = new String[aux.size()];
        for (i = 0; i < aux.size(); i++) {
            palavras[i] = aux.get(i);
        }
        return palavras;
    }

    public ArrayList<Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList<Linha> linhas) {
        this.linhas = linhas;
    }

    private int verificaTokensInseridos(StringTokenizer stringTokenizer) {
        return preencherVetorToken(stringTokenizer).length;

    }

    private StringBuilder obterCadeiaDeTroca(int qTroca, Linha linha2, int posicaoInicial) {
        String cadeiaDeBusca = linha2.getLinha().substring(posicaoInicial);
        StringBuilder retorno = new StringBuilder();
        int indice = 0, nTokens = 0;

        while (nTokens < qTroca) {
            retorno.append(cadeiaDeBusca.charAt(indice));
            if (cadeiaDeBusca.charAt(indice) == ' ') {//Essa lógica não é a mais adequada para tratar desses casos...
                nTokens++;
            }
            indice++;
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private HashMap<String, Integer> estatisticaTipos = new HashMap<String, Integer>();

    public void imprimirEstatisticasDeTipos() {
        System.out.println("Imprimindo Estatística da Página: " + this.numero);
        for (String key : estatisticaTipos.keySet()) {
            System.out.println(key + ":\t" + estatisticaTipos.get(key));
        }
    }

    public void imprimirEstatistica() {
        gerarEstatisticaPagina();
        if (estatisticaPagina.size() > 0) {
            System.out.println("Página: " + getNumero());
            System.out.println("-------------------------------------------------------------------");
            for (String key : estatisticaPagina.keySet()) {
                System.out.println("Tipo: " + key);
                System.out.println("Quantidade: " + estatisticaPagina.get(key).getQuantidade());
                System.out.println("Categorias...");
                for (String subKey : estatisticaPagina.get(key).getSubtipos().keySet()) {
                    System.out.println("- " + subKey + ": " + estatisticaPagina.get(key).getSubtipos().get(subKey));
                }
//                System.out.println("");
            }
            System.out.println("-------------------------------------------------------------------");
        }
    }

    boolean existeEstatisticaAnaliticaDaPagina = false;

    private void gerarEstatisticaPagina() {
        if (!existeEstatisticaAnaliticaDaPagina) {
            int temp;
            for (Frase f : frases) {
                if (f.getVerso() != null) {
                    temp = 0;
                    //  numeroDeVersos++;
                    if (estatisticaPagina.containsKey(f.getVerso().getClassificacao())) {
                        temp = estatisticaPagina.get(f.getVerso().getClassificacao()).getQuantidade();
                        temp++;
                        estatisticaPagina.get(f.getVerso().getClassificacao()).setQuantidade(temp);
//                    if (f.getVerso().getSubCategoria() == null) {
////                        System.out.println("Estou aqui modificando a categoria....");
//                        f.getVerso().setSubCategoria("Desconhecida");
//                    }
                        if (estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().containsKey(f.getVerso().getSubCategoria())) {
                            temp = estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().get(f.getVerso().getSubCategoria());
                            temp++;
                            //   estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().get(f.getVerso().getSubCategoria());
                            estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().put(f.getVerso().getSubCategoria(), temp);
                        } else {
                            estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().put(f.getVerso().getSubCategoria(), 1);
                        }
                    } else {
                        EstatisticaTipo et = new EstatisticaTipo();
                        et.setQuantidade(1);
                        et.setTipo(f.getVerso().getClassificacao());
                        // et.setQuantidade(temp);
                        estatisticaPagina.put(f.getVerso().getClassificacao(), et);
//                    if (f.getVerso().getSubCategoria() == null) {
//                        System.out.println("Estou aqui modificando a categoria....");
//                        f.getVerso().setSubCategoria("Desconhecida");
//                    }
//                    System.out.println("Adicionando subcategoria ao mapa: " + f.getVerso().getSubCategoria());
                        estatisticaPagina.get(f.getVerso().getClassificacao()).getSubtipos().put(f.getVerso().getSubCategoria(), 1);
                    }
                }

            }
            existeEstatisticaAnaliticaDaPagina = true;
        }
    }

    boolean existeEstatiscaSimples = false;

    public int getNumeroDeVersos() {
        // System.out.println("Pagina: " + this.numero);
        if (!existeEstatiscaSimples) {
            int temp;
            for (Frase f : frases) {
                if (f.getVerso() != null) {
                    temp = 0;
                    numeroDeVersos++;
                    if (estatisticaTipos.containsKey(f.getVerso().getClassificacao())) {
                        temp = estatisticaTipos.get(f.getVerso().getClassificacao());
                        estatisticaTipos.replace(f.getVerso().getClassificacao(), temp, ++temp);
                    } else {
                        temp = 1;
                        estatisticaTipos.put(f.getVerso().getClassificacao(), temp);
                    }
                }

            }
            imprimirEstatisticasDeTipos();
            existeEstatiscaSimples = true;
        }
        return numeroDeVersos;
    }

    public HashMap<String, Integer> getEstatisticaTipos() {
        return estatisticaTipos;
    }

    public HashMap<String, EstatisticaTipo> getEstatisticaPagina() {
        return estatisticaPagina;
    }

    public void setEstatisticaPagina(HashMap<String, EstatisticaTipo> estatisticaPagina) {
        this.estatisticaPagina = estatisticaPagina;
    }
}
