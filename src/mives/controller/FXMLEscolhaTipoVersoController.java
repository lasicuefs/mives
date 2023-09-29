/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import mives.controller.helpers.FXMLEscolhaTipoVersoControllerHelper;
import mives.controller.helpers.utils.MivesWizardData;

/**
 *
 * @author Ricardo
 */
public class FXMLEscolhaTipoVersoController implements Initializable {

    @FXML
    ComboBox<String> comboInicio;

    @FXML
    ComboBox<String> comboFim;

    @FXML
    RadioButton radioCompleta;

    @FXML
    RadioButton radioInicio;

    @FXML
    RadioButton radioFinal;

    ToggleGroup group = new ToggleGroup();

    public RadioButton getRadioCompleta() {
        return radioCompleta;
    }

    public RadioButton getRadioInicio() {
        return radioInicio;
    }

    public RadioButton getRadioFinal() {
        return radioFinal;
    }

    FXMLEscolhaTipoVersoControllerHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//Default Monossílabo - Dodecassílabo
    	comboInicio.setValue("1. Monossílabo");
    	comboFim.setValue("12.Dodecassílabo");
        helper = new FXMLEscolhaTipoVersoControllerHelper(this);
        helper.capturarMetros();
        helper.carregarComboboxInicio();
        //Default sentença completa
        definirGruposRadioButton();
        helper.definirTipoDeSentenca();

    }

    @FXML
    protected void carregarComboboxFim() {
        int inicio = comboInicio.getSelectionModel().getSelectedIndex();
        MivesWizardData.TIPODEVERSOINICIO = comboInicio.getSelectionModel().getSelectedItem();
        helper.carregarComboboxFim(inicio);
    }

    @FXML
    protected void recuperarMetroSuperior() {
        MivesWizardData.TIPODEVERSOINICIO = comboFim.getSelectionModel().getSelectedItem();
    }

    @FXML
    protected void definiSentencaCompleta() {
        helper.definirTipoDeSentenca();

    }

    @FXML
    protected void definiInicioSentenca() {
        helper.definirTipoDeSentenca();
    }

    @FXML
    protected void definirFinalSentenca() {
        helper.definirTipoDeSentenca();
    }

    @FXML
    protected void capturarMetros() {
        helper.capturarMetros();
    }

    public ComboBox<String> getComboInicio() {
        return comboInicio;
    }

    public ComboBox<String> getComboFim() {
        return comboFim;
    }

    private void definirGruposRadioButton() {

        radioCompleta.setToggleGroup(group);
        radioCompleta.setSelected(true);
        radioInicio.setToggleGroup(group);
        radioFinal.setToggleGroup(group);

    }
}
