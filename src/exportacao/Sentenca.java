/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exportacao;

import java.util.ArrayList;

/**
 *
 * @author Ricardo
 */
public class Sentenca {

    private String segmento;
    private int link;
    

    private ArrayList<EstruturaVersificacao> estruturaDeVesificacao;

    /**
     * @return the segmento
     */
    public String getSegmento() {
        return segmento;
    }

    /**
     * @param segmento the segmento to set
     */
    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public ArrayList<EstruturaVersificacao> getEstruturaDeVesificacao() {
        if (estruturaDeVesificacao == null) {
            estruturaDeVesificacao = new ArrayList<>();
        }
        return estruturaDeVesificacao;
    }

    public void setEstruturaDeVesificacao(ArrayList<EstruturaVersificacao> estruturaDeVesificacao) {
        this.estruturaDeVesificacao = estruturaDeVesificacao;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

}
