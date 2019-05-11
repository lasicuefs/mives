/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mives.controller.helpers.utils;

import java.util.ArrayList;

/**
 *
 * @author Ricardo
 */
public class Revista {

    public static ArrayList<PageWizard> assinantes = new ArrayList<>();

    public static void registrarAssinante(PageWizard obj) {
        assinantes.add(obj);
    }

    public static void notificar() {
        assinantes.forEach((p) -> {
            p.update();
        });
    }

}
