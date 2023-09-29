/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mives.controller.FXMLEscolhaTipoVersoController;
import mives.controller.helpers.utils.MivesWizardData;
import mives.controller.helpers.utils.Revista;

/**
 *
 * @author Ricardo
 */
public class FXMLEscolhaTipoVersoControllerHelper {

    FXMLEscolhaTipoVersoController controller;

    ObservableList<String> tipoVerso
            = FXCollections.observableArrayList(
                    "1. Monossílabo",
                    "2. Dissílabo",
                    "3. Trissílabo",
                    "4. Tetrassílabo",
                    "5. Pentassílabo",
                    "6. Hexassílabo",
                    "7. Heptassílabo",
                    "8. Octossílabo",
                    "9. Eneassílabo",
                    "10.Decassílabo",
                    "11.Hendecassílabo",
                    "12.Dodecassílabo"
            );

    public FXMLEscolhaTipoVersoControllerHelper(FXMLEscolhaTipoVersoController controller) {
        this.controller = controller;
    }

    public void carregarComboboxInicio() {
        controller.getComboInicio().setItems(tipoVerso);
    }

    public void carregarComboboxFim(int inicio) {
        controller.getComboFim().getItems().clear();
        controller.getComboFim().setValue("12.Dodecassílabo");
        for (int i = inicio; i < tipoVerso.size(); i++) {
            controller.getComboFim().getItems().add(tipoVerso.get(i));
        }

    }

    public void definirTipoDeSentenca() {
        MivesWizardData.FRASECOMPLETA = controller.getRadioCompleta().isSelected();
        MivesWizardData.INICIOFRASE = controller.getRadioInicio().isSelected();
        MivesWizardData.FINALFRASE = controller.getRadioFinal().isSelected();

    }

    public void capturarMetros() {

        if (controller.getComboInicio().getSelectionModel().getSelectedItem() != null
                && controller.getComboFim().getSelectionModel().getSelectedItem() != null) {
            MivesWizardData.TIPODEVERSOINICIO = controller.getComboInicio().getSelectionModel().getSelectedItem();
            MivesWizardData.TIPODEVERSOFINAL = controller.getComboFim().getSelectionModel().getSelectedItem();
            MivesWizardData.TIPOVERSO = true;
            Revista.notificar();
        }
    }

}
