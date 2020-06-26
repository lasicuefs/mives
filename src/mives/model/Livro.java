/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import mives.util.EstatisticaTipo;
import mives.util.Segmento;
import mives.util.SerieGraficoDistancia;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo É necessário verificar se realmente é necessário ter essa
 * classe.
 */
public class Livro {

    /**
     * @return the tiposBuscados
     */
    public String getTiposBuscados() {
        return tiposBuscados;
    }

    /**
     * @param tiposBuscados the tiposBuscados to set
     */
    public void setTiposBuscados(String tiposBuscados) {
        this.tiposBuscados = tiposBuscados;
    }

    /**
     * @return the localBusca
     */
    public String getLocalBusca() {
        return localBusca;
    }

    /**
     * @param localBusca the localBusca to set
     */
    public void setLocalBusca(String localBusca) {
        this.localBusca = localBusca;
    }

    public static StringBuilder getStringLivro() {
        return stringLivro;
    }

    private File arquivoDeOrigem;
    private static StringBuilder stringLivro = new StringBuilder();
    private boolean carragado;
    private int numeracao = 1;
    private int numeroDePaginas;
    private HashMap<Integer, Pagina> paginas;
    private HashMap<String, EstatisticaTipo> estatiticaGeral = new HashMap<>();
    private boolean foiProcessado = false;
    long distanciaMedia;
    private int tipoDeVersoInicio;
    private int tipoDeVersoFim;
    private String tipoDeBusca;
    private MapaConfiguracao mapaConfiguracao;
    private String localBusca;
    private String tiposBuscados;

    private ArrayList<Sentenca> sentencas = new ArrayList<>();

    private static Livro livro = null;

