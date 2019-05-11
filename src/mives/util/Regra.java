/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ricardo
 */
public class Regra {

    private BooleanProperty sim = new SimpleBooleanProperty(false);
    private BooleanProperty tente = new SimpleBooleanProperty(false);
    private BooleanProperty nunca = new SimpleBooleanProperty(false);

    private final SimpleStringProperty primeiraLetra;
    /**
     * 1 - Elisão 2 - Sinérese 3 - Diérese
     */
    private int tipoDeRegra;

    private String valor;

    public void setTipoDeRegra(int tipoDeRegra) {
        this.tipoDeRegra = tipoDeRegra;
    }
    private final SimpleStringProperty segundaLetra;
    private final SimpleObjectProperty<TableSampleBaseView.Participation> participation = new SimpleObjectProperty<TableSampleBaseView.Participation>();

    public Regra(int tipoDeRegra, String fName, String lName, TableSampleBaseView.Participation p) {
        this.tipoDeRegra = tipoDeRegra;
        this.primeiraLetra = new SimpleStringProperty(fName);
        this.segundaLetra = new SimpleStringProperty(lName);
        this.participation.setValue(p);
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(toString().toUpperCase());
    }

    public BooleanProperty simProperty() {
        return sim;
    }

    public BooleanProperty tenteProperty() {
        return tente;
    }

    public BooleanProperty nuncaProperty() {
        return nunca;
    }

    public int getTipoDeRegra() {
        return tipoDeRegra;
    }

    /**
     * @return the sim
     */
    public BooleanProperty getNunca() {
        return nunca;
    }

    /**
     * @return the sim
     */
    public BooleanProperty getSim() {
        return sim;
    }

    /**
     * @param sim the sim to set
     */
    public void setSim(boolean sim) {
        this.sim = new SimpleBooleanProperty(sim);
    }

    /**
     * @return the tente
     */
    public BooleanProperty getTente() {
        return tente;
    }

    /**
     * @param tente the tente to set
     */
    public void setTente(boolean tente) {
        this.tente = new SimpleBooleanProperty(tente);
    }

    public void setNunca(boolean nunca) {
        this.nunca = new SimpleBooleanProperty(nunca);
    }

    public void setParticipation(TableSampleBaseView.Participation p) {
        participation.set(p);
    }

    public TableSampleBaseView.Participation getParticipation() {
        return participation.get();
    }

    public String getPrimeiraLetra() {
        return primeiraLetra.get();
    }

    public void setPrimeiraLetra(String fName) {
        primeiraLetra.set(fName);
    }

    public String getSegundaLetra() {
        return segundaLetra.get();
    }

    public void setSegundaLetra(String fName) {
        segundaLetra.set(fName);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return (primeiraLetra.getValue() + segundaLetra.getValue()).toLowerCase();
    }
}
