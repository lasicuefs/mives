/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mives.controller.helpers.DefinirParametrosControllerHelper;
import mives.observable.Observer;
import mives.util.TableSampleBaseView;

/**
 *
 * @author Ricardo
 */
public class FXMLDefinirParametrosController implements Initializable {

    @FXML
    public Button abrir;

    @FXML
    public Button salvar;

    @FXML
    public Button confirmar;

    @FXML
    public Button salvarComo;

    @FXML
    public CheckBox checkBoxUsoDoH;

    @FXML
    public TabPane painelAbas;
    public Tab elisao;
    public Tab sinerese;
    public Tab dierese;
    public TableSampleBaseView tableViewElisao;
    public TableSampleBaseView tableViewSinerese;
    public TableSampleBaseView tableViewDirese;

    public DefinirParametrosControllerHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new DefinirParametrosControllerHelper((this));
    }

    @FXML
    public void montarPaineis(ActionEvent e) {
        System.out.println("Chamando montar paineis");
        helper.montarPaineisTab();
    }

    @FXML
    public void abrirMapa(ActionEvent e) {
        helper.abrir();
    }

    @FXML
    public void salvarMapa(ActionEvent e) {
        helper.salvar();
    }

    @FXML
    public void confirmarMapa(ActionEvent e) {
        helper.confirmar();
    }

    @FXML
    public void salvarComoMapa(ActionEvent e) {
        helper.salvarComo();
    }

    @FXML
    public void utilizarOH(ActionEvent e) {
        helper.utilizarOH();
    }

   
}