    public static Livro getInstance() {
        if (livro == null) {
            livro = new Livro();
        }
        return livro;

    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    private Livro() {
        paginas = new HashMap<>();
    }

//    private Livro(File arquivoDeOrigem) {
//        paginas = new HashMap<>();
//        this.arquivoDeOrigem = arquivoDeOrigem;
//        carregarTexto2();
//    }
    public static Boolean carregandoLivro = true;

    @Deprecated
    private void carregarTexto() {
        String linha;
        System.out.println("Carregando texto...");
        FileWriter fileWriterDestino = null;
        BufferedWriter bufferedWriterDestino = null;
        String numeroPagina = null;
        StringBuilder PaginaTemp = new StringBuilder();
        Pagina pagina = null;

        try {
            FileReader readerBase = new FileReader(getArquivoDeOrigem()); //Faz novaPal leitura do arquivo base
            BufferedReader bufferedReaderBase = new BufferedReader(readerBase);//Faz novaPal leitura do arquivo base
            linha = bufferedReaderBase.readLine();
            inicio:
            while (linha != null) {
                if (linha.contains("")) {
                    linha = linha.replace("", "");
                    if (linha.length() != 0) {
                        pagina = new Pagina();
                        try {
                            pagina.setNumero(Integer.parseInt(linha.toString()));
                            paginas.put(pagina.getNumero(), pagina);
                        } catch (NumberFormatException nf) {
                            pagina.setNumero(0);
                            paginas.put(0, pagina);
                            System.out.println(nf.getMessage());
                        }
                        numeroPagina = linha;
                    }
                    pagina.getLinhasOriginais().add("Página: >> " + linha);
                    pagina.getLinhas().add(new Linha("Página: >> " + linha));
                    stringLivro.append("Página: " + linha + "\n");
                    linha = bufferedReaderBase.readLine();
                    continue inicio;
                }

                pagina.getLinhasOriginais().add(linha);
                pagina.getLinhas().add(new Linha(linha));
                stringLivro.append(linha + "\n");
                linha = bufferedReaderBase.readLine();

            }
            try {
                this.numeroDePaginas = Integer.parseInt(numeroPagina.replaceAll(" ", ""));
            } catch (NumberFormatException ex) {
                System.out.println("Não foi possível converter em número");
            }
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(SepararSilabaTexto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Falha ao ler o arquivo");
        }
        gerarFrases();
        carragado = true;
        carregandoLivro = false;

    }

    /**
     * Esta funcionalidade foi retirada daqui ficando a cargo do Helper do
     * controlador da interface que carrega o livro em 28/04/2018
     *
     * @param arquivoDeOrigem
     */
    @Deprecated
    public void carregarTexto2(File arquivoDeOrigem) {

        this.arquivoDeOrigem = arquivoDeOrigem;

        FileWriter fileWriterDestino = null;
        BufferedWriter bufferedWriterDestino = null;
        String numeroPagina = null;
        Pagina pagina = new Pagina();
        try {
            String dados = new String(Files.readAllBytes(getArquivoDeOrigem().toPath()), StandardCharsets.UTF_8);
            String linhasDoTexto[] = dados.split("\n");
            boolean temNumero = false;
            inicio:
            for (String linha : linhasDoTexto) {

                //    System.out.println("Linha: " + linha);
                if (linha.contains("")) {
                    linha = linha.replace("", "");
                    if (linha.length() != 0) {
                        pagina = new Pagina();
                        try {
                            pagina.setNumero(Integer.parseInt(linha.toString().trim()));
                            paginas.put(pagina.getNumero(), pagina);
                            temNumero = true;
                        } catch (NumberFormatException nf) {
                            nf.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }
                        numeroPagina = linha;
                    }
                    pagina.getLinhasOriginais().add("Página: >> " + linha);
                    pagina.getLinhas().add(new Linha("Página: >> " + linha));
                    stringLivro.append("Página: " + linha + "\n");
                    continue inicio;
                }
                if (!temNumero) {
                    pagina.setNumero(0);
                    paginas.put(0, pagina);
                    temNumero = true;
                }
                pagina.getLinhasOriginais().add(linha);
                pagina.getLinhas().add(new Linha(linha));
                stringLivro.append(linha + "\n");
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "O arquivo não foi encontrado.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao ler o arquivo");
        }
        gerarFrases();
        carragado = true;
        carregandoLivro = false;

    }

//    public void imprimeLivro() {
//        System.out.println("IMPRIMINDO PÁGINAS DE UM LIVRO");
//        for (Integer chave : paginas.keySet()) {
//            paginas.get(chave).imprimirMinhasLinhas();
//
//        }
//    }
    public void gerarFrases() {
        for (Integer chave : paginas.keySet()) {
            paginas.get(chave).processarFrase();
        }
    }

    public void gerarLinhasEscandidas() {
        for (Integer chave : paginas.keySet()) {
            paginas.get(chave).gerarLinhasEscandidasV2();//Em 26/06/2020

            //paginas.get(chave).gerarLinhasEscandidas();
        }
        this.setFoiProcessado(true);
    }

    FileWriter fileWriterDestino;
    BufferedWriter bufferedWriterDestino;
    File file;

    public void imprimeLivroEscandido() {

        for (Integer chave : paginas.keySet()) {
            //  paginas.get(chave).imprimeLinhasEscadidadas(bufferedWriterDestino);
            paginas.get(chave).imprimeLinhasEscadidadas();
        }

    }

    public void imprimeLivroEscandido(File file) {
        try {
            fileWriterDestino = new FileWriter(file); //Escrever no novo arquivo
            bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            bufferedWriterDestino.write("Livro escandido...");

            for (Integer chave : paginas.keySet()) {
                paginas.get(chave).imprimeLinhasEscadidadas(bufferedWriterDestino);
                paginas.get(chave).imprimeLinhasEscadidadas();
            }
            bufferedWriterDestino.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

//    public void imprimeLivroEscandido() {
//        for (Integer chave : paginas.keySet()) {
//            paginas.get(chave).imprimeLinhasEscadidadas();
//        }
//    }
//    public void imprimeFrases() {
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-Minhas Frases por Página->>>>>>>>>>>>>>>>>>>>>>>>>");
//        for (Integer chave : paginas.keySet()) {
//            System.out.println("Página: " + chave);
//            ArrayList<String> frases = paginas.get(chave).getFraseEncontrada();
//            for (String fra : frases) {
//                System.out.println(fra);
//            }
//            System.out.println("");
//        }
//    }
    public void imprimeFrases() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-Minhas Frases por Página->>>>>>>>>>>>>>>>>>>>>>>>>");
        int contador = 1;
        for (Integer chave : paginas.keySet()) {
            // System.out.println("Página: " + chave);
            ArrayList<Frase> frases = paginas.get(chave).getFrases();
            for (Frase fra : frases) {
//                System.out.println("Linha de Origem da Frase: " + fra.getLinhaDeOrigem() + " >>>>>>>>>>>>>>>>>>>>>>");
                if (fra.toString().length() != 0) {
                    System.out.println("----------------------------------------------------------------------------------------------------------------------");
                    System.out.println(contador + " " + fra.toString());
                    contador++;
                    System.out.println("----------------------------------------------------------------------------------------------------------------------");
                }
            }
//            System.out.println("");
        }
    }

    @Deprecated
    public void extrairFrasesInterando() {
        int numeroDeCaracteres = Livro.stringLivro.length();
        for (int n = 0; n < numeroDeCaracteres; n++) {
            //   if (Livro.stringLivro.charAt(n) == 49.828) {
            //System.out.println("Número de Página: ");
            if (Livro.stringLivro.charAt(n) != '\\' && Livro.stringLivro.charAt(n + 1) != 'n') {
//                    System.out.print(Livro.stringLivro.charAt(n));
//                }
                //  }
                System.out.print(Livro.stringLivro.charAt(n));
                if (Livro.stringLivro.charAt(n) == '.') {
                    System.out.println("Não é mais utilizando");
                }
            }
        }
        System.exit(0);
    }

    @Deprecated
    //Insere a marcação de número de linha no TXT
    public String inserirMarcacaoPagina(String linha) {
        String marcador = "";
        if (linha.split(" ").length != 0 && linha.matches(".*\\w.*")) {//Verifica se a linha tem caracteres

            if (numeracao == 1) {
                marcador = "%----------------------------- Página " + numeracao + " -----------------------------%";
                Livro.stringLivro.append(marcador);
                //   bufferedWriterDestino.write(marcador);
                //    bufferedWriterDestino.newLine();
                numeracao++;
//                    return linha.substring(1); alterado 15/07/2015

                return linha.substring(0);
            } else {
                Pattern pattAlpha = Pattern.compile("[A-Z|a-z]");
                Pattern pattNum = Pattern.compile("[\\d+]");
                //  Pattern pattern = Pattern.compile(patternString);
                String[] s = linha.trim().split(" ");
                String getNumero = "";
                int t = 0;

                while (pattNum.matcher("" + s[0].charAt(0)).matches()//Extrair número quando o paragrafo começa por número concatenado por letra
                        && pattAlpha.matcher("" + s[0].charAt(s[0].length() - 1)).matches()) {
                    getNumero = getNumero + s[0].charAt(0);
                    s[0] = s[0].substring(1);
                    t++;
                }

                if (isSequence(getNumero, numeracao)) {//Verifica se realmente é a seguência das páginas

                    marcador = "%----------------------------- Página " + (numeracao++) + " -----------------------------%";
                    //   bufferedWriterDestino.write(marcador);
                    Livro.stringLivro.append(marcador);
                    //    bufferedWriterDestino.newLine();
                    return linha.substring(t);
                } else if (isSequence(s[0], numeracao)) {
                    marcador = "%----------------------------- Página " + (numeracao++) + " -----------------------------%";
                    //   bufferedWriterDestino.write(marcador);
                    Livro.stringLivro.append(marcador);
                    //    bufferedWriterDestino.newLine();
                    return linha.substring(s[0].length());
                }
            }
        }
        return linha;
    }

    @Deprecated
    //Pensar depois em um lógica mais eficiente essa aqui pode poduzir inconsistências na numeração da linha
    public boolean isSequence(String possivelNumero, int ultimoNumPag) {
        boolean result = true;
        try {
            int n = Integer.parseInt(possivelNumero);
            if (n == (ultimoNumPag)) {
                return result;
            } else {
                result = false;
            }
        } catch (NumberFormatException nfe) {
            result = false;
        }

        return result;
    }

    int numeroDeVersosEncontrados = 0;
    HashMap<String, Integer> estatisticaTiposLivro = new HashMap<String, Integer>();

    boolean existeEstatiscaSintetica = false;

    public void gerarEstatistica() {
        if (!existeEstatiscaSintetica) {
            System.out.println("Gerando Estatística");
            int temp = 0, valorAtual = 0;
            HashMap<String, Integer> estatisticaTiposTemp;
            for (Integer chave : paginas.keySet()) {

                numeroDeVersosEncontrados += paginas.get(chave).getNumeroDeVersos();
                estatisticaTiposTemp = paginas.get(chave).getEstatisticaTipos();

                for (String key : estatisticaTiposTemp.keySet()) {
                    valorAtual = 0;
                    temp = 0;
                    if (estatisticaTiposLivro.containsKey(key)) {
                        temp = estatisticaTiposTemp.get(key);
                        valorAtual = estatisticaTiposLivro.get(key);
                        // estatisticaTiposLivro.replace(key, temp, temp + valorAtual);
                        estatisticaTiposLivro.put(key, temp + valorAtual);
                    } else {
                        //    temp = 1;
                        estatisticaTiposLivro.put(key, estatisticaTiposTemp.get(key));
                    }

                }
            }
            existeEstatiscaSintetica = true;
        }
        imprimirEstatisticasDeTipos();
    }

    public void imprimirEstatisticasDeTipos() {
        System.out.println("Imprimindo Estatística Gerais do Livro...");
        System.out.println("Total de Indicações");
        for (String key : estatisticaTiposLivro.keySet()) {
            System.out.println(key + ":\t" + estatisticaTiposLivro.get(key));
        }
    }

    public void imprimeVersos() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-Meus versos por Página->>>>>>>>>>>>>>>>>>>>>>>>>");
        for (Integer chave : paginas.keySet()) {
            paginas.get(chave).imprimeVersos();
        }
        // gerarEstatistica();
        // imprimirEstatisticasDeTipos();
//        System.out.println("");

    }

    public void imprimeFrasesComVersos() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-imprimeFrasesComVersos->>>>>>>>>>>>>>>>>>>>>>>>>");
        for (Integer chave : paginas.keySet()) {
            paginas.get(chave).imprimeFrasesComVersos();
        }
    }

    public void imprimeSentencasOuVersos(File file) {
        System.out.println("Imprimindo Sentenças");
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter destino = new BufferedWriter(fileWriter);
            ArrayList<Frase> frases;
            int contador = 1;
            int indiceDaFrase = 0;
            for (Integer chave : paginas.keySet()) {
                frases = paginas.get(chave).getFrases();
                for (Frase frase : frases) {
                    if (frase.getVerso() != null) {
                        destino.write(contador + " " + frase.getVerso().getNumeroDeSilabas());
                        //      System.out.println(contador + " " + frase.getVerso().getNumeroDeSilabas());
                    } else {
                        //  System.out.println(contador + " " + "-1");
                        destino.write(contador + " " + "-1");
                    }
                    destino.newLine();
                    contador++;
                }
            }
        } catch (IOException exception) {

        }
    }

