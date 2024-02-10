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
import java.util.Observable;

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
public class FXMLCarregarLivroControllerHelper extends Observable{

	public static FXMLCarregarLivroController carregarLivro;

	public FXMLCarregarLivroControllerHelper(FXMLCarregarLivroController carregarLivro) {
		this.carregarLivro = carregarLivro;
	}

	public void iniciarCarregarLivro() {

		//Primeiro pega o Objeto único Livro e coloca ele como Null
		Livro.getInstance().setLivro(null); // Limpo o livro toda vez que quiser
		// carregar um novo livro

		Thread t = new Thread(new Task<Boolean>() {

			@Override
			public Boolean call() throws Exception {

				Thread.sleep(500);

				//Olhar atributos getInstance e arquivo se retornam nulo
				Livro livro = Livro.getInstance(); //Inicialmente livro é null
				livro.setArquivoDeOrigem(FXMLCarregarLivroController.arquivo); //O arquivo não pode ser null, pq só chega nessa etapa se o usuário escolher um livro
				
				//Código teste de erro
				try {
					int i = 10/0;
				}catch(ArithmeticException ex) {
					//throw ex;
				}
				
				FileWriter fileWriterDestino = null;
				BufferedWriter bufferedWriterDestino = null;
				String numeroPagina = null;
				Pagina pagina = new Pagina();
				try {
					String dados = new String(Files.readAllBytes(livro.getArquivoDeOrigem().toPath()),
							StandardCharsets.UTF_8);
					String linhasDoTexto[] = dados.split("\n");
					int linhasProcessadas = 0;
					boolean temNumero = false;
					inicio: for (String linha : linhasDoTexto) {
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
									throw ex;

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
					throw ex;

				} catch (IOException ex) {
					System.out.println("ERRO: Falha ao ler o arquivo!");
					throw ex;
				}
				livro.gerarFrases();
				return true;
			}

			@Override
			protected void running() {
				super.running();
				carregarLivro.livroEmCarregamento(this.progressProperty());
				//carregarLivro.getLabelProcessamentoConcluido().setVisible(false);
				//carregarLivro.getProgressBar().progressProperty().bind(this.progressProperty());
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				carregarLivro.livroCarregadoComSucesso();
				//carregarLivro.getLabelProcessamentoConcluido().setVisible(true);
				setChanged();
		        notifyObservers(false); //Habilita o avançar caso o carregamento seja bem sucedido
			}

			@Override
			protected void failed() {
				super.failed();
				//Throwable exception = getException();
				carregarLivro.livroComErroDeCarregamento(null);
				System.out.println("(task failed) ERRO AO CARREGAR ARQUIVO!");
				setChanged();
		        notifyObservers(true); //Desabilita o avançar caso o carregamento seja bem sucedido
				// controller.btnSair.setDisable(true);

			}

		});

		t.start();

		System.out.println(t.getState());

		System.out.println(FXMLCarregarLivroController.arquivo.getName());
		carregarLivro.getLabelNomeArquivo().setText(FXMLCarregarLivroController.arquivo.getName());

		// while(t.isAlive()){ //
		carregarLivro.getLabelProcessamentoConcluido().setVisible(false); // }

		//TO-DO: fazer tratamento usando try catch
		try {
			File arquivoOrigem = new File(System.getProperty("user.dir") + "/src/recurso/dicionario.txt");
			if (Dicionario.getInstance().carregarTermos(arquivoOrigem)) {
				System.out.println("Carga do dicionário realizada com sucesso.");
			}
		}catch(DicionarioException ex) {
			carregarLivro.livroComErroDeCarregamento("Erro ao carregar dicionário");
			System.out.println(ex.getMessage());
		}catch(NullPointerException ex){
			carregarLivro.livroComErroDeCarregamento("O arquivo do dicionário não foi encontrado no caminho: "+System.getProperty("user.dir") + "/src/recurso/dicionario.txt");
			System.out.println("O arquivo do dicionário não foi encontrado");
		}
		
		 
	}
}
