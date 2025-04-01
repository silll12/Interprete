package com.mycompany.parte1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Parte1 extends JFrame {
    private JPanel panelNuevo;
    private HashMap<Integer, AFN> AFNS = new HashMap<>();

    public Parte1() {
        setTitle("Interprete");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] opciones = {
                "Crear AFN básico",
                "Unir AFNs",
                "Concatenar 2 AFNs",
                "Cerradura positiva",
                "Cerradura de Kleene",
                "Opcional",
                "Unión Especial",//Union para anlizador Lexico
                "Convertir AFN a AFD",
                "Analizar una cadena",
                "Probar analizador léxico",
                "Crear AFN desde una expresión regular"
        };
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setPreferredSize(new Dimension(200, 25));
        add(comboBox, BorderLayout.NORTH);
//creamos el un panel nuevo de cada JComboBox
        panelNuevo = new JPanel();
        add(panelNuevo , BorderLayout.CENTER);

//ActionListener solo tiene un método "actionPerformed", lo que nos permite
//es direccionarnos al panelNuevo una vez que el usuario escoge alguna opción del JcomboBox
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) comboBox.getSelectedItem();
                cambiarPanel(seleccion);
            }
        });

        setVisible(true);
    }
    //metodo para que podamos visualizar el panel de cada JcomboBox
    private void cambiarPanel(String seleccion) {
        panelNuevo.removeAll();
        //si gustan usar GridLayout que forma una cuadricula, en este caso(filas,columnas,pixles)
        //también existe, flowlayout, borderlayout,boxlayout
        panelNuevo.setLayout(new GridLayout(10, 10, 10, 10)); //Se puede dejar así, evitamos repetir en todos los otros casos
        switch (seleccion) {
            case "Crear AFN básico":
                panelAFNBasico();
                break;
            case "Unir AFNs":
                panelUnirAFNS();
                break;
            case "Concatenar 2 AFNs":
                panelConcatenar();
                break;
            case "Cerradura positiva":
                panelCerraduraPositiva();
                break;
            case "Cerradura de Kleene":
                panelCerraduraKleene();
                break;
            case "Opcional":
                panelOpcional();
                break;
            case "Convertir AFN a AFD":
                panelConvertirAFNaAFD();
                break;
            case "Unión Especial":
                panelUnionEspecialAFN();
                break;
        }

        panelNuevo.revalidate();
        panelNuevo.repaint();
    }
    private void panelAFNBasico(){
        panelNuevo.add(new JLabel("Carácter inferior:"));
        JTextField simboloInferior = new JTextField();
        panelNuevo.add(simboloInferior);

        panelNuevo.add(new JLabel("Carácter superior:"));
        JTextField simboloSuperior = new JTextField();
        panelNuevo.add(simboloSuperior);

        panelNuevo.add(new JLabel("ID del AFN:"));
        JTextField IdAFN = new JTextField();
        panelNuevo.add(IdAFN);

        JButton CrearAFNButton = new JButton("Crear AFN");
        panelNuevo.add(CrearAFNButton);
        //crear una accion para cuando el boton sea pulsado y pueda
        //leer y guardar los caracteres infe,supe y el ID
        CrearAFNButton.addActionListener(new ActionListener() {
            @Override
            //sobrescribirá en el metodo para ahora si definir la
            //acción de cuando sea pulsado el boton
            public void actionPerformed(ActionEvent e) {
                try {
                    char s1 = simboloInferior.getText().charAt(0);
                    char s2 = simboloSuperior.getText().charAt(0);
                    //esto es importante, ya que el id forzosamente
                    //debe ser un entero, y lo que hace parseInt
                    //es convertir un string a entero ¡ID SOLO LEE
                    //Y RECIBE ENTEROS!
                    int id = Integer.parseInt(IdAFN.getText());
                    //IMPORTANTE:se deben hacer las validaciones pertinente para verificar
                    //si ya existe el AFN con el ID, si podemos crearlo o si ingresa ID tipo string
                    //no lo crea
                    //con HashMap no se permiten valores duplicados y como es un ID único
                    //para cada autómata. Es decir almacen el objeto con una CLAVE (ID) única, es por ello que
                    //su estructura contiene containsKEY que regresa un verdadero si una entrada
                    //ya está especificada con su KEY (ID)
                    if (AFNS.containsKey(id)) {
                        JOptionPane.showMessageDialog(null, "Ya existe un AFN con ese ID.");
                    } else {
                        AFN afn = new AFN().CrearAFNBasico(s1, s2);
                        afn.IdAFN = id;
                        AFNS.put(id, afn);
                        JOptionPane.showMessageDialog(null, "AFN creado");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo crear automáta");
                }
            }
        });
    }
    private void panelUnirAFNS(){
        panelNuevo.add(new JLabel("seleccionar AFN 1:"));
        JComboBox<Integer> comboAFN1 = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFN1);
        panelNuevo.add(new JLabel("Seleccionar AFN2:"));
        JComboBox<Integer> comboAFN2 = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFN2);
        // botón para unir los AFNs
        JButton unirButton = new JButton("Unir AFNs");
        panelNuevo.add(unirButton);
        // acción del botón "Unir AFNs"
        unirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // obtenemos los ids seleccionados de los ComboBox y guardamos como enteros en ID1 y ID2
                    Integer id1 = (Integer) comboAFN1.getSelectedItem();
                    Integer id2 = (Integer) comboAFN2.getSelectedItem();
                    // buscamos los AFNs de acuerdo con los ids obtenidos anteriormente y asignamos AFN1 y AFN2
                    AFN afn1 = AFNS.get(id1);
                    AFN afn2 = AFNS.get(id2);
                    // verificamos si los AFNs existen
                    if (afn1 == null || afn2 == null) {
                        JOptionPane.showMessageDialog(null, "Uno o ambos AFNs no existen.");
                        return;
                    }
                    // llamamos al método unir
                    afn1.UnirAFN(afn2);
                    AFNS.remove(id2); //nos ayudara a remover el segundo AFN, similar a como el profe lo realiza
                    JOptionPane.showMessageDialog(null, "AFNs unidos exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al unir los AFNs: " + ex.getMessage());
                }
            }
        });
    }
    private void panelCerraduraPositiva(){
        panelNuevo.add(new JLabel("Aplicar Cerradura +:"));
        JComboBox<Integer> comboCerraduraPositiva = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboCerraduraPositiva);
        //botón para que se aplique la cerradura +
        JButton aplicarpositivaButton = new JButton("Cerradura +");
        panelNuevo.add(aplicarpositivaButton);
        aplicarpositivaButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Integer id =(Integer) comboCerraduraPositiva.getSelectedItem();
                    AFN afn= AFNS.get(id);
                    if (afn == null){
                        JOptionPane.showMessageDialog(null, "El AFN no fue encontrado");
                        return;
                    }

                    AFN nuevoAFN=afn.CerraduraPositiva();
                    AFNS.put(id, nuevoAFN);

                    JOptionPane.showMessageDialog(null, "Se aplicó Cerradura Positiva");

                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "No se puede aplicar la Cerradura positiva" + ex.getMessage());
                }
            }

        });
    }
    private void panelCerraduraKleene(){
        panelNuevo.add(new JLabel("Aplicar Cerradura *:"));
        JComboBox<Integer> comboCerraduraKleene = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboCerraduraKleene);
        //botón para que se aplique la cerradura +
        JButton aplicarKleeneButton = new JButton("Cerradura *");
        panelNuevo.add(aplicarKleeneButton);
        aplicarKleeneButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    Integer afnId = (Integer) comboCerraduraKleene.getSelectedItem();
                    AFN afnSeleccionado = AFNS.get(afnId);
                    AFN afnCerraduraKleene = afnSeleccionado.CerraduraKleene();
                    AFNS.put(afnId, afnCerraduraKleene);
                    JOptionPane.showMessageDialog(panelNuevo, "Se aplicó Cerradura de Kleene");
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(panelNuevo, "No se puede aplica la Cerradura *" + ex.getMessage());
                }
            }

        });
    }
    private void panelConcatenar(){
        panelNuevo.add(new JLabel("Seleccionar AFN 1:"));
        JComboBox<Integer> comboAFN1 = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFN1);
        panelNuevo.add(new JLabel("Seleccionar AFN2:"));
        JComboBox<Integer> comboAFN2 = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFN2);
        // botón para unir los AFNs
        JButton contatenarButton = new JButton("Contatenar AFNs");
        panelNuevo.add(contatenarButton);
        // acción del botón "Concatenar AFNs"
        contatenarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer id1= (Integer) comboAFN1.getSelectedItem();
                    Integer id2= (Integer) comboAFN2.getSelectedItem();
                    AFN afn1= AFNS.get(id1);
                    AFN afn2= AFNS.get(id2);
                    if(afn1==null || afn2==null){
                        JOptionPane.showMessageDialog(null,"Uno o ambos AFNs no existen.");
                        return;
                    }
                    afn1.ConcAFN(afn2);
                    AFNS.remove(id2);
                    JOptionPane.showMessageDialog(null, "Concatenados exitosamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al unir los AFNs: " + ex.getMessage());
                }
            }
        });

    }
    private void panelOpcional(){
        panelNuevo.add(new JLabel("Aplicar Opcional ?:"));
        JComboBox<Integer> comboOpcional = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboOpcional);
        //botón para que se aplique la cerradura +
        JButton OpcionalButton = new JButton("Opcional ?");
        panelNuevo.add(OpcionalButton);
        OpcionalButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Integer id =(Integer) comboOpcional.getSelectedItem();
                    AFN afn= AFNS.get(id);
                    if (afn == null){
                        JOptionPane.showMessageDialog(null, "El AFN no fue encontrado");
                        return;
                    }
                    AFN nuevoAFN = afn.Opcional();
                    AFNS.put(id, nuevoAFN);
                    JOptionPane.showMessageDialog(null, "Se aplicó Opcional");

                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "No se puede aplicar el Opcional" + ex.getMessage());
                }
            }

        });
    }
    private void panelConvertirAFNaAFD(){
        panelNuevo.add(new JLabel("Seleccionar AFN a convertir:"));
        JComboBox<Integer> comboAFNaAFD= new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFNaAFD);
        JButton convertirAFNaAFD= new JButton("Convertir a AFD");
        panelNuevo.add(convertirAFNaAFD);
        convertirAFNaAFD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer id= (Integer) comboAFNaAFD.getSelectedItem();
                    AFN afn = AFNS.get(id);
                    if(afn==null){
                        JOptionPane.showMessageDialog(null, "El AFN no fue encontrado");
                        return;
                    }
                    AFD afd = afn.ConvertirAFNaAFD();
                    JOptionPane.showMessageDialog(null,"Se convirtió el AFN a AFD");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error a l convertir" + ex.getMessage());
                }
            }
        });
    }
    private void panelUnionEspecialAFN() {
        // Limpiar el panel y configurar layout
        panelNuevo.removeAll();
        panelNuevo.setLayout(new BorderLayout());
        // Creamos el modelo para la tabla
        String[] columnNames = {"ID", "Seleccionar AFN", "Token AFN"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Crear el JTable con el modelo
        JTable table = new JTable(tableModel);
        table.setRowHeight(30); // Aumentar altura de las filas
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente más grande

        // Hacer que la columna "Seleccionar AFN" tenga un JCheckBox
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Agregamos los AFNs a la tabla mostrando los tokens
        for (Integer id : AFNS.keySet()) {
            AFN afn = AFNS.get(id);
            int token = -1;
            if (!afn.EdosAcept.isEmpty()) {
                token = afn.EdosAcept.iterator().next().getToken1();
            }
            tableModel.addRow(new Object[]{id, false, token});
        }

        // Crear un JScrollPane para la tabla con tamaño preferido
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(450, 300)); // Tamaño más grande

        // Panel para los controles inferiores
        JPanel panelControles = new JPanel(new FlowLayout());

        // Botón para unir los AFNs
        JButton unirButton = new JButton("Unir AFNs Seleccionados");
        unirButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente más grande
        panelControles.add(unirButton);

        // Agregar componentes al panel principal
        panelNuevo.add(scrollPane, BorderLayout.CENTER);
        panelNuevo.add(panelControles, BorderLayout.SOUTH);
        unirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Crear una lista para los AFNs seleccionados
                    List<AFN> afnsSeleccionados = new ArrayList<>();
                    List<Integer> idsSeleccionados = new ArrayList<>();

                    // Recorrer las filas de la tabla para obtener los AFNs seleccionados
                    for (int i = 0; i < table.getRowCount(); i++) {
                        boolean seleccionado = (boolean) table.getValueAt(i, 1);
                        if (seleccionado) {
                            Integer id = (Integer) table.getValueAt(i, 0);
                            AFN afn = AFNS.get(id);
                            if (afn != null) {
                                afnsSeleccionados.add(afn);
                                idsSeleccionados.add(id);
                            }
                        }
                    }

                    if (afnsSeleccionados.size() < 2) {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar al menos dos AFNs.");
                        return;
                    }

                    // Encontrar el ID más bajo entre los AFNs seleccionados
                    int idMinimo = idsSeleccionados.stream().min(Integer::compare).orElseThrow();

                    // Obtener el token del primer AFN seleccionado (del primer estado de aceptación)
                    int tokenConservado = -1;
                    if (!afnsSeleccionados.get(0).EdosAcept.isEmpty()) {
                        tokenConservado = afnsSeleccionados.get(0).EdosAcept.iterator().next().getToken1();
                    }

                    // Crear un nuevo AFN que será la unión de todos los seleccionados
                    AFN afnBase = afnsSeleccionados.get(0);
                    for (int i = 1; i < afnsSeleccionados.size(); i++) {
                        afnBase.UnionEspecialAFNs(afnsSeleccionados.get(i), tokenConservado);
                    }

                    // Asignar el ID más bajo al nuevo AFN
                    afnBase.IdAFN = idMinimo;

                    // Eliminar los AFNs que ya fueron unidos
                    for (Integer id : idsSeleccionados) {
                        AFNS.remove(id);
                    }

                    // Agregar el nuevo AFN con el ID más bajo
                    AFNS.put(idMinimo, afnBase);

                    JOptionPane.showMessageDialog(null, "AFNs unidos exitosamente. Token asignado: " + tokenConservado);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al unir los AFNs: " + ex.getMessage());
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Parte1());
    }
}
