package com.mycompany.parte1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class TablaLL1 {
    private final List<Regla> reglas;
    private final Set<String> terminales;
    private final Set<String> noTerminales;

    public TablaLL1(List<Regla> reglas) {
        this.reglas = reglas;
        Map<String, Set<String>> simbolos = Regla.clasificarSimbolos(reglas);
        this.terminales = new TreeSet<>(simbolos.get("terminales"));
        this.noTerminales = new TreeSet<>(simbolos.get("noTerminales"));
        this.terminales.add("$"); // Siempre se agrega el símbolo $
    }

    public JTable generarTablaVisual() {
        List<String> columnas = new ArrayList<>(terminales);
        Collections.sort(columnas);

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No Terminal");

        for (String t : columnas) {
            modelo.addColumn(t);
        }

        for (Regla r : reglas) {
            String nt = r.getNoTerminal();

            for (ElementRegla produccion : r.getProducciones()) {
                List<ClassNodo> ladoDerecho = produccion.ListaLadoDerecho;
                Set<String> first = calcularFirst(ladoDerecho);

                for (String simbolo : first) {
                    if (simbolo.equals("epsilon")) {
                        Set<String> follow = calcularFollow(nt);
                        for (String f : follow) {
                            insertarCelda(modelo, nt, f, produccion.toString());
                        }
                    } else {
                        insertarCelda(modelo, nt, simbolo, produccion.toString());
                    }
                }
            }
        }

        return new JTable(modelo);
    }

    private void insertarCelda(DefaultTableModel modelo, String fila, String columna, String contenido) {
        int filaIndex = -1;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(fila)) {
                filaIndex = i;
                break;
            }
        }

        if (filaIndex == -1) {
            filaIndex = modelo.getRowCount();
            Object[] nuevaFila = new Object[modelo.getColumnCount()];
            nuevaFila[0] = fila;
            modelo.addRow(nuevaFila);
        }

        int colIndex = modelo.findColumn(columna);
        if (colIndex != -1) {
            modelo.setValueAt(contenido, filaIndex, colIndex);
        }
    }

    private Set<String> calcularFirst(List<ClassNodo> lista) {
        DesRecGramDeGram des = new DesRecGramDeGram("", "");
        des.ArrReglas = reglas.stream().flatMap(r -> r.getProducciones().stream()).toArray(ElementRegla[]::new);
        des.NumReglas = des.ArrReglas.length;
        return des.First(lista);
    }

    private Set<String> calcularFollow(String simbolo) {
        DesRecGramDeGram des = new DesRecGramDeGram("", "");
        des.ArrReglas = reglas.stream().flatMap(r -> r.getProducciones().stream()).toArray(ElementRegla[]::new);
        des.NumReglas = des.ArrReglas.length;
        return des.Follow(simbolo);
    }
    public List<Regla> getReglas() {
        return this.reglas;
    }
}
