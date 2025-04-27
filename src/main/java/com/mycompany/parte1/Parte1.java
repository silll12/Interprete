package com.mycompany.parte1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Parte1 extends JFrame {
    private JPanel panelNuevo;
    private HashMap<Integer, AFN> AFNS = new HashMap<>();
    private String archivoSeleccionadoRuta = null;


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
            case "Analizar una cadena":
                panelAnalizarCadena();
                break;
            case "Crear AFN desde una expresión regular" :
                panelERaAFN();
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
    private void panelConvertirAFNaAFD() {
        panelNuevo.add(new JLabel("Seleccionar AFN a convertir:"));
        JComboBox<Integer> comboAFNaAFD = new JComboBox<>(AFNS.keySet().toArray(new Integer[0]));
        panelNuevo.add(comboAFNaAFD);
        JButton convertirAFNaAFD = new JButton("Convertir a AFD");
        panelNuevo.add(convertirAFNaAFD);

        convertirAFNaAFD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer id = (Integer) comboAFNaAFD.getSelectedItem();
                    AFN afn = AFNS.get(id);
                    if (afn == null) {
                        JOptionPane.showMessageDialog(null, "El AFN no fue encontrado");
                        return;
                    }

                    AFD afd = afn.ConvertirAFNaAFD();
                    JOptionPane.showMessageDialog(null, "Se convirtió el AFN a AFD");

                    Tabla(afd);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al convertir: " + ex.getMessage());
                }
            }
        });
    }

    private void Tabla(AFD afd) {
        //arreglo para la creación de la tabla ascii
        String[] columnas = new String[258];
        columnas[0] = "Estado";
        columnas[257] = "Token";
        for (int i = 1; i < 257; i++) {
            columnas[i] = Character.toString((char) (i - 1));
        }
        //ordena los estados del AFD, o sea, para que sea, S0,S1,S2...etc
        List<Estado> listaOrdenada = new ArrayList<>(afd.estados);
        listaOrdenada.sort(Comparator.comparingInt(Estado::getIdEstado));
        Map<Estado, String> nombreEstados = new HashMap<>();
        for (int i = 0; i < listaOrdenada.size(); i++) {
            nombreEstados.put(listaOrdenada.get(i), "S" + i);
        }
        //crea la tabla
        Object[][] data = new Object[listaOrdenada.size()][258];
        for (int row = 0; row < listaOrdenada.size(); row++) {
            Estado estado = listaOrdenada.get(row);
            data[row][0] = nombreEstados.get(estado);
            for (int i = 1; i < 257; i++) {
                char c = (char) (i - 1);
                Estado destino = obtenerEstadoPorCaracter(estado, c);
                data[row][i] = (destino != null) ? nombreEstados.get(destino) : "-1";
            }
            // se asigna token de aceptación
            if (afd.EstadoAceptacion(estado)) {
                data[row][257] = estado.getToken1();
            } else {
                data[row][257] = "-1";
            }
        }

        //diseño de la tabla
        JTable tablaASCII = new JTable(data, columnas);
        tablaASCII.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(tablaASCII);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //Boton para guardar el afn, y mandamos a llamar nuestra clase guardarTabla
        JButton btnGuardar = new JButton("Guardar AFN");
        btnGuardar.addActionListener(e -> guardarTabla(data, columnas));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnGuardar, BorderLayout.SOUTH);

        JFrame frameTabla = new JFrame("Tokens");
        frameTabla.add(panel);
        frameTabla.setSize(1400, 600);
        frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTabla.setVisible(true);
    }

    private void guardarTabla(Object[][] data, String[] columnas) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar tabla como .txt");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

                // Escribir encabezados
                writer.print(columnas[0]); // "Estado"

                // Para los caracteres ASCII, usar representación legible
                for (int i = 1; i < 257; i++) {
                    char c = (char) (i - 1);
                    String repr;
                    if (c < 32 || c > 126) { // Si es un carácter de control o no imprimible
                        repr = String.format("<%d>", (int)c); // Representación numérica
                    } else {
                        repr = Character.toString(c); // Carácter normal
                    }
                    writer.print("\t" + repr);
                }
                writer.print("\t" + columnas[257]); // "Token"
                writer.println();

                // Escribir contenido de la tabla
                for (Object[] row : data) {
                    writer.print(row[0]); // Estado
                    for (int i = 1; i < row.length; i++) {
                        writer.print("\t" + row[i]);
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(null, "Tabla guardada exitosamente en: " + archivo.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }


    private Estado obtenerEstadoPorCaracter(Estado estado, char c) {
        for (Transicion transicion : estado.getTrans1()) {
            if (transicion.getEdoTrans(c) != null) {
                return transicion.getEdoTrans(c);
            }
        }
        return null;
    }

    private void panelUnionEspecialAFN() {
        // Limpiar el panel y configurar layout
        panelNuevo.removeAll();
        panelNuevo.setLayout(new BorderLayout());

        // Creamos el modelo para la tabla
        String[] columnNames = {"ID", "Seleccionar AFN", "Token AFN"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Make the second column (index 1) use checkboxes
                return columnIndex == 1 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing checkbox column and token column
                return column == 1 || column == 2;
            }
        };

        // Crear el JTable con el modelo
        JTable table = new JTable(tableModel);
        table.setRowHeight(30); // Aumentar altura de las filas
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente más grande

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
                    // Crear listas para los AFNs seleccionados y sus tokens
                    List<AFN> afnsSeleccionados = new ArrayList<>();
                    List<Integer> idsSeleccionados = new ArrayList<>();
                    List<Integer> tokensSeleccionados = new ArrayList<>();

                    // Recorrer las filas de la tabla para obtener los AFNs seleccionados
                    for (int i = 0; i < table.getRowCount(); i++) {
                        boolean seleccionado = (boolean) table.getValueAt(i, 1);
                        if (seleccionado) {
                            Integer id = (Integer) table.getValueAt(i, 0);
                            // Obtener el token desde la tabla (puede haber sido editado)
                            Integer token = Integer.parseInt(table.getValueAt(i, 2).toString());

                            AFN afn = AFNS.get(id);
                            if (afn != null) {
                                afnsSeleccionados.add(afn);
                                idsSeleccionados.add(id);
                                tokensSeleccionados.add(token);
                            }
                        }
                    }

                    if (afnsSeleccionados.size() < 2) {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar al menos dos AFNs.");
                        return;
                    }

                    // Encontrar el ID más bajo entre los AFNs seleccionados
                    int idMinimo = idsSeleccionados.stream().min(Integer::compare).orElseThrow();

                    // Usar el primer AFN como base
                    AFN afnBase = afnsSeleccionados.get(0);

                    // Unir los AFNs restantes usando el token correspondiente de cada uno
                    for (int i = 1; i < afnsSeleccionados.size(); i++) {
                        AFN afnActual = afnsSeleccionados.get(i);
                        int tokenActual = tokensSeleccionados.get(i);

                        // Usar el token específico para cada AFN que se está uniendo
                        afnBase.UnionEspecialAFNs(afnActual, tokenActual);
                    }

                    // Establecer el token del AFN base (el primer AFN seleccionado)
                    if (!afnBase.EdosAcept.isEmpty()) {
                        for (Estado estado : afnBase.EdosAcept) {
                            if (estado.getToken1() == -1) {
                                estado.setToken1(tokensSeleccionados.get(0));
                            }
                        }
                    }

                    // Asignar el ID más bajo al nuevo AFN
                    afnBase.IdAFN = idMinimo;

                    // Eliminar los AFNs que ya fueron unidos excepto el base
                    for (int i = 1; i < idsSeleccionados.size(); i++) {
                        AFNS.remove(idsSeleccionados.get(i));
                    }

                    // Actualizar o mantener el AFN base con el ID más bajo
                    AFNS.put(idMinimo, afnBase);



                    // Actualizar la tabla principal después de la operación
                    tableModel.setRowCount(0);
                    for (Integer id : AFNS.keySet()) {
                        AFN afn = AFNS.get(id);
                        int token = -1;
                        if (!afn.EdosAcept.isEmpty()) {
                            token = afn.EdosAcept.iterator().next().getToken1();
                        }
                        tableModel.addRow(new Object[]{id, false, token});
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al unir los AFNs: " + ex.getMessage());
                }
            }
        });
    }

    private void panelERaAFN() {


        panelNuevo.removeAll();


        panelNuevo.add(new JLabel("Crear AFN desde una expresión regular"));


        panelNuevo.add(new JLabel("Expresión Regular:"));
        JTextField expresionRegularTextField = new JTextField(20);
        panelNuevo.add(expresionRegularTextField);


        panelNuevo.add(new JLabel("ID para el AFN:"));
        JTextField idAFNTextField = new JTextField(20);
        panelNuevo.add(idAFNTextField);

        // Botón "Seleccionar archivo"
        JButton seleccionarArchivoButton = new JButton("Seleccionar archivo");
        seleccionarArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar archivo");
                int seleccion = fileChooser.showOpenDialog(null);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = fileChooser.getSelectedFile();
                    archivoSeleccionadoRuta = archivoSeleccionado.getAbsolutePath();
                    System.out.println("Archivo seleccionado: " + archivoSeleccionadoRuta);
                }
            }
        });
        panelNuevo.add(seleccionarArchivoButton);

        // Botón "Crear AFN"
        JButton crearAFNButton = new JButton("Crear AFN");
        crearAFNButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String expresionRegular = expresionRegularTextField.getText();
                    int idAFN = Integer.parseInt(idAFNTextField.getText());

                    if (expresionRegular.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor ingresa una expresión regular.");
                        return;
                    }

                    if (AFNS.containsKey(idAFN)) {
                        JOptionPane.showMessageDialog(null, "Ya existe un AFN con este ID.");
                        return;
                    }

                    if (archivoSeleccionadoRuta == null) {
                        JOptionPane.showMessageDialog(null, "No se ha seleccionado un archivo.");
                        return;
                    }

                    ER_AFN erAFN = new ER_AFN(expresionRegular, archivoSeleccionadoRuta);
                    if (erAFN.iniConversion()) {
                        AFN afnResult = erAFN.result;
                        AFNS.put(idAFN, afnResult);
                        JOptionPane.showMessageDialog(null, "AFN creado exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al crear el AFN.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo crear el AFN. Asegúrate de ingresar valores válidos.");
                }
            }
        });
        panelNuevo.add(crearAFNButton);


    }

    private void panelAnalizarCadena() {
        // Limpiamos el panel y configuramos un layout más apropiado para este caso
        panelNuevo.removeAll();
        panelNuevo.setLayout(new BorderLayout());

        // Panel principal con GridBagLayout para organizar mejor los componentes
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        JLabel titleLabel = new JLabel("Analizar Cadena con AFD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Sección para cargar el archivo AFD
        JLabel afdFileLabel = new JLabel("Archivo AFD:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(afdFileLabel, gbc);

        JTextField afdFilePathField = new JTextField(20);
        afdFilePathField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(afdFilePathField, gbc);

        JButton selectFileButton = new JButton("Seleccionar Archivo AFD");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(selectFileButton, gbc);

        // Sección para ingresar la cadena a analizar
        JLabel cadenaLabel = new JLabel("Cadena a analizar:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(cadenaLabel, gbc);

        JTextField cadenaField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(cadenaField, gbc);

        // Botón para analizar
        JButton analizarButton = new JButton("Analizar Cadena");
        analizarButton.setEnabled(false); // Deshabilitado hasta que se cargue un archivo
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(analizarButton, gbc);

        // Área para mostrar los resultados
        JLabel resultadoLabel = new JLabel("Resultado del análisis:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(resultadoLabel, gbc);

        JTextArea resultadoArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPane, gbc);

        // Agregar panel principal al panelNuevo
        panelNuevo.add(mainPanel, BorderLayout.CENTER);

        // Acción del botón para seleccionar archivo
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar archivo AFD (.txt)");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
                    }
                    public String getDescription() {
                        return "Archivos de texto (*.txt)";
                    }
                });

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    afdFilePathField.setText(selectedFile.getAbsolutePath());
                    analizarButton.setEnabled(true); // Habilitar el botón de analizar
                }
            }
        });

        // Acción del botón para analizar la cadena
