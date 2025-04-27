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
                String[] partes = linea.split("\t");
                filas.add(partes);
            }

            int numEstados = filas.size();
            // SOLO 256 columnas (0-255) para los caracteres ASCII
            afd.TablaAFD = new int[numEstados][257];

            System.out.println("Procesando " + numEstados + " estados del AFD...");

            // Procesar cada fila (estado)
            for (int i = 0; i < numEstados; i++) {
                String[] fila = filas.get(i);
                String nombreEstado = fila[0]; // Ej. "S0"
                System.out.println("Procesando estado: " + nombreEstado);

                // Procesar transiciones ASCII (fila[1] hasta fila[256])
                for (int j = 1; j <= 257; j++) { // IMPORTANTE: j <= 256
                    int indiceTabla = j - 1; // índice de 0 a 255
                    if (j < fila.length) { // Si existe esa columna en el archivo
                        String valorCelda = fila[j].trim();
                        int valorEntero;

                        if (valorCelda.equals("-1")) {
                            valorEntero = -1;
                        } else if (valorCelda.startsWith("S")) {
                            try {
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

                        afd.TablaAFD[i][indiceTabla] = valorEntero;
                    } else {
                        // Si no hay dato en el archivo para esa columna, poner -1
                        afd.TablaAFD[i][indiceTabla] = -1;
                    }
                }

                // Crear objeto Estado
                Estado estado = new Estado(i);

                // Leer el token (fila[257])
                if (fila.length > 257) {
                    String tokenStr = fila[257].trim();
                    try {
                        int token = Integer.parseInt(tokenStr);
                        if (token != -1) {
                            estado.setToken1(token);
                            afd.estadosAceptacion.add(estado);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error al parsear el token: " + tokenStr);
                    }
                }

                afd.estados.add(estado);

                // El primer estado es el estado inicial
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
    public Estado obtenerEstadoPorId(int id) {
        for (Estado estado : estados) {
            if (estado.getIdEstado() == id) {
                return estado;
            }
        }
        return null; // No encontrado
    }

}