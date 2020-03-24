/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 *
 * @author Ricardo
 */
public class Utilitario {

    private static HashSet<String> monossilabosAtonos = new HashSet<>();

    private static HashMap<Integer, String> classificacao = new HashMap<>();
    private static HashSet<String> vogais = new HashSet<>();
    private static HashSet<String> vogaisEntrada = new HashSet<>();//Utilizado apenas para validar a entrada de vogais na interface de configuração.

    public static void carregarClassificacao() {
        classificacao.put(6, "Hexassílabo");
        classificacao.put(7, "Heptassílabo");
        classificacao.put(8, "Octossílabo");
        classificacao.put(9, "Eneassílabo");
        classificacao.put(11, "Hendecassílabo");
        classificacao.put(10, "Decassílabo");
        classificacao.put(12, "Dodecassílabo");
        classificacao.put(13, "Bárbaro");
    }

    public static String getClassificacao(int quantidadeDeSilabas) {
        carregarClassificacao();
        return classificacao.get(quantidadeDeSilabas);
    }

    public static boolean isSimboolo(String str) {
        if (str.length() == 1
                && ((str.charAt(0) == '.')
                || (str.charAt(0) == ',')
                || (str.charAt(0) == '!')
                || (str.charAt(0) == '?')
                || (str.charAt(0) == ':')
                || (str.charAt(0) == ';'))) {
            return true;
        }
        return false;
    }

    public static String[] preencherVetorTokenV2(StringTokenizer st) {

        int tamanho = st.countTokens();
        StringBuilder palavras[] = new StringBuilder[tamanho];
        int i = 0;
        StringBuilder temp = new StringBuilder();
        while (st.hasMoreTokens()) {
            temp = new StringBuilder(st.nextToken());
            if (isSimboolo(temp.toString()) && i > 0) {
                palavras[i - 1].append(temp);
            } else {
                palavras[i++] = (StringBuilder) temp;
            }
        }

        String retorno[] = new String[i];
        for (tamanho = 0; tamanho < i; tamanho++) {
//            System.out.println("Transferindo: " + palavras[tamanho].toString() + " - Tamanho: " + tamanho);
            retorno[tamanho] = palavras[tamanho].toString();
        }
        return retorno;
    }

    private static boolean isPalavraGenuina(String str) {
        if (str.contains(".") && str.charAt(str.length() - 1) != '.') {
            return false;
        }
        return true;
    }

    static HashSet<String> caracteres = new HashSet<String>();

    public static void carregarSimbolos() {
        caracteres.add(",");
        caracteres.add("?");
        caracteres.add("º");
        caracteres.add("ª");
        caracteres.add("%");
        caracteres.add("$");
        caracteres.add("#");

    }

    public static boolean isVogal(String token) {
        carregarVogais();
        return vogais.contains(token.toLowerCase());
    }

    private static void carregarVogais() {
        vogais.addAll(Arrays.asList("a", "e", "o", "á", "é", "ó", "í", "ú", "ã", "õ", "â", "ê", "ô", "à", "ü", "u"));
    }

    /**
     * Verifica se o caracter de entrada é realmente uma vogal
     *
     * @param token
     * @return
     */
    public static boolean isVogalInterface(String token) {
        carregarVogaisInterface();
        return vogaisEntrada.contains(token.toLowerCase());
    }

    private static void carregarVogaisInterface() {
        vogaisEntrada.addAll(Arrays.asList("i", "a", "e", "o", "á", "é", "ó", "í", "ú", "ã", "õ", "â", "ê", "ô", "à", "ü", "u"));
    }

    private static ArrayList<String> separarPalavraPontos(String str) {
        int tamanho = str.length();
        int i = 0;
        StringBuilder temp = new StringBuilder();
        ArrayList<String> listString = new ArrayList<>();
        for (i = 0; i < tamanho; i++) {
            if (str.charAt(i) != '.' && !(i == tamanho - 1)) {
                temp.append(str.charAt(i));
            } else if (str.charAt(i) == '.' || i == tamanho - 1) {
//                    if (!(i == tamanho - 1) && (str.charAt(i + 1) == ',')) {
                if (!(i == tamanho - 1) && caracteres.contains("" + str.charAt(i + 1))) {
//                            (str.charAt(i + 1) == ',')) {
                    temp.append(str.charAt(i));
                    temp.append(str.charAt(i + 1));
                    i++;
                    if (i == tamanho - 1) {
                        listString.add(temp.toString());
                    }
                } else {
                    temp.append(str.charAt(i));
                    listString.add(temp.toString());
                    temp = new StringBuilder();
                }
            }
        }
//        System.out.println(listString.size());
        return listString;
    }

    public static String[] preencherVetorToken(StringTokenizer st) {
        int tamanho = 0;
        ArrayList<StringBuilder> palavras = new ArrayList<StringBuilder>();
        StringBuilder temp = new StringBuilder();
        int i = 0;
        while (st.hasMoreTokens()) {
            temp = new StringBuilder(st.nextToken());
//            System.out.println("Pegando a palavra: " + temp);
            if (isSimboolo(temp.toString()) && i > 0) {
                palavras.get(palavras.size() - 1).append(temp);
            } else if (isPalavraGenuina(temp.toString())) {
                //  System.out.println("Adicionando: = " + temp);
                palavras.add(temp);
                i++;
            } else {
//                System.out.println("Estou aqui com a palavra: " + temp);
                for (String str : separarPalavraPontos(temp.toString())) {
//                    System.out.println("Adicionando: = " + str);
                    palavras.add(new StringBuilder(str));
                    i++;
                }
            }
        }
        String retorno[] = new String[palavras.size()];
        for (StringBuilder strb : palavras) {
            retorno[tamanho++] = strb.toString();
        }
        return retorno;
    }

    /**
     * Lembrar de pesquisar as exceções: dum,
     *
     * ó Interj. Us. para chamar alguém, para conciliar-lhe atenção, para
     * invocar, e, ainda, para exprimir vários afetos e impressões da alma:
     * "Deus! ó Deus! onde estás que não respondes? (Castro Alves, Obras
     * Completas, p 209). - Novo Dicionário Aurélio da Língua Portuguesa.
     *
     * Palavra "nos" - Pode ser contração de no em+o Pronome demostrativo nas no
     * pronome pessoal nas no Fonte: Portal da língua portuguesa.
     *
     * Ver palavra "para"
     */
    protected static void carregarMonossilabosAtonos() {
        monossilabosAtonos.addAll(Arrays.asList("o", "a", "os", "as", "um", "uns",
                "me", "te", "se", "lhe", "nos", "vos", "lhes", "mo", "to", "lho",
                "que", "com", "de", "em", "por", "sem", "sob", "ao", "da", "das", "do", "dos",
                "na", "no", "num", "e", "mas", "nem", "ou", "dom", "frei", "são", "seu", "seus", "dum", "ó", "nos", "nas", "para"));
    }

    public static boolean isMonossilabosAtono(String token) {
        carregarMonossilabosAtonos();
//        System.out.println("CONSULTANDO...................................: " + token);
        return monossilabosAtonos.contains(token.toLowerCase());

    }

    public static boolean existeUmaPalavra(String palavra) {
        char[] letras = palavra.toUpperCase().toCharArray();
        for (int i = 0; i < letras.length; i++) {
            if(letras[i]>=65 && letras[i]<=90){
                return true;
            }
        }
        return false;
    }
}
