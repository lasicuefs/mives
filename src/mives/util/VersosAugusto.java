/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.util.HashSet;

/**
 *
 * @author Ricardo
 */
public class VersosAugusto {

    private HashSet<String> decassilabosFrases;
    private HashSet<String> decassilaboInicio;
    private HashSet<String> decassilaboFim;
    private HashSet<String> dodecassilaboFim;

    public VersosAugusto() {
        decassilabosFrases = new HashSet<>();
        decassilaboInicio = new HashSet<>();
        decassilaboFim = new HashSet<>();
        dodecassilaboFim = new HashSet<>();
        carregarDecassilabosDeFrases();
        carregarDecadeInicioDeOracao();
        carregarDecassilabosFim();
        carregarDodecassilabosFim();
    }

    private void carregarDodecassilabosFim() {
        dodecassilaboFim.add("para o desenho dos relevos estupendos.".toLowerCase()); //Corrigido no texto
        dodecassilaboFim.add("para abranger de um lance o conjunto da terra.".toLowerCase());//ok
        dodecassilaboFim.add("a translação dos membros desarticulados.".toLowerCase());//Corrigido no texto
        dodecassilaboFim.add("a criação bizarra de um centauro bronco:".toLowerCase());//Não vai encontrar
        dodecassilaboFim.add("ruído soturno e longo de trovão longínquo...".toLowerCase());//oK
        dodecassilaboFim.add("evaporando-se sugado pelo solo.".toLowerCase());
        dodecassilaboFim.add("não se lhe afrouxa, tão de pronto, o ânimo.".toLowerCase());//13 sílabas
        dodecassilaboFim.add("nas noites misteriosas de luares claros.".toLowerCase());
        dodecassilaboFim.add("mais alto que as mais altas catedrais da terra.".toLowerCase());//OK
        dodecassilaboFim.add("se acotovelam gênios e degerados.".toLowerCase());//OK??
        dodecassilaboFim.add("entre a caverna primitiva e a casa.".toLowerCase());//14 sílabas
        dodecassilaboFim.add("uma torrente escura transudando raios...".toLowerCase());//oK
        dodecassilaboFim.add("matéria-prima dos períodos retumbantes.".toLowerCase());//oK
        dodecassilaboFim.add("homens inermes carregando armas magníficas.".toLowerCase());//ok
        dodecassilaboFim.add("o homem que foge à morte e o homem que quer matar.".toLowerCase());//Ok??? - Pegar o artigo
        dodecassilaboFim.add("dentro de cinco mil casebres em ruínas.".toLowerCase());
        dodecassilaboFim.add("ressudava o salmear merencório das rezas.".toLowerCase());//Ok
        dodecassilaboFim.add("sob uma irradiação de golpes e de tiros.".toLowerCase());//Analisar
        dodecassilaboFim.add("coberto ainda da poeira das batalhas.".toLowerCase());//14 Sílabas
        dodecassilaboFim.add("um desmedido semicírculo de assédio.".toLowerCase());//OK
        dodecassilaboFim.add("naquela imensa ruinaria de Canudos.".toLowerCase());//15 sílabas
        dodecassilaboFim.add("predispunham-se a um suicídio formidável.".toLowerCase());//13 Sílabas
        dodecassilaboFim.add("num ondear longínquo de chapadas...".toLowerCase());//14 Sílabas
        dodecassilaboFim.add("um estalar de chifres embatendo...".toLowerCase());
        dodecassilaboFim.add("um ondular de risos mal contidos...".toLowerCase());//14 Sílabas
        dodecassilaboFim.add("um cascalhar de risos abafados.".toLowerCase());//13 sílabas
        dodecassilaboFim.add("um longo uivar de ventania forte".toLowerCase());//17 sílabas
        dodecassilaboFim.add("um convulsivo pervagar de sombras;".toLowerCase());//Não vai encontrar
        dodecassilaboFim.add("num delírio de curvas incorretas.".toLowerCase());
        dodecassilaboFim.add("num desmoronamento secular e lento.".toLowerCase());//oK
        dodecassilaboFim.add("num vaivém de avançadas e recuos.".toLowerCase());
        dodecassilaboFim.add("numa dissipação inglória de valor.".toLowerCase());//ok
        dodecassilaboFim.add("num longo enxurro de carcaças e molambos...".toLowerCase());//oK
        dodecassilaboFim.add("caíam genuflexos sobre o chão aspérrimo.".toLowerCase());//ok
        dodecassilaboFim.add("as noites sobrevinham frigidíssimas.".toLowerCase());//13 Sílabas
        dodecassilaboFim.add("um círculo vicioso crudelíssimo.".toLowerCase());//14 Sílabas
        dodecassilaboFim.add("retumbou a atroada de explosões fortíssimas.".toLowerCase());//ok
        dodecassilaboFim.add("num delírio de curvas incorretas.".toLowerCase());
        dodecassilaboFim.add("num desmoronamento secular e lento.".toLowerCase());//ok - A comparação do algoritmo não consegue encontrar
        dodecassilaboFim.add("num vaivém de avançadas e recuos.".toLowerCase());
        dodecassilaboFim.add("numa dissipação inglória de valor.".toLowerCase());//Ok
        dodecassilaboFim.add("num longo enxurro de carcaças e molambos...".toLowerCase());//Ok - A comparação do algoritmo não consegue encontrar
        dodecassilaboFim.add("caíam genuflexos sobre o chão aspérrimo.".toLowerCase());//Ok
        dodecassilaboFim.add("as noites sobrevinham frigidíssimas.".toLowerCase());//13 Sílabas
        dodecassilaboFim.add("um círculo vicioso crudelíssimo.".toLowerCase());//14 Sílabas
        dodecassilaboFim.add("retumbou a atroada de explosões fortíssimas.".toLowerCase());//Ok - A comparação do algoritmo não consegue encontrar

    }

