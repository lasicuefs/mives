/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.graficos;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import mives.model.Livro;

/**
 *
 * @author Ricardo
 */
public class ScatterChartSample extends Application {

    private ArrayList<Integer> distancias;

    public ScatterChartSample(ArrayList<Integer> distancias) {
        this.distancias = distancias;

    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("MIVES - Dispersão de Sentenças");
        final NumberAxis xAxis = new NumberAxis(0, Livro.getInstance().getNumeroDeSegmentos(), 1);
        final NumberAxis yAxis = new NumberAxis(0, 2, 1);
        final ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
        xAxis.setLabel("Intervalo de Sentenças");
        yAxis.setLabel("Ocorrências");
        sc.setTitle("Dispersão de sentença com estrutura de versificação ");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Ocorrênca de sentença");

        int i = 0;
        for (Integer in : distancias) {
            i += in;
            series1.getData().add(new XYChart.Data(i, 1));
        }

        sc.getData().addAll(series1);
        Scene scene = new Scene(sc, 1000, 200);
        //scene.getStylesheets().add("dist/Chart.css");

        stage.setScene(scene);
        stage.show();
    }

}
