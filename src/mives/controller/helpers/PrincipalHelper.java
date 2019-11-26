/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import exportacao.SentencasO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mives.controller.helpers.utils.NodeTree;
import java.util.stream.Stream;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mives.controller.FXMLPrincipalController;
import mives.controller.MivesController;
import mives.model.Livro;
import mives.util.GeradorHTML;
import netscape.javascript.JSObject;
import mives.arquivos.LivroIO;
import mives.arquivos.MapaConfiguracaoIO;
import mives.controller.helpers.utils.MivesWizardData;
import mives.graficos.BarCharTonicas;
import mives.graficos.ScatterChartSample;

/**
 *
 * @author Ricardo
 */
public class PrincipalHelper {

    FXMLPrincipalController principal;

    public PrincipalHelper(FXMLPrincipalController principal) {
        this.principal = principal;
        gerarVetorDeNavegacao();
    }

    public void adicionarTitledPane() {
        TitledPane tp = new TitledPane();
        tp.setText("My Titled Pane");
        tp.setContent(new Button("Button"));
        tp.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        tp.setAlignment(Pos.CENTER);

        principal.accordionEsquerdo.getPanes().add(tp);
    }

    public static File arquivoDeDestino = null;

    public void carregarHtml() {
        MivesController.getInstance().gerarLinhasEscandidas();
        String tipoBusca = Livro.getInstance().getTipoDeBusca();

        GeradorHTML geradorHTML = new GeradorHTML();
        StringBuilder conteudo = geradorHTML.construirScript();

        geradorHTML.montarParagrafo(conteudo);

        principal.webEngine = principal.browser.getEngine();
        // Enable Javascript.
        principal.webEngine.setJavaScriptEnabled(true);
        //Para ver o que está sendo gerado
        FileWriter fileWriterDestino;
        BufferedWriter bufferedWriterDestino;

        try {
            //  File f = new File(System.getProperty("user.dir") + "/src/recurso/regraPadrao.xml");

            arquivoDeDestino = new File(System.getProperty("user.dir") + "/src/temp/" + System.currentTimeMillis() + ".html");
//            File arquivoDeDestino = new File("H:\\Barrikl\\arquivo" + System.currentTimeMillis() + ".html");
            String nome = arquivoDeDestino.getName();
            System.out.println("Nome do arquivo: " + nome);
            fileWriterDestino = new FileWriter(arquivoDeDestino); //Escrever no novo arquivo
            bufferedWriterDestino = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoDeDestino), StandardCharsets.UTF_8));
            bufferedWriterDestino.write(conteudo.toString());
            bufferedWriterDestino.close();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
            //  System.out.println("file:///" + System.getProperty("user.dir") + "/src/temp/" + nome);
            principal.webEngine.load("file:///" + System.getProperty("user.dir") + "/src/temp/" + nome);
