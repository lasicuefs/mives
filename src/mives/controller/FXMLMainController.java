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
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import mives.controller.helpers.FXMLCarregarLivroControllerHelper;
import mives.controller.FXMLCarregarLivroController;
import mives.controller.helpers.MainControllerHelper;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.PageWizard;
import mives.controller.helpers.utils.Revista;
import mives.model.MapaConfiguracao;

/**
 *
 * @author Ricardo
 */
public class FXMLMainController implements Initializable, PageWizard {
    
    private MainControllerHelper helper;
    
    private FXMLCarregarLivroControllerHelper carregarLivroHelper;
    
    private FXMLCarregarLivroController carregarLivro;
    
    @FXML
    protected StackPane stack;
    
    @FXML
    public Button btnSair;
    
    @FXML
    public Button btnProximo;
    
    @FXML
    public Button btnVoltar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new MainControllerHelper(this);
        helper.loadNodes();
        
        btnVoltar.setDisable(true);
        btnVoltar.setOpacity(0);
        Revista.registrarAssinante(this);

        //   btnFinalizar.setDisable(true);
    }
    
    public void nextPage() {
        helper.nextPage(6);
    }
    
    @FXML
    protected void nextPage(ActionEvent e) {
    	
    	//Se for a primeura página e eu apertei Avançar
    	if(helper.getCurPageIdx() == 0) {
    		btnVoltar.setDisable(false); //botão de voltar abilitado
            btnVoltar.setOpacity(1); //botão de voltar aparece na tela
            if(FXMLCarregarLivroController.arquivo != null) { //se não tiver arquivo, desabilita Avançar
    			btnProximo.setDisable(false);
    		}else { //caso contrário
    			btnProximo.setDisable(true);
    		}
    	}
    	
    	// Se a página for 1 e apertei avançar, carrega o livro selecionado
    	if(helper.getCurPageIdx() == 1) { 
    		FXMLCarregarLivroController.helper.iniciarCarregarLivro();
    	}
    	
    	//Se estou na página 2 e avanço e não tem parâmetros definidos, botão de avançar desabilitado
    	if(helper.getCurPageIdx() == 2 && MapaConfiguracao.getMapaConfiguracao()==null) {
    		System.out.println("Vazio");
    		btnProximo.setDisable(true);
    	//Se estou na página 2 e avanço e tem parâmetros definidos, botão de avançar abilitado
    	}else if(helper.getCurPageIdx() == 2 && MapaConfiguracao.getMapaConfiguracao()!=null) {
    		System.out.println("Não Vazio");
    		btnProximo.setDisable(false);
    	}
    	
    	/*
    	if(helper.getCurPageIdx() == 3) {
    		if(MivesWizardData.isHabilitarBotaoFinalizar()) {
    			btnProximo.setDisable(false); //botão de voltar abilitado
                System.out.println("com métrica");
    		}else {
    			System.out.println("sem métrica");
    			btnProximo.setDisable(true); //botão de voltar desabilitado
    		}
    	}*/
        helper.nextPage();
    }
    
    @FXML
    protected void priorPage(ActionEvent e) {
    	if(helper.getCurPageIdx() == 1) {
    		btnVoltar.setDisable(true);
            btnVoltar.setOpacity(0);
            btnProximo.setDisable(false);
    	}
    	
    	if(helper.getCurPageIdx() == 3) {
    		btnProximo.setDisable(false);
    	}
    
    	if(helper.getCurPageIdx() == 4 && MapaConfiguracao.getMapaConfiguracao()==null) {
    		System.out.println("Vazio");
    		btnProximo.setDisable(true);
    	}else if(helper.getCurPageIdx() == 4 && MapaConfiguracao.getMapaConfiguracao()!=null) {
    		System.out.println("Não Vazio");
    		btnProximo.setDisable(false);
    	}
    	
        helper.priorPage();
    }
    
    public StackPane getStackPane() {
        return stack;
    }
    
    //Ao apertar o botão de SAIR, fecha a aplicação//
    @FXML
    protected void fecharAplicacao(ActionEvent e) {
    	Stage stage = (Stage) btnSair.getScene().getWindow(); //Obtendo a janela atual//
        stage.close();
    }
    
    @FXML
    protected void minerarVersos(ActionEvent e) {
        helper.processarLivro();
        btnSair.setDisable(true);
        btnProximo.setDisable(true);
        btnVoltar.setDisable(true);
        MainControllerHelper.controller.nextPage(e);
        
    }
    
    @Override
    public void update() {
        if (MivesWizardData.isHabilitarBotaoFinalizar()) {
        	btnSair.setDisable(false);
        }
    }
    
}
