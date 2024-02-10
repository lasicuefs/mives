/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

    public static FXMLCarregarLivroControllerHelper helper;

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
    public void iniciarCarregarLivro() {
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
    
    public void livroEmCarregamento(ReadOnlyDoubleProperty progresso) {
    	this.getLabelProcessamentoConcluido().setVisible(false);
    	this.getProgressBar().progressProperty().bind(progresso);
    }
    
    public void livroCarregadoComSucesso() {
    	this.getLabelProcessamentoConcluido().setVisible(true);
    	
    }
    
    public void livroComErroDeCarregamento(String msg) {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Erro ao carregar o livro");
        if(msg != null) {
        	dialogoInfo.setContentText(msg);
        }
        
        // dialogoInfo.
        dialogoInfo.showAndWait();
    	this.labelProcessamentoConcluido.setText("Erro ao carregar o livro");
    	this.labelProcessamentoConcluido.setVisible(true);
    }
}
