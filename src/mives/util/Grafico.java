package mives.util;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import java.text.NumberFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public class Grafico {

    public Grafico() {

    }

    private static javax.swing.JPanel pizza3D(ArrayList nome, ArrayList valor,
            String tituloGrafico, float transparencia, String tipo) {
        DefaultPieDataset data = new DefaultPieDataset();

        for (int i = 0; i < nome.toArray().length; i++) {
            data.setValue("" + nome.get(i).toString(),
                    new Double(valor.get(i).toString()));
        }

        JFreeChart chart = ChartFactory.createPieChart3D(tituloGrafico,
                data, true, true, true);

        java.awt.Color cor = new java.awt.Color(200, 200, 200);
        chart.setBackgroundPaint(cor);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setLabelLinksVisible(true);
        plot.setNoDataMessage("Não existem dados para serem exibidos no gráfico");

        plot.setStartAngle(90);
        plot.setDirection(Rotation.CLOCKWISE);

        plot.setForegroundAlpha(transparencia);
        plot.setInteriorGap(0.20);

        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    public static javax.swing.JPanel pizza3DStatic(ArrayList nome, ArrayList valor, String tituloGrafico) {
        return pizza3D(nome, valor, tituloGrafico, 0.5f, "Static");
    }

    public void gerarSintetico(HashMap<String, EstatisticaTipo> estatiticaGeral, int quantidade, java.awt.Frame frame) {
        ArrayList nomes = new ArrayList();
        ArrayList valores = new ArrayList();
        JDialog dialog = new JDialog(frame, "Gráfico de Busca", true);

        JPanel painel = new JPanel();
        //    JButton button = new JButton("Salvar");
        //   JFrame frame = new JFrame();
        ScrollPane scroll = new ScrollPane(1);

        if (estatiticaGeral.size() > 0) {
//            System.out.println("-------------------------------------------------------------------");
            for (String key : estatiticaGeral.keySet()) {

                nomes.add(key);
                valores.add(estatiticaGeral.get(key).getQuantidade());

//                System.out.println("Categorias:");
//                for (String subKey : estatiticaGeral.get(key).getSubtipos().keySet()) {
//                    System.out.println("- " + subKey + ":\t" + estatiticaGeral.get(key).getSubtipos().get(subKey));
//                }
//                System.out.println("");
            }
//            System.out.println("-------------------------------------------------------------------");
        }

//        for (int i = 0; i < 5; i++) {
//            nomes.add("Nome" + i);
//            valores.add(5 + i);
//        }
        System.out.println("Gerando gráfico");
        painel.add(Grafico.pizza3DStatic(nomes, valores,
                "Gráfico de Estatística\n Total de Versos: " + quantidade));

        scroll.add(painel);
        //  frame.getContentPane().add(scroll);
        dialog.getContentPane().add(scroll);
//        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setTitle("Gráfico de Busca");
        dialog.setTitle("Gráfico de Busca");
//        frame.setPreferredSize(new Dimension(750, 520));
        dialog.setPreferredSize(new Dimension(750, 520));
        dialog.pack();
//        frame.pack();
//        frame.setVisible(true);
        dialog.setVisible(true);
    }

    public void gerarAnalitico(HashMap<String, EstatisticaTipo> estatiticaGeral, int quantidade, java.awt.Frame frame) {
        ArrayList nomes = new ArrayList();
        ArrayList valores = new ArrayList();
        JDialog dialog = new JDialog(frame, "Gráfico de Busca", true);

        JPanel painel = new JPanel();
        //    JButton button = new JButton("Salvar");
        //   JFrame frame = new JFrame();
        ScrollPane scroll = new ScrollPane(1);
//        StringBuilder temRotulo = new StringBuilder();
        NumberFormat percentual = NumberFormat.getPercentInstance();
        percentual.setMaximumFractionDigits(2);

        if (estatiticaGeral.size() > 0) {

            for (String key : estatiticaGeral.keySet()) {

                for (String subKey : estatiticaGeral.get(key).getSubtipos().keySet()) {
//                    System.out.println("- " + subKey + ":\t" + estatiticaGeral.get(key).getSubtipos().get(subKey));
                    float qtd = estatiticaGeral.get(key).getSubtipos().get(subKey);
                    // System.out.println("Percentual: " + ((qtd / quantidade) * 100));
                    String rotulo = new String(key + " - " + subKey + ":" + percentual.format((qtd / quantidade)));
                    nomes.add(rotulo);
                    valores.add(estatiticaGeral.get(key).getSubtipos().get(subKey));
                }

//                System.out.println("");
            }

        }

//        for (int i = 0; i < 5; i++) {
//            nomes.add("Nome" + i);
//            valores.add(5 + i);
//        }
        System.out.println("Gerando gráfico");
        painel.add(Grafico.pizza3DStatic(nomes, valores,
                "Gráfico de Estatística\n Total de Versos: " + quantidade));

        scroll.add(painel);
        //  frame.getContentPane().add(scroll);
        dialog.getContentPane().add(scroll);
//        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        frame.setTitle("Gráfico de Busca");
        dialog.setTitle("Gráfico de Busca");
//        frame.setPreferredSize(new Dimension(750, 520));
        dialog.setPreferredSize(new Dimension(750, 520));
        dialog.pack();
//        frame.pack();
//        frame.setVisible(true);
        dialog.setVisible(true);
    }

}
