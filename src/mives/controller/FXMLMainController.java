/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import mives.controller.helpers.MainControllerHelper;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.PageWizard;
import mives.controller.helpers.utils.Revista;

/**
 *
 * @author Ricardo
 */
public class FXMLMainController implements Initializable, PageWizard {
    
    private MainControllerHelper helper;
    
    @FXML
    protected StackPane stack;
    
    @FXML
    public Button btnFinalizar;
    
    @FXML
    public Button btnProximo;
    
    @FXML
    public Button btnVoltar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new MainControllerHelper(this);
        helper.loadNodes();
        
        btnFinalizar.setDisable(true);
        Revista.registrarAssinante(this);

        //   btnFinalizar.setDisable(true);
    }
    
    public void nextPage() {
        helper.nextPage(6);
    }
    
    @FXML
    protected void nextPage(ActionEvent e) {
        helper.nextPage();
    }
    
    @FXML
    protected void priorPage(ActionEvent e) {
        helper.priorPage();
    }
    
    public StackPane getStackPane() {
        return stack;
    }
    
    @FXML
    protected void minerarVersos(ActionEvent e) {
        helper.processarLivro();
        btnFinalizar.setDisable(true);
        btnProximo.setDisable(true);
        btnVoltar.setDisable(true);
        MainControllerHelper.controller.nextPage(e);
        
    }
    
    @Override
    public void update() {
        if (MivesWizardData.isHabilitarBotaoFinalizar()) {
            btnFinalizar.setDisable(false);
            btnProximo.setDisable(true);
        }
    
    }
    
}
