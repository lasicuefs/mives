/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import mives.util.Utilitario;

/**
 *
 * @author Ricardo
 */
public class Separador {

    private Dicionario dicionario;
    private HashSet<String> algarismosRomanos;
    private HashSet<String> monossilabosAtonos;
    private HashSet<String> caracteresEspeciais;

    public Separador() {
        this.algarismosRomanos = new HashSet<>();
        this.monossilabosAtonos = new HashSet<>();
        caracteresEspeciais = new HashSet<>();
        this.carregarCaracteresEspeciais();
//        this.carregarMonossilabosAtonos();
        this.carregarAlgarismos();
        this.dicionario = Dicionario.getInstance();
    }

    public StringBuilder separarSilabasTexto(String palavra) {
//        System.out.println("separarSilabasTexto >>>> Recebendo para processar: " + palavra);
        StringBuilder retorno = new StringBuilder();
        String temp1, temp2;
        String simbolo = "";
        String aspasInicio = "";
        StringTokenizer st = new StringTokenizer(palavra);
        String p = null;
        inicio:
        while (st.hasMoreTokens()) {
            p = st.nextToken().toString();
            if (!p.contains("@")
                    && !p.contains("#")//Rever Primeira regra
                    && !p.contains("$") && !p.contains(" ")
                    && !p.matches("\\d\\w*\\d")//pode ser substituida pela última condição
                    && !p.matches("\\d\\w*.")//pode ser substituida pela última condição
                    && !p.matches("[(]\\d.\\w*.")
                    && !p.matches(".*\\d.*")) {
                //verificar se existem caracteres de pontuação na palavra
                if ((!((p.charAt(0) >= 'A' && p.charAt(0) <= 'Z')
                        || (p.charAt(0) >= 'a' && p.charAt(0) <= 'z'))) && !caracteresEspeciais.contains("" + p.charAt(0))) {
                    String simbInicio = extrairSimblosInicio(p);
                    retorno.append(simbInicio);
                    p = p.replace(simbInicio, "");
                }
                if (p.length() == 0) {
                    continue inicio;
                }
                //Se houver caracteres especiais no final da palavra remover e guardar em na variável simbolo
                if ((!((p.charAt(p.length() - 1) >= 'A' && p.charAt(p.length() - 1) <= 'Z')
                        || (p.charAt(p.length() - 1) >= 'a' && p.charAt(p.length() - 1) <= 'z'))) && !caracteresEspeciais.contains("" + p.charAt(p.length() - 1))) {
                    simbolo = extrairSimbolosPontuacaoFim(p);
                    p = p.replace(simbolo, "");
                }
//                System.out.println("Palava que será avaliada: " + p);
                if (p.matches("\\w*.-\\w.*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                    if (dicionario.getTermos().containsKey(p)) {
                        retorno.append(dicionario.getTermos().get(p).toString());
                    } else {
                        dicionario.adicionarTermo(p);
                        retorno.append(dicionario.getTermos().get(p).toString());
                    }
                    retorno.append(simbolo + " ");
                    simbolo = "";

                } else if (p.matches("\\w.+-\\w*") && p.length() > 3 && (!isAlgarismoRomano(p))) {
                    if (dicionario.getTermos().containsKey(p)) {
                        temp1 = aspasInicio + dicionario.getTermos().get(p).toString();
                        retorno.append(aspasInicio + dicionario.getTermos().get(p).toString() + " ");
                    } else {
                        dicionario.adicionarTermo(p);
                        retorno.append(dicionario.getTermos().get(p).toString() + " ");
                    }
                } else {
                    if (dicionario.getTermos().containsKey(p)) {
                        temp1 = dicionario.getTermos().get(p).toString();
                    } else {
                        dicionario.adicionarTermo(p);
//                        System.out.println("Tentando pegar a palavra: " + p);
                        if (dicionario.getTermos().containsKey(p)) {
                            temp1 = dicionario.getTermos().get(p).toString();
                        } else {
                            temp1 = p;
                        }
                    }
                    retorno.append(temp1 + simbolo + " ");
                    simbolo = "";
                }
                retorno.append(" ");
            } else {
                retorno.append(p + " ");
            }
            simbolo = "";
        }//Encerra o while aqui

        return retorno;
    }

