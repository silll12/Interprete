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
            List<int[]> filas = new ArrayList<>();

            String linea = br.readLine(); // Leer encabezados y no hacer nada
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\t");
                int[] fila = new int[258]; // 257 + 1 columnas

                for (int i = 1; i <= 257; i++) { // Empezar en 1 porque partes[0] es el nombre del estado
                    fila[i - 1] = partes[i].equals("-1") ? -1 : Integer.parseInt(partes[i].substring(1)); // S0, S1, etc
                }
                filas.add(fila);
            }

            afd.TablaAFD = new int[filas.size()][258];
            for (int i = 0; i < filas.size(); i++) {
                afd.TablaAFD[i] = filas.get(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
