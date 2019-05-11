/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.arquivos;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mives.model.MapaConfiguracao;

/**
 *
 * @author Ricardo
 */
public class MapaConfiguracaoIO {

    MapaConfiguracao mapaConfiguracao;

    public void salvar(MapaConfiguracao mapaConfiguracao, File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());
            String string_jpanelEmXML = xstream.toXML(mapaConfiguracao);// Passando os dados do JPanel Java para XML e salva em uma String
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
            xstream.toXML(mapaConfiguracao, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public void salvarComo(MapaConfiguracao mapaConfiguracao, File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());
            System.out.println("Quantidade de Elisões: " + MapaConfiguracao.getInstacia().getElisao().size());
            System.out.println("Quantidade de Sinerese: " + MapaConfiguracao.getInstacia().getSinerese().size());
            System.out.println("Quantidade de Diérese: " + MapaConfiguracao.getInstacia().getDierese().size());

            String string_jpanelEmXML = xstream.toXML(MapaConfiguracao.getInstacia());// Passando os dados do JPanel Java para XML e salva em uma String
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
            xstream.toXML(mapaConfiguracao, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public MapaConfiguracao ler(File arquivo) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(arquivo);
            XStream xStream = new XStream(new DomDriver());
            mapaConfiguracao = (MapaConfiguracao) xStream.fromXML(fis);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Arquivo de configuração corrompido ou inapropriado.", "PGCA...", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MapaConfiguracaoIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Mapa de Configuração carregado com sucesso.");

        return mapaConfiguracao;
    }