    public int getNumeroDeSegmentos() {
        ArrayList<Frase> frases;
        int contador = 0;
        for (Integer chave : paginas.keySet()) {
            contador += paginas.get(chave).getFrases().size();
        }
        return contador;
    }
    /**
     * Retorna um vetor com segmentos extraido do texto processado.
     *
     * @return
     */

    Segmento segmentos[] = null;//Segmentos utilizados para a geração do gráfico na interface de visualização

    public Segmento[] imprimeSentencasOuVersosGraf() {
        if (segmentos == null) {

            ArrayList<Frase> frases;
            ArrayList<Segmento> segmentosAux = new ArrayList<>();
            int contador = 1;
            int indiceDaFrase = 0;

            for (Integer chave : paginas.keySet()) {
                frases = paginas.get(chave).getFrases();
                for (Frase frase : frases) {

                    if (frase.getVerso() != null) {
                        segmentosAux.add(new Segmento(contador, frase.getVerso().getNumeroDeSilabas()));
                    } else {
                        segmentosAux.add(new Segmento(contador, -1));
                    }

                    contador++;
                }

            }
            //Mudar isso Trabalho desnecessário.
            segmentos = new Segmento[segmentosAux.size()];
            for (int i = 0; i < segmentos.length; i++) {
                segmentos[i] = segmentosAux.get(i);
            }
        }

        return segmentos;
    }

    /**
     * @return the numeroDePaginas
     */
    public int getNumeroDePaginas() {
        return numeroDePaginas;
    }

    /**
     * @param numeroDePaginas the numeroDePaginas to set
     */
    public void setNumeroDePaginas(int numeroDePaginas) {
        this.numeroDePaginas = numeroDePaginas;
    }

    public HashMap<Integer, Pagina> getPaginas() {
        return paginas;
    }

    public boolean isCarragado() {
        return carragado;
    }

    public void setCarragado(boolean carragado) {
        this.carragado = carragado;
    }

    public void gerarEstaticaComSubTiposPorPagina() {
        for (Integer chave : paginas.keySet()) {
            paginas.get(chave).imprimirEstatistica();
        }
        gerarEstatisticaDoLivro();

    }
    private int quantidadeGeralDeVersos;
    boolean existeEstatisticaAnalitica = false;