    /**
     * Separa as sílabas da palavra ou da palavra utilizando um dicionário da
     * aplicação. Quando essa palavra não contém no dicionário, um rotina
     * externa será invocada.
     *
     * @param palavra
     * @return
     */
    public StringBuilder separarSilabasTextoV1(String palavra) {
//        System.out.println("REBENDO A PALAVRA: " + palavra);
        StringBuilder retorno = new StringBuilder();
        String palavraRetorno;
        String simbolo = "";
        String aspasInicio = "";
        String p = null;
        inicio:
        p = palavra;
        String simbInicio = "";
        if ((!((p.charAt(0) >= 'A' && p.charAt(0) <= 'Z')
                || (p.charAt(0) >= 'a' && p.charAt(0) <= 'z'))) && !caracteresEspeciais.contains(("" + p.charAt(0)).toLowerCase())) {
            simbInicio = extrairSimblosInicio(p);
            // retorno.append(simbInicio);//01/03/2017
            p = p.replace(simbInicio, "");
        }
        if (p.length() == 0) {

            return new StringBuilder(palavra);
        }
        //Se houver caracteres especiais no final da palavra remover e guardar em na variável simbolo
        if ((!((p.charAt(p.length() - 1) >= 'A' && p.charAt(p.length() - 1) <= 'Z')
                || (p.charAt(p.length() - 1) >= 'a' && p.charAt(p.length() - 1) <= 'z'))) && !caracteresEspeciais.contains("" + p.charAt(p.length() - 1))) {
            simbolo = extrairSimbolosPontuacaoFim(p);
            p = p.replace(simbolo, "");
        }

        if (p.length() == 0) {
            return new StringBuilder(palavra);
        }
        boolean Uppercase = false;
        if ((p.charAt(0) >= 'A' && p.charAt(0) <= 'Z')) {
            Uppercase = true;
        }
        try {
            if ((!isAlgarismoRomano(p))) {
                //System.out.println("A palavra contém: " + p.toLowerCase());
                if (dicionario.getTermos().containsKey(p.toLowerCase())) {
               //     System.out.println("A palavra existe");
                    palavraRetorno = dicionario.getTermos().get(p.toLowerCase()).toString();

                } else {
                   // System.out.println("A palavra não existe");
                    dicionario.adicionarTermo(p);
                    if (dicionario.getTermos().containsKey(p.toLowerCase())) {//01/03/2017
                        palavraRetorno = dicionario.getTermos().get(p.toLowerCase()).toString();
                    } else {
                        palavraRetorno = p;
                    }
                }
                if (Uppercase) {
                    if (palavraRetorno.charAt(0) == '#') {
                        palavraRetorno = palavraRetorno.replaceFirst(Pattern.quote(palavraRetorno.charAt(1) + ""), (palavraRetorno.charAt(1) + "").toUpperCase());
                    } else {
                        palavraRetorno = palavraRetorno.replaceFirst(Pattern.quote(palavraRetorno.charAt(0) + ""), (palavraRetorno.charAt(0) + "").toUpperCase());
                    }
                }
                retorno.append(simbInicio + palavraRetorno + simbolo + " ");
                simbolo = "";
                simbInicio = "";
            } else {
                retorno.append(simbInicio + p + simbolo + " ");
                simbolo = "";
                simbInicio = "";
            }

            return retorno;
        } catch (Exception ex) {
            System.out.println("ERRO DO SEPARADOR....");
            ex.printStackTrace();
        }
        return retorno;
    }

    private String extrairSimbolosPontuacaoFim(String palavra) {
        int indice = palavra.length() - 1;
        StringBuilder simbolos = new StringBuilder();
        while ((!((palavra.charAt(indice) >= 'A' && palavra.charAt(indice) <= 'Z')
                || (palavra.charAt(indice) >= 'a' && palavra.charAt(indice) <= 'z'))) && !caracteresEspeciais.contains("" + palavra.charAt(indice))) {
            simbolos.append(palavra.charAt(indice) + "");
            indice--;
            if (indice < 0) {
                break;
            }
        }

        return simbolos.reverse().toString();
    }

    private boolean isAlgarismoRomano(String token) {
//        System.out.println("Verificando algarismo romano: " + token);
//        System.out.println("É um algarismo: " + algarismosRomanos.contains(token));
        return algarismosRomanos.contains(token);
    }

    private void carregarAlgarismos() {
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
    private void carregarMonossilabosAtonos() {
        monossilabosAtonos.addAll(Arrays.asList("o", "a", "os", "as", "um", "uma", "umas", "uns", "me", "te", "se", "lhe", "nos", "vos", "lhes", "mo", "to", "lho", "que", "com", "de", "em", "por", "sem", "sob", "ao", "da", "do", "dos", "na", "no", "num", "e", "mas", "nem", "ou"));

    }

    private boolean isMonossilabosAtono(String token) {
        return Utilitario.isMonossilabosAtono(token);
    }

    private String extrairSimblosInicio(String palavra) {
        int indice = 0;
        StringBuilder simbolos = new StringBuilder();
        while (!((palavra.charAt(indice) >= 'A' && palavra.charAt(indice) <= 'Z')
                || (palavra.charAt(indice) >= 'a' && palavra.charAt(indice) <= 'z'))) {
            simbolos.append(palavra.charAt(indice) + "");
            indice++;
            if (indice > palavra.length() - 1) {
                break;
            }
        }

        return simbolos.toString();
    }

    private void carregarCaracteresEspeciais() {
        caracteresEspeciais.add("Ç");
        caracteresEspeciais.add("ü");
        caracteresEspeciais.add("é");
        caracteresEspeciais.add("â");
        caracteresEspeciais.add("ä");
        caracteresEspeciais.add("à");
        caracteresEspeciais.add("ç");
        caracteresEspeciais.add("ê");
        caracteresEspeciais.add("ë");
        caracteresEspeciais.add("è");
        caracteresEspeciais.add("ï");
        caracteresEspeciais.add("î");
        caracteresEspeciais.add("ì");
        caracteresEspeciais.add("Ä");
        caracteresEspeciais.add("É");
        caracteresEspeciais.add("ô");
        caracteresEspeciais.add("û");
        caracteresEspeciais.add("ù");
        caracteresEspeciais.add("ÿ");
        caracteresEspeciais.add("Ö");
        caracteresEspeciais.add("Ü");
        caracteresEspeciais.add("á");
        caracteresEspeciais.add("ù");
        caracteresEspeciais.add("ó");
        caracteresEspeciais.add("ú");
        caracteresEspeciais.add("ñ");
        caracteresEspeciais.add("Ñ");
        caracteresEspeciais.add("Á");
        caracteresEspeciais.add("Â");
        caracteresEspeciais.add("Ã");
        caracteresEspeciais.add("Ê");
        caracteresEspeciais.add("Ë");
        caracteresEspeciais.add("È");
        caracteresEspeciais.add("Í");
        caracteresEspeciais.add("Ï");
        caracteresEspeciais.add("Ó");
        caracteresEspeciais.add("õ");
        caracteresEspeciais.add("Õ");
        caracteresEspeciais.add("Ú");
        caracteresEspeciais.add("Ù");
        caracteresEspeciais.add("ý");
        caracteresEspeciais.add("Ý");

    }
}
