package com.mycompany.parte1;

import java.util.*;

public class AnalizadorLL1 {

    private List<Regla> reglas;
    private Set<String> terminales;
    private Set<String> noTerminales;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public AnalizadorLL1(String textoReglas) {
        this.reglas = Regla.cargarGramaticasDesdeTexto(textoReglas);
        this.terminales = new HashSet<>();
        this.noTerminales = new HashSet<>();
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        clasificarSimbolos();
    }

    // Clasifica todos los símbolos como terminales o no terminales
    private void clasificarSimbolos() {
        for (Regla regla : reglas) {
            noTerminales.add(regla.getNoTerminal());

            for (ElementRegla produccion : regla.getProducciones()) {
                for (ClassNodo nodo : produccion.ListaLadoDerecho) {
                    if (nodo.Simbolo.equals("epsilon")) continue;

                    if (nodo.Terminal) {
                        terminales.add(nodo.Simbolo);
                    } else {
                        noTerminales.add(nodo.Simbolo);
                    }
                }
            }
        }
    }

    public List<Regla> getReglas() {
        return reglas;
    }

    public Set<String> getTerminales() {
        return terminales;
    }

    public Set<String> getNoTerminales() {
        return noTerminales;
    }

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    // Métodos próximos:
    // - calcularFirst()
    // - calcularFollow()
    // - construirTablaLL1()
    // - analizarCadena()
}
