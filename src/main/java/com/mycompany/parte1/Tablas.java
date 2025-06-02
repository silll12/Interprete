package com.mycompany.parte1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class Tablas extends JFrame {
    private JTable tablaNoTerminales;
    private JTable tablaTerminales;
    private JTable tablaLL1;
    public Tablas(){
        setTitle("Tablas LL(1)");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel panelTabla = new JPanel(new GridLayout(3,1));
        add(panelTabla, BorderLayout.CENTER);
        
        DefaultTableModel modeloNoTerminales = new DefaultTableModel(new Object[]{"Símbolo no terminal", "Token"},0);
        JTable tablaNoTerminales = new JTable(modeloNoTerminales);
        
        DefaultTableModel modeloTerminales = new DefaultTableModel(new Object[]{"Símbolo terminales", "Token"},0);
        JTable tablaTerminales = new JTable(modeloTerminales);
        
        DefaultTableModel modeloLL1 = new DefaultTableModel();
        JTable tablaLL1 = new JTable(modeloLL1);
      
        panelTabla.add(new JScrollPane(tablaNoTerminales));
        panelTabla.add(new JScrollPane(tablaTerminales));
        
        JPanel panelLL1 = new JPanel(new BorderLayout());
        JLabel etiquetaLL1 = new JLabel ("Tabla LL1", SwingConstants.CENTER);
        panelLL1.add(etiquetaLL1, BorderLayout.NORTH);
        panelLL1.add(new JScrollPane(tablaLL1), BorderLayout.CENTER);
        panelTabla.add(panelLL1);
        
        }
        
        public void vaciadoTablaNoTerminales(Map<String, Integer> noTerminalesTokens){
            DefaultTableModel modelo = (DefaultTableModel) tablaNoTerminales.getModel();
            modelo.setRowCount(0);
            noTerminalesTokens.forEach((simbolo, token) -> modelo.addRow(new Object[]{simbolo, token}));
        }
        
        public void vaciadoTablaTerminales(Map<String, Integer> terminalesTokens){
            DefaultTableModel modelo = (DefaultTableModel) tablaTerminales.getModel();
            modelo.setRowCount(0);
            terminalesTokens.forEach((simbolo, token) -> modelo.addRow(new Object[]{simbolo, token}));
            
        }
        
        public void generarTablaLL1(Map<String, Map<String, Integer>> tablaLL1Datos){
            DefaultTableModel modelo = (DefaultTableModel) tablaLL1.getModel();
            modelo.setRowCount(0);
            
            Set<String> terminales = new HashSet<>();
            tablaLL1Datos.values().forEach(mapa -> terminales.addAll(mapa.keySet()));
            modelo.setColumnIdentifiers(terminales.toArray());
            
            tablaLL1Datos.forEach((noTerminal, reglas)->{
                Object[] fila = new Object[terminales.size() + 1];
                fila[0] = noTerminal;
                
                int index = 1;
                for (String terminal : terminales){
                    fila[index++]= reglas.getOrDefault(terminal, -1);
                }
                
                modelo.addRow(fila);
            });
        }

    
}
