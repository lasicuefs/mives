/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.graficos;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import mives.model.Livro;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 *
 * @author Ricardo
 */
public class BarCharTonicas extends Application {

//    private HashMap<String, Integer> distribuicaoDeTonicas;
//
//    public BarCharTonicas(HashMap<String, Integer> distribuicaoDeTonicas) {
//        this.distribuicaoDeTonicas = distribuicaoDeTonicas;
//
//    }
    @Override
    public void start(Stage stage) {

        stage.setTitle("MIVES - Frequência de Tônicas");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc
                = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Frequência de Tônicas");
        xAxis.setLabel("Padrões");
        yAxis.setLabel("Valores");

        HashMap<String, Integer> distribuicaoDeTonicas = sortByComparator(Livro.getInstance().getDistribuicaoDeTonicas(), false);

        int quantidade = 0;
        for (String key : distribuicaoDeTonicas.keySet()) {
            XYChart.Series series = new XYChart.Series();
            series.setName(key);
            series.getData().add(new XYChart.Data(key, distribuicaoDeTonicas.get(key)));
            bc.getData().add(series);
            quantidade++;
            if (quantidade == 40) {
                break;
            }
        }

        Scene scene = new Scene(bc, 1000, 600);

        stage.setScene(scene);
        stage.show();

    }

    private static HashMap<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order) {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
