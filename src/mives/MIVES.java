/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mives.controller.helpers.PrincipalHelper;
import mives.util.Utilitario;
import net.sf.jni4net.Bridge;

/**
 *
 * @author Ricardo
 */
public class MIVES extends Application {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        launch(args);
    }

    public static Parent telaPrimeira;
    public static Parent telaPrimeirav2;
    public static Parent telaAnalise;
    public static Parent mainScreen;
    public static Parent telaCarregarLivro;
    public static Parent telaParametros;
    public static Parent telaEscolhaTipoVerso;
    public static Parent telaResumo;

    public static Parent telaProcessandoLivro;

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainScreen = FXMLLoader.load(getClass().getResource("view/MainScreenMives.fxml"));
        telaAnalise = FXMLLoader.load(getClass().getResource("view/Principal.fxml"));
        telaPrimeira = FXMLLoader.load(getClass().getResource("view/FXMLEscolhaIncial.fxml"));
        telaPrimeirav2 = FXMLLoader.load(getClass().getResource("view/FXMLEscolhaIncialDepre.fxml"));
        telaCarregarLivro = FXMLLoader.load(getClass().getResource("view/FXMLCarregarLivro.fxml"));
        telaParametros = FXMLLoader.load(getClass().getResource("view/FXMLParametros.fxml"));
        telaEscolhaTipoVerso = FXMLLoader.load(getClass().getResource("view/FXMLEscolhaTipoVerso.fxml"));
        telaProcessandoLivro = FXMLLoader.load(getClass().getResource("view/FXMLProcessandoLivro.fxml"));
        telaResumo = FXMLLoader.load(getClass().getResource("view/FXMLResumoController.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("view/MainMivesWizard.fxml"));
        java.net.CookieHandler.setDefault(null);
        Scene scene = new Scene(root);
        
        primaryStage.getIcons().add(new Image("mives/icon.png"));

        carregarDLL();//Carrega a DLL utilizada do LapSeparator
        Utilitario.carregarSimbolos();

        primaryStage.setScene(scene);
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        primaryStage.setTitle("Mining Verse Structure - MIVES");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void closeWindowEvent(WindowEvent event) {
        PrincipalHelper.arquivoDeDestino.delete();
    }

    public void carregarDLL() {
        try {
            System.out.println("Carregando DLL....");
            Bridge.setVerbose(true);
            Bridge.init();
            File proxyAssemblyFile = new File("bibliotecas\\LapSeparatorJNI.j4n.dll");
//                File proxyAssemblyFile = new File("LapSeparatorJNI.j4n.dll");
//            File proxyAssemblyFile = new File("lib\\LapSeparatorJNI.j4n.dll");// Colocar quando for gerar o jar
            Bridge.LoadAndRegisterAssemblyFrom(proxyAssemblyFile);
            System.out.println("Carregando DLL....Pronto.");
        } catch (IOException ioEx) {
            System.out.println("Erro ao carregar a DLL");
        }
    }
}
