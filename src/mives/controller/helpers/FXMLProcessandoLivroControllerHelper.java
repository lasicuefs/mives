package mives.controller.helpers;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mives.controller.helpers.MainControllerHelper;
import javafx.concurrent.Task;
import mives.controller.FXMLMainController;
import mives.controller.FXMLProcessandoLivroController;
import mives.controller.MivesController;
import mives.controller.helpers.utils.MivesWizardData;
import mives.exceptions.LivroException;
import mives.model.Livro;

public class FXMLProcessandoLivroControllerHelper{
	
	public static FXMLProcessandoLivroController processar;
	
	public FXMLProcessandoLivroControllerHelper(FXMLProcessandoLivroController controller) {
		processar = controller;
	}
	
	public void processarLivro() {
		
		Thread t = new Thread(new Task<Void>() {
            @Override
            public Void call() throws Exception {
            	
                try {
                	Thread.sleep(500);
                    System.out.println("Processando Livro...");
                    MivesController.getInstance().minerarVersosCustomizados(Livro.getInstance(),
                            MivesWizardData.INICIOFRASE, MivesWizardData.FINALFRASE, MivesWizardData.FRASECOMPLETA,
                            MivesWizardData.TIPODEVERSOINICIO, MivesWizardData.TIPODEVERSOFINAL,
                            true, true, true, true, true);
                    updateProgress(1, 1);
                    System.out.println("Livro processado!");
                } catch (LivroException ex) {
                    Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                return null;

            }

            @Override
			protected void running() {
            	super.running();
            	processar.processamentoEmAndamento(this.progressProperty());
            }
            
            @Override
            protected void succeeded() {
                super.succeeded();
                //controller.btnSair.setDisable(false);
                processar.processamentoConcluido();;

            }

            @Override
            protected void failed() {
                super.failed();
                System.out.println("(task failed) ERRO AO PROCESSAR TEXTO!");
                processar.erroProcessamento(null);
                //controller.btnSair.setDisable(true);

            }

        });
        t.start();
	}
}