// Acción del botón para analizar la cadena
        analizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String afdFilePath = afdFilePathField.getText();
                String cadena = cadenaField.getText();

                if (afdFilePath.isEmpty() || cadena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un archivo AFD y proporcionar una cadena para analizar.");
                    return;
                }

                try {
                    // Crear un nuevo analizador léxico con la cadena y el archivo AFD
                    AnalizadorLex analizador = new AnalizadorLex(cadena, afdFilePath);

                    // Obtener el AFD cargado para poder mostrar información detallada
                    AFD afd = analizador.getAFD();

                    // Limpiar el área de resultados
                    resultadoArea.setText("");

                    // Encabezado para el análisis
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("Análisis de la cadena: \"").append(cadena).append("\"\n\n");

                    // Sección para análisis paso a paso
                    resultado.append("Análisis detallado por carácter:\n");
                    resultado.append("---------------------------\n");
                    resultado.append("Posición\tCarácter\tEstado\tTransición\tToken\n");

                    // Guardar el estado actual del analizador
                    String originalInput = cadena;

                    // Reiniciar el analizador para análisis paso a paso
                    analizador.reset();

                    // Análisis paso a paso
                    int estadoActual = 0; // Estado inicial
                    for (int i = 0; i < cadena.length(); i++) {
                        char c = cadena.charAt(i);
                        int ascii = (int) c;

                        // Obtener la transición del estado actual con el carácter actual
                        int siguienteEstado = -1;
                        if (ascii >= 0 && ascii < 256 && estadoActual >= 0 && estadoActual < afd.TablaAFD.length) {
                            siguienteEstado = afd.TablaAFD[estadoActual][ascii];
                        }

                        // Verificar si el siguiente estado es un estado de aceptación
                        int tokenEnEstado = -1;
                        if (siguienteEstado != -1 && siguienteEstado >= 0 && siguienteEstado < afd.TablaAFD.length) {
                            tokenEnEstado = afd.TablaAFD[siguienteEstado][256]; // Columna 256 contiene el token
                        }

                        resultado.append(i).append("\t")
                                .append("'").append(c).append("'").append("\t")
                                .append("S").append(estadoActual).append("\t")
                                .append(siguienteEstado == -1 ? "ERROR" : "→ S" + siguienteEstado).append("\t")
                                .append(tokenEnEstado == -1 ? "-" : tokenEnEstado).append("\n");

                        // Actualizar estado actual si la transición es válida
                        if (siguienteEstado != -1) {
                            estadoActual = siguienteEstado;
                        } else {
                            // Si no hay transición, indicamos un error y salimos del bucle
                            resultado.append("** Error en la posición ").append(i).append(": No hay transición definida **\n");
                            break;
                        }
                    }

                    resultado.append("\n");

                    // Restaurar el estado del analizador
                    analizador = new AnalizadorLex(originalInput, afdFilePath);

                    // Sección de tokens reconocidos (análisis completo)
                    resultado.append("Tokens reconocidos:\n");
                    resultado.append("---------------------------\n");
                    resultado.append("Token\tLexema\n");

                    int token;
                    while ((token = analizador.yylex()) != SimbolosEspeciales.FIN) {
                        if (token == SimbolosEspeciales.ERROR) {
                            resultado.append("ERROR\t\"").append(analizador.Lexema).append("\"\n");
                        } else {
                            resultado.append(token).append("\t\"").append(analizador.Lexema).append("\"\n");
                        }
                    }

                    resultado.append("\nAnálisis completado.");
                    resultadoArea.setText(resultado.toString());

                } catch (Exception ex) {
                    resultadoArea.setText("Error al analizar: " + ex.getMessage() + "\n\n" +
                            Arrays.toString(ex.getStackTrace()));
                    ex.printStackTrace();
                }
            }
        });
        // Actualizar el panel
        panelNuevo.revalidate();
        panelNuevo.repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Parte1());
    }
}