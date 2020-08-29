/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import mives.exceptions.DicionarioException;
import mives.util.Utilitario;

/**
 *
 * @author Ricardo Necessário refatorar, deixar mais eficiente.
 */
public class Dicionario {

    private int numeracao = 1;
    private HashSet<String> algarismosRomanos = new HashSet<>();
    private HashSet<String> monossilabosAtonos = new HashSet<>();
    private HashSet<String> vogais = new HashSet<>();

    //File arquivoDeOrigem;
    BufferedReader bufferedReaderBase;
    //private FileWriter fileWriterDestino;
    BufferedWriter bufferedWriterDestino;
    int numeroSilabas;
    FileReader readerBase;

    private File arquivoDeOrigem;
    private File arquivoDeDestino;
    private FileWriter fileWriterDestino;
    private int cont = 1;//Conta o número de caracteres de pontuação da palavra
    private String linha;

    private HashMap<String, String> dicio = new HashMap<>();

    private static Dicionario dicionario = null;

    File arquivoOrigem;

    private Dicionario() {

        carregarAlgarismos();
        carregarMonossilabosAtonos();
        carregarVogais();

    }

    public static Dicionario getInstance() {
        if (dicionario != null) {
            return dicionario;
        } else {
            dicionario = new Dicionario();
            return dicionario;
        }
    }
    boolean flag = true;

    public boolean adicionarTermos(File arquivoDeOrigem) throws DicionarioException {
        if (dicio.isEmpty()) {
            throw new DicionarioException("É necessário carregar o dicionário");
        } else {
            this.arquivoDeOrigem = arquivoDeOrigem;
            adicionarTermos();
            return flag;
        }

    }

