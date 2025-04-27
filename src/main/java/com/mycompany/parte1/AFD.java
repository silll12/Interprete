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

            // Leer la primera línea (encabezados)
            String lineaEncabezados = br.readLine();

            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir por tabulaciones
                String[] partes = linea.split("\t");
                filas.add(partes);
            }

            // Crear la tabla con el tamaño adecuado
            afd.TablaAFD = new int[filas.size()][258];

            // Rellenar la tabla
            for (int i = 0; i < filas.size(); i++) {
                String[] fila = filas.get(i);

                // Rellenar cada columna (caracteres ASCII + token)
                for (int j = 1; j < fila.length; j++) {
                    // La columna 0 es el nombre del estado (S0, S1, etc.)
                    // Las columnas 1-256 son las transiciones para cada caracter
                    // La columna 257 es el token

                    int valor;
                    if (fila[j].equals("-1")) {
                        valor = -1;
                    } else if (j < fila.length - 1) { // Si no es la última columna (token)
                        // Las transiciones pueden ser como "S0", "S1", etc.
                        if (fila[j].startsWith("S")) {
                            try {
                                valor = Integer.parseInt(fila[j].substring(1));
                            } catch (NumberFormatException e) {
                                valor = -1;
                            }
                        } else {
                            valor = Integer.parseInt(fila[j]);
                        }
                    } else { // Es la columna del token
                        valor = Integer.parseInt(fila[j]);
                    }

                    // Ajustar j para que corresponda al índice correcto en TablaAFD
                    // j-1 porque el primer elemento de fila es el nombre del estado
                    afd.TablaAFD[i][j-1] = valor;
                }
            }

            System.out.println("AFD cargado exitosamente con " + filas.size() + " estados.");

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