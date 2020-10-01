/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import mives.util.ConfiguracaoEncansao;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo
 */
public class EscansaoCustomizada2 {

    HashMap<Integer, String> classificacao = new HashMap<>();
    // MapaConfiguracao mapaConfiguracao;
    HashSet<String> vogais = new HashSet<>();
    HashSet<String> vogaisAcentuadas = new HashSet<>();
    String verso;
    static boolean saidaTeste = true;
    boolean classificarVerso = false;
    int tipoDeElisao = 0;
    boolean considerarOH = false;
    boolean considerarAtona = true;
    boolean considerarTonica = true;
    boolean utilizarCustomizacao = false;
    private boolean versoLivre = false;
    private boolean versoContado = false;
    Separador separador;

    private HashSet<String> ditongoCresecente = new HashSet<>();
//    boolean utilizarConfiguracao = false;

    public EscansaoCustomizada2() {
//        mapaConfiguracao = MapaConfiguracao.getInstacia();
        separador = new Separador();
        carregarVogais();
        carregarClassificacaoDoVerso();
        carregarVogaisAcentuadas();
        carragarMapaConfiguracao();
        carregarDitongoCrescente();

    }

    public EscansaoCustomizada2(boolean verificarSinerese, boolean verificarDierese, boolean considerarElisao) {
        this();
    }

    public boolean isPalavra(String token) {

        if (isVogalAcentuada(token.charAt(0))) {
            return true;
        }
        return !(token.length() == 1 && token.matches(".*\\W.*"));

    }

    public String extraisSimbolo(String token) {
        return "";
    }

    private boolean existeElisao(String palavraAnterior, String palavraAtual) {

        if (palavraAtual.charAt(0) == '#') {
            palavraAtual = palavraAtual.replace("#", "");
        }

        if (isVogal(palavraAnterior.replace(",", "").replace(".", "").charAt(palavraAnterior.replace(",", "").replace(".", "").length() - 1)) // && !(arrayVerso[i].contains("#"))) - Alteração realizada em 06/07 referente a utilização da vírgula - removida para faciliar o caso de elisão.
                //E a próxima começar com vogal
                && (isVogal(palavraAtual.replace("#", "").charAt(0)) //a próxima comecar por uma vogal - A VOGAL PRECISA SER ÁTONA
                //Ou começar por com H - houver elisão.
                || (buscarLetra(palavraAtual.replace("#", "").charAt(0), new char[]{'h', 'H'}))) //                        && (!arrayVerso[i].contains("#")
                ) {

            //Se terminar com átona
            if (!isOxitona(palavraAnterior)) {
                //Se a próxima começar com H e se for para considerar o H...
                if ((buscarLetra(palavraAtual.replace("#", "").charAt(0), new char[]{'h', 'H'}) && considerarOH)) {
                    return true;
//                } else if (palavraAtual.charAt(palavraAtual.length() - 1) != '#' && considerarAtona) {// Se a próxima começar com vogal átona
                } else if (palavraAtual.charAt(0) != '#' && considerarAtona) {// Se a próxima começar com vogal átona -----16/05/2107

                    if (verificarElisao(palavraAnterior, palavraAtual)) {

                        return true;
                    }
//                } else if (palavraAtual.charAt(palavraAtual.length() - 1) == '#' && considerarTonica) {
                } else if (palavraAtual.charAt(0) == '#' && false) {//Isso nunca é executado.

                    if (verificarElisao(palavraAnterior, palavraAtual)) {
                        return true;

                    }
                }
            }

        }

        return false;
    }

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

    @Deprecated
    public Verso contarSilabasPoeticasFinal(String verso, int maxSilabas, boolean validarContagem) {
        StringTokenizer tokensPalavra = new StringTokenizer(verso.toString());
        if (verso.trim().length() == 0) {
            return null;
        }
        this.verso = verso;
        int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
        StringTokenizer st = new StringTokenizer(this.verso);
        //StringTokenizer st = new StringTokenizer(this.verso);
        int b = 0;//Número de palavras do meu possível verso.

        String temp = "";
        StringBuilder novoVerso = new StringBuilder();
        boolean continuarBusca = true;
        int numeroDeTokens = tokensPalavra.countTokens() - 1;
        String[] vetorPalavra = Utilitario.preencherVetorToken(new StringTokenizer(verso));
        Palavra arrayVerso[] = new Palavra[vetorPalavra.length];//Vetor com as palavras do possível verso.
//        int palavraAtual = st.countTokens() - 1;
        int r = vetorPalavra.length - 1;
        Palavra palavraTemp;
        numeroDeTokens = vetorPalavra.length - 1;
        while (numeroDeTokens >= 0 && continuarBusca) {
            temp = vetorPalavra[numeroDeTokens];
            temp = separador.separarSilabasTexto(temp).toString().trim();
            //   System.out.println("Palavra separada: " + temp);

            if (b == 0) {
                palavraTemp = contarSilabasPalavra(temp, 0, true);
                numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                novoVerso.append(palavraTemp.getPalavraSeparada());
                arrayVerso[r] = palavraTemp;
                r--;
                b--;
            } else {
                palavraTemp = contarSilabasPalavra(temp, 0, false);
                numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                arrayVerso[r] = palavraTemp;
                boolean houveElisao = existeElisao(arrayVerso[r].getPalavraSeparada(), arrayVerso[r + 1].getPalavraSeparada());
//                System.out.println("Olha Olha Olha Olha Olha Olha Olha Olha Olha : " + houveElisao);
//                System.out.println("Palavra anterior: " + arrayVerso[palavraAtual].getPalavraSeparada());
//                System.out.println("Palavra próxima: " + arrayVerso[palavraAtual + 1].getPalavraSeparada());
                r--;
                if (houveElisao) {
                    numSilabasPoeticas--;
                    novoVerso.insert(0, palavraTemp.getPalavraSeparada() + " ");
                } else {
                    novoVerso.insert(0, palavraTemp.getPalavraSeparada() + "/ ");
                }
            }
            numeroDeTokens--;
            if (numSilabasPoeticas >= maxSilabas) {
                continuarBusca = false;
            }
        }
        Verso versoEncontrado = new Verso();
        versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());

        return versoEncontrado;
    }

    public Verso contarSilabasPoeticasFinalNew(String frase, int minimo, int maximo, boolean validarContagem) {

        try {
            if (frase.trim().length() == 0) {
                return null;
            }

            this.verso = frase;
            int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
            int numPalavrasVerso = 0;//Número de palavras do meu possível frase.
            Verso versoEncontrado = new Verso();
            String temp = "";
            StringBuilder novoVerso = new StringBuilder();
            boolean continuarBusca = true;
            int numeroDeTokensDaFrase = new StringTokenizer(frase.toString()).countTokens() - 1;
            String[] vetorPalavrasDaFrase = Utilitario.preencherVetorToken(new StringTokenizer(frase));
            Palavra arrayVerso[] = new Palavra[vetorPalavrasDaFrase.length];//Vetor com as palavras do possível frase.
            int palavraAtual = vetorPalavrasDaFrase.length - 1;
            Palavra palavraTemp;
            numeroDeTokensDaFrase = vetorPalavrasDaFrase.length - 1;
            while (numeroDeTokensDaFrase >= 0 && continuarBusca) {
                temp = vetorPalavrasDaFrase[numeroDeTokensDaFrase];//Pegue a última palavra
                //    System.out.println("ENVIANDO PARA O SEPARADOR 1: " + temp);
                temp = separador.separarSilabasTextoV1(temp).toString().trim();//separe a sílaba
                //    System.out.println("RECEBENDO DO SEPARADOR: " + temp);
                if (numPalavrasVerso == 0) {
                    palavraTemp = contarSilabasPalavraNew(temp, 0, true);//Considerar contagem até a tônica da palavra
                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                    novoVerso.append(palavraTemp.getPalavraSeparada());

                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    arrayVerso[palavraAtual] = palavraTemp;
                    palavraAtual--;
                    numPalavrasVerso--;
                } else {
                    palavraTemp = contarSilabasPalavraNew(temp, 0, false);
                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());
                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                    arrayVerso[palavraAtual] = palavraTemp;
//                    System.out.println("VERIFICANDO SE EXISTE ELISÃO NAS PALAVRAS:");
//                    System.out.println(arrayVerso[palavraAtual].getPalavraSeparada());
//                    System.out.println(arrayVerso[palavraAtual + 1].getPalavraSeparada());
                    boolean houveElisao = existeElisao(arrayVerso[palavraAtual].getPalavraSeparada(), arrayVerso[palavraAtual + 1].getPalavraSeparada());

                    if (houveElisao) {
                        try {
                            //Será necessário inverter essas ordens internamente no verso
                            //Para os onde será necessário realizar reversão de elisões, diéreses e sinéreses
                            versoEncontrado.getRegrasDeElisoesAplicadas().add(
                                    arrayVerso[palavraAtual].getPalavraSeparada().replace(",", "").replace(".", "").substring(arrayVerso[palavraAtual].getPalavraSeparada().replace(",", "").replace(".", "").length() - 1) + arrayVerso[palavraAtual + 1].getPalavraSeparada().replace("#", "").charAt(0));
                            numSilabasPoeticas--;
//                            System.out.println("--------------------------------------------------------------: HOUVE ELISÃO.................");
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[palavraAtual + 1]);
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[palavraAtual]);
//                            System.out.println("ELISÕES ARMAZENADAS: " + versoEncontrado.getNumeroDeElisoes() + "........................................................");
                            novoVerso.insert(0, palavraTemp.getPalavraSeparada() + " ");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        novoVerso.insert(0, palavraTemp.getPalavraSeparada() + "/ ");
                    }
                    palavraAtual--;
                }
                numeroDeTokensDaFrase--;

//            if (numSilabasPoeticas >= maximo) {
//                continuarBusca = false;
//            }
                continuarBusca = true;
                /**
                 * Verificando se a métrica buscada já foi atingida. Aqui
                 * trabalho na questão da última palavra do verso. Se a métrica
                 * buscada já tiver sido atinginda, entre um mínimo e um máximo
                 * e Se está palavra contiver uma sílaba tônica, então eu
                 * decremento o total do número de sílbas desta palavra e, em
                 * seguida, realizo a contagem desta mesma palavra considerando
                 * até a sílaba tônica.
                 */
                if ((numSilabasPoeticas >= minimo && numSilabasPoeticas <= maximo)
                        || (numSilabasPoeticas >= maximo)) { //É necessário criar um mecanismo para dizer ao método quando isso deve ser feito
//                    System.out.println("Verificando métrica de: " + novoVerso.toString());
//                    System.out.println("Validando contagem: " + novoVerso.toString());
//                    System.out.println("Quantidade de sílabas poéticas: " + numSilabasPoeticas);

                    //Se já ultrapassou pare a busca, vefique se tem histórico, se tiver reverta, 
                    //Caso contrário deixe como está
                    if (numSilabasPoeticas > maximo) {
                        continuarBusca = false;
                        if (versoEncontrado.isHistoricoDeContagem()) {
//                            System.out.println("Revertendo verso");
                            novoVerso = new StringBuilder(versoEncontrado.getVersoAnterior());
                            versoEncontrado.restaurarHistorico();
                            numSilabasPoeticas = versoEncontrado.getQuantidadeDeSilabasAnterior();
                            palavraAtual++;//Foi trabalhada uma palavra que deve ser ignorada no momento de 
                            //informar ao versoEncontrado quais Palavras ele é formado.
                        }
                    } else {
                        //Preciso de um mecanismo que verifique se vale a pena pegar a próxima palavra
                        if (numSilabasPoeticas >= minimo && numSilabasPoeticas <= maximo) {//Implementado em 05/01/2017
                            //Se estiver entre o método guarda o histórico e continua
                            versoEncontrado.setHistoricoDeContagem(true);
                            versoEncontrado.guardarHistorico();
                            versoEncontrado.setVersoAnterior(novoVerso.toString());
                            versoEncontrado.setQuantidadeDeSilabasAnterior(numSilabasPoeticas);
                            if (numSilabasPoeticas < maximo && palavraAtual < numeroDeTokensDaFrase) {
                                continuarBusca = true;
//                                System.out.println("Continuando busca 1: " + novoVerso.toString());
                            }
                        }
                    }

                }
            }
            //Necessário apenas aqui por conta das coisas serem feitas de maneira inversa
            versoEncontrado.reordenarElisoes();