    private void adicionarTermos() {
//        carregarAlgarismos();
////        carregarMonossilabosAtonos();
//        carregarVogais();
        try {
            flag = true;
            String temp = null;
            FileReader readerBase = new FileReader(arquivoDeOrigem);//Faz novaPal leitura do arquivo base
            BufferedReader bufferedReaderBase = new BufferedReader(readerBase);//Faz novaPal leitura do arquivo base
            fileWriterDestino = new FileWriter(arquivoDeDestino, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            linha = bufferedReaderBase.readLine();
            inicio:
            while (linha != null) {
                String simbolo = "";
                StringTokenizer st = new StringTokenizer(linha);
                String p = null;
                while (st.hasMoreTokens()) {
                    p = st.nextToken().toString();
                    simbolo = "";
                    if (!p.contains("@")
                            && !p.contains("#")//Rever Primeira regra
                            && !p.contains("$") && !p.contains(" ")
                            && !(p.length() <= 2)
                            && !p.matches("\\d\\w*\\d")//pode ser substituida pela última condição
                            && !p.matches("\\d\\w*.")//pode ser substituida pela última condição
                            && !p.matches("[(]\\d.\\w*.")
                            && !p.matches(".*\\d.*")) {
                        //verificar se existem caracteres de pontuação na palavra
                        if (p.contains(",")
                                || p.contains(".")
                                || p.contains(";")
                                || p.contains(":")
                                || p.contains("!")
                                || p.contains("?")
                                || p.contains(")")) {
                            while (p.length() - cont > 0 && p.charAt(p.length() - cont) == '.') {
                                cont++;
                            }
                            if (cont > 1) {
                                simbolo = p.substring(p.length() - (cont - 1));
                            } else {
                                simbolo = p.substring(p.length() - cont);
                            }
                            p = p.replace(simbolo, "");
                            cont = 1;
                        }
                        //Verifica se o termo vem antecedido por aspas e remove o caracter                   
                        if (p.contains("\"")) {
                            p = p.replaceAll("\"", "");
                            linha = bufferedReaderBase.readLine();
                            continue inicio;

                        }
                        if (p.contains("'")) {
                            p = p.replace("'", "");
                        }
                        if ((p.contains("—") && p.charAt(0) == '—')
                                || p.contains("(")) {
                            simbolo = "" + p.charAt(0);
                            p = p.replace(simbolo, "");
                        }

                        if (p.matches("\\w*.-\\w.*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                            //    p = p.replace(",", "");
                            String[] pns = p.split("-");
                            if (pns[1].length() > 3) {
                                if (!dicio.containsKey(p.toLowerCase())) {
                                    //dicionario.put(p, pns[0] + "-" + separarSilaba(pns[1]));

                                    temp = pns[0] + "§-" + separarSilaba(pns[1]);
                                    bufferedWriterDestino.newLine();
                                    bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                    dicio.put(p.toLowerCase(), pns[0] + "§-" + separarSilaba(pns[1]));
                                }
                            } else {
                                if (!dicio.containsKey(p.toLowerCase())) {
                                    if (!p.contains("-")) {

                                        temp = separarSilaba(p);
                                        bufferedWriterDestino.newLine();
                                        bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                        dicio.put(p.toLowerCase(), separarSilaba(p));
                                    } else {//Trata palavras compostas a exemplo de: Demonstram-no, Quebra-se, torna-se                                        
                                        String aux[] = p.split("-");
                                        temp = separarSilaba(aux[0]) + "-" + aux[1];

                                        int ultimoHifen = temp.length() - 1;
                                        for (int i = ultimoHifen; i > 0; i--) {
                                            if ((temp.charAt(i) + "").equals("-")) {
//                                                System.out.println("Entrei aqui");
                                                StringBuilder stTemp = new StringBuilder(temp);
                                                temp = stTemp.replace(i, i, "§").toString();
                                                break;
                                            }
                                        }
                                        bufferedWriterDestino.newLine();
                                        bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                        dicio.put(p.toLowerCase(), separarSilaba(p));
                                    }
                                }
                            }
                        } else if (p.matches("\\w.+-\\w*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                            String[] pns = p.split("-");
                            if (!dicio.containsKey(p.toLowerCase())) {
                                temp = separarSilaba(pns[0]) + "-§" + separarSilaba(pns[1]);
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                dicio.put(p.toLowerCase(), temp);
                            }
                        } else {
                            if (!dicio.containsKey(p.toLowerCase())) {
                                temp = separarSilaba(p);
                                if (temp.length() == 0) {
                                    linha = bufferedReaderBase.readLine();
                                    continue inicio;
                                }
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                dicio.put(p.toLowerCase(), temp);
                            }
                        }
                    } else {
                        if (!dicio.containsKey(p.toLowerCase())) {
                            if (!p.matches("\\d\\w*\\d")//pode ser substituida pela última condição
                                    && !p.matches("\\d\\w*.")//pode ser substituida pela última condição
                                    && !p.matches("[(]\\d.\\w*.")
                                    && !p.matches(".*\\d.*")) {

                                if (p.contains("\"")) {
                                    p = p.replaceAll("\"", "");
                                    if (p.length() == 0) {
                                        linha = bufferedReaderBase.readLine();
                                        continue inicio;
                                    }
                                }
                                if (p.contains("'")) {
                                    p = p.replace("'", "");
                                    if (p.length() == 0) {
                                        linha = bufferedReaderBase.readLine();
                                        continue inicio;
                                    }
                                }
                                if ((p.contains("—") && p.charAt(0) == '—')
                                        || p.contains("(")) {
                                    simbolo = "" + p.charAt(0);
                                    p = p.replace(simbolo, "");
                                    if (p.length() == 0) {
                                        linha = bufferedReaderBase.readLine();
                                        continue inicio;
                                    }
                                }
                                dicio.put(p.toLowerCase(), p.toLowerCase());
                                temp = p;
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p + "\t" + temp);
                                dicio.put(p.toLowerCase(), temp);
                            }
                        }
                    }

                }//Encerra o while aqui
                bufferedWriterDestino.flush();
                linha = bufferedReaderBase.readLine();
            }

        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
            flag = false;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            flag = false;
        }

        // gerarDicionario();
    }

    public void adicionarTermo(String linha) {
//        carregarAlgarismos();
//        carregarMonossilabosAtonos();
//        carregarVogais();
        try {
            flag = true;
            String temp = null;
            fileWriterDestino = new FileWriter(arquivoDeDestino, true); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo
            String simbolo = "";
            StringTokenizer st = new StringTokenizer(linha);
            String p = null;
            String retorno;
            while (st.hasMoreTokens()) {
                p = st.nextToken().toString();
                simbolo = "";
                if (!p.contains("@")
                        && !p.contains("#")//Rever Primeira regra
                        && !p.contains("$") && !p.contains(" ")
                        && !p.matches("\\d\\w*\\d")//pode ser substituida pela última condição
                        && !p.matches("\\d\\w*.")//pode ser substituida pela última condição
                        && !p.matches("[(]\\d.\\w*.")
                        && !p.matches(".*\\d.*")) {
                    //verificar se existem caracteres de pontuação na palavra
                    if (p.contains(",")
                            || p.contains(".")
                            || p.contains(";")
                            || p.contains(":")
                            || p.contains("!")
                            || p.contains("?")
                            || p.contains(")")) {
                        while (p.charAt(p.length() - cont) == '.') {
                            cont++;
                        }
                        if (cont > 1) {
                            simbolo = p.substring(p.length() - (cont - 1));
                        } else {
                            simbolo = p.substring(p.length() - cont);
                        }
                        p = p.replace(simbolo, "");
                        cont = 1;
                    }
                    //Verifica se o termo vem antecedido por aspas e remove o caracter                   
                    if (p.contains("\"")) {
                        p = p.replaceAll("\"", "");
                    }
                    if (p.charAt(0) == '\'' || p.charAt(p.length() - 1) == '\'') {
                        p = p.replace("'", "");
                    }
                    if ((p.contains("—") && p.charAt(0) == '—')
                            || p.contains("(")) {
                        simbolo = "" + p.charAt(0);
                        p = p.replace(simbolo, "");
                    }

                    if (p.matches("\\w*.-\\w.*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                        String[] pns = p.split("-");
                        if (pns[1].length() > 3) {
                            if (!dicio.containsKey(p.toLowerCase())) {
                                temp = pns[0].toLowerCase() + "§-" + separarSilaba(pns[1]);
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);

                                dicio.put(p.toLowerCase(), pns[0].toLowerCase() + "§-" + separarSilaba(pns[1]));
                            }
                        } else {
                            if (!dicio.containsKey(p.toLowerCase())) {
                                if (!p.contains("-")) {
                                    temp = separarSilaba(p);
                                    bufferedWriterDestino.newLine();
                                    bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);

                                    dicio.put(p.toLowerCase(), separarSilaba(p));
                                } else {//Trata palavras compostas a exemplo de: Demonstram-no, Quebra-se, torna-se                                        
                                    String aux[] = p.split("-");
                                    retorno = separarSilaba(aux[0]);
                                    temp = retorno + "-" + aux[1].toLowerCase();
                                    retorno = temp; //Necessário para inserir no dicionário sem o "§"
                                    int ultimoHifen = temp.length() - 1;
                                    for (int i = ultimoHifen; i > 0; i--) {
                                        if ((temp.charAt(i) + "").equals("-")) {
                                            StringBuilder stTemp = new StringBuilder(temp);
                                            temp = stTemp.replace(i, i, "§").toString();
                                            break;
                                        }
                                    }
                                    bufferedWriterDestino.newLine();
                                    bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                    dicio.put(p.toLowerCase(), retorno);
                                }
                            }
                        }
                    } else if (p.matches("\\w.+-\\w*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                        String[] pns = p.split("-");
                        if (!dicio.containsKey(p.toLowerCase())) {
                            temp = separarSilaba(pns[0]) + "-§" + separarSilaba(pns[1]);
                            bufferedWriterDestino.newLine();
                            bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                            dicio.put(p.toLowerCase(), temp);
                        }
                    } else {
                        if (!dicio.containsKey(p.toLowerCase())) {
                            if (p.length() > 1) {
                                temp = separarSilaba(p);
//                                System.out.println("01 - Enviando: " + p);
//                                System.out.println("01 - Recebendo: " + temp);
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                                dicio.put(p.toLowerCase(), temp);
                            } else {
                                //temp = separarSilaba(p);
//                                System.out.println("01 - Enviando: " + p);
//                                System.out.println("01 - Recebendo: " + temp);
                                bufferedWriterDestino.newLine();
                                bufferedWriterDestino.write(p + "\t" + p);
                                dicio.put(p, p);
                            }

                        }
                    }
                } else {
                    if (!dicio.containsKey(p.toLowerCase())) {
                        if (!p.matches("\\d\\w*\\d")//pode ser substituida pela última condição
                                && !p.matches("\\d\\w*.")//pode ser substituida pela última condição
                                && !p.matches("[(]\\d.\\w*.")
                                && !p.matches(".*\\d.*")) {

                            if (p.contains("\"")) {
                                p = p.replaceAll("\"", "");
                            }
                            if (p.contains("'")) {
                                p = p.replace("'", "");
                            }
                            if ((p.contains("—") && p.charAt(0) == '—')
                                    || p.contains("(")) {
                                simbolo = "" + p.charAt(0);
                                p = p.replace(simbolo, "");
                            }
                            dicio.put(p.toLowerCase(), p.toLowerCase());
                            temp = p.toLowerCase();
                            bufferedWriterDestino.newLine();
                            bufferedWriterDestino.write(p.toLowerCase() + "\t" + temp);
                            dicio.put(p.toLowerCase(), temp);
                        }
                    }
                }

            }//Encerra o while aqui
            bufferedWriterDestino.flush();

        } catch (FileNotFoundException exception) {
            System.out.println("Olha o erro do dicionario aqui: " + exception.getMessage());
            flag = false;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            flag = false;
        }

    }

    public String separarSilaba(String palavra) {
        if (isAlgarismoRomano(palavra) || isMonossilabosAtono(palavra.toLowerCase())) {
            return palavra;
        }

        if (palavra.length() == 1) {
            return palavra;
        }

        String vetor[] = {palavra};
        // ele ta retornando vazio?
        //Não, parece que não faz a chamada
        //Fiz o teste com um método lá no C# para executar o simples Writeln e nada. Também não funciona.
        // vamos facilitar os testes, espera um pouco
        String retorno[] = lapseparatorjni.TextAnalysisToolApp.Main(vetor);//aqui nessa linha
//        System.out.println("Retorno:");
//        for (String ret : retorno) {
//            System.out.println(ret);
//        }

        int posTonica = Integer.parseInt(retorno[2]);

        String pal = "";
        if (retorno[1].length() == 0) {
            pal = retorno[0];
        } else {
            pal = retorno[1];
        }

        try {
            int b = 0, t = 0, letras = 0;
            while (b <= (pal.length() - 1) && posTonica > letras) {
                if (pal.charAt(b) != '-') {
                    letras++;
                } else {
                    t++;
                }
                b++;
            }

            t += posTonica;

            if ((!(t < 0)) && (pal.length() - 1) > t && pal.charAt(t) == '-') {
                t++;
            } else {
                if (t < 0) {
                    return pal;
                }
            }

            //Tirei a instrução daqui
            StringBuffer buffer = new StringBuffer(pal);

            if (!(t < 0)) {
                return pal = buffer.insert(t, "#").toString();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

//Precisa melhorar - Tem processamento desnecessário
    @Deprecated
    public String separarSilabaOLd(String palavra) {

        //Algarismos romanos e Monossílabos Átonos não possuem marcação de tónica
        //Ver - Nova Gramática do Português Contemporâneo 5ª Edição
        if (isAlgarismoRomano(palavra) || isMonossilabosAtono(palavra)) {

            return palavra;
        }
        String command = "cmd.exe /K ConsoleApplication " + palavra;

        String ton, novaPal;
        String[] palavras;
        int posTonica;
        String pal;
        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "CP857"));

            ton = reader.readLine();

            novaPal = reader.readLine();

            while (novaPal.matches("\\d")) {
                novaPal = reader.readLine();
            }
            //Melhorar novaPal lógica, código horrível
            novaPal = novaPal.replaceAll("	", " ");
            palavras = novaPal.split(" ");
//            if (palavra.length() < 2) {
//                return palavra;
//            }
            posTonica = Integer.parseInt(ton);
            if (palavras.length < 2 || isAlgarismoRomano(palavra) || isMonossilabosAtono(palavra)) {//Isso é para não produzir o erro no momento de acessar o vetor.
                return palavra;
            }
            pal = palavras[1];
            int b = 0, t = 0, letras = 0;
            while (b <= (pal.length() - 1) && posTonica > letras) {
                if (pal.charAt(b) != '-') {
                    letras++;
                } else {
                    t++;
                }
                b++;
            }

            //    posTonica +=t;
            t += posTonica;

//            if (t > 0) {
//                t = t + (posTonica - 2);
//            } else if (posTonica < 0) {
//                t = 0;
//            } else {
//                t = posTonica;
//            }
            if ((!(t < 0)) && (pal.length() - 1) > t && pal.charAt(t) == '-') {
                t++;
            } else {
                if (t < 0) {
                    return pal;
                }
            }
//            if (t == 1) {
//                t = 0;
//            }

            StringBuffer buffer = new StringBuffer(pal);

            if (!(t < 0)) {
                return pal = buffer.insert(t, "#").toString();
            }
//            else {
//                return pal = buffer.insert(t, "#").toString();
//            }

        } catch (IOException ex) {
            this.gerarDicionario();
//            Logger.getLogger(SepararSilabaTexto.class
//                    .getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Retornando vazio");
        return "";
    }

    private boolean isVogal(String token) {
        return vogais.contains(token.toLowerCase());
    }

    protected void carregarVogais() {
        vogais.addAll(Arrays.asList("a", "e", "o", "á", "é", "ó", "í", "ú", "ã", "õ", "â", "ê", "ô", "à", "ü"));
    }

    //ATENÇÃO: MELHORAR BUUSCA - INSERIR MÉTODO MAIS EFICIENTE
    private boolean buscarLetra(char c, char[] caracters) {
        int i;
        for (i = 0; i < caracters.length; i++) {
            if (c == caracters[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlgarismoRomano(String token) {
        return algarismosRomanos.contains(token.toUpperCase());
    }

    protected void carregarAlgarismos() {
        algarismosRomanos.addAll(Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX", "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX", "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC", "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"));
    }

    /*
     Átonos são aqueles que pronunciados tão fracamente que, na frase, precisam 
     apoiar-se no acento tônico de um vocábulo vizinho, formando, por assim dizer,
     uma sílaba deste. Por exemplo:
     Diga-me/ o preço / do livro.
    
     ATENÇÃO: ainda falta verificar quais seriam as outras combinações.
     Ver - Nova Gramática do Português Contemporâneo 5ª Edição. Página: 69
     */
    protected void carregarMonossilabosAtonos() {
        monossilabosAtonos.addAll(Arrays.asList("o", "a", "os", "as", "um", "uns",
                "me", "te", "se", "lhe", "nos", "vos", "lhes", "mo", "to", "lho",
                "que", "com", "de", "em", "por", "sem", "sob", "ao", "da", "do", "dos",
                "na", "no", "num", "e", "mas", "nem", "ou"));
    }

    private boolean isMonossilabosAtono(String token) {
        return Utilitario.isMonossilabosAtono(token.toLowerCase());
        //  isMonossilabosAtonos(t);
    }

    //Melhorar essa rotina - precisa, ao passo que separar escrever no dicio
    private void gerarDicionario() {
        try {
            fileWriterDestino = new FileWriter(arquivoDeDestino); //Escrever no novo arquivo
            BufferedWriter bufferedWriterDestino = new BufferedWriter(fileWriterDestino);//Escrever no novo arquivo

            for (String obj : dicio.keySet()) {
                bufferedWriterDestino.write(obj + "\t" + dicio.get(obj));
                bufferedWriterDestino.newLine();
            }
            bufferedWriterDestino.close();
            fileWriterDestino.close();

        } catch (IOException ex) {
            Logger.getLogger(Dicionario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {

        }
    }

    public boolean carregarTermos(File arquivoDicionario) throws DicionarioException {
        arquivoDeDestino = arquivoDicionario;
        boolean retorno = true;
        int linhaACarregar = 0;
        int numeroDalinha = 0;
        try {

//            System.out.println("Nome do arquivo: " + arquivoDicionario.getPath());
            readerBase = new FileReader(arquivoDicionario);//Faz novaPal leitura do arquivo base
            bufferedReaderBase = new BufferedReader(readerBase);//Faz novaPal leitura do arquivo base

            String linha = bufferedReaderBase.readLine();

            StringTokenizer st;
            String termos[] = new String[2];
            int cont = 0;
            while (linha != null) {
                numeroDalinha++;
                st = new StringTokenizer(linha);
                linhaACarregar++;//Conta a qual está sendo lido do arquivo de dicionário.
                if (st.countTokens() != 2) {
                    dicio.clear();//Caso seja econtrado algum erro no arquivo de dicionario os termos atuais serão removidos do mapa
                    throw new DicionarioException("Inconsistência no arquivo do dicionário. Verificar Linha: " + linhaACarregar);
                }
                while (st.hasMoreTokens()) {
                    termos[cont++] = st.nextToken();
                }
                cont = 0;
                dicio.put(termos[0], termos[1]);

//                if (dicio.containsKey("ó")) {
//                    break;
//                }
                linha = bufferedReaderBase.readLine();
            }

        } catch (IOException e) {
//            Logger.getLogger(FraseVerso.class.getName()).log(Level.SEVERE, null, e);
            retorno = false;
        }
        //gerarDicionario();

        return retorno;

    }

    public HashMap getTermos() {
        return dicio;
    }

    public void imprimirDicionario() {
//        System.out.println("Entradas Dicionário:");
        int linha = 1;
        for (String obj : dicio.keySet()) {
//            System.out.printf("%s %s\n", obj, dicio.get(obj));
            if (obj.charAt(0) == 'ó') {
//                System.out.println("Achei+++++++++++++++++++++++++++++++++++++++++++: " + linha);
            }
            linha++;
        }
//        System.out.println("Processo Concluído!!");
    }

//    public static void main(final String[] args) {
//        String vertor[] = {"Teste"};
//        // gente precisa carregar as DLLs antes coloca ai pra carregar...blz
//         try {
//            Bridge.setVerbose(true);
//            Bridge.init();
//            File proxyAssemblyFile = new File("lib\\LapSeparatorJNI.j4n.dll");
//            Bridge.LoadAndRegisterAssemblyFrom(proxyAssemblyFile);
//            System.out.println("Carregando DLL....Pronto.");
//            //System.loadLibrary("jni4net.n.w64.v40-0.8.8.0");
//            //System.loadLibrary("LapSeparatorJNI.j4n");
//            // ele ta carregando ela na pasta dist e na lib e ta se perdendo
//            //agora vai da erro porque a pasta lib não tem no seu projeto, só quando gera o jar
//        } catch (IOException ioEx) {
//            System.out.println("Erro ao carregar a DLL");
//        }
//        String retorno[] = lapseparatorjni.TextAnalysisToolApp.Main(vertor);//aqui nessa linha
//    }
}
