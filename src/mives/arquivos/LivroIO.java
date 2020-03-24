/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.arquivos;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mives.model.Frase;
import mives.model.Livro;
import mives.model.Pagina;
import mives.model.Verso;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo
 */
public class LivroIO {

    Livro livro;

    public void salvar(Livro livro, File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            String string_livro_EmXML = xstream.toXML(livro);// Passando os dados do JPanel Java para XML e salva em uma String
            File xmlMap = caminho;
            OutputStream streamOut = null;
            if (!(xmlMap.exists())) {
                xmlMap.createNewFile();
            } else {
                xmlMap.delete();
                xmlMap.createNewFile();
            }
            streamOut = new FileOutputStream(xmlMap);
            //XStream xstreamr = new XStream(new DomDriver());
            xstream.toXML(livro, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public void salvarVersosEncontrados(Livro livro, File caminho) {
        List<Verso> versos = livro.getVersosLivro();

        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            int contador = 1;
            for (Verso v : versos) {

                bufferedWriterDestino.write(v.getPalavras() + "¢" + v.getVersoEscandido() + "¢" + v.getClassificacao() + "¢" + v.getSubCategoria());
                bufferedWriterDestino.newLine();
                contador++;
            }
            bufferedWriterDestino.write("Número de Versos Encontrados: " + (contador - 1));
            bufferedWriterDestino.flush();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }

    }

    /**
     * O código abaixo é uma cópia do métrod salvarMatrizDeTipos Ricardo, você
     * deve refatorar o código assim que puder
     */
    public void gerarDadosComJanela(Livro livro, File caminho, int tamanhoDaJanela, int passo) {
        ArrayList<Integer> sentecas = new ArrayList<Integer>();
        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            PrintWriter printWriter = new PrintWriter(fileWriterDestino);//Escrever no novo arquivo

//            for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
//                printWriter.print(i + "\t");
//            }
//            printWriter.print("\n");
            int quantidadeAssociada = 0;
            for (Integer numeroPagina : livro.getPaginas().keySet()) {
                Pagina pagina = livro.getPaginas().get(numeroPagina);
                for (Frase frase : pagina.getFrases()) {
                    if (frase.existeVerso()) {
                        sentecas.add(1);//Tem 
                        quantidadeAssociada++;
                    } else {
                        sentecas.add(0);//Não tem
                    }
                }
            }
//            int tamanhoDaJanela = sentecas.size() / quantidadeDeJanelas;
//            int tamanhoDaJanela = 200;
//            int passo = (int) (tamanhoDaJanela * 0.2);
//            int passo = 20;
            int inicioDaJanela = 0;
            int fimDaJanela = tamanhoDaJanela;
            //int passo = 0;
            int frequencia = 0;
            int i = 0;
//            printWriter.print("Tamanho da Janela;" + tamanhoDaJanela + "\n");
//            printWriter.print("Tamanho do passo;" + passo + "\n");
//            printWriter.print("Quantidade de Sentenças; " + sentecas.size() + "\n");
//            printWriter.print("Total de Sentenças com estrutura de versificação;" + quantidadeAssociada + "\n");
//
//            printWriter.print("Janela;tFreq. Ab;tFreq. Rel Jan;Freq. Rel Total;Freq. Rel Total versos\n");
            printWriter.print("Janela;Freq. Ab\n");
            boolean continuar = true;
//            while (i < sentecas.size()) {
            int p = 0;
            while (continuar) {
                for (p = inicioDaJanela; p < fimDaJanela && p < sentecas.size(); p++) {
                    if (sentecas.get(p) == 1) {
                        frequencia++;
                    }
                }
                //   i = fimDaJanela;
//                printWriter.print(inicioDaJanela + " - " + fimDaJanela + ";" + frequencia + ";" + ((frequencia / tamanhoDaJanela)*100) + ";" + ((frequencia / sentecas.size())*100) + ";" + ((frequencia / quantidadeAssociada)*100));
//                printWriter.print(inicioDaJanela + " - " + fimDaJanela + ";" + frequencia);
                printWriter.print(fimDaJanela + ";" + frequencia);
                printWriter.print("\n");
                frequencia = 0;
                inicioDaJanela += passo;
                fimDaJanela += passo;
                /*
                Se o fim da janela não for do mesmo tamanho da quantidade de senteças
                E se a próxima janela for menor que tamanho do passo. A última janela será maior
                 */
                if (fimDaJanela + passo > sentecas.size()) {
                    float tolerancia = sentecas.size() - fimDaJanela;
                    tolerancia = (tolerancia * 100) / passo;
                    if (tolerancia < 0.7) {
                        fimDaJanela = sentecas.size();
                    }
                }

                if (p == sentecas.size()) {
                    continuar = false;
                }

            }
            printWriter.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

    }

    public void salvarMatrizDeTipos(Livro livro, File caminho, int inicioDoIntervalo, int fimDoIntervalo) {
        ArrayList<String> simNao = new ArrayList<String>();
        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            PrintWriter printWriter = new PrintWriter(fileWriterDestino);//Escrever no novo arquivo
            printWriter.print("Palavras\tCaracteres\t");
            for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
                printWriter.print(i + "\t");
            }
            printWriter.print("\n");

            for (Integer numeroPagina : livro.getPaginas().keySet()) {
                Pagina pagina = livro.getPaginas().get(numeroPagina);

                for (Frase frase : pagina.getFrases()) {
                    if (frase.existeVerso()) {
                        simNao.add("SIM");
                        printWriter.printf("%s\t%10s", frase.getQuantidadeDePalavras(), frase.getQuantidadeDeLetras());
                        for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
                            if (existeValor(frase.getMetricasDeVerso(), i)) {
                                if (i == inicioDoIntervalo) {
                                    printWriter.printf("\t%10s", i);
                                } else {
                                    printWriter.print("\t" + i);
                                }
                            } else {
                                if (i == inicioDoIntervalo) {
                                    printWriter.printf("\t%10s", (-1));
                                } else {
                                    printWriter.print("\t-1");
                                }
                            }
                        }
                        printWriter.print("\n");
                    } else {
                        simNao.add("Não");
                        printWriter.printf("%s\t%10s", frase.getQuantidadeDePalavras(), frase.getQuantidadeDeLetras());
                        for (int i = inicioDoIntervalo; i <= fimDoIntervalo; i++) {
                            if (i == inicioDoIntervalo) {
                                printWriter.printf("\t%10s", (-1));
                            } else {
                                printWriter.print("\t-1");
                            }
                        }
                        printWriter.print("\n");
                    }
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        int intervalo = simNao.size() / 5;
        int numeroDeIntervalos = 1;
        int indice = 0, qtd = 0;
        int inicio = 0;
        int intervaloAtual = 0;
        int totalComputado = 0;
        for (String s : simNao) {
            if (s.equals("SIM")) {
                qtd++;
                indice++;
            } else {
                indice++;
            }
            intervaloAtual++;
            if (indice == intervalo - 1 && numeroDeIntervalos < 5) {
                System.out.println((numeroDeIntervalos) + ": " + inicio + "-" + intervaloAtual + "\t" + qtd);
                qtd = 0;
                numeroDeIntervalos++;
                totalComputado += indice;
                inicio = intervaloAtual + 1;
                indice = 0;
            }
        }
        System.out.println((numeroDeIntervalos) + ": " + inicio + "-" + intervaloAtual + "\t" + qtd);

        System.out.println("Tamanho do Intervalo: " + intervalo);
        System.out.println("Número de Segmentos: " + simNao.size());
        System.out.println("Intervalo restante: " + (simNao.size() - inicioDoIntervalo));

    }

    private boolean existeValor(ArrayList<Integer> metricas, int metrica) {
        int aux;
        for (Integer me : metricas) {
            aux = me;
            if (aux == metrica) {
                return true;
            }
        }
        return false;
    }

    public void salvarSentencaLivro(Livro livro, File caminho) {
        ArrayList<Sentenca> sentencas = livro.comporSentencas();

        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo

            for (Sentenca sentenca : sentencas) {
                bufferedWriterDestino.write(sentenca.getSegmento());
                bufferedWriterDestino.newLine();
                for (EstruturaVersificacao estruturaVersificacao : sentenca.getEstruturaDeVesificacao()) {
                    bufferedWriterDestino.write(estruturaVersificacao.getNumeroDeSilabas() + " - ");
                    bufferedWriterDestino.write(estruturaVersificacao.getPosicaoDasTonicas() + " - ");
                    bufferedWriterDestino.write(estruturaVersificacao.getSentecaEscandida());
                    bufferedWriterDestino.newLine();
                }

            }
            bufferedWriterDestino.flush();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }

    public void salvarFrasesLivro(Livro livro, File caminho) {
        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            int contador = 1;
            for (Integer chave : livro.getPaginas().keySet()) {
                ArrayList<Frase> frases = livro.getPaginas().get(chave).getFrases();
                for (Frase fra : frases) {
                    if (fra.toString().length() != 0) {
                        if (Utilitario.existeUmaPalavra(fra.toString())) {
                            bufferedWriterDestino.write(contador + ": " + fra.toString());
                            bufferedWriterDestino.newLine();
                            contador++;
                        }

                    }
                }
            }
            bufferedWriterDestino.flush();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }

    public void imprimeSentencasOuVersos(Livro livro, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter destino = new BufferedWriter(fileWriter);
            ArrayList<Frase> frases;
            int contador = 1;
            int indiceDaFrase = 0;
            for (Integer chave : livro.getPaginas().keySet()) {
                frases = livro.getPaginas().get(chave).getFrases();
                for (Frase frase : frases) {
                    if (frase.getVerso() != null) {
                        destino.write(contador + " " + frase.getVerso().getNumeroDeSilabas());
                    } else {
                        destino.write(contador + " " + "-1");
                    }
                    destino.newLine();
                    contador++;
                }
            }
            destino.flush();
            destino.close();
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception: " + exception.getMessage());
        }

    }

