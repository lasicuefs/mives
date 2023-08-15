/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mives.controller.helpers.MainControllerHelper;
import mives.observable.Observer;

/**
 *
 * @author Ricardo
 */
public class FXMLParametrosController implements Initializable, Observer {

    @FXML
    Button regraPadrao;

    @FXML
    Button definirParametros;

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

    public void abrirLivroProcessar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de texto para processamento.");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arquivo de texto", "txt"));
        File file = fileChooser.showOpenDialog(null);
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
    protected void carregarRegrasSistema(ActionEvent e) {
        MivesController.getInstance().carregarRegraPadrao();
        definirParametros.setDisable(false);
        regraPadrao.getStylesheets().add("/mives/view/css/buttonutilizarparametro2.css");
        definirParametros.getStylesheets().remove("/mives/view/css/buttondefinirparametros2.css");
        regraPadrao.setDisable(true);
        MainControllerHelper.controller.btnProximo.setDisable(false);
    }

    public void abrirJanelaConfiguracao(ActionEvent event) {

        try {
        	regraPadrao.setDisable(false);
        	definirParametros.setDisable(true);
        	definirParametros.getStylesheets().add("/mives/view/css/buttondefinirparametros2.css");
            regraPadrao.getStylesheets().remove("/mives/view/css/buttonutilizarparametro2.css");
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    FXMLParametrosController.class.getResource("/mives/view/FXMLDefinirParametros.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("My modal window");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
            MainControllerHelper.controller.btnProximo.setDisable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void update(int progresso) {
        regraPadrao.setDisable(true);
        definirParametros.getStylesheets().add("/mives/view/css/mensagemtelainicial.css");
    }

}
