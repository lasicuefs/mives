/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.PageWizard;
import mives.controller.helpers.utils.Revista;
import mives.model.Livro;

/**
 *
 * @author Ricardo
 */
public class FXMLResumoController implements Initializable, PageWizard {
    
    @FXML
    public Label labelNomeArquivo;
    
    @FXML
    Label labelBuscaEm;
    
    @FXML
    Label labelMetros;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Revista.registrarAssinante(this);
    }
    
    @Override
    public void update() {
        if (Livro.getInstance().getPaginas().size() > 0) {
            labelNomeArquivo.setText("Nome do arquivo: " + Livro.getInstance().getArquivoDeOrigem().getName());
        }
        if (MivesWizardData.FINALFRASE || MivesWizardData.FRASECOMPLETA || MivesWizardData.INICIOFRASE) {
            labelBuscaEm.setText("Buscar em: " + MivesWizardData.getLocalBusca());
            Livro.getInstance().setLocalBusca(MivesWizardData.getLocalBusca());
        }
        if (MivesWizardData.TIPODEVERSOINICIO != null
                && MivesWizardData.TIPODEVERSOINICIO.length() > 0
                && MivesWizardData.TIPODEVERSOFINAL != null
                && MivesWizardData.TIPODEVERSOFINAL.length() > 0) {
            Livro.getInstance().setTiposBuscados(MivesWizardData.TIPODEVERSOINICIO
                    + " a " + MivesWizardData.TIPODEVERSOFINAL);
            labelMetros.setText("Metros a serem buscados: " + MivesWizardData.TIPODEVERSOINICIO
                    + " a " + MivesWizardData.TIPODEVERSOFINAL);
        }
        
    }
    
}
