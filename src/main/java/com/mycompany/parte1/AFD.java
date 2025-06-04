package com.mycompany.parte1;

import java.io.*;
import java.util.*;


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
            String[] encabezados = lineaEncabezados.split("\t"); // ← aquí guardamos los símbolos ASCII

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
                estado.setNombre(nombreEstado); // aquí estás usando el nombre del estado como símbolo

                // Leer el token (fila[257])
                if (fila.length > 257) {
                    String tokenStr = fila[257].trim();
                    try {
                        int token = Integer.parseInt(tokenStr);
                        if (token != -1) {
                            estado.setToken1(token);
                            afd.estadosAceptacion.add(estado);

                            // Asignar el símbolo asociado usando encabezados
                            String simbolo = afd.getSimboloPorToken(token);
                            estado.setSimboloAsociado(simbolo);
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
    public boolean EstadoAceptacion(Estado estado) {
        return estadosAceptacion.contains(estado);
    }
    public Estado obtenerEstadoPorId(int id) {
        for (Estado estado : estados) {
            if (estado.getIdEstado() == id) {
                return estado;
            }
        }
        return null; // No encontrado
    }
    public String getSimboloPorToken(int tokenBuscado) {
        for (Estado estado : estadosAceptacion) {
            if (estado.getToken1() == tokenBuscado) {
                int estadoId = estado.getIdEstado(); // fila en la matriz TablaAFD


                // Recorremos todas las columnas (ASCII 0–255)
                for (int col = 0; col < 256; col++) {
                    for (int fila = 0; fila < TablaAFD.length; fila++) {
                        if (TablaAFD[fila][col] == estadoId) {
                            // Si desde 'fila' se llega al estado actual por el carácter col
                            char simbolo = (char) col;
                            return Character.isISOControl(simbolo) ? "<" + col + ">" : String.valueOf(simbolo);
                        }
                    }
                }
            }
        }
        return "N/A";
    }

    public List<Integer> analizarCadena(String entrada) {
        List<Integer> tokens = new ArrayList<>();
        int estadoActual = estadoInicial.getIdEstado();
        StringBuilder lexema = new StringBuilder();

        for (int i = 0; i < entrada.length(); i++) {
            char c = entrada.charAt(i);
            int ascii = (int) c;

            if (ascii < 0 || ascii >= 256) {
                throw new RuntimeException("Carácter fuera de rango ASCII: " + c);
            }

            int siguienteEstado = TablaAFD[estadoActual][ascii];

            if (siguienteEstado == -1) {
                Estado estadoObj = obtenerEstadoPorId(estadoActual);
                if (estadoObj != null && EstadoAceptacion(estadoObj)) {
                    tokens.add(estadoObj.getToken1());
                    estadoActual = estadoInicial.getIdEstado();
                    lexema.setLength(0); // limpiar
                    i--; // volver a procesar este carácter desde el nuevo estado
                } else {
                    throw new RuntimeException("Error léxico en: '" + lexema.toString() + c + "'");
                }
            } else {
                lexema.append(c);
                estadoActual = siguienteEstado;
            }
        }

        // Verificar si último estado es de aceptación
        Estado finalEstado = obtenerEstadoPorId(estadoActual);
        if (finalEstado != null && EstadoAceptacion(finalEstado)) {
            tokens.add(finalEstado.getToken1());
        } else {
            throw new RuntimeException("Cadena termina en estado no aceptado: " + lexema.toString());
        }

        return tokens;
    }
    public List<LexemaToken> analizarConLexemas(String entrada) {
        List<LexemaToken> resultado = new ArrayList<>();
        int estadoActual = estadoInicial.getIdEstado();
        StringBuilder lexema = new StringBuilder();

        for (int i = 0; i < entrada.length(); i++) {
            char c = entrada.charAt(i);
            int ascii = (int) c;

            int siguiente = TablaAFD[estadoActual][ascii];
            if (siguiente == -1) {
                Estado estado = obtenerEstadoPorId(estadoActual);
                if (EstadoAceptacion(estado)) {
                    resultado.add(new LexemaToken(estado.getToken1(), lexema.toString()));
                    estadoActual = estadoInicial.getIdEstado();
                    lexema.setLength(0);
                    i--;
                } else {
                    throw new RuntimeException("Error léxico en: " + lexema + c);
                }
            } else {
                lexema.append(c);
                estadoActual = siguiente;
            }
        }

        Estado estado = obtenerEstadoPorId(estadoActual);
        if (EstadoAceptacion(estado)) {
            resultado.add(new LexemaToken(estado.getToken1(), lexema.toString()));
        } else {
            throw new RuntimeException("Cadena no aceptada: " + lexema.toString());
        }

        return resultado;
    }

}