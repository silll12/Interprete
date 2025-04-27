package com.mycompany.parte1;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class AFD {
    public int[][] TablaAFD;
    Set<Estado> estados;
    Estado estadoInicial;
    Set<Estado> estadosAceptacion;
    Set<Character> alfabeto;

    public AFD() {
        estados = new HashSet<>();
        estadosAceptacion = new HashSet<>();
        alfabeto = new HashSet<>();
    }

    public static AFD cargarAFDDesdeArchivo(File archivo) {
        AFD afd = new AFD();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            List<String[]> filas = new ArrayList<>();
            String linea;

            // Leer la primera línea (encabezados) y descartarla
            String lineaEncabezados = br.readLine();

            // Leer las filas de datos
            while ((linea = br.readLine()) != null) {
                // Dividir por tabulaciones
                String[] partes = linea.split("\t");
                filas.add(partes);
            }

            // Crear la tabla con el tamaño adecuado
            int numEstados = filas.size();
            // 257 columnas: 256 para caracteres ASCII (0-255) y 1 para el token (índice 256)
            afd.TablaAFD = new int[numEstados][257];

            System.out.println("Procesando " + numEstados + " estados del AFD...");

            // Procesar cada fila (estado)
            for (int i = 0; i < numEstados; i++) {
                String[] fila = filas.get(i);
                String nombreEstado = fila[0]; // S0, S1, etc.
                System.out.println("Procesando estado: " + nombreEstado);

                // Si hay menos columnas que las esperadas, completar con -1
                int columnasDisponibles = fila.length;

                // Procesar cada columna (transiciones para cada caracter ASCII)
                // Comenzamos desde j=1 porque la columna 0 es el nombre del estado
                for (int j = 1; j < columnasDisponibles; j++) {
                    if (j > 256) {
                        // Si hay más de 257 columnas (256 caracteres + token), ignorar el exceso
                        break;
                    }

                    // Determinar el índice en TablaAFD (j-1 porque el índice 0 en fila es el nombre del estado)
                    int indiceTabla = j - 1;

                    // Obtener el valor en la celda
                    String valorCelda = fila[j].trim();
                    int valorEntero;

                    if (valorCelda.equals("-1")) {
                        valorEntero = -1;
                    } else if (valorCelda.startsWith("S")) {
                        try {
                            // Convertir S0, S1, etc. a 0, 1, etc.
                            valorEntero = Integer.parseInt(valorCelda.substring(1));
                        } catch (NumberFormatException e) {
                            System.out.println("Error al parsear el estado: " + valorCelda);
                            valorEntero = -1;
                        }
                    } else {
                        try {
                            valorEntero = Integer.parseInt(valorCelda);
                        } catch (NumberFormatException e) {
                            System.out.println("Error al parsear el valor: " + valorCelda);
                            valorEntero = -1;
                        }
                    }

                    // Guardar el valor en la tabla
                    afd.TablaAFD[i][indiceTabla] = valorEntero;
                }

                // Completar las columnas restantes con -1 si es necesario
                for (int j = columnasDisponibles - 1; j < 257; j++) {
                    if (j > 0) { // No modificar el índice 0 que corresponde al estado
                        afd.TablaAFD[i][j-1] = -1;
                    }
                }
            }

            // Configurar estados del AFD
            for (int i = 0; i < numEstados; i++) {
                Estado estado = new Estado(i);

                // Si el estado tiene un token asociado (columna 256), establecerlo
                int token = afd.TablaAFD[i][256];
                if (token != -1) {
                    estado.setToken1(token);
                    afd.estadosAceptacion.add(estado);
                }

                afd.estados.add(estado);

                // El estado 0 es el inicial
                if (i == 0) {
                    afd.estadoInicial = estado;
                }
            }

            System.out.println("AFD cargado exitosamente con " + numEstados + " estados.");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el archivo AFD: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error inesperado: " + e.getMessage());
        }

        return afd;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public boolean EstadoAceptacion(Estado estado) {
        return estadosAceptacion.contains(estado);
    }

    public String ObtenerToken(Estado estado) {
        return EstadoAceptacion(estado) ? String.valueOf(estado.getToken1()) : "-1";
    }

}