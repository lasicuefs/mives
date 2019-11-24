/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import exportacao.Sentenca;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import mives.model.Linha;
import mives.model.Livro;
import mives.model.Pagina;
import mives.model.Verso;

/**
 *
 * @author Ricardo
 */
public class GeradorHTML {

    private HashMap<Integer, Pagina> paginas;
    private ArrayList<Linha> linhas;
    File arquivoDeDestino;
    Livro livro;
    ArrayList<Verso> versos;
    private FileWriter fileWriterDestino;
    BufferedWriter bufferedWriterDestino;

    public GeradorHTML(Livro livro, File arquivoDeDestino) {
        this.livro = livro;
        paginas = livro.getPaginas();
        this.arquivoDeDestino = arquivoDeDestino;

    }

    public GeradorHTML() {
        this.livro = Livro.getInstance();
        paginas = Livro.getInstance().getPaginas();
    }

    public void gerarHTML() {
        this.gerarArquivoDestino(arquivoDeDestino);
        this.montarParagrafo();
    }

    private void escreverDestino(String linha) {
        try {
            bufferedWriterDestino.write(linha);
            bufferedWriterDestino.newLine();
            bufferedWriterDestino.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Deprecated //07 de Sentembro de 2018
    private void gerarArquivoDestino(File arquivoDeDestino) {
        try {
            fileWriterDestino = new FileWriter(arquivoDeDestino); //Escrever no novo arquivo

            bufferedWriterDestino = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoDeDestino), StandardCharsets.UTF_8));

            // bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            bufferedWriterDestino.write("<!DOCTYPE html>\n");
            bufferedWriterDestino.write("<html>\n");
            bufferedWriterDestino.write("<head>\n"
                    + "<title>" + arquivoDeDestino.getName() + "</title>"
                    + "<style type=\"text/css\">"
                    + "body{"
                    + "color:#333333;"
                    + "font-family: Courier, monospace;"
                    + "font-size: 12px;"
                    + "margin-left: 20px;"
                    + "margin-right: 20px;"
                    //  + "width: 740px;"
                    + "text-align: justify;"
                    + "background-color: white;"
                    + "}"
                    + "</style>"
                    //+ "<link rel=\"stylesheet\" href=\"css/estilo.css\">"
                    + "<meta charset=\"utf-8\">"
                    + "</head>\n");
            bufferedWriterDestino.write("<body>\n");
            bufferedWriterDestino.newLine();
            bufferedWriterDestino.flush();
        } catch (IOException ex) {
//            Logger.getLogger(FraseVerso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuilder construirScript() {

        StringBuilder conteudo = new StringBuilder();

        conteudo.append("<!DOCTYPE html>\n");
        conteudo.append("<html>\n");
        conteudo.append("<head>\n");
        conteudo.append("<title>Meu Teste</title>\n");

        conteudo.append("<style>");
        conteudo.append("   @import url('https://fonts.googleapis.com/css?family=Ubuntu');");
        conteudo.append("</style>");
        
        
        conteudo.append("<style type=\"text/css\">\n");
        conteudo.append("body{\n");
        conteudo.append("   color:#333333;\n");
        conteudo.append("   font-family: Courier, monospace;\n");
        conteudo.append("   font-size: 12px;\n");
        conteudo.append("   margin-left: 20px;\n");
        conteudo.append("   margin-right: 20px;\n");
        conteudo.append("   text-align: justify;\n");
        conteudo.append("   background-color: white;\n");
        conteudo.append("   font-family: 'Ubuntu', sans-serif;\n");
        conteudo.append("}\n");
        conteudo.append("</style>\n");
        conteudo.append("<meta charset=\"UTF-8\">\n");
        conteudo.append("</head>\n");
        //   conteudo.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n");
        conteudo.append("<script src=\"js/jquery-3.3.1.min.js\"></script>\n");
        conteudo.append("<script type=\"text/javascript\" src=\"js/jquery.highlight-5.js\"></script>\n");
        conteudo.append("\n");
        conteudo.append("<script type=\"text/javascript\" src=\"boxMessageManager.js\"></script>\n");
        conteudo.append("\n");

        conteudo.append("<script type=\"text/javascript\">\n");
        conteudo.append("$(document).ready(function(){\n");
        conteudo.append("});\n");
        conteudo.append("</script>\n");
        conteudo.append("<script language=\"javascript\">\n");
        conteudo.append("function jumpToHtmlId(object) { \n");
        conteudo.append("window.location.hash = '#'+object;\n");
        conteudo.append("}\n");
        conteudo.append("</script>\n");
        conteudo.append("<style type=\"text/css\">\n");
        conteudo.append(".highlight { \n");
        conteudo.append("background-color: yellow;\n");
        conteudo.append("}\n");
        conteudo.append("\n");
        conteudo.append("#bm{\n");
        conteudo.append("   display:none;	\n");
        conteudo.append("   box-shadow: 2px 2px 2px rgba(220,220,220, 0.7);\n");
        conteudo.append("   position: absolute;\n");
        conteudo.append("   top:0;\n");
        conteudo.append("   bottom:0; \n");
        conteudo.append("}\n");
        conteudo.append("\n");
        conteudo.append("#bm-child{\n");
        conteudo.append("   position:relative;\n");
        conteudo.append("   top:0;\n");
        conteudo.append("   bottom:0;\n");
        conteudo.append("   min-height: 25px;\n");
        conteudo.append("   border: solid #8CC5F4 1px;\n");
        conteudo.append("   color: #0C8C8C8;\n");
        conteudo.append("   font-family: arial;\n");
        conteudo.append("   border-radius: 10px;\n");
        conteudo.append("   min-height: 25px;\n");
        conteudo.append("   background-color: #8CC5F4;\n");
        conteudo.append("   padding: 10px;\n");
        conteudo.append("  font-family: 'Ubuntu', sans-serif;\n");
        conteudo.append("}\n");

        conteudo.append("\n");

        conteudo.append("br{\n");
        conteudo.append("    display:none;\n");
        conteudo.append("}\n");

        conteudo.append("\n");
        conteudo.append("#bm-title{\n");
        conteudo.append("   display: block;\n");
        conteudo.append("   width: 100%;\n");
        conteudo.append("   margin-bottom: 5px;\n");
        conteudo.append("}\n");
        conteudo.append("span{\n");
        conteudo.append("   cursor:pointer;\n");
        conteudo.append("}\n");
        conteudo.append("\n");
        conteudo.append("span:hover{\n");
        conteudo.append("   background-color: #ff0 !important;\n");
        conteudo.append("}\n");

        conteudo.append(" .seta-1{ \n");
        conteudo.append("   position: absolute;\n");
        conteudo.append("   top:-16px;\n");
        conteudo.append("   border-bottom: 16px solid #8CC5F4; \n");
        conteudo.append("   border-left: 8px solid transparent;  \n");
        conteudo.append("   border-right: 8px solid transparent; float:left;	\n");
        conteudo.append("}\n");

        conteudo.append("\n");
        conteudo.append("</style>\n");
        conteudo.append("<body id=\"body\">\n");

        conteudo.append("<div id=\"bm\">\n");
        conteudo.append("<div id=\"bm-child\">\n");
        conteudo.append("<div class=\"seta-1\"></div>\n");
        conteudo.append("<b><span id=\"bm-title\"></span></b>\n");
        conteudo.append("<span id=\"bm-monossilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-dissilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-tetrassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-pentassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-hexassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-heptassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-octossilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-eneassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-decassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-hendecassilabo\"></span><br>\n");
        conteudo.append("<span id=\"bm-dodecassilabo\"></span>\n");

        conteudo.append("</div>\n");
        conteudo.append("</div>\n");

        return conteudo;
    }