    /**
     * Pensar melhor no que está acontecendo para que o resultado da contagem de
     * versos encontrados está projetando um valor errado. Importante é remover
     * o último "FOR" desse método. - 07/04/2106
     */
    public void gerarEstatisticaDoLivro() {
        if (!existeEstatisticaAnalitica) {
            HashMap<String, EstatisticaTipo> estTemp;
            int quantidade, qtdSub;
            for (Integer chave : paginas.keySet()) {
                estTemp = paginas.get(chave).getEstatisticaPagina();
                for (String key : estTemp.keySet()) {
                    if (estatiticaGeral.containsKey(key)) {
                        quantidade = estatiticaGeral.get(key).getQuantidade();
                        quantidade += estTemp.get(key).getQuantidade();
                        // quantidadeGeralDeVersos += quantidade;
                        estatiticaGeral.get(key).setQuantidade(quantidade);
                        for (String keySub : estTemp.get(key).getSubtipos().keySet()) {
                            if (estatiticaGeral.get(key).getSubtipos().containsKey(keySub)) {
                                qtdSub = estatiticaGeral.get(key).getSubtipos().get(keySub);
                                qtdSub += estTemp.get(key).getSubtipos().get(keySub);
                                estatiticaGeral.get(key).getSubtipos().put(keySub, qtdSub);
                            } else {
                                estatiticaGeral.get(key).getSubtipos().put(keySub, estTemp.get(key).getSubtipos().get(keySub));
                            }
                        }
                    } else {
                        EstatisticaTipo novaEst = new EstatisticaTipo();
                        estatiticaGeral.put(key, novaEst);
                        novaEst.setQuantidade(estTemp.get(key).getQuantidade());
                        for (String keySub : estTemp.get(key).getSubtipos().keySet()) {
                            novaEst.getSubtipos().put(keySub, estTemp.get(key).getSubtipos().get(keySub));
                        }

                    }
                }
            }
            quantidadeGeralDeVersos = 0;//É um erro ter isso na Classe stringLivro. Deve ficar na classe que encapsula as estatísticas
            for (String key : estatiticaGeral.keySet()) {
                quantidadeGeralDeVersos += estatiticaGeral.get(key).getQuantidade();
            }
            existeEstatisticaAnalitica = true;
        }
        imprimirEstatistica();
    }

    public void imprimirEstatistica() {
        System.out.println("Imprimindo resumo...");
        System.out.println("Total de indicações encontradas: " + quantidadeGeralDeVersos);
        if (estatiticaGeral.size() > 0) {
            System.out.println("-------------------------------------------------------------------");
            for (String key : estatiticaGeral.keySet()) {
                System.out.println("Tipo:\t" + key);
                System.out.println("Quantidade:\t" + estatiticaGeral.get(key).getQuantidade());
                System.out.println("Categorias:");
                for (String subKey : estatiticaGeral.get(key).getSubtipos().keySet()) {
                    System.out.println("- " + subKey + ":\t" + estatiticaGeral.get(key).getSubtipos().get(subKey));
                }
//                System.out.println("");
            }
            System.out.println("-------------------------------------------------------------------");
        }
    }

    public HashMap<String, EstatisticaTipo> getEstatiticaGeral() {
        return estatiticaGeral;
    }

    public void setEstatiticaGeral(HashMap<String, EstatisticaTipo> estatiticaGeral) {
        this.estatiticaGeral = estatiticaGeral;
    }

    public int getQuantidadeGeralDeVersos() {
        return quantidadeGeralDeVersos;
    }

    public void setQuantidadeGeralDeVersos(int quantidadeGeralDeVersos) {
        this.quantidadeGeralDeVersos = quantidadeGeralDeVersos;
    }

    public HashSet<String> getHashDeVersos() {
        HashSet<String> hash = new HashSet<>();
        for (Integer chave : paginas.keySet()) {
            for (Verso verso : paginas.get(chave).getVersos()) {
                hash.add(verso.getPalavras().toLowerCase());
            }
        }
        return hash;
    }

    public boolean isFoiProcessado() {
        return foiProcessado;
    }

    public void setFoiProcessado(boolean foiProcessado) {
        this.foiProcessado = foiProcessado;
    }

    /**
     * @return the arquivoDeOrigem
     */
    public File getArquivoDeOrigem() {
        return arquivoDeOrigem;
    }

    /**
     * @param arquivoDeOrigem the arquivoDeOrigem to set
     */
    public void setArquivoDeOrigem(File arquivoDeOrigem) {
        this.arquivoDeOrigem = arquivoDeOrigem;
    }

    public List<Verso> getVersosLivro() {
        List<Verso> versos = new ArrayList<>();
        for (Integer chave : paginas.keySet()) {
            versos.addAll(paginas.get(chave).getVersos());
        }
        return versos;
    }
    boolean jaFoiCalculadaADistancia = false;
    //public int vetorDist[];

    private HashMap<Integer, ArrayList<Integer>> distanciasPorTipo;

    public HashMap<Integer, ArrayList<Integer>> getDistanciasPorTipo() {
        return calcularDistanciasDeTipos();
    }

    static boolean existeDistanciaPorTipo = false;