//            System.out.println("Palavra atual: " + arrayVerso[palavraAtual + 1].getPalavra());
//            System.out.println("Indice da palavra atual: " + (palavraAtual + 1));
//            System.out.println("Verso atual: " + novoVerso.toString());
//            System.out.println("Frase processada: " + frase);
            for (int i = palavraAtual + 1; arrayVerso.length > i; i++) {
                versoEncontrado.getPalavrasVerso().add(arrayVerso[i]);
            }

            //O verso encontrado possui o número de sílabas acima do número da métrica buscada?
            //Em caso positivo, verificar se a quantidades de diéreses aplicadas se desfeitas pode resultar em estruturas dentro 
            //da métrica que se deseja alcançar.
            //Em caso positivo descafazer a diérese e decrementar o no número de sílabas poéticas
            if (numSilabasPoeticas > maximo && versoEncontrado.getNumeroDeDiereses() > 0 && numSilabasPoeticas - versoEncontrado.getNumeroDeDiereses() <= maximo) {
//                System.out.println("DESFAZENDO CASO 1");
                versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
                versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
                versoEncontrado.setStatusDaEscansao("Modificado");
//                return ajustarDierese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
                versoEncontrado = ajustarDierese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
            }

            //O verso encontrado possui o número de sílabas abaixo do número da métrica buscada?
            //Desfazer as sinéreses pode resultar na métrica que sendo buscada? – Sim, então, desfazer sucessivamente.
            if (numSilabasPoeticas < minimo
                    && versoEncontrado.getNumeroDeSinereses() > 0
                    && numSilabasPoeticas + versoEncontrado.getNumeroDeSinereses() >= maximo) {
//                System.out.println("DESFAZENDO CASO 2");
                versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
                versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
                versoEncontrado = ajustarSinerese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
//                return ajustarSinerese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
            }

            //Desfazer as elisões pode resultar na métrica que está sendo buscada? 
            //– Sim, desfazer sucessivamente até que a métrica seja alcançada. 
            if (numSilabasPoeticas < minimo
                    && versoEncontrado.getNumeroDeElisoes() > 0
                    && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes()) >= minimo) {
//                System.out.println("DESFAZENDO CASO 3");
                versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
                versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
                versoEncontrado = ajustarElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
//                return ajustarElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
            }

            //Desfazer as sinéreses e elisões pode resultar na métrica que está sendo buscada? 
            //– Sim, desfazer os dois sucessivamente até que a métrica seja alcançada. 
            //O software irá começar pelas sinéreses.
            if (numSilabasPoeticas < minimo
                    && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes() + versoEncontrado.getNumeroDeSinereses()) >= minimo) {
//                System.out.println("DESFAZENDO CASO 4");
                versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
                versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
//                return ajustarSinereseElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
                versoEncontrado = ajustarSinereseElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minimo);
            }

            if (classificacao.containsKey(numSilabasPoeticas)) {
                versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
            } else {
                versoEncontrado.setClassificacao("MaiorQ13");
            }
//            System.out.println("Retornando :" + novoVerso.toString() + " - " + numSilabasPoeticas);
//            System.out.println("Verso encontrado númerod e Elisões: " + versoEncontrado.getNumeroDeElisoes());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
            versoEncontrado.setVersoEscandido(novoVerso.toString());
            //Se o número de tokens igual a -1 retorne o verso encontrado. Isso significa que toda a frase já foi explorada.
            if (numeroDeTokensDaFrase < 0) {
                return versoEncontrado;
            } else {
                //Se o número de tokens for maior ou igual a zero verifique se na palavra anterior exista algum símbolo de pontuação;
                //Se existir retorne o verso. Caso contrário retorne null.
                //Aqui ainda existem palavras que não exploradas ainda.
//                System.out.println("--------------------------------------------------");
//                System.out.println("Verificando existência:");
//                System.out.println("Verso: " + versoEncontrado.getVersoEscandido());
//                System.out.println("Classificação: " + versoEncontrado.getClassificacao());
//                System.out.println("Palavra anterior: " + vetorPalavrasDaFrase[numeroDeTokensDaFrase + 1]);
//                System.out.println("--------------------------------------------------");

                if (numeroDeTokensDaFrase >= 0 && verifcarSimboloPontuacao(vetorPalavrasDaFrase[numeroDeTokensDaFrase + 1])) {
//                    System.out.println("???????????????????????????????????????");
//                    System.out.println("Retornando: " + versoEncontrado.getVersoEscandido());
//                    System.out.println("Palavra anterior: " + (vetorPalavrasDaFrase[numeroDeTokensDaFrase + 1]));
//                    System.out.println("Frase: " + frase);
//                    System.out.println("???????????????????????????????????????");

                    return versoEncontrado;
                } else {
//                    System.out.println("****************************************\nDescartando o verso: " + versoEncontrado.getVersoEscandido());
//                    System.out.println("Palavra anterior: " + (vetorPalavrasDaFrase[numeroDeTokensDaFrase + 1]));
//                    System.out.println("Frase: " + frase + "\n****************************************");
                    return null;
                }
            }

            //  return versoEncontrado;
        } catch (Exception ex) {
            System.out.println("Olha o erro aqui: ");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica se a palavra é seguinda por um sinal de pontuação considerando a
     * forma correta de posicionamento destes sinais no texto. Próximo da
     * palavra.
     *
     * ESTÁ COMO PÚBLICO APENAS PARA AGILIZAR O PROCESSO DE DESENSOLVIMENTO. O
     * CORRETO SERIA DEIXÁ-LO COMO PRIVADO E ALTERAR O MÉTODO QUE FAZ A ESCANSÃO
     * É BOM VER ISSO NO FUTUTO.
     *
     * @param palavra - Palavra que será analisada
     * @return verdadeiro caso a palavra venha acompnhada de ".", ",", ";", ":",
     * "....", "!"
     */
    public boolean verifcarSimboloPontuacao(String palavra) {//Melhorar isso o processo aqui é muito lento.
        return palavra.contains(".") || palavra.contains(",") || palavra.contains(";") || palavra.contains(":") || palavra.contains("...") || palavra.contains("!") || palavra.contains("—");
    }

    @Deprecated
    public Verso contarSilabasPoeticas(String verso, int maxSilabas, boolean validarContagem) {

        if (verso.trim().length() == 0) {
            return null;
        }
        this.verso = verso;
        int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
        StringTokenizer st = new StringTokenizer(this.verso);
        int b = st.countTokens() - 1;//Número de palavras do meu possível verso.
        Palavra arrayVerso[] = new Palavra[st.countTokens()];//Vetor com as palavras do possível verso.
        int r = -1;
        String temp = "";
        StringBuilder novoVerso = new StringBuilder();
        boolean continuarBusca = false;
        int houveUmSalto = 0;

        do {
            temp = st.nextToken();
            temp = separador.separarSilabasTexto(temp).toString().trim();
            r++;
            if (isPalavra(temp)) {
                if (r < b || b == 0) {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, false);
                    if (r == 0 || b == 0) {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        novoVerso.append(palavraTemp.getPalavraSeparada());
                        arrayVerso[r] = palavraTemp;
                    } else {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        arrayVerso[r] = palavraTemp;
                        boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
//                        System.out.println("1 - Olha Olha Olha Olha Olha Olha Olha Olha Olha : " + houveElisao);
//                        System.out.println("Palavra anterior: " + arrayVerso[palavraAtual].getPalavraSeparada());
//                        System.out.println("Palavra próxima: " + arrayVerso[palavraAtual + 1].getPalavraSeparada());
                        if (houveElisao) {
                            numSilabasPoeticas--;
                            novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                        } else {
                            novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                        }
                    }
                } else {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, true);
                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                    arrayVerso[r] = palavraTemp;
                    boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                    if (houveElisao) {
                        numSilabasPoeticas--;
                        novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                    } else {
                        novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                    }
                }
            } else {
                novoVerso.append(" " + temp);
                r--;
            }
            continuarBusca = true;
            if (numSilabasPoeticas >= maxSilabas && temp.contains("#")) { //É necessário criar um mecanismo para dizer ao método quando isso deve ser feito
                if (validarContagem) {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, false);
                    int quantidadeTotal = palavraTemp.getQtdSilabasPoeticas();
                    numSilabasPoeticas -= quantidadeTotal;
                    palavraTemp = contarSilabasPalavra(temp, 0, true);
                    int auxNumSilabas = palavraTemp.getQtdSilabasPoeticas();
                    if (auxNumSilabas + numSilabasPoeticas >= maxSilabas) {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        continuarBusca = false;
                    } else {
                        numSilabasPoeticas += quantidadeTotal;
                    }
                } else {
                    continuarBusca = false;
                }

            }
        } while (st.hasMoreTokens() && continuarBusca);

        Verso versoEncontrado = new Verso();
        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