    private void carregarDecassilabosFim() {
        decassilaboFim.add("a ossatura partida das montanhas.".toLowerCase());//Ok??
        decassilaboFim.add("o aspecto atormentado das paisagens.".toLowerCase());//Ok??
        decassilaboFim.add("e entoando a cantiga predileta...".toLowerCase());//OK??
        decassilaboFim.add("o mesmo batedor sinistro, o incêndio.".toLowerCase());//ok
        decassilaboFim.add("um círculo vicioso de catástrofes.".toLowerCase());//OK??
        decassilaboFim.add("a norma verticalis dos jagunços.".toLowerCase());//Ok
        decassilaboFim.add("a fealdade típica dos fracos.".toLowerCase());//Ok
        decassilaboFim.add("os meandros das trilhas sertanejas.".toLowerCase());//Ok
        decassilaboFim.add("é um vitorioso jovial e forte.".toLowerCase());
        decassilaboFim.add("o espantalho das secas no sertão.".toLowerCase());//Ok
        decassilaboFim.add("a insurreição da terra contra o homem.".toLowerCase());//ok
        decassilaboFim.add("melopéia plangente dos benditos.".toLowerCase());//ok
        decassilaboFim.add("finge que ora, remascando cifras.".toLowerCase());//ok
        decassilaboFim.add("lembra uma enorme escadaria em ruínas.".toLowerCase());
        decassilaboFim.add("na ossatura rígida da serra.".toLowerCase());
        decassilaboFim.add("em arremessos doidos contra o vácuo.".toLowerCase());//Ok
        decassilaboFim.add("entre caída multidão de espectros...".toLowerCase());//Ok
        decassilaboFim.add("desafiando as últimas granadas;".toLowerCase());
        decassilaboFim.add("a palavra de ordem da desordem.".toLowerCase());//OK
        decassilaboFim.add("era a caricatura do heroísmo.".toLowerCase());//Ok
        decassilaboFim.add("em longo risco negro e tortuoso.".toLowerCase());
        decassilaboFim.add("a onda rugidora dos jagunços.".toLowerCase());//Ok
        decassilaboFim.add("um empolgante lance teatral.".toLowerCase());
        decassilaboFim.add("feitas a um tempo lares e redutos.".toLowerCase());//Ok
        decassilaboFim.add("caía-se num dédalo de sangas.".toLowerCase());//Ok
        decassilaboFim.add("ao minotauro de seis mil estômagos.".toLowerCase());
        decassilaboFim.add("sem que o jejum lhes sopeasse o arrojo.".toLowerCase());//OK??
        decassilaboFim.add("o derradeiro ímpeto da tropa.".toLowerCase());
        decassilaboFim.add("na praxe costumeira das tocaias:".toLowerCase());
        decassilaboFim.add("desafiando um choque braço a braço.".toLowerCase());//Ok
        decassilaboFim.add("malbaratado a vida em toda a linha.".toLowerCase());//Ok
        decassilaboFim.add("moribundos e desafiando a morte.".toLowerCase());//Ok
        decassilaboFim.add("feito um rasgão no firmamento escuro.".toLowerCase());//ok
        decassilaboFim.add("a luta mais brutal dos nossos tempos.".toLowerCase());//OK
        decassilaboFim.add("laivos rubros de calças carmesins...".toLowerCase());//Ok
        decassilaboFim.add("irrefreáveis frêmitos de espanto.".toLowerCase());//Ok
        decassilaboFim.add("distante dois quilômetros -Canudos...".toLowerCase());
        decassilaboFim.add("vigilante, tenteando a pontaria.".toLowerCase());//Ok
        decassilaboFim.add("a moldura lutuosa dos incêndios;".toLowerCase());
        decassilaboFim.add("se acolheram os últimos jagunços.".toLowerCase());//ok
        decassilaboFim.add("devorando talvez os próprios donos.".toLowerCase());//OK
        decassilaboFim.add("o templo monstruoso dos jagunços.".toLowerCase());//Ok??
        decassilaboFim.add("seguindo a extensa fila de infelizes...".toLowerCase());//Ok
    }

