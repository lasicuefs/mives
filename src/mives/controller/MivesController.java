/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.io.File;
import java.util.HashMap;
import mives.arquivos.MapaConfiguracaoIO;
import mives.exceptions.DicionarioException;
import mives.exceptions.LivroException;
import mives.model.Dicionario;
import mives.model.Livro;
import mives.model.MapaConfiguracao;
import mives.model.Mineracao;
import mives.util.Grafico;
import mives.util.VersosAugusto;

/**
 *
 * @author Ricardo
 */
public class MivesController {

    private static MivesController mivesController = null;

    public static MivesController getInstance() {
        if (mivesController == null) {
            mivesController = new MivesController();
        }
        return mivesController;
    }

    private MivesController() {
        carregarMapasDeTipos();
    }

    public Livro carregarLivro(File arquivoDeOrigem) {
        Livro.getInstance().carregarTexto2(arquivoDeOrigem);
        return Livro.getInstance();

    }

    public void imprimirLivro(Livro livro) {
//        livro.imprimeLivro();
    }

    public void imprimeFrases(Livro livro) {
        livro.imprimeFrases();
    }

    public void minerarVersos(Livro livro, boolean buscaInicio, boolean buscarNoFim, boolean buscarFraseInteira) {
//        Mineracao mineracao = new Mineracao(livro);
//        mineracao.setBuscaNoFinal(false);
//        mineracao.setBuscaNoInicio(false);
//        mineracao.setBuscarFraseInteira(true);
//        mineracao.setTipoDeVerso(10);
//        mineracao.minerarVersos();
    }

    public void minerarVersos(Livro livro, boolean buscaInicio, boolean buscarNoFim, boolean buscarFraseInteira,
            String tipoDeVerso, boolean considerarPosicionamentoTonica, boolean considerarSinerese, boolean ConsiderarDierese,
            boolean considerarHiato, boolean considerarElisao) {
//        Mineracao mineracao = new Mineracao(livro);
//        mineracao.setBuscaNoFinal(buscaInicio);
//        mineracao.setBuscaNoInicio(false);
//        mineracao.setBuscarFraseInteira(true);
//        mineracao.setTipoDeVerso(Integer.parseInt(tipoDeVerso.split("-")[0].trim()));
//        mineracao.minerarVersos();
    }

    public void minerarVersosCustomizados(Livro livro, boolean buscaInicio, boolean buscarNoFim, boolean buscarFraseInteira,
            String tipoDeVersInicio, String tipoDeVersoFim, boolean considerarPosicionamentoTonica, boolean considerarSinerese, boolean ConsiderarDierese,
            boolean considerarHiato, boolean considerarElisao) throws LivroException {
        if (livro == null) {
            throw new LivroException("É necessário carregar o Livro.");
        }

        //   Mineracao mineracao = new Mineracao(livro, buscaInicio, buscarNoFim, buscarFraseInteira, tipos.get(tipoDeVersInicio), tipos.get(tipoDeVersoFim), considerarPosicionamentoTonica, considerarSinerese, ConsiderarDierese, considerarHiato, considerarElisao);
        Mineracao.getInstance().definirParametros(livro, buscaInicio, buscarNoFim, buscarFraseInteira, tipos.get(tipoDeVersInicio), tipos.get(tipoDeVersoFim), considerarPosicionamentoTonica, considerarSinerese, ConsiderarDierese, considerarHiato, considerarElisao);
        Mineracao.getInstance().minerarVersos();
        //  mineracao.minerarVersos();
    }

    public void imprimeVersos(Livro livro) {
        livro.imprimeVersos();
    }

    public void gerarLinhasEscandidas() {
        Livro.getInstance().gerarLinhasEscandidas();
    }

    public void imprimeFrasesComVersos(Livro livro) {
        livro.imprimeFrasesComVersos();
    }

    public void imprimeLivroEscandido(Livro livro) {
        livro.imprimeLivroEscandido();
    }

    public void imprimeLivroEscandido(Livro livro, File file) {
        livro.imprimeLivroEscandido(file);
    }

    public Dicionario carregarDicionario(File arquivoDeOrigem) throws DicionarioException {
        Dicionario dicionario = Dicionario.getInstance();
        if (dicionario.getTermos().isEmpty()) {
            dicionario.carregarTermos(arquivoDeOrigem);
        }
        return dicionario;
    }

    public boolean adicionarTermosAoDicionario(File arquivoDeOrigem) throws DicionarioException {
        Dicionario dicionario = Dicionario.getInstance();
        return dicionario.adicionarTermos(arquivoDeOrigem);
    }

    public void carregarRegraPadrao() {
        MapaConfiguracaoIO configuracaoIO = new MapaConfiguracaoIO();
        String local = System.getProperty("user.dir");
        File f = new File(System.getProperty("user.dir") + "/src/recurso/regraPadrao.xml");
        MapaConfiguracao.setMapaConfiguracao(configuracaoIO.ler(f));
    }

    public void imprimirEstaticasDaBusca(Livro livro) {
        livro.gerarEstatistica();
        livro.imprimirEstatisticasDeTipos();
    }

    public void imprimirEstatisticaPorPagina(Livro livro) {
        livro.gerarEstaticaComSubTiposPorPagina();
    }

    public void gerarGraficoDeBuscaSintetico(Livro livro, java.awt.Frame frame) {
        livro.gerarEstaticaComSubTiposPorPagina();
        livro.gerarEstatisticaDoLivro();
        Grafico graf = new Grafico();
        graf.gerarSintetico(livro.getEstatiticaGeral(), livro.getQuantidadeGeralDeVersos(), frame);
    }

    public void gerarGraficoDeBuscaAnalitico(Livro livro, java.awt.Frame frame) {
        livro.gerarEstaticaComSubTiposPorPagina();
        livro.gerarEstatisticaDoLivro();
        Grafico graf = new Grafico();
        graf.gerarAnalitico(livro.getEstatiticaGeral(), livro.getQuantidadeGeralDeVersos(), frame);
    }

    public int[] comparacaoDeBuscaFrasesCompletas(int tipo, Livro livro) {
        VersosAugusto versosAugusto = new VersosAugusto();
        return versosAugusto.processeEquivalencia(livro.getHashDeVersos(), tipo);
    }

    HashMap<String, Integer> tipos = new HashMap<>();

    private void carregarMapasDeTipos() {
        tipos.put("1. Monossílabo", 1);
        tipos.put("2. Dissílabo", 2);
        tipos.put("3. Trissílabo", 3);
        tipos.put("4. Tetrassílabo", 4);
        tipos.put("5. Pentassílabo", 5);
        tipos.put("6. Hexassílabo", 6);
        tipos.put("7. Heptassílabo", 7);
        tipos.put("8. Octossílabo", 8);
        tipos.put("9. Eneassílabo", 9);
        tipos.put("10.Decassílabo", 10);
        tipos.put("11.Hendecassílabo", 11);
        tipos.put("12.Dodecassílabo", 12);

    }

}