//        System.out.println("Número de Sílabas Poéticas: " + numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        return versoEncontrado;
    }

    //Esse é o método que está sendo chamado...15/09/2016
    public Verso contarSilabasPoeticas(String verso, int maxSilabas, int minSilabas, boolean validarContagem) {
        if (verso.trim().length() == 0) {
            return null;
        }
        this.verso = verso;
        int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
        StringTokenizer st = new StringTokenizer(this.verso);
        int b = st.countTokens() - 1;//Número de palavras do meu possível verso.
        Palavra arrayVerso[] = new Palavra[st.countTokens()];//Vetor com as palavras do possível verso.
        int r = -1;
        String temp = "";
        StringBuilder novoVerso = new StringBuilder();
        boolean continuarBusca = false;
        int houveUmSalto = 0;

        do {
            temp = st.nextToken();
//            System.out.println("ENVIANDO PARA O SEPARADOR 2: " + temp);
            temp = separador.separarSilabasTextoV1(temp).toString().trim();
            r++;
            if (isPalavra(temp)) {
                if (r < b || b == 0) {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, false);
                    if (r == 0 || b == 0) {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        novoVerso.append(palavraTemp.getPalavraSeparada());
                        arrayVerso[r] = palavraTemp;
                    } else {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        arrayVerso[r] = palavraTemp;
                        boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                        if (houveElisao) {
                            numSilabasPoeticas--;
                            novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                        } else {
                            novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                        }
                    }
                } else {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, true);
                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                    arrayVerso[r] = palavraTemp;
                    boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                    if (houveElisao) {
                        numSilabasPoeticas--;
                        novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                    } else {
                        novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                    }
                }
            } else {
                novoVerso.append(" " + temp);
                r--;
            }
            continuarBusca = true;
            if ((numSilabasPoeticas >= minSilabas && numSilabasPoeticas <= maxSilabas) && temp.contains("#")) { //É necessário criar um mecanismo para dizer ao método quando isso deve ser feito
                if (validarContagem) {
                    Palavra palavraTemp = contarSilabasPalavra(temp, 0, false);
                    int quantidadeTotal = palavraTemp.getQtdSilabasPoeticas();
                    numSilabasPoeticas -= quantidadeTotal;
                    palavraTemp = contarSilabasPalavra(temp, 0, true);
                    int auxNumSilabas = palavraTemp.getQtdSilabasPoeticas();
                    if (auxNumSilabas + numSilabasPoeticas >= maxSilabas) {
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        continuarBusca = false;
                    } else {
                        numSilabasPoeticas += quantidadeTotal;
                    }
                } else {
                    if (numSilabasPoeticas < maxSilabas && st.hasMoreTokens()) {
                        continuarBusca = true;
                    } else {
                        continuarBusca = false;
                    }
                }

            }
        } while (st.hasMoreTokens() && continuarBusca);

        Verso versoEncontrado = new Verso();
        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        return versoEncontrado;
    }

    //Essa variável deve ser excluída após o processo de tratamento para reverter as elisões
    static int numeroDeVersosDescartado = 0;

    /**
     * Realiza a contagem do número de sílabas poéticas baseada nos parâmetros
     * de escansão informados no painel de configuração.
     *
     * @param verso - Cadeia de caracteres em que o algoritmo deve agir;
     * @param maxSilabas - O número máximo de sílabas que deve ser considerado;
     * @param minSilabas - O número mínimo de sílabas que deve ser considerado;
     * @param validarContagem - Se falso irá considerar apenas os segmentos
     * frásicos compostos por frases completas. Caso contrário, irá considerar
     * segmentos frásico de início de cadeia, aqui são realizadas a contagem dos
     * segmentos frásicos de início de frase.
     * @return Verso e suas caracterísiticas;
     */
    public Verso contarSilabasPoeticasNew(String verso, int maxSilabas, int minSilabas, boolean validarContagem) {
        if (verso.trim().length() == 0) {
            return null;
        }
        this.verso = verso;
        int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
        StringTokenizer st = new StringTokenizer(this.verso);
        int b = st.countTokens() - 1;//Número de palavras do meu possível verso.
        Palavra arrayVerso[] = new Palavra[st.countTokens()];//Vetor com as palavras do possível verso.
        int r = -1;
        String temp = "";
        StringBuilder novoVerso = new StringBuilder();
        boolean continuarBusca = false;
        int houveUmSalto = 0;
        Verso versoEncontrado = new Verso();//Objeto que será retornado ao final do processamento.
        do {
            temp = st.nextToken();
//            System.out.println("Enviando para separador.separarSilabasTextoV1(temp).toString().trim(): " + temp);
            temp = separador.separarSilabasTextoV1(temp).toString().trim();
//            System.out.println("Retorno do separarSilabasTextoV1: " + temp);
            r++;//Conta quantas palavras são trabalhadas
            if (isPalavra(temp)) {//Se for uma palavra
                if (r < b || b == 0) {
                    Palavra palavraTemp = contarSilabasPalavraNew(temp, 0, false);
                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    if (r == 0 || b == 0) {//Se for a primeira palavra do verso
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        novoVerso.append(palavraTemp.getPalavraSeparada());
                        versoEncontrado.getPalavrasVerso().add(palavraTemp);
                        arrayVerso[r] = palavraTemp;
                    } else {
                        versoEncontrado.getPalavrasVerso().add(palavraTemp);
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        arrayVerso[r] = palavraTemp;
                        boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                        if (houveElisao) {
                            try {
                                versoEncontrado.getRegrasDeElisoesAplicadas().add(
                                        arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").substring(arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").length() - 1) + arrayVerso[r].getPalavraSeparada().replace("#", "").charAt(0));
                                numSilabasPoeticas--;
                                // As palavras que passaram pelo processo de “junção” são guardadas em pares. 
                                // É bom lembrar que por ser um ArrayList são guardadas no final.
                                versoEncontrado.getPalavrasComElisao().add(arrayVerso[r - 1]);
                                versoEncontrado.getPalavrasComElisao().add(arrayVerso[r]);

                                novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                        }
                    }
                } else { //a partir da segunda palavra do verso
                    Palavra palavraTemp = contarSilabasPalavraNew(temp, 0, true);
                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    versoEncontrado.getPalavrasVerso().add(palavraTemp);
                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                    arrayVerso[r] = palavraTemp;
                    boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                    if (houveElisao) {
                        try {
                            numSilabasPoeticas--;
                            //Melhorar código, essa linha já existe no método "verificaElisao"
                            versoEncontrado.getRegrasDeElisoesAplicadas().add(
                                    arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").substring(arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").length() - 1) + arrayVerso[r].getPalavraSeparada().replace("#", "").charAt(0));
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[r - 1]);
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[r]);
                            novoVerso.append(" " + palavraTemp.getPalavraSeparada());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        novoVerso.append("/ " + palavraTemp.getPalavraSeparada());
                    }
                }
            } else {
                novoVerso.append(" " + temp);
                r--;//Se for uma palavra decrementa o "R"
            }
            continuarBusca = true;
            /**
             * Verificando se a métrica buscada já foi atingida. Aqui trabalho
             * na questão da última palavra do verso. Se a métrica buscada já
             * tiver sido atinginda, entre um mínimo e um máximo e Se está
             * palavra contiver uma sílaba tônica, então eu decremento o total
             * do número de sílbas desta palavra e, em seguida, realizo a
             * contagem desta mesma palavra considerando até a sílaba tônica.
             */
            if (((numSilabasPoeticas >= minSilabas && numSilabasPoeticas <= maxSilabas) && temp.contains("#"))
                    || (numSilabasPoeticas >= maxSilabas && temp.contains("#"))) { //É necessário criar um mecanismo para dizer ao método quando isso deve ser feito
                if (validarContagem) {
                    Palavra palavraTemp = versoEncontrado.getPalavrasVerso().get(versoEncontrado.getPalavrasVerso().size() - 1);//Realizo a contagem das sílabas mais uma vez. - ISSO PODE SER MELHORADO.
                    int quantidadeTotal = palavraTemp.getQtdSilabasPoeticas();
                    numSilabasPoeticas -= quantidadeTotal;
                    palavraTemp = contarSilabasPalavraNew(temp, 0, true);
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    int auxNumSilabas = palavraTemp.getQtdSilabasPoeticas();
                    //Se já ultrapassou pare a busca, vefique se tem histórico, se tiver reverta, 
                    //Caso contrário deixe como está
                    if (auxNumSilabas + numSilabasPoeticas >= maxSilabas) {

                        continuarBusca = false;
                        if (versoEncontrado.isHistoricoDeContagem()) {
//                            System.out.println("Revertendo verso");
                            novoVerso = new StringBuilder(versoEncontrado.getVersoAnterior());
                            versoEncontrado.restaurarHistorico();
                            numSilabasPoeticas = versoEncontrado.getQuantidadeDeSilabasAnterior();
                        } else {
                            numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();
                        }
                    } else {
                        //Preciso de um mecanismo que verifique se vale a pena pegar a próxima palavra
                        if (auxNumSilabas + numSilabasPoeticas >= minSilabas && auxNumSilabas + numSilabasPoeticas <= maxSilabas) {//Implementado em 05/01/2017
                            //Se estiver entre o método guarda o histórico e continua
                            versoEncontrado.setHistoricoDeContagem(true);
                            versoEncontrado.guardarHistorico();
                            versoEncontrado.setVersoAnterior(novoVerso.toString());
                            versoEncontrado.setQuantidadeDeSilabasAnterior(auxNumSilabas + numSilabasPoeticas);
                            numSilabasPoeticas += quantidadeTotal;
                        } else {//Se não estiver entre o método continua
                            numSilabasPoeticas += quantidadeTotal;
                        }
                        // numSilabasPoeticas += quantidadeTotal;//Comentado em: 05/01/2017
                    }
                } else {
                    if (numSilabasPoeticas < maxSilabas && st.hasMoreTokens()) {
                        continuarBusca = true;
                    } else {
                        continuarBusca = false;
                    }
                }

            }
        } while (st.hasMoreTokens() && continuarBusca);
        //O verso encontrado possui o número de sílabas acima do número da métrica buscada?
        //Em caso positivo, verificar se a quantidades de diéreses aplicadas se desfeitas pode resultar em estruturas dentro 
        //da métrica que se deseja alcançar.
        //Em caso positivo desfazer a diérese e decrementar o no número de sílabas poéticas
        if (versoEncontrado.getNumeroDeDiereses() > 0 && numSilabasPoeticas - versoEncontrado.getNumeroDeDiereses() <= maxSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            versoEncontrado.setStatusDaEscansao("Modificado");
            return ajustarDierese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), maxSilabas);
        }

        //O verso encontrado possui o número de sílabas abaixo do número da métrica buscada?
        //Desfazer as sinéreses pode resultar na métrica que sendo buscada? – Sim, então, desfazer sucessivamente.
        if (numSilabasPoeticas < minSilabas
                && versoEncontrado.getNumeroDeSinereses() > 0
                && numSilabasPoeticas + versoEncontrado.getNumeroDeSinereses() >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            return ajustarSinerese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        //Desfazer as elisões pode resultar na métrica que está sendo buscada? 
        //– Sim, desfazer sucessivamente até que a métrica seja alcançada. 
        if (numSilabasPoeticas < minSilabas
                && versoEncontrado.getNumeroDeElisoes() > 0
                && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes()) >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            return ajustarElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        //Desfazer as sinéreses e elisões pode resultar na métrica que está sendo buscada? 
        //– Sim, desfazer os dois sucessivamente até que a métrica seja alcançada. 
        //O software irá começar pelas sinéreses.
        if (numSilabasPoeticas < minSilabas
                && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes() + versoEncontrado.getNumeroDeSinereses()) >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            return ajustarSinereseElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
        versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
        versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        return versoEncontrado;
    }

    /**
     * Realiza a contagem do número de sílabas poéticas baseada nos parâmetros
     * de escansão informados no painel de configuração.
     *
     * @param verso - Cadeia de caracteres em que o algoritmo deve agir;
     * @param maxSilabas - O número máximo de sílabas que deve ser considerado;
     * @param minSilabas - O número mínimo de sílabas que deve ser considerado;
     * @param validarContagem - Se falso irá considerar apenas os segmentos
     * frásicos compostos por frases completas. Caso contrário, irá considerar
     * segmentos frásico de início de cadeia, aqui são realizadas a contagem dos
     * segmentos frásicos de início de frase.
     * @return Verso e suas caracterísiticas;
     */
    public Verso contarSilabasPoeticasCompleta(String verso, int maxSilabas, int minSilabas, boolean validarContagem) {//Precisa melhorar

        if (verso.trim().length() == 0) {
            return null;
        }
        this.verso = verso;
        int numSilabasPoeticas = 0;//Numero de silabas poeticas contadas
        StringTokenizer st = new StringTokenizer(this.verso);
        int b = st.countTokens() - 1;//Número de palavras do meu possível verso.
        Palavra arrayVerso[] = new Palavra[st.countTokens()];//Vetor com as palavras do possível verso.
        int r = -1;
        String temp = "";
        StringBuilder novoVerso = new StringBuilder();
        boolean continuarBusca = false;
        int houveUmSalto = 0;
        Verso versoEncontrado = new Verso();//Objeto que será retornado ao final do processamento.
        do {
            temp = st.nextToken();
//            System.out.println("ENVIANDO PARA O SEPARADOR 4: " + temp);
            temp = separador.separarSilabasTextoV1(temp).toString().trim();
            r++;//Conta quantas palavras são trabalhadas
            if (isPalavra(temp)) {//Se for uma palavra
                if (r < b || b == 0) {
                    Palavra palavraTemp = contarSilabasPalavraNew(temp, 0, false);
                    palavraTemp.setPalavraOriginalSeparada(temp);
                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    if (r == 0 || b == 0) {//Se for a primeira palavra do verso
                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();

                        novoVerso.append(palavraTemp.getPalavraSeparada());

                        versoEncontrado.getPalavrasVerso().add(palavraTemp);
                        arrayVerso[r] = palavraTemp;
                    } else {
                        versoEncontrado.getPalavrasVerso().add(palavraTemp);

                        numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();

                        arrayVerso[r] = palavraTemp;
                        boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());

                        if (houveElisao) {
                            try {
                                versoEncontrado.getRegrasDeElisoesAplicadas().add(
                                        arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").substring(arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").length() - 1) + arrayVerso[r].getPalavraSeparada().replace("#", "").charAt(0));
                                numSilabasPoeticas--;

                                // As palavras que passaram pelo processo de “junção” são guardadas em pares. 
                                // É bom lembrar que por ser um ArrayList são guardadas no final.
                                versoEncontrado.getPalavrasComElisao().add(arrayVerso[r - 1]);
                                versoEncontrado.getPalavrasComElisao().add(arrayVerso[r]);

                                novoVerso.append(" " + palavraTemp.getPalavraSeparada());

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            novoVerso.append("/ " + palavraTemp.getPalavraSeparada());

                        }
                    }
                } else { //a partir da segunda palavra do verso
                    Palavra palavraTemp = contarSilabasPalavraNew(temp, 0, true);
                    palavraTemp.setPalavraOriginalSeparada(temp);
                    //Sempre que as sílabas forem contadas o verso é avisado de que naquela palavra houve um caso de diérese
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    versoEncontrado.getPalavrasVerso().add(palavraTemp);

                    numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();

                    arrayVerso[r] = palavraTemp;
                    boolean houveElisao = existeElisao(arrayVerso[r - 1].getPalavraSeparada(), arrayVerso[r].getPalavraSeparada());
                    if (houveElisao) {
                        try {
                            numSilabasPoeticas--;

                            //Melhorar código, essa linha já existe no método "verificaElisao"
                            versoEncontrado.getRegrasDeElisoesAplicadas().add(
                                    arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").substring(arrayVerso[r - 1].getPalavraSeparada().replace(",", "").replace(".", "").length() - 1) + arrayVerso[r].getPalavraSeparada().replace("#", "").charAt(0));
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[r - 1]);
                            versoEncontrado.getPalavrasComElisao().add(arrayVerso[r]);
                            novoVerso.append(" " + palavraTemp.getPalavraSeparada());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        novoVerso.append("/ " + palavraTemp.getPalavraSeparada());

                    }
                }
            } else {
//                System.out.println("Entrei no else: " + temp);
                novoVerso.append(" " + temp);

                r--;//Se for uma palavra decrementa o "R"
            }
            continuarBusca = true;
            /**
             * Verificando se a métrica buscada já foi atingida. Aqui trabalho
             * na questão da última palavra do verso. Se a métrica buscada já
             * tiver sido atinginda, entre um mínimo e um máximo e Se está
             * palavra contiver uma sílaba tônica, então eu decremento o total
             * do número de sílbas desta palavra e, em seguida, realizo a
             * contagem desta mesma palavra considerando até a sílaba tônica.
             */
            if (((numSilabasPoeticas >= minSilabas && numSilabasPoeticas <= maxSilabas) && temp.contains("#"))
                    || (numSilabasPoeticas >= maxSilabas && temp.contains("#"))) { //É necessário criar um mecanismo para dizer ao método quando isso deve ser feito
                if (validarContagem) {
                    Palavra palavraTemp = versoEncontrado.getPalavrasVerso().get(versoEncontrado.getPalavrasVerso().size() - 1);//Realizo a contagem das sílabas mais uma vez. - ISSO PODE SER MELHORADO.
                    int quantidadeTotal = palavraTemp.getQtdSilabasPoeticas();
                    numSilabasPoeticas -= quantidadeTotal;

                    palavraTemp = contarSilabasPalavraNew(temp, 0, true);
                    versoEncontrado.setNumeroDeDiereses(versoEncontrado.getNumeroDeDiereses() + palavraTemp.getSilabasDirese().size());
                    //O verso sabe quantos casos de sinerese aconteram
                    versoEncontrado.setNumeroDeSinereses(versoEncontrado.getNumeroDeSinereses() + palavraTemp.getSilabasSinerese().size());

                    int auxNumSilabas = palavraTemp.getQtdSilabasPoeticas();
                    //Se já ultrapassou pare a busca, vefique se tem histórico, se tiver reverta, 
                    //Caso contrário deixe como está
                    if (auxNumSilabas + numSilabasPoeticas >= maxSilabas) {
//                        System.out.println("Parei aqui 01");
                        continuarBusca = false;
                        if (versoEncontrado.isHistoricoDeContagem()) {
                            novoVerso = new StringBuilder(versoEncontrado.getVersoAnterior());
                            versoEncontrado.restaurarHistorico();
                            numSilabasPoeticas = versoEncontrado.getQuantidadeDeSilabasAnterior();

                        } else {
                            numSilabasPoeticas += palavraTemp.getQtdSilabasPoeticas();

                        }
                    } else {
                        //Preciso de um mecanismo que verifique se vale a pena pegar a próxima palavra
                        if (auxNumSilabas + numSilabasPoeticas >= minSilabas && auxNumSilabas + numSilabasPoeticas <= maxSilabas) {//Implementado em 05/01/2017
                            //Se estiver entre o método guarda o histórico e continua
                            versoEncontrado.setHistoricoDeContagem(true);
                            versoEncontrado.guardarHistorico();
                            versoEncontrado.setVersoAnterior(novoVerso.toString());
                            versoEncontrado.setQuantidadeDeSilabasAnterior(auxNumSilabas + numSilabasPoeticas);

                            numSilabasPoeticas += quantidadeTotal;

                        } else {//Se não estiver entre o método continua
                            numSilabasPoeticas += quantidadeTotal;

                        }
                    }
                } else {

                    if (numSilabasPoeticas < maxSilabas && st.hasMoreTokens()) {
                        continuarBusca = true;
                    } else {
                        if (numSilabasPoeticas - versoEncontrado.getNumeroDeDiereses() < maxSilabas && st.hasMoreTokens()) {

                            continuarBusca = true;
                        } else {

                            if (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes() + versoEncontrado.getNumeroDeSinereses() < maxSilabas && st.hasMoreTokens()) {
                                continuarBusca = true;
                            } else {
//                                System.out.println("Parei aqui 02");
                                continuarBusca = false;
                            }
                        }
                    }

                }

            }
        } while (st.hasMoreTokens() && continuarBusca);

        /**
         * Por falta de tempo, resolvi fazer um ajuste forçado. Eu verifico se a
         * variável numSilabasPoeticas corresponde a posição da última tônica.
         * Se for diferente eu considero a posição da última tônica.
         * Início--------->>>>>>>>> 01 de outubro de 2020
         */
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        if (versoEncontrado.getValidaMetro() != numSilabasPoeticas) {
            numSilabasPoeticas = versoEncontrado.getValidaMetro();
        }

        //Fim--------->>>>>>>>> 01 de outubro de 2020
        //O verso encontrado possui o número de sílabas acima do número da métrica buscada?
        //Em caso positivo, verificar se a quantidades de diéreses aplicadas se desfeitas pode resultar em estruturas dentro 
        //da métrica que se deseja alcançar.
        //Em caso positivo desfazer a diérese e decrementar o no número de sílabas poéticas
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
//        System.out.println("SAI COM O VERSO: " + novoVerso + " NÚMERO DE SÍLABAS: " + versoEncontrado.getNumeroDeSilabas() + " Diéreses: " + versoEncontrado.getNumeroDeDiereses());
        //Se o número de sílabas antes e depois do processo da aplicação de Diérese nas na palavra for igual 
        //COmo no caso da palavra Meio = Mei-o, Me-io não adianta desfazer a dierese. Pois a ação não irá alterar o 
        //número de sílabas. 
        if ((!(versoEncontrado.getNumeroDeSilabas() < minSilabas)) && versoEncontrado.getNumeroDeDiereses() > 0 && numSilabasPoeticas - versoEncontrado.getNumeroDeDiereses() <= maxSilabas) {

            boolean flag = verificarDesfazerDierese(versoEncontrado, numSilabasPoeticas, minSilabas, maxSilabas);
            if (flag) {
                return null;
            }
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);
            versoEncontrado.setStatusDaEscansao("Modificado");
