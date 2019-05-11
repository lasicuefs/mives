/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import javafx.scene.chart.XYChart;

/**
 *
 * @author Ricardo
 */
public class SerieGraficoDistancia {

    private Integer numeroDaFrase;
    private Integer distanciaAntes;
    //private XYChart.Series series = new XYChart.Series();

    public SerieGraficoDistancia() {

    }

    public SerieGraficoDistancia(Integer numeroDaFrase, Integer distanciaAntes) {
        this.numeroDaFrase = numeroDaFrase;
        this.distanciaAntes = distanciaAntes;
    }

    /**
     * @return the numeroDaFrase
     */
    public Integer getNumeroDaFrase() {
        return numeroDaFrase;
    }

    /**
     * @param numeroDaFrase the numeroDaFrase to set
     */
    public void setNumeroDaFrase(Integer numeroDaFrase) {
        this.numeroDaFrase = numeroDaFrase;
    }

    /**
     * @return the distanciaAntes
     */
    public Integer getDistanciaAntes() {
        return distanciaAntes;
    }

    /**
     * @param distanciaAntes the distanciaAntes to set
     */
    public void setDistanciaAntes(Integer distanciaAntes) {
        this.distanciaAntes = distanciaAntes;
    }

}
