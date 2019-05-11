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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import mives.controller.helpers.MainControllerHelper;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.PageWizard;
import mives.exceptions.LivroException;
import mives.model.Livro;
import mives.model.Mineracao;
import mives.observable.Observer;

/**
 *
 * @author Ricardo
 */
public class FXMLProcessandoLivroController implements Initializable, PageWizard, Observer {

    @FXML
    Button btnAnalisarResultado;

    @FXML
    ProgressBar barraProcessarLivro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAnalisarResultado.setDisable(false);
        Mineracao.getInstance().registerObserver(this);
    }

    private void minerarLivro() {

        Thread t = new Thread(task);
        t.start();
    }

    Task task = new Task<Void>() {
        @Override
        public Void call() {
            try {
                MivesController.getInstance().minerarVersosCustomizados(Livro.getInstance(), MivesWizardData.INICIOFRASE, MivesWizardData.FINALFRASE, MivesWizardData.FRASECOMPLETA,
                        MivesWizardData.TIPODEVERSOINICIO, MivesWizardData.TIPODEVERSOFINAL,
                        true, true, true, true, true);
                btnAnalisarResultado.setDisable(false);
                updateProgress(Mineracao.valorInicial, Livro.getInstance().getPaginas().size());
            } catch (LivroException ex) {
                Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;

        }

        @Override
        protected void succeeded() {
            super.succeeded();
            btnAnalisarResultado.setDisable(false);

        }

    };

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void testeAnalisar() {
        MainControllerHelper.controller.nextPage(null);
        
    }

    @Override
    public void update(int progresso) {
        float total = Livro.getInstance().getNumeroDeSegmentos();


        barraProcessarLivro.setProgress((float) ((progresso * 100.0) / total));
   
        if (((progresso * 100.0) / total) == 100.0) {
            btnAnalisarResultado.setDisable(false);
        }

    }

}