    private void carregarDecadeInicioDeOracao() {
        decassilaboInicio.add("Varada a estreita faixa dos cerrados,".toLowerCase());//Ok
        decassilaboInicio.add("À luz crua dos dias sertanejos,".toLowerCase());//Ok
        decassilaboInicio.add("Na postura, no gesto, na palavra,".toLowerCase());//Ok
        decassilaboInicio.add("Rebrilham longas noites nas chapadas,".toLowerCase());//Ok
        decassilaboInicio.add("À noite, a suçuarana traiçoeira e ladra,".toLowerCase());//Não
        decassilaboInicio.add("Espécie de grande homem pelo avesso,".toLowerCase());//ok
        decassilaboInicio.add("Chegavam, estropiados, da jornada longa,".toLowerCase());//Não
        decassilaboInicio.add("Na claridade amortecida dos braseiros,".toLowerCase());//Ok
        decassilaboInicio.add("A religiosidade ingênua dos matutos".toLowerCase());//Não
        decassilaboInicio.add("Vêem-se as capelinhas alvas, que a pontilham".toLowerCase());//Ok
        decassilaboInicio.add("A natureza toda quedava-se imóvel".toLowerCase());//Ok
        decassilaboInicio.add("O tropear soturno das fileiras".toLowerCase());//Ok
        decassilaboInicio.add("E nesse prosseguir tumultuário,".toLowerCase());//Ok
        decassilaboInicio.add("Dentro da claridade morta do crepúsculo".toLowerCase());//Ok
        decassilaboInicio.add("Os demais, sucumbidos de fadigas,".toLowerCase());//Ok
        decassilaboInicio.add("O Sol irradiava a pino sobre a terra,".toLowerCase());//ok - Dodecassilábo
        decassilaboInicio.add("Transverberando nas rochas expostas,".toLowerCase());//Ok
        decassilaboInicio.add("Sob a adustão dos dias ardentíssimos,".toLowerCase());//Ok

    }

