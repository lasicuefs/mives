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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import mives.controller.helpers.FXMLCarregarLivroControllerHelper;

/**
 *
 * @author Ricardo
 */
public class FXMLCarregarLivroController implements Initializable {

    @FXML
    ProgressBar barraCarregarLivro;

    @FXML
    private Label labelNomeArquivo;

    @FXML
    private Label labelProcessamentoConcluido;

    @FXML
    public Button btnCarregar;

    FXMLCarregarLivroControllerHelper helper;

    public static File arquivo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new FXMLCarregarLivroControllerHelper(this);
        this.labelProcessamentoConcluido.setVisible(false);
    }

    @FXML
    public void testeProgressBar(MouseEvent ev) {

    }

    @FXML
    public void iniciarCarregarLivro(ActionEvent e) {
        helper.iniciarCarregarLivro();
    }

    public ProgressBar getProgressBar() {
        return barraCarregarLivro;
    }

    /**
     * @return the labelNomeArquivo
     */
    public Label getLabelNomeArquivo() {
        return labelNomeArquivo;
    }

    /**
     * @return the labelProcessamentoConcluido
     */
    public Label getLabelProcessamentoConcluido() {
        return labelProcessamentoConcluido;
    }
}
