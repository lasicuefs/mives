/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.concurrent.Task;
import mives.controller.FXMLCarregarLivroController;
import mives.exceptions.DicionarioException;
import mives.model.Dicionario;

import mives.model.Linha;
import mives.model.Livro;
import mives.model.Pagina;

/**
 *
 * @author Ricardo
 */
public class FXMLCarregarLivroControllerHelper {

    public static FXMLCarregarLivroController carregarLivro;

    public FXMLCarregarLivroControllerHelper(FXMLCarregarLivroController carregarLivro) {
        this.carregarLivro = carregarLivro;
    }

    public void iniciarCarregarLivro() {
    	
    	Livro.getInstance().setLivro(null); //Limpo o livro toda vez que quiser carregar um novo livro

        Thread t = new Thread(new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
            	Thread.sleep(500);
            	
                Livro livro = Livro.getInstance();
                livro.setArquivoDeOrigem(FXMLCarregarLivroController.arquivo);
                
                FileWriter fileWriterDestino = null;
                BufferedWriter bufferedWriterDestino = null;
                String numeroPagina = null;
                Pagina pagina = new Pagina();
                try {
                    String dados = new String(Files.readAllBytes(livro.getArquivoDeOrigem().toPath()), StandardCharsets.UTF_8);
                    String linhasDoTexto[] = dados.split("\n");
                    int linhasProcessadas = 0;
                    boolean temNumero = false;
                    inicio:
                    for (String linha : linhasDoTexto) {
                        updateProgress(linhasProcessadas++, linhasDoTexto.length);

                        if (linha.contains("")) {
                            linha = linha.replace("", "");
                            if (linha.length() != 0) {
                                pagina = new Pagina();
                                try {
                                    pagina.setNumero(Integer.parseInt(linha.toString().trim()));
                                    livro.getPaginas().put(pagina.getNumero(), pagina);
                                    temNumero = true;
                                } catch (NumberFormatException nf) {
                                    nf.printStackTrace();
                                    System.out.println("CORRIGINDO ERRO: " + linha);

                                    pagina.setNumero(Integer.parseInt(linha.toString().trim().replaceAll("\\?", "")));
                                    livro.getPaginas().put(pagina.getNumero(), pagina);
                                    temNumero = true;
                                    System.out.println("Erro solucionado");

                                } catch (Exception ex) {
                                    ex.printStackTrace();

                                }
                                numeroPagina = linha;
                            }
                            pagina.getLinhasOriginais().add("Página: >> " + linha);
                            pagina.getLinhas().add(new Linha("Página: >> " + linha));
                            Livro.getStringLivro().append("Página: " + linha + "\n");
                            continue inicio;
                        }
                        if (!temNumero) {
                            pagina.setNumero(0);
                            livro.getPaginas().put(0, pagina);
                            temNumero = true;
                        }
                        pagina.getLinhasOriginais().add(linha);
                        pagina.getLinhas().add(new Linha(linha));
                        Livro.getStringLivro().append(linha + "\n");
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("ERRO: O arquivo não foi encontrado!");

                } catch (IOException ex) {
                    System.out.println("ERRO: Falha ao ler o arquivo!");

                }
                livro.gerarFrases();
                return true;
            }
            
            @Override
            protected void running() {
                super.running();
                carregarLivro.getLabelProcessamentoConcluido().setVisible(false);
                carregarLivro.getProgressBar().progressProperty().bind(this.progressProperty());
            }
            
            @Override
            protected void succeeded() {
                super.succeeded();
                carregarLivro.getLabelProcessamentoConcluido().setVisible(true);
            }

        }
        );
        
        t.start();
        
        System.out.println(t.getState());
        
        System.out.println(FXMLCarregarLivroController.arquivo.getName());
        carregarLivro.getLabelNomeArquivo().setText(FXMLCarregarLivroController.arquivo.getName());
        
//        while(t.isAlive()){
//             carregarLivro.getLabelProcessamentoConcluido().setVisible(false);
//        }

        File arquivoOrigem = new File(System.getProperty("user.dir") + "/src/recurso/dicionario.txt");
        if (arquivoOrigem == null) {
            System.out.println("O arquivo do dicionário não foi encontrado");
        }
        try {
            if (Dicionario.getInstance().carregarTermos(arquivoOrigem)) {
                System.out.println("Carga do dicionário realizada com sucesso.");
            }
        } catch (DicionarioException exception) {
            System.out.println(exception.getMessage());

        }

    }
}