    private void carregarDecassilabosDeFrases() {
        decassilabosFrases.add("É uma paragem impressionadora.".toLowerCase());//OK
        decassilabosFrases.add("É uma mutação de apoteose.".toLowerCase());//OK
        decassilabosFrases.add("Entra-se, de surpresa, no deserto.".toLowerCase());//ok
        decassilabosFrases.add("É a escarpa abrupta e viva dos planaltos.".toLowerCase());//OK
        decassilabosFrases.add("É o homem permanentemente fatigado.".toLowerCase());//OK
        decassilabosFrases.add("Vimo-lo neste steeple-chase bárbaro.".toLowerCase());//ok
        decassilabosFrases.add("Decorre-lhes a vida variada e farta.".toLowerCase());//Não
        decassilabosFrases.add("E assim passam numa agitação estéril.".toLowerCase());//ok
        decassilabosFrases.add("É o prelúdio da sua desgraça.".toLowerCase());//OK
        decassilabosFrases.add("É mais um inimigo a suplantar.".toLowerCase());//OK
        decassilabosFrases.add("Esta ilusão é empolgante ao longe.".toLowerCase());//ok
        decassilabosFrases.add("Ali estava — defronte o sertão...".toLowerCase());//Ok
        decassilabosFrases.add("Correra nos sertões um toque de chamada...".toLowerCase());//ok
        decassilabosFrases.add("Foi uma diversão gloriosa e rápida.".toLowerCase());//Não
        decassilabosFrases.add("Não o combate; cansa-o; Não o vence; esgota-o".toLowerCase());
        decassilabosFrases.add("Irrompiam, troteando, no terreiro...".toLowerCase());//ok
        decassilabosFrases.add("E o dia derivou tranquilamente.".toLowerCase());//Ok
        decassilabosFrases.add("Era a suprema petulância do bandido!".toLowerCase());//OK
        decassilabosFrases.add("Um primor de estatuária modelado em lama.".toLowerCase());//Não
        decassilabosFrases.add("Terminara afinal a luta crudelíssima...".toLowerCase());//Ok
        decassilabosFrases.add("O sertanejo é, antes de tudo, um forte.".toLowerCase());//Não
        decassilabosFrases.add("O vaqueiro do Norte é a sua antítese.".toLowerCase());//Não
        decassilabosFrases.add("Fez-se homem, quase sem ter sido criança.".toLowerCase());//ok
        decassilabosFrases.add("Era a objetivação daquela insânia imensa.".toLowerCase());//Não
        decassilabosFrases.add("Confudia-se com o próprio chão.".toLowerCase());//ok
        decassilabosFrases.add("Porque o Cambaio é uma montanha em ruínas.".toLowerCase());//Não
        decassilabosFrases.add("A travessia foi penosamente feita.".toLowerCase());//Não
        decassilabosFrases.add("Far-se-ia o expoente da nevrose.".toLowerCase());//Não
        decassilabosFrases.add("Renovou-se a investida febrilmente.".toLowerCase());//Ok
        decassilabosFrases.add("Ninguém os vê; ninguém os pode ver.".toLowerCase());//Ok
        decassilabosFrases.add("Assaltam-no; aferram-no; jugulam-no.".toLowerCase());//Ok
        decassilabosFrases.add("Tinha o que quer que fosse de um castigo.".toLowerCase());//OK
        decassilabosFrases.add("Era um parêntese; era um hiato; era um vácuo.".toLowerCase());
        decassilabosFrases.add("Esvaziou-se, de repente, a praça.".toLowerCase());//Ok
        decassilabosFrases.add("Os demais prosseguiam impassíveis.".toLowerCase());//Ok
        decassilabosFrases.add("Octogenário, não se lhe dobrava o tronco.".toLowerCase());//Não
        decassilabosFrases.add("Vimos como quem vinga uma montanha altíssima.".toLowerCase());//Não

    }

    private int[] decassilabosDeFrases(HashSet<String> decassilabosVersosEncontrados) {
        int quantidade = 0;
        for (String key : decassilabosFrases) {
            if (decassilabosVersosEncontrados.contains(key.toLowerCase())) {
                System.out.println("Chave correspondente: " + key);
                quantidade++;
            }
        }
        int retorno[] = {quantidade, decassilabosFrases.size()};
        return retorno;
    }

    private int[] decassilabosDeFrasesInicio(HashSet<String> decassilabosVersosEncontrados) {
        int quantidade = 0;
        for (String key : decassilaboInicio) {
            if (decassilabosVersosEncontrados.contains(key.toLowerCase())) {
                System.out.println("Chave correspondente: " + key);
                quantidade++;
            }
        }
        int retorno[] = {quantidade, decassilaboInicio.size()};
        return retorno;
    }

    private int[] decassilabosDeFrasesFim(HashSet<String> decassilabosVersosEncontrados) {
        int quantidade = 0;
        for (String key : decassilaboFim) {
            if (decassilabosVersosEncontrados.contains(key.toLowerCase())) {
                System.out.println("Chave correspondente: " + key);
                quantidade++;
            }
        }
        int retorno[] = {quantidade, decassilaboFim.size()};
        return retorno;
    }

    private int[] dodecassilabosDeFrasesFim(HashSet<String> decassilabosVersosEncontrados) {
        int quantidade = 0;
        for (String key : dodecassilaboFim) {
            if (decassilabosVersosEncontrados.contains(key.toLowerCase())) {
                System.out.println("Chave correspondente: " + key);
                quantidade++;
            }
        }
        int retorno[] = {quantidade, dodecassilaboFim.size()};
        return retorno;
    }

    public int[] processeEquivalencia(HashSet<String> decassilabosVersosEncontrados, int tipo) {
        if (tipo == 1) {
            return decassilabosDeFrases(decassilabosVersosEncontrados);
        } else if (tipo == 2) {
            return decassilabosDeFrasesInicio(decassilabosVersosEncontrados);
        } else if (tipo == 3) {
            return decassilabosDeFrasesFim(decassilabosVersosEncontrados);
        } else if (tipo == 4) {
            return dodecassilabosDeFrasesFim(decassilabosVersosEncontrados);
        }
        return null;
    }

}
