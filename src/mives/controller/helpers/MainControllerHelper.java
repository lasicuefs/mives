/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import mives.MIVES;
import mives.controller.FXMLMainController;
import mives.controller.MivesController;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.Revista;
import mives.exceptions.LivroException;
import mives.model.Livro;

/**
 *
 * @author Ricardo
 */
public class MainControllerHelper {

    public static FXMLMainController controller;
    private static final int UNDEFINED = -1;
    private ObservableList<Node> pages = FXCollections.observableArrayList();
    private Stack<Integer> history = new Stack<>();
    private int curPageIdx = UNDEFINED;

    public MainControllerHelper(FXMLMainController controller) {
        this.controller = controller;
    }

    public void loadNodes() {
        //   pages.add(MIVES.telaPrimeira);
        pages.add(MIVES.mainScreen);
        pages.add(MIVES.telaPrimeirav2);
        pages.add(MIVES.telaCarregarLivro);
        pages.add(MIVES.telaParametros);
        pages.add(MIVES.telaEscolhaTipoVerso);
        pages.add(MIVES.telaResumo);
        pages.add(MIVES.telaProcessandoLivro);
        pages.add(MIVES.telaAnalise);

        System.out.println("Tamanho de PAGE: " + pages.size());
        navTo(0);
    }

    public void nextPage(int i) {
        if (hasNextPage()) {

            Revista.notificar();
            navTo(curPageIdx + 6);
        }
    }

    public void nextPage() {
        if (hasNextPage()) {

            Revista.notificar();

            navTo(curPageIdx + 1);
        }
    }

    public void priorPage() {
        if (hasPriorPage()) {
            navTo(history.pop(), false);
        }
    }

    boolean hasNextPage() {
        return (curPageIdx < pages.size() - 1);
    }

    boolean hasPriorPage() {
        return !history.isEmpty();
    }

    void navTo(int nextPageIdx, boolean pushHistory) {
        if (nextPageIdx < 0 || nextPageIdx >= pages.size()) {
            return;
        }
        if (curPageIdx != UNDEFINED) {
            if (pushHistory) {
                history.push(curPageIdx);
            }
        }
        Node nextPage = pages.get(nextPageIdx);
        curPageIdx = nextPageIdx;
        controller.getStackPane().getChildren().clear();
        if (nextPage != null) {
            controller.getStackPane().getChildren().add(nextPage);
        } else {
            System.out.println("Esta nullo");
        }
        //   nextPage.manageButtons();
    }

    void navTo(int nextPageIdx) {
        navTo(nextPageIdx, true);
    }

    void navTo(String id) {
        Node page = controller.getStackPane().lookup("#" + id);
        if (page != null) {
            int nextPageIdx = pages.indexOf(page);
            if (nextPageIdx != UNDEFINED) {
                navTo(nextPageIdx);
            }
        }
    }

    public void processarLivro() {
//        try {
//            System.out.println("Processando Livro");
//            MivesController.getInstance().minerarVersosCustomizados(Livro.getInstance(),
//                    MivesWizardData.INICIOFRASE, MivesWizardData.FINALFRASE, MivesWizardData.FRASECOMPLETA,
//                    MivesWizardData.TIPODEVERSOINICIO, MivesWizardData.TIPODEVERSOFINAL,
//                    true, true, true, true, true);
//            System.out.println("Livro processado");
//        } catch (LivroException ex) {
//            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        

        task.setOnFailed(evt -> {
            System.err.println("Task failed, exception:");
            task.getException().printStackTrace(System.err);
        });

        Thread t = new Thread(task);
        t.start();
    }

    Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            try {
                System.out.println("Processando Livro...");
                MivesController.getInstance().minerarVersosCustomizados(Livro.getInstance(),
                        MivesWizardData.INICIOFRASE, MivesWizardData.FINALFRASE, MivesWizardData.FRASECOMPLETA,
                        MivesWizardData.TIPODEVERSOINICIO, MivesWizardData.TIPODEVERSOFINAL,
                        true, true, true, true, true);
                System.out.println("Livro processado!");
            } catch (LivroException ex) {
                Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        }

        @Override
        protected void succeeded() {
            super.succeeded();
            controller.btnFinalizar.setDisable(false);

        }

        @Override
        protected void failed() {
            super.failed();
            System.out.println("(task failed) ERRO AO PROCESSAR TEXTO!");
            controller.btnFinalizar.setDisable(true);

        }

    };

}