//            System.out.println(" ajustarDierese - MODIFICANDO VERSO: " + novoVerso.toString());
            return ajustarDierese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), maxSilabas);
        }

        //O verso encontrado possui o número de sílabas abaixo do número da métrica buscada?
        //Desfazer as sinéreses pode resultar na métrica que sendo buscada? – Sim, então, desfazer sucessivamente.
        if (numSilabasPoeticas < minSilabas
                && versoEncontrado.getNumeroDeSinereses() > 0
                && numSilabasPoeticas + versoEncontrado.getNumeroDeSinereses() >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);

            return ajustarSinerese(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        //Desfazer as elisões pode resultar na métrica que está sendo buscada? 
        //– Sim, desfazer sucessivamente até que a métrica seja alcançada. 
        if (numSilabasPoeticas < minSilabas
                && versoEncontrado.getNumeroDeElisoes() > 0
                && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes()) >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);

            return ajustarElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        //Desfazer as sinéreses e elisões pode resultar na métrica que está sendo buscada? 
        //– Sim, desfazer os dois sucessivamente até que a métrica seja alcançada. 
        //O software irá começar pelas sinéreses.
        if (numSilabasPoeticas < minSilabas
                && (numSilabasPoeticas + versoEncontrado.getNumeroDeElisoes() + versoEncontrado.getNumeroDeSinereses()) >= minSilabas) {
            versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
            versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);

            return ajustarSinereseElisoes(versoEncontrado, numSilabasPoeticas, novoVerso.toString(), minSilabas);
        }

        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