    private HashMap<Integer, ArrayList<Integer>> calcularDistanciasDeTipos() {
        if (!existeDistanciaPorTipo) {

            int numeroDaOcorrencia = 0;
            int distanciaAnterior = 0;
            distanciasPorTipo = new HashMap<>();
            distanciasPorTipo.put(1, new ArrayList<>());
            for (Integer chave : paginas.keySet()) {
                for (Frase frase : paginas.get(chave).getFrases()) {
                    numeroDaOcorrencia++;
                    if (frase.existeVerso()) {
                        distanciasPorTipo.get(1).add(numeroDaOcorrencia);
                        if (!(distanciasPorTipo.containsKey(frase.getVerso().getNumeroDeSilabas()))) {
                            distanciasPorTipo.put(frase.getVerso().getNumeroDeSilabas(), new ArrayList<>());
                            distanciasPorTipo.get(frase.getVerso().getNumeroDeSilabas()).add(numeroDaOcorrencia);
                        } else {
                            distanciasPorTipo.get(frase.getVerso().getNumeroDeSilabas()).add(numeroDaOcorrencia);
                        }
                        if (frase.getVersosExtra().size() > 0) {
                            for (Verso ve : frase.getVersosExtra()) {

                                if (!(distanciasPorTipo.containsKey(ve.getNumeroDeSilabas()))) {
                                    distanciasPorTipo.put(ve.getNumeroDeSilabas(), new ArrayList<>());
                                    distanciasPorTipo.get(ve.getNumeroDeSilabas()).add(numeroDaOcorrencia);
                                } else {
                                    distanciasPorTipo.get(ve.getNumeroDeSilabas()).add(numeroDaOcorrencia);
                                }

                                if (ve.getOutrasFormas() != null) {
                                    for (Verso of : ve.getOutrasFormas().getVersosExtras()) {
                                        if (!(distanciasPorTipo.containsKey(ve.getNumeroDeSilabas()))) {
                                            distanciasPorTipo.put(of.getNumeroDeSilabas(), new ArrayList<>());
                                            distanciasPorTipo.get(of.getNumeroDeSilabas()).add(numeroDaOcorrencia);
                                        } else {
                                            distanciasPorTipo.get(of.getNumeroDeSilabas()).add(numeroDaOcorrencia);
                                        }
                                    }
                                }
                            }
                        }
                        distanciaAnterior = numeroDaOcorrencia;
                    }
                }
            }
        }
        existeDistanciaPorTipo = true;
        return distanciasPorTipo;
    }

    private HashMap<String, Integer> distribuicaoDeTonicas;
    static boolean existeDistribuicaoDeTonias = false;

