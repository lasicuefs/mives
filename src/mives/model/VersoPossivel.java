/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.model;

import java.util.ArrayList;

/**
 *
 * @author Ricardo Depois de escandido o verso de início ou final de frase,
 * outras possibilidades são testadas a partir da métrica inicial e final
 * fornecida pelo usuário. Mas é possível que outras possibilidades de escansão
 * tenham sido descartadas pelo algoritmo inicialmente. Aqui é o local onde tudo
 * isso é armazenado. Uma frase pode ter uma lista destas possibilidades.
 */
public class VersoPossivel {

    private Verso versoBase;
    private ArrayList<Verso> versosExtras = new ArrayList<>();

    public Verso getVersoBase() {
        return versoBase;
    }

    public void setVersoBase(Verso versoBase) {
        this.versoBase = versoBase;
    }

    public ArrayList<Verso> getVersosExtras() {
        if (versosExtras == null) {
            versosExtras = new ArrayList<>();
        }
        return versosExtras;
    }

    public void setVersosExtras(ArrayList<Verso> versosExtras) {
        this.versosExtras = versosExtras;
    }

}