//        System.out.println("Retornando :" + novoVerso.toString() + " - " + numSilabasPoeticas);
        versoEncontrado.setNumeroDeSilabasOriginais(numSilabasPoeticas);//???
        versoEncontrado.setVersoOriginalmenteEscandido(novoVerso.toString());
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
//        System.out.println("Estou retornando o que eu encontrei..... pense!");
        return versoEncontrado;
    }

    private Verso ajustarSinereseElisoes(Verso versoEncontrado, int numSilabasPoeticas, String novoVerso, int minSilabas) {
//        System.out.println("----------------------------------------------------");
//        System.out.println("Recebendo verso: " + novoVerso);
//        System.out.println("Número de Sílabas: " + numSilabasPoeticas);
        //Primeiro resolver as sinéreses
        if (versoEncontrado.getNumeroDeSinereses() > 0) {
            versoEncontrado = ajustarSinerese(versoEncontrado, numSilabasPoeticas, novoVerso, minSilabas);
        }
        //Agora resolver as elisões
        versoEncontrado = ajustarElisoes(versoEncontrado, versoEncontrado.getNumeroDeSilabas(), versoEncontrado.getVersoEscandido(), minSilabas);
//        System.out.println("------");
//        System.out.println("Devolvendo verso: " + versoEncontrado.getVersoEscandido());
//        System.out.println("Número de Sílabas: " + versoEncontrado.getNumeroDeSilabas());
//        System.out.println("----------------------------------------------------");
        return versoEncontrado;
    }

    private Verso ajustarElisoes(Verso versoEncontrado, int numSilabasPoeticas, String novoVerso, int minSilabas) {
        int numeroDeElisoesDoVersoes = versoEncontrado.getNumeroDeElisoes();
        int indiceDasElisoes = 0;
        int indiceDasRegras = 0;

//        for (String st : versoEncontrado.getRegrasDeElisoesAplicadas()) {
//            System.out.println(st);
//        }

        //Enquanto o número de sílabas mínimo não for alcançado e as regras aplicadas já não tiverem se esgotado....
        while (numSilabasPoeticas < minSilabas && indiceDasRegras < versoEncontrado.getRegrasDeElisoesAplicadas().size()) {
            String regra = versoEncontrado.getRegrasDeElisoesAplicadas().get(indiceDasRegras++).toLowerCase();
            if (MapaConfiguracao.getInstacia().getExcecoesElisao().contains(regra)) {
                String primeiraPalavra = versoEncontrado.getPalavrasComElisao().get(indiceDasElisoes).getPalavra().replaceAll("-", "/");
                String segundaPalavra = versoEncontrado.getPalavrasComElisao().get(++indiceDasElisoes).getPalavra().replaceAll("-", "/");
                indiceDasElisoes++;

                novoVerso = novoVerso.replaceFirst(Pattern.quote(primeiraPalavra + " " + segundaPalavra), primeiraPalavra + "/ " + segundaPalavra);

                numSilabasPoeticas++;
                versoEncontrado.setNumeroDeElisoesDesfeitas(versoEncontrado.getNumeroDeElisoesDesfeitas() + 1);
            }
        }

        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        return versoEncontrado;
    }

    private Verso ajustarSinerese(Verso versoEncontrado, int numSilabasPoeticas, String novoVerso, int minSilabas) {
//        System.out.println("Ajustando sinérese para: " + versoEncontrado.getPalavras());
        int aux = 0;
        int indiceSinerese = 0;
        int numeroSineresesPalavra = 0;
//        System.out.println("Verso recebido (novoVerso): " + novoVerso);
//        System.out.println("numSilabasPoeticas: " + numSilabasPoeticas);
//        System.out.println("minSilabas: " + minSilabas);
        while (numSilabasPoeticas < minSilabas) {
            while (aux < versoEncontrado.getPalavrasVerso().size() && !versoEncontrado.getPalavrasVerso().get(aux).isSinerese()) {//Enquanto não encontrar um palavra com dierese
                aux++;//Vá para a próxima palavra
            }
            try {
                if (aux == versoEncontrado.getPalavrasVerso().size()) {
                    break;
                }

                Palavra palavraAux = versoEncontrado.getPalavrasVerso().get(aux);//Peque a palavra com a Sinérese
//                System.out.println("Palavra com sinérese: " + palavraAux.getPalavra());

                //Veja se não é a última palavra
                if (!isUltimaPalavra(versoEncontrado, aux)) {
//                    System.out.println("Entrei aqui. Não é a última palavra.");
                    //Guarde quantas diéreses existem em uma palavra
                    numeroSineresesPalavra = palavraAux.getSilabasSinerese().size();
                    indiceSinerese = 0;
                    //Enquanto existir sinérese na palavra e quantidade de sílabas poéticas for maior do está sendo buscado...
                    while (numeroSineresesPalavra > 0 && numSilabasPoeticas < minSilabas) {
                        if (MapaConfiguracao.getInstacia().getExcecoesSinerese().contains(palavraAux.getRegrasAplicadasSinerese().get(indiceSinerese))) {
                            palavraAux.setPalavraOrigialEscandida(palavraAux.getPalavra());
                            //Guarde a palavra anterior
                            String palavraAnterior = palavraAux.getPalavra();//Palavra como está no verso

//                            System.out.println("Palavra antes da substituição: " + palavraAux.getPalavra());
                            //Pegue a palavra com a Sinérese e substitua a sílaba alterada pela anterior
//                            System.out.println("palavraAux.getSilabasSinerese().get(indiceSinerese): " + palavraAux.getSilabasSinerese().get(indiceSinerese));
                            // palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getSilabasSinerese().get(indiceSinerese), palavraAux.getSilabasSinerese().get(indiceSinerese)).replace("-", "/"));
                            palavraAux.setPalavra(palavraAux.getSilabasSinerese().get(indiceSinerese).replace("-", "/"));
//                            System.out.println("Palavra depois da substituição: " + palavraAux.getPalavra());
                            numeroSineresesPalavra--;
//                            System.out.println("Novo verso antes: " + novoVerso);
                            //Substitua a palavra no verso que será devolvido
//                            System.out.println("palavraAux.getNovaSilabaComSinerese().get(indiceSinerese): " + palavraAux.getNovaSilabaComSinerese().get(indiceSinerese));
//                            System.out.println("palavraAux.getSilabasSinerese().get(indiceSinerese): " + palavraAux.getSilabasSinerese().get(indiceSinerese));
                            novoVerso = novoVerso.replaceFirst(palavraAux.getNovaSilabaComSinerese().get(indiceSinerese).replaceAll("-", "/"), palavraAux.getSilabasSinerese().get(indiceSinerese)).replaceAll("-", "/");
//                            System.out.println("Novo verso depois: " + novoVerso);
                            indiceSinerese++;
                            //Decremente o número de sílabas poéticas
                            numSilabasPoeticas++;
                            versoEncontrado.setNumeroDeSinereseDesfeitas(versoEncontrado.getNumeroDeSinereseDesfeitas() + 1);
                        } else {
                            indiceSinerese++;
                            numeroSineresesPalavra--;
                        }
                    }
                } else {
                    numeroSineresesPalavra = palavraAux.getSilabasSinerese().size();
                    indiceSinerese = 0;
                    //Se a diérese for aplicada na sílaba tônica não deve ser revertida
                    if (palavraAux.getSilabasSinerese().size() > 0 && palavraAux.getSilabasSinerese().get(indiceSinerese).contains("#")) {
                        //  System.out.println("NÃO DEVE SER REVERTIDO");
                    } else {
                        if (sinereseEstaDepoisDaTonica(palavraAux, palavraAux.getSilabasSinerese().get(indiceSinerese))) {
//                            System.out.println("ESTA DEPOIS DA TÔNICA NÃO DEVE SER REVERTIDO");
                        } else {
//                            System.out.println("ESTA ANTES DA TÔNICA DEVE SER REVERTIDO");
                            while (numeroSineresesPalavra > 0 && numSilabasPoeticas < minSilabas) {
                                if (MapaConfiguracao.getInstacia().getExcecoesSinerese().contains(palavraAux.getRegrasAplicadasSinerese().get(indiceSinerese))) {
                                    palavraAux.setPalavraOrigialEscandida(palavraAux.getPalavra());
                                    //Guarde a palavra anterior
                                    String palavraAnterior = palavraAux.getPalavra();//Palavra como está no verso
                                    //Pegue a palavra com a Diérese e substitua a sílaba alterada pela anterior
                                    palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getSilabasSinerese().get(indiceSinerese), palavraAux.getSilabasSinerese().get(indiceSinerese)).replace("-", "/"));
//                            palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComSinerese().get(indiceSinerese), palavraAux.getSilabasSinerese().get(indiceSinerese)));
                                    numeroSineresesPalavra--;
                                    //Substitua a palavra no verso que será devolvido
                                    novoVerso = novoVerso.replace(palavraAux.getNovaSilabaComSinerese().get(indiceSinerese), palavraAux.getSilabasSinerese().get(indiceSinerese)).replaceAll("-", "/");
                                    indiceSinerese++;
                                    //Decremente o número de sílabas poéticas
                                    numSilabasPoeticas++;
                                    versoEncontrado.setNumeroDeSinereseDesfeitas(versoEncontrado.getNumeroDeSinereseDesfeitas() + 1);
                                } else {
                                    numeroSineresesPalavra--;
                                    indiceSinerese++;
                                }
                            }
                        }
                    }
                    //É a última palavra... É necessário pensar melhor em como isso irá ficar
                    //Por enquanto...
                    break;
                }
                aux++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }

        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
        return versoEncontrado;
    }

    /**
     * Retorna verdadeiro se a sinérese da palavra estiver antes da tônica Deve
     * ser utilizado sempre que estiver manipulando a última palavra
     *
     * @param palavra
     * @param sinerese - Junção que deve ser analisada
     * @return
     */
    public boolean sinereseEstaDepoisDaTonica(Palavra palavra, String sinerese) {
        String palavraAux = palavra.getPalavra();
        int posicaoDaTonica = palavraAux.indexOf("#", 0);
        int posicaoDaSinerese = palavraAux.indexOf(sinerese, 0);
        if (posicaoDaSinerese > posicaoDaTonica) {
            return true;
        }
        return false;
    }

    private Verso ajustarDierese(Verso versoEncontrado, int numSilabasPoeticas, String novoVerso, int maxSilabas) {

        int aux = 0;
        int indiceDiereses = 0;
        int numeroDiresesPalavra = 0;
        while (numSilabasPoeticas > maxSilabas) {
//            System.out.println("Entrei no primeiro Laço");
            while (aux < versoEncontrado.getPalavrasVerso().size() && !versoEncontrado.getPalavrasVerso().get(aux).isDierese()) {//Enquanto não encontrar um palavra com dierese
                aux++;//Vá para a próxima palavra
            }
//            System.out.println("Índice da palavra com diérese: " + aux);
            try {
                if (aux == versoEncontrado.getPalavrasVerso().size()) {
                    break;
                }
                Palavra palavraAux = versoEncontrado.getPalavrasVerso().get(aux);//Peque a palavra com a Diérese
//                System.out.println("PALAVRA COM DIÉRESE>>>>>>>>>>>>>>>>>>>>> " + palavraAux);

                //Veja se não é a última palavra
                if (!isUltimaPalavra(versoEncontrado, aux)) {
//                    System.out.println("Não é a última palavra...");
                    //Guarde quantas diéreses existem em uma palavra
                    numeroDiresesPalavra = palavraAux.getSilabasDirese().size();
//                    System.out.println("Número de diéreses da palavra: " + numeroDiresesPalavra);
                    indiceDiereses = 0;
                    //Enquanto existir diérese na palavra e quantidade de sílabas poéticas for maior do está sendo buscado...
                    while (numeroDiresesPalavra > 0 && numSilabasPoeticas > maxSilabas) {
//                        System.out.println("Entrei no laço");
                        //Verficar se a Dierese está na lista de exceções ante de processar
                        if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(palavraAux.getRegrasAplicadasDirese().get(indiceDiereses))) {
                            palavraAux.setPalavraOrigialEscandida(palavraAux.getPalavra());
//                            System.out.println(palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Está no Mapa de exceções");
                            //Guarde a palavra anterior
                            String palavraAnterior = palavraAux.getPalavra().replaceAll("-", "/");//Palavra como está no verso
//                            System.out.println("Palavra anterior: " + palavraAnterior);
                            //Pegue a palavra com a Diérese e substitua a sílaba alterada pela anterior
                            //palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses)));
                            palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses).getSilaba()));
                            indiceDiereses++;
                            numeroDiresesPalavra--;
                            //Substitua a palavra no verso que será devolvido
                            novoVerso = novoVerso.replace(palavraAnterior, palavraAux.getPalavra().replaceAll("-", "/"));
                            //Decremente o número de sílabas poéticas
                            numSilabasPoeticas--;
                            versoEncontrado.setNumeroDeDieresesDesfeitas(versoEncontrado.getNumeroDeDieresesDesfeitas() + 1);
                        } else {
//                            System.out.println(palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Não está no Mapa de exceções");
                            indiceDiereses++;
                            numeroDiresesPalavra--;
                        }

                        //Guarde a palavra anterior
//                        String palavraAnterior = palavraAux.getPalavra().replaceAll("-", "/");//Palavra como está no verso
//                        //Pegue a palavra com a Diérese e substitua a sílaba alterada pela anterior
//                        palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses)));
//                        indiceDiereses++;
//                        numeroDiresesPalavra--;
//                        //Substitua a palavra no verso que será devolvido
//                        novoVerso = novoVerso.replace(palavraAnterior, palavraAux.getPalavra().replaceAll("-", "/"));
//                        //Decremente o número de sílabas poéticas
//                        numSilabasPoeticas--;
                    }

                } else {
//                    System.out.println("É A ÚLTIMA PALAVRA DO VERSO: " + palavraAux.getPalavra());
//                    System.out.println("A diérese foi aplicada na Tonica? " + palavraAux.isPossuiDiereseNaTonica());
                    numeroDiresesPalavra = palavraAux.getSilabasDirese().size();
                    indiceDiereses = 0;

                    //Se a diérese for aplicada na sílaba tônica não deve ser revertida - deve sim.
                    if (palavraAux.getSilabasDirese().size() > 0 && palavraAux.isPossuiDiereseNaTonica()) {//Isso aqui é uma coisa muito mal feita. - RESOLVER ISSO MELHORAR CÓDIGO.
                        //Início - 22/05/2017
                        while (numeroDiresesPalavra > 0 && numSilabasPoeticas > maxSilabas) {
                            if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(palavraAux.getRegrasAplicadasDirese().get(indiceDiereses))) {
                                palavraAux.setPalavraOrigialEscandida(palavraAux.getPalavra());

//                                System.out.println("Última palavra: " + palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Está no Mapa de exceções");
                                //Guarde a palavra anterior
                                String palavraAnterior = palavraAux.getPalavra().replaceAll("-", "/");//Palavra como está no verso
                                //Pegue a palavra com a Diérese e substitua a sílaba alterada pela anterior
                                //palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses)));
                                palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses).getSilaba()));
                                indiceDiereses++;
                                numeroDiresesPalavra--;
                                //Substitua a palavra no verso que será devolvido
                                novoVerso = novoVerso.replace(palavraAnterior, palavraAux.getPalavra().replaceAll("-", "/"));
                                //Decremente o número de sílabas poéticas
                                numSilabasPoeticas--;
                                versoEncontrado.setNumeroDeDieresesDesfeitas(versoEncontrado.getNumeroDeDieresesDesfeitas() + 1);
