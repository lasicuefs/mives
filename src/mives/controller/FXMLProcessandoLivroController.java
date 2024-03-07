/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import mives.controller.helpers.MainControllerHelper;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.PageWizard;
import mives.exceptions.LivroException;
import mives.model.Livro;
import mives.model.Mineracao;
import mives.observable.Observer;
import mives.controller.helpers.FXMLProcessandoLivroControllerHelper;

/**
 *
 * @author Ricardo
 */
public class FXMLProcessandoLivroController implements Initializable, PageWizard, Observer {

    @FXML
    Button btnAnalisarResultado;

    @FXML
    ProgressBar barraProcessarLivro;
    
    @FXML
    Label label;
    
    public static FXMLProcessandoLivroControllerHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAnalisarResultado.setDisable(false);
        Mineracao.getInstance().registerObserver(this);
        helper = new FXMLProcessandoLivroControllerHelper(this);
        btnAnalisarResultado.setDisable(true);
        label.setVisible(false);
    }


    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //public void testeAnalisar() {
    //    MainControllerHelper.controller.nextPage(null);
    //}

    @Override
    public void update(int progresso) {
//        float total = Livro.getInstance().getNumeroDeSegmentos();
////        System.out.println("TOTAL DE SEGMENTOS..........................: " + total);
////        System.out.println("PROGRESSO: " + progresso);
//        barraProcessarLivro.setProgress((float) ((progresso * 100.0) / total));
//        System.out.println(barraProcessarLivro.getProgress());
//        // System.out.println("Progresso:.... " + ((progresso * 100.0) / total));
//        if (((progresso * 100.0) / total) == 100.0) {
//            btnAnalisarResultado.setDisable(false);
//        }
////        System.out.println("ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO ATUALIZANDO");
    }
    @FXML
    void analisarResultados(ActionEvent event) {
    	MainControllerHelper.controller.nextPage(null);
    }
    
    public static void processarLivro() {
    	helper.processarLivro();
    }
    
    public void erroProcessamento(String msg) {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Erro ao processar o livro");
        if(msg != null) {
        	dialogoInfo.setContentText(msg);
        }
    }
    
    public void processamentoEmAndamento(ReadOnlyDoubleProperty progresso) {
    	barraProcessarLivro.progressProperty().bind(progresso);
    	btnAnalisarResultado.setDisable(true);
    	label.setText("Minerando texto ... Aguarde");
    	label.setVisible(true);
    }
    
    public void processamentoConcluido() {
    	btnAnalisarResultado.setDisable(false);
    	label.setText("Livro Processado!");
    }

}