//            principal.webEngine.load("file:///H://Barrikl//" + nome);
        } catch (IOException ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void gerarGraficoDistancia() {
        Livro.getInstance().calcularDistancias();
        ArrayList<Integer> distancias = Livro.getInstance().gerarVetorDistancia();
        ScatterChartSample graf = new ScatterChartSample(distancias);
        Stage secondStage = new Stage();
        //  secondStage.setScene(new Scene(graf));
        graf.start(secondStage);

//      Exemplo
//        Stage secondStage = new Stage();
//            secondStage.setScene(new Scene(new HBox(4, new Label("Second window"))));
//            secondStage.show();
    }

    public void gerarGraficoDistTonica() {
        BarCharTonicas bct = new BarCharTonicas();
        Stage secondStage = new Stage();
        bct.start(secondStage);
    }

    public void distanciasMetrificadas() {
        Livro.getInstance().calcularDistancias();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Distância entre versos.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            Livro.getInstance().imprimirEmArquivo(file);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dsitribuicaDeTonicas() {
        Livro.getInstance().calcularDistancias();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Ocorrências de tônicas.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.imprimeDistribuicaoDeTonicas(Livro.getInstance(), file);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void frasesDoTexto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Frases do Texto.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.salvarFrasesLivro(Livro.getInstance(), file);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mapaConfiguracao() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Mapa de Configuração.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            MapaConfiguracaoIO mapaIO = new MapaConfiguracaoIO();
            mapaIO.imprimeMapaConfiguracaoEmTxtV2(file);

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ocorrenciaPorFrase() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Ocorrência de Estruturas por frase");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.imprimeSentencasOuVersos(Livro.getInstance(), file);

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sentecasTXT() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Exportar Sentenças para arquivo de texto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.salvarSentencaLivro(Livro.getInstance(), file);

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sentencasXml() {
        SentencasO sentencasO = new SentencasO(Livro.getInstance().comporSentencas());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Exportar Sentenças para XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text XML(*.xml)", "*.xml"));
        fileChooser.setInitialFileName("*.xml");
        try {
            File file = fileChooser.showSaveDialog(null);
            sentencasO.salvarComo(file);

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sentencasETipos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Exportar Matriz de Sentenças para TXT");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.salvarVersosEncontrados(Livro.getInstance(), file);
            livroIO.salvarMatrizDeTipos(Livro.getInstance(), file, Livro.getInstance().getTipoDeVersoInicio(), Livro.getInstance().getTipoDeVersoFim());

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void versosClassificacao() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Versos encontrados no texto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");
        try {
            File file = fileChooser.showSaveDialog(null);
            LivroIO livroIO = new LivroIO();
            livroIO.salvarVersosEncontrados(Livro.getInstance(), file);

        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preencherEstatisticas() {
        principal.labelArquivo.setText(Livro.getInstance().getArquivoDeOrigem().getName());
        principal.labelTipos.setText(MivesWizardData.getLocalBusca());
        principal.labelTipos.setText(Livro.getInstance().getTiposBuscados());
        principal.labelEstruturas.setText(Livro.getInstance().getSentencas().size() + "");
    }

    class nodeArvore {

        String verso;
        String link;
        EstruturaVersificacao estrutura;

        public nodeArvore(String verso, EstruturaVersificacao estrutura, String link) {
            this.verso = verso;
            this.estrutura = estrutura;
            this.link = link;

        }
    }

    HashMap<Integer, ArrayList<nodeArvore>> estrutraArvore = new HashMap<>();

    private void gerarEstruturaArvore() {
        ArrayList<Sentenca> sentencas = Livro.getInstance().comporSentencas();
        System.out.println("TOTAL DE SENTENÇAS: " + sentencas.size());
        int link = -1;
        for (Sentenca sentenca : sentencas) {
//            link++;
            System.out.println("Sentença: " + sentenca.getSegmento());
            String verso = sentenca.getEstruturaDeVesificacao().get(0).getPalavrasVerso();
            link = sentenca.getLink();
            System.out.println("Valor de link: " + link);
            for (EstruturaVersificacao estruturaVersificacao : sentenca.getEstruturaDeVesificacao()) {
                if (!(estrutraArvore.containsKey(estruturaVersificacao.getNumeroDeSilabas()))) {
                    ArrayList<nodeArvore> nodes = new ArrayList<>();
                    nodes.add(new nodeArvore(verso, estruturaVersificacao, "" + link));
                    estrutraArvore.put(estruturaVersificacao.getNumeroDeSilabas(), nodes);
                } else {
                    nodeArvore node = new nodeArvore(verso, estruturaVersificacao, "" + link);
                    estrutraArvore.get(estruturaVersificacao.getNumeroDeSilabas()).add(node);
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

    public void gerarArvores() {
        gerarEstruturaArvore();
        carregarMetro();
        int tipos[] = new int[estrutraArvore.size()];
        int k = 0;
        for (Integer key : estrutraArvore.keySet()) {
            tipos[k] = key;
            k++;
        }
        ordenarVetor(tipos);
        System.out.println("Tamanho do vetor de tipos: " + tipos.length);

        for (int i : tipos) {
            System.out.println("TIPO ENVIADO: " + i);
            System.out.println("Quantidade de enviados: " + estrutraArvore.get(i).size());
            TreeItem<NodeTree> rootItem = montarArvoreV2(estrutraArvore.get(i));
            adicionarTitledPane(rootItem, i);
        }

    }

    public void adicionarTitledPane(TreeItem<NodeTree> rootItem, int tipo) {
        TitledPane tp = new TitledPane();
        tp.setText(metro.get(tipo).toUpperCase());
        TreeView<NodeTree> arvore = new TreeView<>();
        arvore.getStylesheets().add("/mives/view/css/treeviewestilo.css");

        arvore.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> {
                    try {
                        sendHtmlToWebView(n.getValue().getValor());
                        System.out.println("n: " + n.getValue().getValor());
                    } catch (Exception exe) {

                    }
                });

        arvore.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        arvore.setRoot(rootItem);
        tp.setContent(arvore);
        tp.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        tp.setAlignment(Pos.CENTER);

        principal.accordionEsquerdo.getPanes().add(tp);
    }

    HashMap<Integer, String> metro = new HashMap<>();

    private void carregarMetro() {
        metro.put(1, "Monossílabo");
        metro.put(2, "Dissílabo");
        metro.put(3, "Trissílabo");
        metro.put(4, "Tetrassílabo");
        metro.put(5, "Pentassílabo");
        metro.put(6, "Hexassílabo");
        metro.put(7, "Heptassílabo");
        metro.put(8, "Octossílabo");
        metro.put(9, "Eneassílabo");
        metro.put(10, "Decassílabo");
        metro.put(11, "Hendecassílabo");
        metro.put(12, "Dodecassílabo");
    }

    public TreeItem<NodeTree> montarArvoreV2(ArrayList<nodeArvore> nodes) {

        //gerarEstruturaArvore();
        TreeItem<NodeTree> rootItem = new TreeItem<NodeTree>(new NodeTree("Estruturas de Versificação", ""));
        rootItem.setExpanded(true);

        TreeItem<NodeTree> verso;
        TreeItem<NodeTree> versoEscandido;
        TreeItem<NodeTree> tonicas;
        TreeItem<NodeTree> classificacao;

        System.out.println("TAMANHO DE NODES: " + nodes.size());

        for (nodeArvore node : nodes) {
            verso = new TreeItem<NodeTree>(new NodeTree(node.verso, node.link));
            versoEscandido = new TreeItem<NodeTree>(new NodeTree(node.estrutura.getSentecaEscandida(), node.link));
            verso.getChildren().add(versoEscandido);
            tonicas = new TreeItem<NodeTree>(new NodeTree(node.estrutura.getPosicaoDasTonicas(), node.link));
            verso.getChildren().add(tonicas);
            classificacao = new TreeItem<NodeTree>(new NodeTree(metro.get(node.estrutura.getNumeroDeSilabas()), node.link));
            verso.getChildren().add(classificacao);
            rootItem.getChildren().add(verso);
        }

        return rootItem;
    }

    public void montarArvore() {

    }

    int numeroMaximoDeSegmentos = 0;

    public void gerarVetorDeNavegacao() {
        numeroMaximoDeSegmentos = Livro.getInstance().getNumeroDeSegmentos();
        System.out.println("Número de Segmentos gerados no vetor: " + numeroMaximoDeSegmentos);
    }

    public void avancar(int id) {
        if (id < numeroMaximoDeSegmentos) {
            sendHtmlToWebView(id + "");
        } else {
            sendHtmlToWebView("0");
        }
    }

    public void voltar(int id) {
        if (id == 0) {
            sendHtmlToWebView("0");
        } else {
            sendHtmlToWebView(id + "");
        }
    }

    public void sendHtmlToWebView(String jumpToHtmlId) {
        if (principal.webEngine != null) {
            JSObject jSObject = (JSObject) principal.webEngine.executeScript("window");
            Object retorno = jSObject.call("jumpToHtmlId", jumpToHtmlId);
            System.out.println("Retorno: " + retorno.toString());
        }
    }

    public void carregarAncoras() {
        // Para cada site no array adicionamos na lista visual
        Stream.of(principal.sites).forEach(principal.listaSites.getItems()::add);

        // Quando o usuário seleciona um item, carregamos a página
        principal.listaSites.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> {
                    sendHtmlToWebView(n.toString());
                    try {
                        FXMLPrincipalController.idAtual = Integer.parseInt(n.toString());
                    } catch (Exception en) {

                    }
                    System.out.println("n: " + n.toString());
                });

    }

    public void carregarGraficosOcorrencias() {

        principal.graficoBarras.setTitle("   ");

        int tipos[] = new int[estrutraArvore.size()];
        int k = 0;
        for (Integer key : estrutraArvore.keySet()) {
            tipos[k] = key;
            k++;
        }
        ordenarVetor(tipos);
        System.out.println("IMPRIMINDO TIPOS ORDENADOS...............................................");
        for (int j = 0; j < tipos.length; j++) {
            System.out.println(tipos[j]);
        }
        System.out.println("FIM - IMPRIMINDO TIPOS ORDENADOS...............................................");
        principal.graficoBarras.setBarGap(7.5);

        principal.graficoBarras.setLegendSide(Side.BOTTOM);

        principal.graficoBarras.setPrefSize(350, 600);

        XYChart.Series series1;

        for (int j = 0; j < tipos.length; j++) {
//            System.out.println(tipos[j]);
            series1 = new XYChart.Series();
            series1.setName(metro.get(tipos[j]));
            series1.getData().add(new XYChart.Data("", new Float(estrutraArvore.get(tipos[j]).size())));
            principal.graficoBarras.getData().add(series1);
        }

//        for (Integer key : estrutraArvore.keySet()) {
//            series1 = new XYChart.Series();
//            series1.setName(metro.get(key));
//            series1.getData().add(new XYChart.Data("", new Float(estrutraArvore.get(key).size())));
//            principal.graficoBarras.getData().add(series1);
//        }
    }

    String texto = null;

    public void buscarNoTexto() {
        texto = principal.searchtext.getText();

        if (principal.searchtext.getText().length() == 0) {
            removeHighlight();
        } else {
            highlight(texto.trim());
        }
    }

    private void highlight(String text) {
        try {
            principal.webEngine.executeScript("$('body').removeHighlight().highlight('" + text + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeHighlight() {
        principal.webEngine.executeScript("$('body').removeHighlight()");
    }

    public void carregarTextoExemplo() {
        principal.webEngine = principal.browser.getEngine();
        // Enable Javascript.
        principal.webEngine.setJavaScriptEnabled(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de texto para processamento.");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arquivos html", "html"));
        //URL url = getClass().getResource(fileChooser.showOpenDialog(null).getPath());

        try {
            File file = new File(fileChooser.showOpenDialog(null).getPath());
            URL url = file.toURL();
            if (url != null) {
                System.out.println("File: " + url.toString());
                principal.webEngine.load(url.toString());
            } else {
                System.out.println("valor invãlido");
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void listarVersosEClassificacoes() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de texto para processamento.");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arquivos html", "html"));

        try {
            File file = new File(fileChooser.showOpenDialog(null).getPath());
            LivroIO livroIO = new LivroIO();
            livroIO.salvarVersosEncontrados(Livro.getInstance(), file);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void imprimirEmArquivo(String titulo, String tipo) {

    }

}