//                                System.out.println("Palavra revertida...nova palavra: " + palavraAux.getPalavra());
                            } else {
//                                System.out.println("Última palavra: " + palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Não está no Mapa de exceções");
                                indiceDiereses++;
                                numeroDiresesPalavra--;
                            }
                        }

                        //Fim - 22/05/2017
//                        System.out.println("NÃO DEVE SER REVERTIDO");- 22/05/2017 - Deve ser sim.
                    } else {
//                        System.out.println("DEVE SER REVERTIDO");
                        while (numeroDiresesPalavra > 0 && numSilabasPoeticas > maxSilabas) {
                            if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(palavraAux.getRegrasAplicadasDirese().get(indiceDiereses))) {
                                palavraAux.setPalavraOrigialEscandida(palavraAux.getPalavra());

//                                System.out.println("Última palavra: " + palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Está no Mapa de exceções");
                                //Guarde a palavra anterior
                                String palavraAnterior = palavraAux.getPalavra().replaceAll("-", "/");//Palavra como está no verso
                                //Pegue a palavra com a Diérese e substitua a sílaba alterada pela anterior
                                // palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses)));
                                palavraAux.setPalavra(palavraAux.getPalavra().replace(palavraAux.getNovaSilabaComDierese().get(indiceDiereses), palavraAux.getSilabasDirese().get(indiceDiereses).getSilaba()));
                                indiceDiereses++;
                                numeroDiresesPalavra--;
                                //Substitua a palavra no verso que será devolvido
                                novoVerso = novoVerso.replace(palavraAnterior, palavraAux.getPalavra().replaceAll("-", "/"));
                                //Decremente o número de sílabas poéticas
                                numSilabasPoeticas--;
                                versoEncontrado.setNumeroDeDieresesDesfeitas(versoEncontrado.getNumeroDeDieresesDesfeitas() + 1);
//                                System.out.println("Palavra revertida...nova palavra: " + palavraAux.getPalavra());
                            } else {
//                                System.out.println("Última palavra: " + palavraAux.getRegrasAplicadasDirese().get(indiceDiereses) + " - Não está no Mapa de exceções");
                                indiceDiereses++;
                                numeroDiresesPalavra--;
                            }
                        }
                    }

                    //É a última palavra... É necessário pensar melhor em como isso irá ficar
                    //Por enquanto...
                    break;
                }

                aux++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (classificacao.containsKey(numSilabasPoeticas)) {
            versoEncontrado.setClassificacao(classificacao.get(numSilabasPoeticas));
        } else {
            versoEncontrado.setClassificacao("MaiorQ13");
        }
        versoEncontrado.setNumeroDeSilabas(numSilabasPoeticas);
        versoEncontrado.setVersoEscandido(novoVerso.toString());
