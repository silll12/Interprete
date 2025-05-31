package com.mycompany.parte1;

import java.util.List;

public class ElementRegla {
    public ClassNodo InfoSimbolo;                    // Lado izquierdo (no terminal)
    public List<ClassNodo> ListaLadoDerecho;         // Lado derecho (lista de nodos)

    public ElementRegla() {
        this.InfoSimbolo = null;
        this.ListaLadoDerecho = null;
    }

    public ElementRegla(ClassNodo izquierdo, List<ClassNodo> derecho) {
        this.InfoSimbolo = izquierdo;
        this.ListaLadoDerecho = derecho;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(InfoSimbolo != null ? InfoSimbolo.Simbolo : "null").append(" -> ");
        if (ListaLadoDerecho != null) {
            for (ClassNodo nodo : ListaLadoDerecho) {
                sb.append(nodo.Simbolo).append(" ");
            }
        } else {
            sb.append("null");
        }
        return sb.toString().trim();
    }
}
