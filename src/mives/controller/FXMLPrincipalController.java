/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import mives.controller.helpers.PrincipalHelper;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import mives.arquivos.LivroIO;
import mives.controller.helpers.utils.NodeTree;
import mives.model.Livro;
import mives.model.MapaConfiguracao;
import mives.util.ErroContagem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author Ricardo
 */
public class FXMLPrincipalController implements Initializable {

    @FXML
    public WebView browser;

    public WebEngine webEngine;

    @FXML
    public Button botaoAnterior;

    @FXML
    public Accordion accordionEsquerdo;

    @FXML
    public Accordion accordionDireito;

    @FXML
    public TextField searchtext;

    @FXML
    public ProgressIndicator carregando;

    @FXML
    public BarChart graficoBarras;

    @FXML
    public ListView<String> listaSites;

    @FXML
    public TreeView<NodeTree> decassilabosTree;

    @FXML
    Button botonResult;

    @FXML
    MenuBar menuBar;

    @FXML
    MenuItem menuSalvar;

    private PrincipalHelper helper;

    @FXML
    public Label labelArquivo;

    @FXML
    public Label labelNumeroFrases;

    @FXML
    public Label labelEstruturas;

    @FXML
    public Label labelMetros;

    @FXML
    public Label labelTipos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.setDisable(true);
        searchtext.setPromptText("Entre com o texto para realçar");
        carregando.setVisible(false);
        helper = new PrincipalHelper(this);

        exibirMargens();
//        helper.carregarGraficosOcorrencias();
        helper.carregarAncoras();
        accordionEsquerdo.getPanes().clear();
        helper.montarArvore();

        // carregarTextoExemplo();
    }

    int index = 0;

    public void testeLink() {
        helper.sendHtmlToWebView("Headline2");
    }

    public final String[] sites = {
        "HeadlineFim",
        "Headline2",
        "aprendendo-javase.blogspot.com de um texto um pouco maior do que aquele que estava antes",
        "aprendendo-jboss.blogspot.com",
        "jugvale.com",
        "javafx.com",
        "java.com",
        "google.com",
        "aprendendo-jboss.blogspot.com",
        "jugvale.com",
        "javafx.com",
        "java.com",
        "google.com"
    };

    public void irParaFim() {
        helper.sendHtmlToWebView("20");
    }

    public void listarVersosEClassificacoes() {
        helper.listarVersosEClassificacoes();
    }

    public void carregarArquivoExemplo() {
        webEngine = browser.getEngine();
        // Enable Javascript.
        webEngine.setJavaScriptEnabled(true);
        webEngine.load("file:///C://Users//Ricardo//Documents//arquivo.html");
    }

    //Remover - Utilizado apenas para teste
    public void proximo() {
        idAtual++;
        helper.avancar(idAtual);
//        System.out.println(" testeClick2()");
//        helper.sendHtmlToWebView("20");
    }

    public void anterior() {
        idAtual--;
        helper.voltar(idAtual);

//        System.out.println("TesteClick");
//        helper.sendHtmlToWebView("1");
    }

    // @FXML
    public void exibirMargens() {

//        areaDeTexto.setPadding(new Insets(30, 30, 0, 30));
//        areaDeTexto.setWrapText(true);2603042@15fev
    }
    String texto = "";

    public void buscarNoTexto() {//Realizar modificação aqui
        helper.buscarNoTexto();
    }

    public void carregarTextoExemplo() {
        // helper.carregarTextoExemplo();

        helper.carregarHtml();
        helper.gerarArvores();
        helper.carregarGraficosOcorrencias();

        //  webEngine.load("file:///C://Users//Ricardo//Documents//AMargem.html");
    }

    public void gerarGraficoDistancia() {
        helper.gerarGraficoDistancia();
    }

    public void gerarGraficoDistTonica() {
        helper.gerarGraficoDistTonica();
    }

    public void distanciasMetrificadas() {
        helper.distanciasMetrificadas();
    }

    public void exportarParaHTML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Exporta resumo em HTML...");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("HTML", "html"));
        try {
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                helper.exportarHtml(file);

            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dsitribuicaDeTonicas() {
        helper.dsitribuicaDeTonicas();
    }

    public void frasesDoTexto() {
        helper.frasesDoTexto();
    }

    public void mapaConfiguracao() {
        helper.mapaConfiguracao();
    }

    public void ocorrenciaPorFrase() {
        helper.ocorrenciaPorFrase();
    }

    public void sentecasTXT() {
        helper.sentecasTXT();
    }

    public void sentencasXml() {
        helper.sentencasXml();
    }

    /**
     * Incremento em 2020. Método responsável por gerar um XML com informações
     * tais como: O número da sentença que foi escandida.
     */
    public void sentencasXmlGeral() {
        helper.sentencasXmlGeral();
    }

    public void sentencasETipos() {
        helper.sentencasETipos();
    }

    public void versosClassificacao() {
        helper.versosClassificacao();
    }

    public void apresentarResultados() {
        helper.carregarHtml();
        helper.gerarArvores();
        helper.carregarGraficosOcorrencias();
        botonResult.setVisible(false);
        menuBar.setDisable(false);
        testarGerarEvento();
        helper.gerarVetorDeNavegacao();
        helper.preencherEstatisticas();
        ErroContagem.imprimirRelatorio();

    }

    public static int idAtual = 0;

    public void testarGerarEvento() {
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    // note next classes are from org.w3c.dom domain
                    EventListener listener = new EventListener() {

                        @Override
                        public void handleEvent(org.w3c.dom.events.Event evt) {
                            String ident = ((Element) evt.getTarget()).getAttribute("id");
                            idAtual = Integer.parseInt(ident);
//                            System.out.println("id: " + ident);
                        }
                    };

                    Document doc = webEngine.getDocument();
                    Element el = doc.getElementById("a");
                    NodeList lista = doc.getElementsByTagName("a");
                    for (int i = 0; i < lista.getLength(); i++) {
                        ((EventTarget) lista.item(i)).addEventListener("click", listener, false);
                    }
                }
            }
        });
    }

    public void salvar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Salvar arquivo de configuração...");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML", "xml"));
        try {
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                LivroIO livroIO = new LivroIO();
                Livro.getInstance().setMapaConfiguracao(MapaConfiguracao.getInstacia());
                livroIO.salvarComo(Livro.getInstance(), file);

            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        JFileChooser salvar = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "XML", "xml");
//        salvar.setDialogType(JFileChooser.SAVE_DIALOG);
//        salvar.setDialogTitle("Salvar arquivo Processado");
//        salvar.setFileFilter(filter);
//        int resultado = salvar.showSaveDialog(this);
//        if (resultado != JFileChooser.APPROVE_OPTION) {
//            return;
//        } else {
//            File caminho = salvar.getSelectedFile();
//            LivroIO livroIO = new LivroIO();
//            livro.setMapaConfiguracao(MapaConfiguracao.getInstacia());
//            livroIO.salvarComo(livro, caminho);
//        }
    }

//    public void apagarArquivo() {
//        helper.apagarArquivo();
//    }
}
