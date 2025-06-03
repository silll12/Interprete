package com.mycompany.parte1;

import java.util.*;

public class Regla {

    private String noTerminal; // lado izquierdo de la producción
    private List<ElementRegla> producciones; // cada ElementRegla representa una producción alternativa

    public Regla(String noTerminal) {
        this.noTerminal = noTerminal;
        this.producciones = new ArrayList<>();
    }

    public void agregarProduccion(ElementRegla prod) {
        producciones.add(prod);
    }

    public String getNoTerminal() {
        return noTerminal;
    }

    public List<ElementRegla> getProducciones() {
        return producciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(noTerminal + " -> ");
        for (int i = 0; i < producciones.size(); i++) {
            ElementRegla er = producciones.get(i);
            for (ClassNodo nodo : er.ListaLadoDerecho) {
                sb.append(nodo.Simbolo).append(" ");
            }
            if (i < producciones.size() - 1) sb.append("| ");
        }
        return sb.toString().trim();
    }

    public static Regla parsearRegla(String linea) {
        linea = linea.replace(";", "").trim();
        String[] partes = linea.split("->");

        String noTerminal = partes[0].trim();
        String cuerpo = partes[1].trim();

        Regla regla = new Regla(noTerminal);

        for (String alternativa : cuerpo.split("\\|")) {
            List<ClassNodo> listaDerecha = new ArrayList<>();
            for (String simbolo : alternativa.trim().split("\\s+")) {
                boolean esTerminal = !Character.isUpperCase(simbolo.charAt(0));
                if (simbolo.equals("epsilon")) esTerminal = true; // puede ser tratado como terminal especial

                ClassNodo nodo = new ClassNodo(simbolo, esTerminal);
                listaDerecha.add(nodo);
            }

            // Crear una producción (lado izquierdo + lista derecha)
            ElementRegla produccion = new ElementRegla(new ClassNodo(noTerminal, false), listaDerecha);
            regla.agregarProduccion(produccion);
        }

        return regla;
    }
    public static List<Regla> cargarGramaticasDesdeTexto(String texto) {
        List<Regla> listaReglas = new ArrayList<>();
        String[] lineas = texto.split("\\r?\\n");

        for (String linea : lineas) {
            if (linea.trim().isEmpty()) continue;
            Regla regla = parsearRegla(linea);
            listaReglas.add(regla);
        }

        return listaReglas;
    }
    public static Map<String, Set<String>> clasificarSimbolos(List<Regla> reglas) {
        Set<String> noTerminales = new HashSet<>();
        Set<String> terminales = new HashSet<>();

        for (Regla regla : reglas) {
            noTerminales.add(regla.getNoTerminal());
        }

        for (Regla regla : reglas) {
            for (ElementRegla produccion : regla.getProducciones()) {
                for (ClassNodo nodo : produccion.ListaLadoDerecho) {
                    String simbolo = nodo.Simbolo;
                    if (!simbolo.equals("epsilon") && !noTerminales.contains(simbolo)) {
                        terminales.add(simbolo);
                    }
                }
            }
        }

        Map<String, Set<String>> resultado = new HashMap<>();
        resultado.put("terminales", terminales);
        resultado.put("noTerminales", noTerminales);
        return resultado;
    }

}
