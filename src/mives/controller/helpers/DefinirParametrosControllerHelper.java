/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers;

import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import mives.arquivos.MapaConfiguracaoIO;
import mives.controller.FXMLDefinirParametrosController;
import mives.controller.MivesController;
import mives.model.MapaConfiguracao;
import mives.observable.CarregarRegrasObservable;
import mives.observable.Observer;
import mives.util.TableSampleBaseView;

/**
 *
 * @author Ricardo
 */
public class DefinirParametrosControllerHelper implements CarregarRegrasObservable {

    FXMLDefinirParametrosController controller;

    public DefinirParametrosControllerHelper(FXMLDefinirParametrosController controller) {
        this.controller = controller;
        carregarConfiguracaoPadrao();
        this.montarPaineisTab();
        this.controller.checkBoxUsoDoH.setSelected(true);

    }

    public void montarPaineisTab() {
        this.controller.salvar.setDisable(true);
        this.controller.salvarComo.setDisable(true);

        this.controller.painelAbas.getTabs().clear();
        System.out.println("Carregando Paineis de configuração");

        // this.controller.painelAbas = new TabPane();
        this.controller.elisao = new Tab("Elisão");
        this.controller.sinerese = new Tab("Sinérese");
        this.controller.dierese = new Tab("Diérese");

        this.controller.tableViewElisao = new TableSampleBaseView(1, MapaConfiguracao.getInstacia(), "Sinalefa/Elisão/Crase");
        this.controller.tableViewElisao.montarTabela();
        this.controller.tableViewSinerese = new TableSampleBaseView(2, MapaConfiguracao.getInstacia(), "Sinérese");
        this.controller.tableViewSinerese.montarTabela();
        this.controller.tableViewDirese = new TableSampleBaseView(3, MapaConfiguracao.getInstacia(), "Diérese");
        this.controller.tableViewDirese.montarTabela();

        this.controller.elisao.setClosable(false);
        this.controller.sinerese.setClosable(false);
        this.controller.dierese.setClosable(false);

        final VBox vboxElisao = new VBox();
        vboxElisao.setSpacing(5);
        vboxElisao.setPadding(new Insets(10, 0, 0, 10));
        vboxElisao.getChildren().addAll(this.controller.tableViewElisao.label, this.controller.tableViewElisao.table, this.controller.tableViewElisao.hb);

        final VBox vboxSinerese = new VBox();
        vboxSinerese.setSpacing(5);
        vboxSinerese.setPadding(new Insets(10, 0, 0, 10));
        vboxSinerese.getChildren().addAll(this.controller.tableViewSinerese.label, this.controller.tableViewSinerese.table, this.controller.tableViewSinerese.hb);

        final VBox vboxDierese = new VBox();
        vboxDierese.setSpacing(5);
        vboxDierese.setPadding(new Insets(10, 0, 0, 10));
        vboxDierese.getChildren().addAll(this.controller.tableViewDirese.label, this.controller.tableViewDirese.table, this.controller.tableViewDirese.hb);

        this.controller.painelAbas.getTabs().addAll(this.controller.elisao, this.controller.sinerese, this.controller.dierese);

        this.controller.painelAbas.setSide(Side.LEFT);
        this.controller.elisao.setContent(vboxElisao);
        this.controller.sinerese.setContent(vboxSinerese);
        this.controller.dierese.setContent(vboxDierese);

        System.out.println("Paineis carregados com sucesso");
    }

    File caminhaArquivoMapa = null;

    public void abrir() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Abrir arquivo de configuração...");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML", "xml"));
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                MapaConfiguracaoIO configuracaoIO = new MapaConfiguracaoIO();
                MapaConfiguracao.getInstacia().setMapaConfiguracao(configuracaoIO.ler(file));
                caminhaArquivoMapa = file;
                montarPaineisTab();
            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void salvar() {
        if (caminhaArquivoMapa != null) {
            MapaConfiguracaoIO configuracaoOI = new MapaConfiguracaoIO();
            configuracaoOI.salvarComo(MapaConfiguracao.getInstacia(), caminhaArquivoMapa);
        } else {
            System.out.println("Chamando o salvar como...");
            salvarComo();
        }

        this.controller.salvar.setDisable(true);
    }

    public void confirmar() {
        notifyObservers();
        
    }

    public void salvarComo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("MIVES - Salvar arquivo de configuração...");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML", "xml"));
        try {
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                this.caminhaArquivoMapa = file;
                MapaConfiguracaoIO configuracaoOI = new MapaConfiguracaoIO();
                MapaConfiguracao.getInstacia().setTipo(2);
                configuracaoOI.salvarComo(MapaConfiguracao.getInstacia(), file);
                this.controller.salvar.setDisable(false);
            } else {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void carregarConfiguracaoPadrao() {
        MivesController.getInstance().carregarRegraPadrao();
        this.controller.salvar.setDisable(false);
    }

    public void utilizarOH() {
        if (this.controller.checkBoxUsoDoH.isSelected()) {
            MapaConfiguracao.getInstacia().setConsiderarH(true);
        } else {
            MapaConfiguracao.getInstacia().setConsiderarH(false);
        }
        this.controller.salvar.setDisable(false);
    }

    ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(1);
        }
    }

}
