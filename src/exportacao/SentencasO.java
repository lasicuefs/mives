/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exportacao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author Ricardo
 */
public class SentencasO {

    private ArrayList<Sentenca> sentencas;

    public SentencasO(ArrayList<Sentenca> sentencas) {
        this.sentencas = sentencas;
    }

    public void salvar(File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());
            String string_jpanelEmXML = xstream.toXML(sentencas);// Passando os dados do JPanel Java para XML e salva em uma String
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
            xstream.toXML(sentencas, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public void salvarComo(File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());

            String string_jpanelEmXML = xstream.toXML(sentencas);// Passando os dados do JPanel Java para XML e salva em uma String
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
            xstream.toXML(sentencas, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

}