    public StringBuilder montarParagrafo(StringBuilder conteudo) {
        int flag = 0;
        boolean escrevi = false;
        Pagina pagina;

        for (Integer chave : paginas.keySet()) {
            pagina = paginas.get(chave);
            String marcador = null;
            conteudo.append("<p style=\"text-align: center;\"> --" + pagina.getNumero() + "--</p>");
            linhas = pagina.getLinhas();
            for (Linha linha : linhas) {
                if (linha.getLinha().length() != 0) {
                    if (!escrevi) {
                        marcador = "<p>";
                    }
                    conteudo.append(linha.getLinhaComEscansao());
                    if (linha.getLinhaComEscansao().contains(".<<")//Se contém marcação e
                            && linha.getLinhaComEscansao().trim().charAt(linha.getLinhaComEscansao().trim().length() - 1) == '>') {
                        conteudo.append("</p>\n<p>");
                    }
                    escrevi = true;
                    flag = 0;
                } else {
                    if (linha.getLinha().length() == 0 && escrevi) {
                        conteudo.append("</p>");
                        escrevi = false;
                    }
                }
            }
        }
        System.gc();
        conteudo.append("\n</body>\n");
        conteudo.append("\n<script>\n");

        for (Sentenca sentenca : Livro.getInstance().comporSentencas()) {
            conteudo.append("\n arrayMessages.push('{\"title\" : \"" + sentenca.getEstruturaDeVesificacao().get(0).getPalavrasVerso().replaceAll("'", " ").replaceAll("\"", "") + "\",");
            int qtd = sentenca.getEstruturaDeVesificacao().size();
            for (int i = 0; i < qtd; i++) {
                if (i > 0 && (i < qtd)) {
                    conteudo.append(",");
                }
                if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 1) {
                    conteudo.append(" \"monossilabo\" : \"Monossilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 2) {
                    conteudo.append(" \"dissilabo\" : \"Dissilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 4) {
                    conteudo.append(" \"tetrassilabo\" : \"Tetrassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 5) {
                    conteudo.append(" \"pentassilabo\" : \"Pentassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 6) {
                    conteudo.append(" \"hexassilabo\" : \"Hexassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 7) {
                    conteudo.append(" \"heptassilabo\" : \"Heptassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 8) {
                    conteudo.append(" \"octossilabo\" : \"Octossilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 9) {
                    conteudo.append(" \"eneassilabo\" : \"Eneassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 10) {
                    conteudo.append(" \"decassilabo\" : \"Decassilabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else if (sentenca.getEstruturaDeVesificacao().get(i).getNumeroDeSilabas() == 11) {
                    conteudo.append(" \"hendecassilabo\" : \"Hendecassílabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                } else {
                    conteudo.append(" \"dodecassilabo\" : \"Dedecassílabo: " + sentenca.getEstruturaDeVesificacao().get(i).getSentecaEscandida().replaceAll("'", " ").replaceAll("\"", "") + "\"");
                }

            }
            conteudo.append("}'); \n");
            //conteudo.append(" \"decassilabo\" : \"Decassilabo: " + sentenca.getEstruturaDeVesificacao().get(0).getSentecaEscandida() + "\"}'); \n");
        }
        conteudo.append("\n</script>\n");
        conteudo.append("</html>\n");
        return conteudo;

    }

    @Deprecated
    public StringBuilder montarParagrafoFinal(StringBuilder conteudo
    ) {
        int flag = 0;
        boolean escrevi = false;
        Pagina pagina;

        for (Integer chave : paginas.keySet()) {
            pagina = paginas.get(chave);
            String marcador = null;
            conteudo.append("<p style=\"text-align: center;\"> --" + pagina.getNumero() + "--</p>");
            linhas = pagina.getLinhas();
            for (Linha linha : linhas) {
                if (linha.getLinha().length() != 0) {
                    if (!escrevi) {
                        marcador = "<p>";
                    }
                    conteudo.append(linha.getLinhaComEscansao());
                    if (linha.getLinhaComEscansao().contains(".<<")//Se contém marcação e
                            && linha.getLinhaComEscansao().trim().charAt(linha.getLinhaComEscansao().trim().length() - 1) == '>') {
                        conteudo.append("</p>\n<p>");
                    }
                    escrevi = true;
                    flag = 0;
                } else {
                    if (linha.getLinha().length() == 0 && escrevi) {
                        conteudo.append("</p>");
                        escrevi = false;
                    }
                }
            }
        }
        System.gc();
        conteudo.append("</body>\n");
        conteudo.append("</html>\n");
        return conteudo;

    }

    private void montarParagrafo() {
        int flag = 0;
        boolean escrevi = false;
        Pagina pagina;
        try {
            for (Integer chave : paginas.keySet()) {
                pagina = paginas.get(chave);
                String marcador = null;
                this.escreverDestino("<p style=\"text-align: center;\"> --" + pagina.getNumero() + "--</p>");
                linhas = pagina.getLinhas();
                for (Linha linha : linhas) {
                    if (linha.getLinha().length() != 0) {
                        if (!escrevi) {
                            marcador = "<p>";
                        }
                        escreverDestino(linha.getLinhaComEscansao());
                        if (linha.getLinhaComEscansao().contains(".<<")//Se contém marcação e
                                && linha.getLinhaComEscansao().trim().charAt(linha.getLinhaComEscansao().trim().length() - 1) == '>') {
                            escreverDestino("</p>\n<p>");
                        }
                        escrevi = true;
                        flag = 0;
                    } else {
                        if (linha.getLinha().length() == 0 && escrevi) {
                            escreverDestino("</p>");
                            escrevi = false;
                        }
                    }
                }
            }
            System.gc();
            bufferedWriterDestino.write("</body>\n");
            bufferedWriterDestino.write("</html>\n");
            bufferedWriterDestino.newLine();
            bufferedWriterDestino.flush();
        } catch (Exception ex) {

        }

    }
}
