package com.mycompany.parte1;

import java.util.Arrays;
import java.util.HashSet;

public class Conjlj {
    public HashSet<Estado> ConjI;
    public int j;
    public int[] TransicionesAFD;

    public Conjlj(int cardAlfabeto) {
        ConjI = new HashSet<>();
        TransicionesAFD = new int[cardAlfabeto];
        // Inicializa todas las transiciones con -1 (indicando que no hay transición)
        Arrays.fill(TransicionesAFD, -1);
    }

    //Verifica si este conjunto contiene algún estado de aceptación
    public boolean ConjuntoAceptacion() {
        for (Estado e : ConjI) {
            if (e.getEdoAcept()) {
                return true;
            }
        }
        return false;
    }

    //Compara si dos objetos ConjIj son iguales basándose en sus conjuntos de estados.

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conjlj other = (Conjlj) obj;
        return this.ConjI.equals(other.ConjI);
    }

    @Override
    public int hashCode() {
        return ConjI.hashCode();
    }
    public int obtenerTokenAceptacion() {
        for (Estado e : ConjI) {
            if (e.getEdoAcept()) {
                return e.getToken1();
            }
        }
        return -1;
    }
}