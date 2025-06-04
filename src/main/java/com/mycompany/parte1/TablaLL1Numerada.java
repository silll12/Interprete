package com.mycompany.parte1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class TablaLL1Numerada {
    private final List<Regla> reglas;
    private final Map<String, Map<String, String>> tabla = new LinkedHashMap<>();
    private final Map<ElementRegla, Integer> mapaProduccion = new LinkedHashMap<>();
    private int contadorProducciones = 1;

    public TablaLL1Numerada(List<Regla> reglas) {
        this.reglas = reglas;
        numerarProducciones();
        construirTabla();
    }

    // TablaLL1Numerada.java

    private void numerarProducciones() {
        List<ElementRegla> producciones = new ArrayList<>();
        for (Regla r : reglas) {
            for (ElementRegla prod : r.getProducciones()) {
                producciones.add(prod);
                mapaProduccion.put(prod, contadorProducciones++);
            }
        }

        DesRecGramDeGram analizador = new DesRecGramDeGram();
        analizador.ArrReglas = producciones.toArray(new ElementRegla[0]);
        analizador.NumReglas = producciones.size();

        Map<String, Set<String>> simbolos = Regla.clasificarSimbolos(reglas);
        Set<String> noTerminales = simbolos.get("noTerminales");
        Set<String> terminales = simbolos.get("terminales");
        terminales.add("$");

        // Inicializar tabla vacía para no terminales
        for (String nt : noTerminales) {
            tabla.put(nt, new LinkedHashMap<>());
        }

        for (Regla r : reglas) {
            for (ElementRegla prod : r.getProducciones()) {
                Set<String> first = analizador.First(prod.ListaLadoDerecho);
                int id = mapaProduccion.get(prod);
                String contenido = String.valueOf(id); // solo el número

                for (String t : first) {
                    if (!t.equals("epsilon")) {
                        tabla.get(r.getNoTerminal()).put(t, contenido);
                    }
                }

                if (first.contains("epsilon")) {
                    for (String f : analizador.Follow(r.getNoTerminal())) {
                        tabla.get(r.getNoTerminal()).put(f, contenido);
                    }
                }
            }
        }

        // Llenar las celdas vacías con -1
        for (String nt : tabla.keySet()) {
            for (String t : terminales) {
                tabla.get(nt).putIfAbsent(t, "-1");
            }
        }
    }


    private void construirTabla() {
        Map<String, Set<String>> simbolos = Regla.clasificarSimbolos(reglas);
        Set<String> noTerminales = simbolos.get("noTerminales");
        Set<String> terminales = simbolos.get("terminales");
        terminales.add("$");

        DesRecGramDeGram analizador = new DesRecGramDeGram();
        analizador.ArrReglas = reglas.stream().flatMap(r -> r.getProducciones().stream()).toArray(ElementRegla[]::new);
        analizador.NumReglas = analizador.ArrReglas.length;

        // Inicializar tabla vacía para no terminales
        for (String nt : noTerminales) {
            tabla.put(nt, new LinkedHashMap<>());
        }

        for (Regla r : reglas) {
            for (ElementRegla prod : r.getProducciones()) {
                Set<String> first = analizador.First(prod.ListaLadoDerecho);
                int id = mapaProduccion.get(prod);
                String contenido = String.valueOf(id); // 🔹 Solo el número

                for (String t : first) {
                    if (!t.equals("epsilon")) {
                        tabla.get(r.getNoTerminal()).put(t, contenido);
                    }
                }

                if (first.contains("epsilon")) {
                    for (String f : analizador.Follow(r.getNoTerminal())) {
                        tabla.get(r.getNoTerminal()).put(f, contenido);
                    }
                }
            }
        }


        // Llenar las celdas vacías con -1
        for (String nt : tabla.keySet()) {
            for (String t : terminales) {
                tabla.get(nt).putIfAbsent(t, "-1");
            }
        }
    }

    public JTable generarTablaVisual() {
        Set<String> columnas = new TreeSet<>(tabla.values().iterator().next().keySet());
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("↘");
        columnas.forEach(modelo::addColumn);

        for (String fila : tabla.keySet()) {
            Object[] row = new Object[columnas.size() + 1];
            row[0] = fila;
            int i = 1;
            for (String col : columnas) {
                row[i++] = tabla.get(fila).get(col);
            }
            modelo.addRow(row);
        }

        return new JTable(modelo);
    }
    // Retorna si el símbolo es un no terminal presente en la tabla
    public boolean esNoTerminal(String simbolo) {
        return tabla.containsKey(simbolo);
    }

    // Obtiene la producción asociada (como lista de símbolos) usando los números de regla
    public List<String> getProduccion(String noTerminal, String terminal) {
        String idStr = tabla.getOrDefault(noTerminal, new HashMap<>()).getOrDefault(terminal, "-1");

        if (idStr.equals("-1")) return null;

        int id = Integer.parseInt(idStr);

        // Buscar el ElementRegla que tiene ese ID
        for (Map.Entry<ElementRegla, Integer> entry : mapaProduccion.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey().ListaLadoDerecho.stream()
                        .map(n -> n.Simbolo)
                        .collect(Collectors.toList());

            }
        }

        return null;
    }

}
