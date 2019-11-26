/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import mives.arquivos.LivroIO;
import mives.controller.helpers.MainControllerHelper;
import mives.model.Livro;
import mives.model.MapaConfiguracao;

/**
 *
 * @author Ricardo
 */
public class FXMLEscolhaInicialControllerV2 implements Initializable {

    @FXML
    Button adicionarLivro;

    @FXML
    Button adicionarDicionario;

    @FXML
    Button analisarProcessado;

    @FXML
    Button ajudaNovoAnalise;

    @FXML
    Button ajudaDicionario;

    @FXML
    Button ajudaNovoLivro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void startJanela() {
        adicionarDicionario.setDisable(true);
    }

    @FXML
    public void abrirLivroProcessar(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de texto para processamento.");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arquivo de texto", "txt"));

        System.out.println("Estou aqui");
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                FXMLCarregarLivroController.arquivo = file;
                MainControllerHelper.controller.nextPage(e);
                System.out.println("Já estou aqui");
            } else {
                System.out.println("valor invãlido");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void abrirArquivoProcessar() {
        LivroIO livroIO;
        livroIO = new LivroIO();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir livro processado.");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML", "xml"));

        try {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                Livro.getInstance().setLivro(livroIO.ler(file));
                MapaConfiguracao.setMapaConfiguracao(Livro.getInstance().getMapaConfiguracao());
                MainControllerHelper.controller.nextPage();
            } else {
                System.out.println("valor invãlido");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    protected void carregarAjudaArqAnalise() {
        ajudaNovoAnalise.getStylesheets().add("/mives/view/css/mensagemtelainicial.css");
    }

    @FXML
    protected void carregarAjudaArqAnaliseExit() {
        ajudaNovoAnalise.getStylesheets().clear();
    }

    @FXML
    protected void carregarAjudaDicionario() {
        ajudaDicionario.getStylesheets().add("/mives/view/css/mensagemtelainicial.css");
    }

    @FXML
    protected void carregarAjudaDicionarioExit() {
        ajudaDicionario.getStylesheets().clear();
    }

    @FXML
    protected void carregarAjudaLivro() {
        ajudaNovoLivro.getStylesheets().add("/mives/view/css/mensagemtelainicial.css");
    }

    @FXML
    protected void carregarAjudaLivroExit() {
        ajudaNovoLivro.getStylesheets().clear();
    }

    @FXML
    public void addDicionario(ActionEvent e) {
        System.out.println("oioioioioiioio");
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Adicionar termos ao dicionário");
        dialogoInfo.setContentText("Função indisponível para esta versão do MIVES!");
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
}
