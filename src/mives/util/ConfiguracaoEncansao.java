/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.util;

import java.util.HashMap;

/**
 *
 * @author Ricardo
 */
public class ConfiguracaoEncansao {

    private boolean utilizar = false;
    private HashMap<String, String> sinerese;
    private HashMap<String, String> dierese;
    private int tipoDeElisao;
    private boolean considerarH;
    private int tipoDeVerso;
    private static ConfiguracaoEncansao configuracaoEncansao = null;

    private ConfiguracaoEncansao() {
        sinerese = new HashMap<>();
        dierese = new HashMap<>();
    }

    public static ConfiguracaoEncansao getInstacia() {
        if (configuracaoEncansao == null) {
            configuracaoEncansao = new ConfiguracaoEncansao();
        }
        return configuracaoEncansao;
    }

    public HashMap<String, String> getSinerese() {
        return sinerese;
    }

    public void setSinerese(HashMap<String, String> sinerese) {
        this.sinerese = sinerese;
    }

    public HashMap<String, String> getDierese() {
        return dierese;
    }

    public void setDierese(HashMap<String, String> dierese) {
        this.dierese = dierese;
    }

    public int getTipoDeElisao() {
        return tipoDeElisao;
    }

    public void setTipoDeElisao(int tipoDeElisao) {
        this.tipoDeElisao = tipoDeElisao;
    }

    public boolean isConsiderarH() {
        return considerarH;
    }

    public void setConsiderarH(boolean considerarH) {
        this.considerarH = considerarH;
    }

    public int getTipoDeVerso() {
        return tipoDeVerso;
    }

    public void setTipoDeVerso(int tipoDeVerso) {
        this.tipoDeVerso = tipoDeVerso;
    }

    public boolean isUtilizar() {
        return utilizar;
    }

    public void setUtilizar(boolean utilizar) {
        this.utilizar = utilizar;
    }

}