//        System.out.println("Retornando Verso: " + novoVerso.toString());
        return versoEncontrado;

    }

    public boolean isUltimaPalavra(Verso versoEncontrado, int posicao) {
//        System.out.println("OIOIOIOIOIOIOIOIOI ------>>>>>>>>>> isUltimaPalavra: " + versoEncontrado.getPalavrasVerso().get(posicao).getPalavra());
//        System.out.println("Quantidade de palavras do verso: " + versoEncontrado.getPalavrasVerso().size());
//        System.out.println("Valor de posição: " + posicao);
        return versoEncontrado.getPalavrasVerso().size() - 1 == posicao;
    }

    //Realiza a contagem das sílabas quando o objetivo for verificar as terminações
    private boolean isVogal(char c) {
        return vogais.contains(new String("" + c).toLowerCase());

    }

    private boolean isVogalAcentuada(char c) {
        return vogaisAcentuadas.contains("" + c);
    }

    private boolean buscarLetra(char c, char[] caracters) {
        int i;
        for (i = 0; i < caracters.length; i++) {
            if (c == caracters[i]) {
                return true;
            }
        }
        return false;
    }

    //Verifica casos de junção entre vogais em uma palavra e retorna o número de sílabas poéticas
    public Palavra contarSilabasPalavra(String s, int op, boolean considerarComoUltimaPalavra) {
        String[] temp = s.split("-");
        Palavra palavra = null;
        int numero = s.split("-").length;
        int numeroSilabasUltimaPalavra = numero;
        if (numero == 1) {
            palavra = new Palavra();
            palavra.setPalavra(s);
            palavra.setPalavraSeparada(s);
            palavra.setQtdSilabasPoeticas(numero);
            return palavra;
        }
        //Processar casos de dierese
        boolean houveDirese = false;
        String novaSilaba = null;
        for (int i = 0; i < temp.length; i++) {
            if (verificarDierese(temp[i])) {// Isso precisa melhorar
                novaSilaba = temp[i].substring(0, temp[i].length() - 1) + "-" + temp[i].charAt(temp[i].length() - 1);
                s = s.replace(temp[i], novaSilaba);
                temp[i] = novaSilaba;
                numero++;//Isso deve causar um alongamento no número de sílabas.
                houveDirese = true;
            }
        }
        StringBuilder ns = new StringBuilder("");

        for (int i = 0; i < temp.length; i++) {
            if (i < (temp.length - 1)) {
                if (isVogal(temp[i].charAt(temp[i].length() - 1)) //se a última letra da sílaba for uma vogal
                        && (isVogal(temp[i + 1].replace("#", "").charAt(0)) //a próxima comecar por uma vogal
                        || (buscarLetra(temp[i + 1].replace("#", "").charAt(0), new char[]{'h', 'H'})))) {//ou verifica se a próxima palavra começa com h ou H
                    if (verificarSinerese(temp[i], temp[i + 1])) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        numero--;
                    } else if ((buscarLetra(temp[i + 1].replace("#", "").charAt(0), new char[]{'h', 'H'})) && considerarOH) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        numero--;
                    } else if (temp[i].charAt(temp[i].length() - 1) == temp[i + 1].replace("#", "").charAt(0)) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        numero--;
                    }
                }
            }
            if (considerarComoUltimaPalavra && temp[i].contains("#")) {
                numero = (i + 1) - (numeroSilabasUltimaPalavra - numero);
                break;
            }
        }
        if (ns.toString().isEmpty()) {
            ns = new StringBuilder(s.replaceAll("-", "/"));
        }
        if (!ns.toString().isEmpty() && op == 0) {
            verso = verso.replace(s, ns.toString());
            palavra = new Palavra();
            palavra.setPalavra(s);
            palavra.setPalavraSeparada(ns.toString());
            palavra.setQtdSilabasPoeticas(numero);
        }
        return palavra;
    }

    //Verifica casos de junção entre vogais em uma palavra e retorna o número de sílabas poéticas
    /**
     * Método editado em 29/12/2016 O objetivo aqui é catalogar as ocorrências
     * de diérese e sinérese para os casos em que a métrica não for alcançada as
     * ocorrências do evento serão desfeitas para forçar que a métrica seja
     * alcançada.
     *
     * @param s - Palavra que deve ter suas sílabas contadas.
     * @param op
     * @param considerarComoUltimaPalavra - true signigica que o método irá
     * contabilizar a quantidade de sílabas até a a sílaba tônica da palavra.
     * @return
     */
    public Palavra contarSilabasPalavraNew(String s, int op, boolean considerarComoUltimaPalavra) {

        String[] temp = s.split("-");
        Palavra palavra = null;
        int numero = s.split("-").length;
        int numeroSilabasUltimaPalavra = numero;
        if (numero == 1) {
            palavra = new Palavra();
            palavra.setPalavra(s);
            palavra.setPalavraSeparada(s);
            palavra.setQtdSilabasPoeticas(numero);
//            System.out.println("Retornando no início: " + numero);
            return palavra;
        }
        //Processar casos de dierese
        boolean houveDirese = false;
        String novaSilaba = null;
        palavra = new Palavra();
        for (int i = 0; i < temp.length; i++) {
            if (verificarDierese(temp[i])) {// Isso precisa melhorar
                if (temp[i].contains("#")) {
                    palavra.setPossuiDiereseNaTonica(true);
                }
                SilabaModificada sm = new SilabaModificada();
                sm.setSilaba(temp[i]);
                sm.setPosicaoDaSilabaNaPalavra(i);
                palavra.getSilabasDirese().add(sm);
                //palavra.getSilabasDirese().add(temp[i]);

                int tamanho = temp[i].length();
                palavra.getRegrasAplicadasDirese().add((temp[i].charAt(tamanho - 2) + "" + temp[i].charAt(tamanho - 1)));
                novaSilaba = temp[i].substring(0, temp[i].length() - 1) + "-" + temp[i].charAt(temp[i].length() - 1);
                s = s.replace(temp[i], novaSilaba);
                palavra.setPalavraOrigialEscandida(s);
//                System.out.println("novaSilaba: " + novaSilaba);
                temp[i] = novaSilaba;
                numero++;//Isso deve causar um alongamento no número de sílabas.
                houveDirese = true;
                palavra.setDierese(true);
                palavra.getNovaSilabaComDierese().add(temp[i]);
                // palavra.getSilabasDirese().add(temp[i]);
            }
        }
        StringBuilder ns = new StringBuilder("");

        for (int i = 0; i < temp.length; i++) {
            if (i < (temp.length - 1)) {
                if (temp[i].length() > 0 && isVogal(temp[i].charAt(temp[i].length() - 1)) //se a última letra da sílaba for uma vogal
                        && (isVogal(temp[i + 1].replace("#", "").charAt(0)) //a próxima comecar por uma vogal
                        || (buscarLetra(temp[i + 1].replace("#", "").charAt(0), new char[]{'h', 'H'})))) {//ou verifica se a próxima palavra começa com h ou H
                    if (verificarSinerese(temp[i], temp[i + 1])) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        palavra.getRegrasAplicadasSinerese().add(temp[i].substring(temp[i].length() - 1) + temp[i + 1].charAt(0));
                        palavra.setSinerese(true);
                        palavra.getSilabasSinerese().add(temp[i] + "-" + temp[i + 1]);
                        palavra.getNovaSilabaComSinerese().add(temp[i] + "" + temp[i + 1]);
                        palavra.setPalavraOrigialEscandida(ns.toString());
                        numero--;
                    } else if ((buscarLetra(temp[i + 1].replace("#", "").charAt(0), new char[]{'h', 'H'})) && considerarOH) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        numero--;
                    } else if (temp[i].charAt(temp[i].length() - 1) == temp[i + 1].replace("#", "").charAt(0)) {
                        ns = new StringBuilder(s.replace((temp[i] + "-" + temp[i + 1]), (temp[i] + "" + temp[i + 1])));
                        numero--;
                    }
                }
            }
            if (considerarComoUltimaPalavra && temp[i].contains("#")) {
                numero = (i + 1) - (numeroSilabasUltimaPalavra - numero);
                //numero = contaSilabasPoeticasUltimaPalavra(ns);
                break;
            }
        }
        if (ns.toString().isEmpty()) {
            ns = new StringBuilder(s.replaceAll("-", "/"));
        }
        if (!ns.toString().isEmpty() && op == 0) {
            verso = verso.replace(s, ns.toString());
            //palavra = new Palavra();
            palavra.setPalavra(s);
            palavra.setPalavraSeparada(ns.toString().replaceAll("-", "/"));//01/03/2017
            palavra.setQtdSilabasPoeticas(numero);
        }