    public void imprimeMapaConfiguracaoEmTxt(File caminho) {
        try {
            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo

            Iterator<String> elisoes = MapaConfiguracao.getInstacia().getElisao().iterator();
            String chave;
            bufferedWriterDestino.write("-----------------Mapa de Conguração-----------------");
            bufferedWriterDestino.newLine();
            bufferedWriterDestino.write("Elisões:");
            bufferedWriterDestino.newLine();
            while (elisoes.hasNext()) {
                chave = elisoes.next();
                bufferedWriterDestino.write(chave);
                bufferedWriterDestino.newLine();

            }
            bufferedWriterDestino.write("----------------------------------------------------");
            bufferedWriterDestino.newLine();
            Iterator<String> sinereses = MapaConfiguracao.getInstacia().getSinerese().iterator();
            bufferedWriterDestino.write("Sinérese:");
            bufferedWriterDestino.newLine();
            while (sinereses.hasNext()) {
                chave = sinereses.next();
                bufferedWriterDestino.write(chave);
                bufferedWriterDestino.newLine();
            }
            bufferedWriterDestino.write("----------------------------------------------------");
            bufferedWriterDestino.newLine();
            Iterator<String> diereses = MapaConfiguracao.getInstacia().getDierese().iterator();
            bufferedWriterDestino.write("Diérese:");
            bufferedWriterDestino.newLine();
            while (diereses.hasNext()) {
                chave = diereses.next();
                bufferedWriterDestino.write(chave);
                bufferedWriterDestino.newLine();
            }
            bufferedWriterDestino.write("----------------------------------------------------");
            bufferedWriterDestino.flush();
            bufferedWriterDestino.close();
            fileWriterDestino.close();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }

//    ArrayList<Regra> regrasElisao = new ArrayList<Regra>();
//    ArrayList<Regra> regrasSinerese = new ArrayList<Regra>();
//    ArrayList<Regra> regrasDierese = new ArrayList<Regra>();
    int quantidade = -1;

    private void carregarRegras() {
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getElisao().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            if (MapaConfiguracao.getInstacia().getExcecoesElisao().contains(regraAtual)) {
//                regrasElisao.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.TENTE));
//                regrasElisao.get(quantidade + 1).setTente(true);
//
//            } else {
//                regrasElisao.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.SEMPRE));
//                regrasElisao.get(quantidade + 1).setSim(true);
//            }
//            quantidade++;
//        }
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getExcluirElisao().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            regrasElisao.add(new Regra(1, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.NUNCA));
//            regrasElisao.get(quantidade + 1).setNunca(true);
//            quantidade++;
//        }
//        quantidade = -1;
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getSinerese().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            if (MapaConfiguracao.getInstacia().getExcecoesSinerese().contains(regraAtual)) {
//                regrasSinerese.add(new Regra(2, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.TENTE));
//                regrasSinerese.get(quantidade + 1).setTente(true);
//            } else {
//                regrasSinerese.add(new Regra(2, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.SEMPRE));
//                regrasSinerese.get(quantidade + 1).setSim(true);
//            }
//            quantidade++;
//        }
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getExcluirSinerese().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            regrasSinerese.add(new Regra(2, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.NUNCA));
//            regrasSinerese.get(quantidade + 1).setNunca(true);
//            quantidade++;
//        }
//
//        quantidade = -1;
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getDierese().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            if (MapaConfiguracao.getInstacia().getExcecoesDierese().contains(regraAtual)) {
//                regrasDierese.add(new Regra(3, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.TENTE));
//                regrasDierese.get(quantidade + 1).setTente(true);
//            } else {
//                regrasDierese.add(new Regra(3, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.SEMPRE));
//                regrasDierese.get(quantidade + 1).setSim(true);
//            }
//            quantidade++;
//        }
//
//        for (Iterator<String> iter = MapaConfiguracao.getInstacia().getExcluirDierese().iterator();
//                iter.hasNext();) {
//            String regraAtual = iter.next();
//            regrasDierese.add(new Regra(3, (regraAtual.charAt(0) + "").toUpperCase(), (regraAtual.charAt(1) + "").toUpperCase(), TableSampleBaseView.Participation.NUNCA));
//            regrasDierese.get(quantidade + 1).setNunca(true);
//            quantidade++;
//        }

    }

    public void imprimeMapaConfiguracaoEmTxtV2(File caminho) {
        carregarRegras();
//        try {
//            FileWriter fileWriterDestino = new FileWriter(caminho, true); //Escrever no novo arquivo
//            PrintWriter print = new PrintWriter(fileWriterDestino);//Escrever no novo arquivo
//
//            Iterator<Regra> elisoes = regrasElisao.iterator();
//            Regra chave;
//            print.print("-----------------Mapa de Conguração-----------------\n");
//            print.write("Elisões...:\n");
//            print.printf("Regra\tSempre\tNunca\tTente\n");
//            while (elisoes.hasNext()) {
//                chave = elisoes.next();
//                print.printf("%s%s\t", chave.getPrimeiraLetra(), chave.getSegundaLetra());
//                if (chave.getParticipation() == TableSampleBaseView.Participation.SEMPRE) {
//                    print.printf("%5s\t", "x");
//                } else {
//                    print.printf("%5s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.NUNCA) {
//                    print.printf("%6s\t", "x");
//                } else {
//                    print.printf("%6s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.TENTE) {
//                    print.printf("%6s\n", "x");
//                } else {
//                    print.printf("%6s\n", "-");
//                }
//
//            }
//            print.printf("----------------------------------------------------\n");
//
//            Iterator<Regra> sinereses = regrasSinerese.iterator();
//            print.print("Sinérese:\n");
//
//            while (sinereses.hasNext()) {
//                chave = sinereses.next();
//                print.printf("%s%s\t", chave.getPrimeiraLetra(), chave.getSegundaLetra());
//                if (chave.getParticipation() == TableSampleBaseView.Participation.SEMPRE) {
//                    print.printf("%5s\t", "x");
//                } else {
//                    print.printf("%5s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.NUNCA) {
//                    print.printf("%6s\t", "x");
//                } else {
//                    print.printf("%6s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.TENTE) {
//                    print.printf("%6s\n", "x");
//                } else {
//                    print.printf("%6s\n", "-");
//                }
//            }
//            print.write("----------------------------------------------------\n");
//
//            Iterator<Regra> diereses = regrasDierese.iterator();
//            print.write("Diérese:\n");
//            while (diereses.hasNext()) {
//                chave = diereses.next();
//                print.printf("%s%s\t", chave.getPrimeiraLetra(), chave.getSegundaLetra());
//                if (chave.getParticipation() == TableSampleBaseView.Participation.SEMPRE) {
//                    print.printf("%5s\t", "x");
//                } else {
//                    print.printf("%5s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.NUNCA) {
//                    print.printf("%6s\t", "x");
//                } else {
//                    print.printf("%6s\t", "-");
//                }
//                if (chave.getParticipation() == TableSampleBaseView.Participation.TENTE) {
//                    print.printf("%6s\n", "x");
//                } else {
//                    print.printf("%6s\n", "-");
//                }
//            }
//            print.printf("----------------------------------------------------");
//            print.flush();
//            print.close();
//            fileWriterDestino.close();
//        } catch (FileNotFoundException exception) {

//        } catch (IOException ioe) {

//
//        }
    }

}