    public HashMap<String, Integer> getDistribuicaoDeTonicas() {
        if (!existeDistribuicaoDeTonias) {
            int valorAux = 0;
            distribuicaoDeTonicas = new HashMap<>();
            for (Integer chave : paginas.keySet()) {
                for (Frase frase : paginas.get(chave).getFrases()) {
                    if (frase.existeVerso()) {
                        if (!(distribuicaoDeTonicas.containsKey(frase.getVerso().getPosicionamentoDasTonicas()))) {
                            distribuicaoDeTonicas.put(frase.getVerso().getPosicionamentoDasTonicas(), 0);
                            valorAux = distribuicaoDeTonicas.get(frase.getVerso().getPosicionamentoDasTonicas()) + 1;
                            distribuicaoDeTonicas.put(frase.getVerso().getPosicionamentoDasTonicas(), valorAux);

                        } else {
                            valorAux = distribuicaoDeTonicas.get(frase.getVerso().getPosicionamentoDasTonicas()) + 1;
                            distribuicaoDeTonicas.put(frase.getVerso().getPosicionamentoDasTonicas(), valorAux);
                        }
                        if (frase.getVersosExtra().size() > 0) {
                            for (Verso ve : frase.getVersosExtra()) {

                                if (!(distribuicaoDeTonicas.containsKey(ve.getPosicionamentoDasTonicas()))) {
                                    distribuicaoDeTonicas.put(ve.getPosicionamentoDasTonicas(), 0);
                                    valorAux = distribuicaoDeTonicas.get(ve.getPosicionamentoDasTonicas()) + 1;
                                    distribuicaoDeTonicas.put(ve.getPosicionamentoDasTonicas(), valorAux);

                                } else {
                                    valorAux = distribuicaoDeTonicas.get(ve.getPosicionamentoDasTonicas()) + 1;
                                    distribuicaoDeTonicas.put(ve.getPosicionamentoDasTonicas(), valorAux);
                                }
                                if (ve.getOutrasFormas() != null) {
                                    for (Verso of : ve.getOutrasFormas().getVersosExtras()) {
                                        if (!(distribuicaoDeTonicas.containsKey(of.getPosicionamentoDasTonicas()))) {
                                            distribuicaoDeTonicas.put(of.getPosicionamentoDasTonicas(), 0);
                                            valorAux = distribuicaoDeTonicas.get(of.getPosicionamentoDasTonicas()) + 1;
                                            distribuicaoDeTonicas.put(of.getPosicionamentoDasTonicas(), valorAux);

                                        } else {
                                            valorAux = distribuicaoDeTonicas.get(of.getPosicionamentoDasTonicas()) + 1;
                                            distribuicaoDeTonicas.put(of.getPosicionamentoDasTonicas(), valorAux);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
        existeDistribuicaoDeTonias = true;
        return distribuicaoDeTonicas;
    }

    public void calcularDistancias() {
        this.getNumeroDeSegmentos();
        if (jaFoiCalculadaADistancia) {
            return;
        }
        carregarTiposEncontrados();
        int quantidadeDeFrases = 0;
        int numeroDeVersos = 0;
        float distanciaMedia = 0;
        int somaDasDistancias = 0;
        Verso versoTemp = null;
        int distTipo = 0;
        int indice = -1;
        for (Integer chave : paginas.keySet()) {
            for (Frase frase : paginas.get(chave).getFrases()) {
                indice++;
                if (frase.existeVerso()) {
                    //   vetorDist[indice] = frase.getVerso().getNumeroDeSilabas();
                    if (versoTemp != null) {
                        versoTemp.setNumeroDeVersoDepois(quantidadeDeFrases);

                    }
                    //Calculando o intervalo entre as estruturas de mesmo tipo
                    for (String tipo : distancias.keySet()) {
                        if (!(frase.getVerso().getClassificacao().equals(tipo))) {
                            distancias.get(tipo).intervalo++;
                        } else {
                            distancias.get(tipo).quantidade++;
                            distTipo = distancias.get(tipo).intervalo;
                            distancias.get(tipo).guardarIntervalo();
                        }
                    }
                    frase.getVerso().setNumeroDeVersoAntes(quantidadeDeFrases);
                    frase.getVerso().setDistanciaACima(distTipo);//Distância para versos do mesmo tipo
                    versoTemp = frase.getVerso();
                    somaDasDistancias += quantidadeDeFrases;//Soma das distâncias.
                    numeroDeVersos++;//Acumula o número de versos encontrados.
                    quantidadeDeFrases = 0;
                } else {
                    quantidadeDeFrases++;
                }
            }
        }
        distanciaMedia = (float) (somaDasDistancias / numeroDeVersos);

    }

    public void exibirDistancias() {

        System.out.println("--------------------------------------------------------------");
        System.out.println("Resumo");
        for (String tipo : distancias.keySet()) {
            distancias.get(tipo).calcularDistanciaMedia();
            System.out.println("Tipo: " + tipo);
            System.out.println("Distância Média: " + distancias.get(tipo).getDistanciaMedia());
        }
        System.out.println("--------------------------------------------------------------");

        for (Integer chave : paginas.keySet()) {
            for (Frase frase : paginas.get(chave).getFrases()) {
                System.out.println("--------------------------------------------------------------");
                System.out.println("Verso: " + frase.getVerso().getPalavras());
                System.out.println("Classificação: " + frase.getVerso().getClassificacao());
                System.out.println("Distância entre versos do mesmo tipo: " + frase.getVerso().getDistanciaACima());
                System.out.println("Distância para o metro anterior: " + frase.getVerso().getNumeroDeVersoAntes());
                System.out.println("Distância para o próximo metro: " + frase.getVerso().getNumeroDeVersoDepois());
                System.out.println("--------------------------------------------------------------");
            }
        }
    }

    public ArrayList gerarVetorDistancia() {
        ArrayList<Integer> vetorDistancias = new ArrayList<>();
        for (Integer chave : paginas.keySet()) {
            for (Frase frase : paginas.get(chave).getFrases()) {
                if (frase.existeVerso()) {
                    vetorDistancias.add(frase.getVerso().getNumeroDeVersoAntes());
                }
            }
        }
        return vetorDistancias;
    }

    public void imprimirEmArquivo(File file) {

        try {
            fileWriterDestino = new FileWriter(file); //Escrever no novo arquivo
            bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
//            bufferedWriterDestino.write("Valores de distância entre segmentos frásicos...");
            bufferedWriterDestino.newLine();
            for (Integer chave : paginas.keySet()) {
                for (Frase frase : paginas.get(chave).getFrases()) {
                    if (frase.existeVerso()) {
//                        bufferedWriterDestino.write("--------------------------------------------------------------");
//                        bufferedWriterDestino.newLine();
//                        bufferedWriterDestino.write("Verso: " + frase.getVerso().getPalavras());
//                        bufferedWriterDestino.newLine();
//                        bufferedWriterDestino.write("Classificação: " + frase.getVerso().getClassificacao());
//                        bufferedWriterDestino.newLine();
//                        bufferedWriterDestino.write("Distância entre versos do mesmo tipo: " + frase.getVerso().getDistanciaACima());
//                        bufferedWriterDestino.newLine();
                        bufferedWriterDestino.write("Distância para o metro anterior: " + frase.getVerso().getNumeroDeVersoAntes());
                        bufferedWriterDestino.newLine();
//                        bufferedWriterDestino.write("Distância para o próximo metro: " + frase.getVerso().getNumeroDeVersoDepois());
//                        bufferedWriterDestino.newLine();
//                        bufferedWriterDestino.write("--------------------------------------------------------------");
//                        bufferedWriterDestino.newLine();
                    }
                }
            }
            bufferedWriterDestino.close();
            fileWriterDestino.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public HashMap<String, Distancia> distancias = new HashMap<String, Distancia>();

    private void carregarTiposEncontrados() {
        for (Integer chave : paginas.keySet()) {
            for (Verso v : paginas.get(chave).getVersos()) {
                if (!(distancias.containsKey(v.getClassificacao()))) {
                    distancias.put(v.getClassificacao(), new Distancia());
                }
            }

        }

    }

    public ArrayList<Sentenca> getSentencas() {
        if (sentencas == null) {
            sentencas = new ArrayList<>();
        }
        return sentencas;
    }

    public void setSentencas(ArrayList<Sentenca> sentencas) {
        this.sentencas = sentencas;
    }

    public String getTipoDeBusca() {
        return tipoDeBusca;
    }

    public void setTipoDeBusca(String tipoDeBusca) {
        this.tipoDeBusca = tipoDeBusca;
    }

    public MapaConfiguracao getMapaConfiguracao() {
        return mapaConfiguracao;
    }

    public void setMapaConfiguracao(MapaConfiguracao mapaConfiguracao) {
        this.mapaConfiguracao = mapaConfiguracao;
    }

    private class Distancia {

        private int quantidade = 0;//Quantidade de tipos encontrados
        private int distanciaMedia = 0;
        private int intervalo = 0;//Quantidade de frases entre os tipos

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public int getDistanciaMedia() {
            return distanciaMedia;
        }

        public void setDistanciaMedia(int distanciaMedia) {
            this.distanciaMedia = distanciaMedia;
        }

        public int getIntervalo() {
            return intervalo;
        }

        public void setIntervalo(int intervalo) {
            this.intervalo = intervalo;
        }

        public void guardarIntervalo() {
            distanciaMedia += intervalo;
            intervalo = 0;
        }

        public void calcularDistanciaMedia() {
            distanciaMedia = distanciaMedia / quantidade;
        }
    }

    public void imprimirDadosDeSegmentosFrasiscos() {
        for (Integer numeroPagina : paginas.keySet()) {
            Pagina pagina = paginas.get(numeroPagina);
            for (Frase frase : pagina.getFrases()) {
                if (frase.existeVerso()) {
                    System.out.println(frase.toString());
                    System.out.println(frase.getVerso().getNumeroDeSilabas() + " - " + frase.getVerso().getPosicionamentoDasTonicas() + " - " + frase.getVerso().getVersoEscandido());
                    if (frase.getVersosExtra().size() > 0) {
                        for (Verso ve : frase.getVersosExtra()) {
                            System.out.println(ve.getNumeroDeSilabas() + " - " + ve.getPosicionamentoDasTonicas() + " - " + ve.getVersoEscandido());
                            if (ve.getOutrasFormas() != null) {
                                System.out.println(ve.getOutrasFormas().getVersoBase().getNumeroDeSilabas() + " - " + ve.getOutrasFormas().getVersoBase().getPosicionamentoDasTonicas() + " - " + ve.getOutrasFormas().getVersoBase().getVersoEscandido());
                                for (Verso of : ve.getOutrasFormas().getVersosExtras()) {
                                    System.out.println(of.getNumeroDeSilabas() + " - " + of.getPosicionamentoDasTonicas() + " - " + of.getVersoEscandido());
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    private void ordenarVetor(int vetor[]) {
        int i = vetor.length;
        int j;
        int aux;
        for (i = 1; i < vetor.length; i++) {
            for (j = 0; j < vetor.length - i; j++) {
                if (vetor[j] > vetor[j + 1]) {
                    aux = vetor[j + 1];
                    vetor[j + 1] = vetor[j];
                    vetor[j] = aux;

                }
            }
        }
    }

    static boolean sentencasCompostas = true;

    public ArrayList<Sentenca> comporSentencas() {

//        int vetorPaginas[] = new int[paginas.size()];
//        int i = 0;
//        for (Integer numeroPagina : paginas.keySet()) {
//            vetorPaginas[i] = numeroPagina;
//            i++;
//        }
//        
//        ordenarVetor(vetorPaginas);
        if (sentencasCompostas) {
            Sentenca sentenca;
            for (Integer numeroPagina : paginas.keySet()) {
//            for (Integer numeroPagina : vetorPaginas) {
                Pagina pagina = paginas.get(numeroPagina);
                for (Frase frase : pagina.getFrases()) {
                    if (frase.existeVerso() && frase.getVerso().isSubstituicao()) {
                        sentenca = new Sentenca();
                        sentenca.setSegmento(frase.toString());
                        sentenca.setLink(frase.getVerso().getLink());
                        EstruturaVersificacao estruturaVersificacao = new EstruturaVersificacao();
                        sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                        estruturaVersificacao.setNumeroDeSilabas(frase.getVerso().getNumeroDeSilabas());
                        estruturaVersificacao.setPosicaoDasTonicas(frase.getVerso().getPosicionamentoDasTonicas());
                        estruturaVersificacao.setSentecaEscandida(frase.getVerso().getVersoEscandido());
                        estruturaVersificacao.setPalavrasVerso(frase.getVerso().getPalavras());

                        if (frase.getVersosExtra().size() > 0) {
                            for (Verso ve : frase.getVersosExtra()) {
                                estruturaVersificacao = new EstruturaVersificacao();
                                sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                estruturaVersificacao.setNumeroDeSilabas(ve.getNumeroDeSilabas());
                                estruturaVersificacao.setPosicaoDasTonicas(ve.getPosicionamentoDasTonicas());
                                estruturaVersificacao.setSentecaEscandida(ve.getVersoEscandido());
                                estruturaVersificacao.setPalavrasVerso(ve.getPalavras());
                                if (ve.getOutrasFormas() != null) {
                                    estruturaVersificacao = new EstruturaVersificacao();
                                    sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                    estruturaVersificacao.setNumeroDeSilabas(ve.getOutrasFormas().getVersoBase().getNumeroDeSilabas());
                                    estruturaVersificacao.setPosicaoDasTonicas(ve.getOutrasFormas().getVersoBase().getPosicionamentoDasTonicas());
                                    estruturaVersificacao.setSentecaEscandida(ve.getOutrasFormas().getVersoBase().getVersoEscandido());
                                    estruturaVersificacao.setPalavrasVerso(ve.getOutrasFormas().getVersoBase().getPalavras());
                                    for (Verso of : ve.getOutrasFormas().getVersosExtras()) {
                                        estruturaVersificacao = new EstruturaVersificacao();
                                        sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                        estruturaVersificacao.setNumeroDeSilabas(of.getNumeroDeSilabas());
                                        estruturaVersificacao.setPosicaoDasTonicas(of.getPosicionamentoDasTonicas());
                                        estruturaVersificacao.setSentecaEscandida(of.getVersoEscandido());
                                        estruturaVersificacao.setPalavrasVerso(of.getPalavras());
                                    }
                                }
                            }
                        }
                        sentencas.add(sentenca);

                    }
                }

            }
            sentencasCompostas = false;
        }
        return sentencas;
    }

    boolean sentencasCompostasCompletas = true;

    /**
     * Date: 24 de março de 2020 Gerar uma array de sentenças com informações
     * complementares. Como por exemplo o número do segmento.
     *
     * @return
     */
    public ArrayList<Sentenca> comporSentencasGeral() {
//        System.out.println("Estou aqui...");
        //24/03/2020
        //this.gerarNumeracaoDeSentecas();

        ArrayList<Sentenca> sentencas = new ArrayList<>();

        int contador = 1;
        if (sentencasCompostasCompletas) {
            Sentenca sentenca;
//            System.out.println("Número de Sentenças:");
            for (Integer numeroPagina : paginas.keySet()) {
                Pagina pagina = paginas.get(numeroPagina);
                for (Frase frase : pagina.getFrases()) {

                    if (frase.existeVerso() && frase.getVerso().isSubstituicao()) {
                        sentenca = new Sentenca();
                        //24/03/2020
                        sentenca.setNumeroDaFrase(contador);
//                        System.out.println(sentenca.getNumeroDaFrase());

                        sentenca.setSegmento(frase.toString());
                        sentenca.setLink(frase.getVerso().getLink());
                        EstruturaVersificacao estruturaVersificacao = new EstruturaVersificacao();
                        sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                        estruturaVersificacao.setNumeroDeSilabas(frase.getVerso().getNumeroDeSilabas());
                        estruturaVersificacao.setPosicaoDasTonicas(frase.getVerso().getPosicionamentoDasTonicas());
                        estruturaVersificacao.setSentecaEscandida(frase.getVerso().getVersoEscandido());
                        estruturaVersificacao.setPalavrasVerso(frase.getVerso().getPalavras());

                        if (frase.getVersosExtra().size() > 0) {
                            for (Verso ve : frase.getVersosExtra()) {
                                estruturaVersificacao = new EstruturaVersificacao();
                                sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                estruturaVersificacao.setNumeroDeSilabas(ve.getNumeroDeSilabas());
                                estruturaVersificacao.setPosicaoDasTonicas(ve.getPosicionamentoDasTonicas());
                                estruturaVersificacao.setSentecaEscandida(ve.getVersoEscandido());
                                estruturaVersificacao.setPalavrasVerso(ve.getPalavras());
                                if (ve.getOutrasFormas() != null) {
                                    estruturaVersificacao = new EstruturaVersificacao();
                                    sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                    estruturaVersificacao.setNumeroDeSilabas(ve.getOutrasFormas().getVersoBase().getNumeroDeSilabas());
                                    estruturaVersificacao.setPosicaoDasTonicas(ve.getOutrasFormas().getVersoBase().getPosicionamentoDasTonicas());
                                    estruturaVersificacao.setSentecaEscandida(ve.getOutrasFormas().getVersoBase().getVersoEscandido());
                                    estruturaVersificacao.setPalavrasVerso(ve.getOutrasFormas().getVersoBase().getPalavras());
                                    for (Verso of : ve.getOutrasFormas().getVersosExtras()) {
                                        estruturaVersificacao = new EstruturaVersificacao();
                                        sentenca.getEstruturaDeVesificacao().add(estruturaVersificacao);
                                        estruturaVersificacao.setNumeroDeSilabas(of.getNumeroDeSilabas());
                                        estruturaVersificacao.setPosicaoDasTonicas(of.getPosicionamentoDasTonicas());
                                        estruturaVersificacao.setSentecaEscandida(of.getVersoEscandido());
                                        estruturaVersificacao.setPalavrasVerso(of.getPalavras());
                                    }
                                }
                            }
                        }
                        sentencas.add(sentenca);

                    }
                    if (Utilitario.existeUmaPalavra(frase.toString())) {
                        contador++;
                    }
                }

            }
            sentencasCompostasCompletas = false;
        }
        return sentencas;
    }

    public void imprimirMatrizDeTiposEncontrados(int inicioDoIntervalo, int fimDoIntervalo) {

        System.out.print("Palavras\tCaracteres\t");
        for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
            System.out.print(i + "\t");
        }
        System.out.print("\n");
        for (Integer numeroPagina : paginas.keySet()) {
            Pagina pagina = paginas.get(numeroPagina);

            for (Frase frase : pagina.getFrases()) {
                if (frase.existeVerso()) {
                    System.out.printf("%s\t%10s", frase.getVerso().getQuantidadeDePalavras(), frase.getVerso().getQuantidadeDeLetras());
                    for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
                        if (existeValor(frase.getMetricasDeVerso(), i)) {
                            if (i == inicioDoIntervalo) {
                                System.out.printf("\t%10s", i);
                            } else {
                                System.out.print("\t" + i);
                            }
                        } else {
                            if (i == inicioDoIntervalo) {
                                System.out.printf("\t%10s", (-1));
                            } else {
                                System.out.print("\t-1");
                            }
                        }
                    }
                    System.out.print("\n");
                } else {
                    for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
                        if (i == inicioDoIntervalo) {
                            System.out.printf("\t%10s", (-1));
                        } else {
                            System.out.print("\t-1");
                        }
                    }
                }
            }

        }
    }

    private boolean existeValor(ArrayList<Integer> metricas, int metrica) {
        int aux;
        for (Integer me : metricas) {
            aux = me;
            if (aux == metrica) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the tipoDeVersoInicio
     */
    public int getTipoDeVersoInicio() {
        return tipoDeVersoInicio;
    }

    /**
     * @param tipoDeVersoInicio the tipoDeVersoInicio to set
     */
    public void setTipoDeVersoInicio(int tipoDeVersoInicio) {
        this.tipoDeVersoInicio = tipoDeVersoInicio;
    }

    /**
     * @return the tipoDeVersoFim
     */
    public int getTipoDeVersoFim() {
        return tipoDeVersoFim;
    }

    /**
     * @param tipoDeVersoFim the tipoDeVersoFim to set
     */
    public void setTipoDeVersoFim(int tipoDeVersoFim) {
        this.tipoDeVersoFim = tipoDeVersoFim;
    }

    int vetorVersos[];

    ArrayList<SerieGraficoDistancia> serieDistancia = new ArrayList<>();

    public ArrayList<SerieGraficoDistancia> gerarVetorDeTipos() {
        serieDistancia = new ArrayList<>();
        int indice = 0;
        SerieGraficoDistancia serie;
        for (Integer chave : this.getPaginas().keySet()) {
            for (Frase frase : this.getPaginas().get(chave).getFrases()) {
                if (frase.getVerso() != null) {
                    serie = new SerieGraficoDistancia(indice, frase.getVerso().getNumeroDeVersoAntes());
                    serieDistancia.add(serie);
                }
                indice++;
            }

        }
        return serieDistancia;
    }

    private void gerarNumeracaoDeSentecas() {
        ArrayList<Frase> frases;
        int contador = 1;

        for (Integer chave : this.getPaginas().keySet()) {
            frases = this.getPaginas().get(chave).getFrases();
            for (Frase frase : frases) {
                if (frase.getVerso() != null) {
                    frase.setNumeroDaFrase(contador);

                }
                contador++;
            }
        }
    }

}