//        System.out.println("Retornando no fim: " + numero);
        //Medida emergencial - 23 de setembro 2020
        if (considerarComoUltimaPalavra) {
            palavra.setQtdSilabasPoeticas(contaSilabasPoeticasUltimaPalavra(ns));
        }
        return palavra;

    }

    private int contaSilabasPoeticasUltimaPalavra(StringBuilder ns) {

        String[] temp = null;
        if (ns.toString().contains("/")) {
            temp = ns.toString().split("/");
        } else {
            temp = ns.toString().split("-");
        }
        int n = 0;
        for (String s : temp) {
            n++;
            if (s.contains("#")) {
                break;
            }
        }

        return n;
    }

    //é a junção de dois sons (hiatos) num só (ditongo) dentro da mesma palavra
    public boolean verificarSinerese(String pre, String pos) {
        if (pos.length() > 1) {
            return false;
        }
        StringBuilder sb = new StringBuilder(pre.substring(pre.length() - 1) + pos.charAt(0));
        return MapaConfiguracao.getInstacia().getSinerese().contains(sb.toString());
    }

    //O contrario da sinérese. Divisão do ditongo em dois hiatos, dando origem a duas sílabas
    /**
     * Esté método não representa corretamente o fenomeno da Diérese. Ver método
     * verificarDierese(silaba)
     *
     * @param pre
     * @param pos
     * @return
     */
    @Deprecated
    public boolean verificarDierese(String pre, String pos) {
        StringBuilder sb = new StringBuilder(pre.substring(pre.length() - 1) + pos.replace("#", "").charAt(0));
        return MapaConfiguracao.getInstacia().getDierese().contains(sb.toString());
    }

    private void carregarDitongoCrescente() {
        ditongoCresecente.add("gua");
        ditongoCresecente.add("qua");
        ditongoCresecente.add("gui");
        ditongoCresecente.add("qui");
        ditongoCresecente.add("güa");
        ditongoCresecente.add("qüa");
        ditongoCresecente.add("güi");
        ditongoCresecente.add("qüi");

    }

    //Ver a questão das consoantes....
    public boolean verificarDierese(String silaba) {
        if (ditongoCresecente.contains(silaba.toLowerCase())) {
            return false;
        }
        if (silaba.length() <= 2) {
            return false;
        }
        StringBuilder sb = null;
        if (isVogal(silaba.charAt(silaba.length() - 2)) && isVogal(silaba.charAt(silaba.length() - 1))) {
            int tamanho = silaba.length();
            sb = new StringBuilder(silaba.charAt(tamanho - 2) + "" + silaba.charAt(tamanho - 1));
//            System.out.println("O que ficou sb::::::::::::::: " + sb);
        } else {
            return false;
        }
//        System.out.println("Retornando Mapa de Configuração: " + silaba + " Houve dierese: " + MapaConfiguracao.getInstacia().getDierese().contains(sb.toString()));
        return MapaConfiguracao.getInstacia().getDierese().contains(sb.toString());

    }

    public boolean verificarElisao(String pre, String pos) {
//        System.out.println("Verifica elisão: " + pre);
        StringBuilder sb = new StringBuilder(pre.replace(",", "").replace(".", "").substring(pre.replace(",", "").replace(".", "").length() - 1) + pos.replace("#", "").charAt(0));
//        System.out.println("Procurando elisão em: " + sb);
        return MapaConfiguracao.getInstacia().getElisao().contains(sb.toString().toLowerCase());

    }

    public boolean buscarJuncao(String juncao, String[] lista) {
        for (int i = 0; i < lista.length; i++) {
            if (juncao.equalsIgnoreCase(lista[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isOxitona(String palavra) {
//        System.out.println("String Palavra recebida: isOxitona: " + palavra);
        String silabas[] = palavra.split("/");
//        System.out.println("TAMANHO DO VETOR: " + silabas.length);
        int tam = silabas.length;
        if (silabas[tam - 1].contains("#")) {
            return true;
        }
        return false;
    }

    public void carregarClassificacaoDoVerso() {
        classificacao.put(3, "Trissílabo");
        classificacao.put(4, "Tetrassílabo");
        classificacao.put(5, "Pentassílabo");
        classificacao.put(6, "Hexassílabo");
        classificacao.put(7, "Heptassílabo");
        classificacao.put(8, "Octossílabo");
        classificacao.put(9, "Eneassílabo");
        classificacao.put(11, "Hendecassílabo");
        classificacao.put(10, "Decassílabo");
        classificacao.put(12, "Dodecassílabo");
        classificacao.put(13, "Bárbaro");
    }

    private void carragarMapaConfiguracao() {
        if (MapaConfiguracao.getInstacia().isVersoContado()) {
            carregarTipoDeVerso();
        }

        this.considerarOH = MapaConfiguracao.getInstacia().isConsiderarH();
        this.considerarAtona = MapaConfiguracao.getInstacia().isConsiderarAtona();
        this.considerarTonica = MapaConfiguracao.getInstacia().isConsiderarTonica();
        this.versoContado = MapaConfiguracao.getInstacia().isVersoContado();
        this.versoLivre = MapaConfiguracao.getInstacia().isVersoLivre();

    }

    private void carregarTipoDeVerso() {
        if (ConfiguracaoEncansao.getInstacia().getTipoDeVerso() == 2) {
            this.carregarClassificacaoDoVerso();
            classificarVerso = true;
        }
    }

    private void carregarVogais() {

        vogais.add("a");
        vogais.add("e");
        vogais.add("i");
        vogais.add("o");
        vogais.add("u");
        vogais.add("ô");
        vogais.add("á");
        vogais.add("è");
        vogais.add("ò");
        vogais.add("â");
        vogais.add("ë");
        vogais.add("à");
        vogais.add("ð");
        vogais.add("õ");
        vogais.add("å");
        vogais.add("í");
        vogais.add("ö");
        vogais.add("ã");
        vogais.add("î");
        vogais.add("ä");
        vogais.add("ú");
        vogais.add("ì");
        vogais.add("û");
        vogais.add("ï");
        vogais.add("ù");
        vogais.add("é");
        vogais.add("ó");
        vogais.add("ü");
        vogais.add("ê");
    }

    private void carregarVogaisAcentuadas() {
        vogaisAcentuadas.add("à");
        vogaisAcentuadas.add("À");
        vogaisAcentuadas.add("á");
        vogaisAcentuadas.add("á");
        vogaisAcentuadas.add("Á");
        vogaisAcentuadas.add("â");
        vogaisAcentuadas.add("Â");
        vogaisAcentuadas.add("é");
        vogaisAcentuadas.add("É");
        vogaisAcentuadas.add("ê");
        vogaisAcentuadas.add("Ê");
        vogaisAcentuadas.add("í");
        vogaisAcentuadas.add("Í");
        vogaisAcentuadas.add("ó");
        vogaisAcentuadas.add("Ó");
        vogaisAcentuadas.add("ô");
        vogaisAcentuadas.add("Ô");
        vogaisAcentuadas.add("ú");
        vogaisAcentuadas.add("Ú");

    }

    private boolean verificarDesfazerDierese(Verso versoEncontrado, int numSilabasPoeticas, int minSilabas, int maxSilabas) {
        int naoCompensaDesfazer = 0;
        int i = 0;
        int comp = 0;

        while (i < versoEncontrado.getPalavrasVerso().size()) {
            if (versoEncontrado.getPalavrasVerso().get(i).isDierese()) {
                Palavra palavraAux = versoEncontrado.getPalavrasVerso().get(i);//Peque a palavra com a Diérese

                boolean ehAUltimaPalavra = isUltimaPalavra(versoEncontrado, i);
                if (ehAUltimaPalavra) {
                    comp = compensaDesfazerDierezeUltimaPalavra(palavraAux);
                    if (comp == 0) {

                        palavraAux.setDesfazerDierese(false);
                        naoCompensaDesfazer++;
                    }
                }

                int numeroSilabaOriginal = palavraAux.getPalavraOriginalSeparada().split("-").length;
                int numeroSilabaPalavraModificada = palavraAux.getPalavraOrigialEscandida().split("-").length;
            }
            i++;
//            System.out.println("i: " + i);
        }

        if (numSilabasPoeticas - naoCompensaDesfazer <= maxSilabas) {
            return false;
        } else {
            return true;
        }
    }

    private int compensaDesfazerDierezeUltimaPalavra(Palavra palavra) {
        /*Quando um ditongo passa a ser um hiato, temos duas sílabas. No caso da última palavra,
        precisamos verificar, se no hiato, a tônica ficou na primeira nova sílaba ou na segunda nova sílaba.
        Se for na primeira, não adianta desfazer porque ação não altera o número de sílabas poéticas, mas no caso 
        contrário sim.
         */

        int n = palavra.getSilabasDirese().size();
        int dv = 0;
        if (n == 1) {
            String silaba = palavra.getSilabasDirese().get(0).getSilaba();
            if (silaba.contains("#")) {//Contém a tônica??
                /*A tônica está antes ou depois na forma da diérese?
                Se a primeira for a tônica não faz sentido desfazer. A ação não irá 
                modificar o número de sílabas poéticas, por se tratar da tônica da última 
                palavra
                 */
                if (palavra.getNovaSilabaComDierese().get(0).contains("#")) {
                    return 0;//Nada deve ser modificado
                } else {
                    return 1;//Apenas uma diérese poderá ser desfeita.
                }
            }
        } else {//Caso tenha mais que uma diérese na palavra, poderá ser desfeita  número de diér
            int num;

            for (num = 0; num < palavra.getSilabasDirese().size(); num++) {
                if (!palavra.getSilabasDirese().get(num).getSilaba().contains("#")) {//Se não for a tônica
                    dv++;
                } else {
                    if (!palavra.getNovaSilabaComDierese().get(0).contains("#")) {//Se for a tônica mas compensa desfazer
                        dv++;
                        break;
                    }

                }
            }

        }
        return dv;
    }

    /**
     * Verifica se o parametro "palavra" corresponde a última palavra do verso
     *
     * @param verso - Verso a ser analisado.
     * @param palavra - Palavra a ser verificada
     * @return true, caso a palavra seja a última palavra do verso. false caso
     * contrário.
     */
    private boolean isUltimaPalavra(String verso, String palavra, int posicao) {
        verso = verso.replaceAll("/", "");
        StringTokenizer palavras = new StringTokenizer(verso, " ");
        String vetorPalavras[] = Utilitario.preencherVetorToken(palavras);
        /* Se o verso tiver apenas uma palavra e a posição for igual a for igual a zero 
        significa é que o verso só tem uma palavra. Retorna verdadeiro
         */
        if (vetorPalavras.length - 1 == posicao) {
            return true;
        }

        palavra = palavra.replaceAll("/", "");

        String ultimaPalavra = vetorPalavras[vetorPalavras.length - 1];

        if (palavra.equalsIgnoreCase(ultimaPalavra)) {
            return true;
        }

        return false;
    }

}
