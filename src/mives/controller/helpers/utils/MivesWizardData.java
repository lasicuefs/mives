/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import mives.model.Livro;

/**
 *
 * @author Ricardo
 */
public class MivesWizardData {

    public BooleanProperty instrucoes = new SimpleBooleanProperty();
    public BooleanProperty escolhaInicial = new SimpleBooleanProperty();
    public BooleanProperty carregarLivro = new SimpleBooleanProperty();
    public BooleanProperty parametros = new SimpleBooleanProperty();
    public BooleanProperty tipoVerso = new SimpleBooleanProperty();
    public BooleanProperty tipoBusca = new SimpleBooleanProperty();

    public static boolean FRASECOMPLETA = false;
    public static boolean INICIOFRASE = false;
    public static boolean FINALFRASE = false;

    public static boolean TIPOVERSO = false;

    public static String TIPODEVERSOINICIO;
    public static String TIPODEVERSOFINAL;

    public static String getLocalBusca() {
        if (FRASECOMPLETA) {
            return "Sentenças Completa";
        } else if (INICIOFRASE) {
            return "Início de Sentenças";
        } else if (FINALFRASE) {
            return "Final de Sentenças";
        }
        return null;
    }

    private static MivesWizardData instance;

    private MivesWizardData() {

    }

    public static MivesWizardData getInstance() {
        if (instance == null) {
            instance = new MivesWizardData();
        }
        return instance;
    }

    public static boolean isHabilitarBotaoFinalizar() {

        if ((FRASECOMPLETA || INICIOFRASE || FINALFRASE)
                && TIPOVERSO
                && (Livro.getInstance().getPaginas().size() > 0)) {
            return true;
        }
        return false;

    }

}