    /**
     * Objetivo é gerar um arquivo contendo uma relação de quais estruturas
     * sentenças possuem estruturas de versificação.
     *
     * @param livro
     * @param file - arquivo txt onde o resultado deve ser salvo.
     */
    public void imprimeDispersaoOcorrencia(Livro livro, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter destino = new BufferedWriter(fileWriter);
            ArrayList<Frase> frases;
            int contador = 1;
            int indiceDaFrase = 0;
            destino.write("Geral");
            destino.newLine();
            for (Integer chave : livro.getPaginas().keySet()) {
                frases = livro.getPaginas().get(chave).getFrases();
                for (Frase frase : frases) {
                    if (frase.getVerso() != null) {
                        destino.write(1 + " " + contador);
                        destino.newLine();
                    }
                    contador++;
                }
            }
            destino.flush();
            destino.close();
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception: " + exception.getMessage());
        }

    }

    public void imprimeDispersaoOcorrenciaPorTipo(Livro livro, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter destino = new BufferedWriter(fileWriter);
            HashMap<Integer, ArrayList<Integer>> distanciasPorTipo = livro.getDistanciasPorTipo();
            for (Integer key : distanciasPorTipo.keySet()) {
                ArrayList<Integer> distancias = distanciasPorTipo.get(key);
                for (Integer distancia : distancias) {
                    destino.write(distancia + ";" + key);
                    destino.newLine();
                }
            }
            destino.flush();
            destino.close();
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception: " + exception.getMessage());
        }

    }

    public void imprimeDistribuicaoDeTonicas(Livro livro, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter destino = new BufferedWriter(fileWriter);
            HashMap<String, Integer> distribuicaoDeTonicas = livro.getDistribuicaoDeTonicas();
            for (String key : distribuicaoDeTonicas.keySet()) {
                destino.write(key + ";" + distribuicaoDeTonicas.get(key));
                destino.newLine();
            }
            destino.flush();
            destino.close();
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Exception: " + exception.getMessage());
        }

    }

    public void salvarComo(Livro livro, File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            String string_jpanelEmXML = xstream.toXML(livro);// Passando os dados do JPanel Java para XML e salva em uma String
            File xmlMap = caminho;
            OutputStream streamOut = null;
            if (!(xmlMap.exists())) {
                xmlMap.createNewFile();
            } else {
                String novoArquivo = xmlMap.getCanonicalPath();
                xmlMap.delete();
                System.out.println("Caminho do arquivo: " + novoArquivo);
                xmlMap = new File(novoArquivo);
                // xmlMap.createNewFile();
            }
            streamOut = new FileOutputStream(xmlMap);
            //XStream xstreamr = new XStream(new DomDriver());
            xstream.toXML(livro, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public Livro ler(File arquivo) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(arquivo);
            XStream xStream = new XStream(new DomDriver());
            System.out.println("Preparando para ler....");
            livro = (Livro) xStream.fromXML(fis);
            System.out.println("Leitura finalizadas");
//            System.out.println("MAPA CONFIGURAÇÃO SINERESE: " + mapaConfiguracao.getSinerese().size());
//            System.out.println("MAPA CONFIGURAÇÃO DIERESE: " + mapaConfiguracao.getDierese().size());
//            System.out.println("MAPA CONFIGURAÇÃO ELISÃO: " + mapaConfiguracao.getElisao().size());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LivroIO.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("OLha o erro aqui: " + ex.getMessage());
        }
        return livro;
    }

}
