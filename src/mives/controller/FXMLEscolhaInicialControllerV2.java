/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import mives.arquivos.LivroIO;
import mives.controller.helpers.MainControllerHelper;
import mives.model.Livro;
import mives.model.MapaConfiguracao;

/**
 *
 * @author Ricardo
 */
public class FXMLEscolhaInicialControllerV2 implements Initializable {

    @FXML
    Button adicionarLivro;

    @FXML
    Button adicionarDicionario;

    @FXML
    Button analisarProcessado;

    @FXML
    Button ajudaNovoAnalise;
    
    @FXML
    Label nomeArquivo;
    
    @FXML
    Button ajudaDicionario;

    @FXML
    Button ajudaNovoLivro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void startJanela() {
        adicionarDicionario.setDisable(true);
    }

    @FXML
    public void abrirLivroProcessar(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de texto para processamento.");
        //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arquivo de texto", "txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"));
        
        System.out.println("Estou aqui");
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
            	if(file.getCanonicalPath().endsWith(".txt")) {
            		FXMLCarregarLivroController.arquivo = file;
                    nomeArquivo.setText(file.getName());
                    //MainControllerHelper.controller.nextPage(e);
                    MainControllerHelper.controller.btnProximo.setDisable(false);
                    System.out.println("Já estou aqui");
            	}else {
            		alertaEscolhaExtensao(".txt");
            	}
                
            } else {
                System.out.println("valor inválido");
            }
        } catch (IOException IOex) {
            alertaIO();
        } catch (SecurityException SecEx) {
        	alertaSeguranca();
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void abrirArquivoProcessar() {
        LivroIO livroIO;
        livroIO = new LivroIO();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir livro processado.");
        //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML", "xml"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
            	if(file.getCanonicalPath().endsWith(".xml")) {
	                Livro.getInstance().setLivro(livroIO.ler(file));
	                MapaConfiguracao.setMapaConfiguracao(Livro.getInstance().getMapaConfiguracao());
	                nomeArquivo.setText(file.getName());
	                MainControllerHelper.controller.btnProximo.setDisable(true);
	                MainControllerHelper.controller.btnVoltar.setDisable(true);
	                MainControllerHelper.controller.nextPage();
            	} else {
            		alertaEscolhaExtensao(".xml");
            	}
            } else {
                System.out.println("valor inválido");
            }
        } catch (IOException IOex) {
            alertaIO();
        } catch (SecurityException SecEx) {
        	alertaSeguranca();
        } catch (ClassCastException CastEx) {
        	alertaEscolhaXML(); //Se arquivo XML não é um livro já processado
        	System.out.println("Erro");
            //ex.printStackTrace();
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }

    }

    @FXML
    protected void carregarAjudaArqAnalise() {
        ajudaNovoAnalise.getStylesheets().add("/mives/view/css/instrucoes.css");
    }

    @FXML
    protected void carregarAjudaArqAnaliseExit() {
        ajudaNovoAnalise.getStylesheets().clear();
    }

    @FXML
    protected void carregarAjudaDicionario() {
        ajudaDicionario.getStylesheets().add("/mives/view/css/mensagemtelainicial.css");
    }

    @FXML
    protected void carregarAjudaDicionarioExit() {
        ajudaDicionario.getStylesheets().clear();
    }

    @FXML
    protected void carregarAjudaLivro() {
        ajudaNovoLivro.getStylesheets().add("/mives/view/css/instrucoes.css");
    }

    @FXML
    protected void carregarAjudaLivroExit() {
        ajudaNovoLivro.getStylesheets().clear();
    }

    @FXML
    public void addDicionario(ActionEvent e) {
//        System.out.println("oioioioioiioio");
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Adicionar termos ao dicionário");
        dialogoInfo.setContentText("Função indisponível para esta versão do MIVES!");
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
    
    public void alertaEscolhaXML() {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Abertura de arquivo incompatível");
        dialogoInfo.setContentText("Esse arquivo escolhido não é um texto já processado!");
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
    
    public void alertaEscolhaExtensao(String Extensao) {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Abertura de arquivo incompatível");
        dialogoInfo.setContentText("Esse arquivo escolhido não é "+Extensao);
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
    
    public void alertaIO() {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Erro de I/O");
        dialogoInfo.setContentText("Falha na operação de leitura do caminho do arquivo");
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
    
    public void alertaSeguranca() {
    	Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle("MIVES");
        dialogoInfo.setHeaderText("Erro de Segurança");
        dialogoInfo.setContentText("Acesso de leitura do arquivo negada");
        // dialogoInfo.
        dialogoInfo.showAndWait();
    }
}
