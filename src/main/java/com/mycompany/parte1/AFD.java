package com.mycompany.parte1;

import java.util.*;

public class AFD {
    Set<Estado> estados;
    Estado estadoInicial;
    Set<Estado> estadosAceptacion;
    Set<Character> alfabeto;

    public AFD() {
        estados = new HashSet<>();
        estadosAceptacion = new HashSet<>();
        alfabeto = new HashSet<>();
    }

    // Método para verificar si una cadena es aceptada por este AFD
    public boolean analizarCadena(String cadena) {
        Estado estadoActual = estadoInicial;

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);

            // Verificar si el carácter está en el alfabeto
            if (!alfabeto.contains(c)) {
                return false;
            }

            // Buscar la transición para este carácter
            Estado siguiente = null;
            for (Transicion t : estadoActual.getTrans1()) {
                Estado destino = t.getEdoTrans(c);
                if (destino != null) {
                    siguiente = destino;
                    break;
                }
            }

            // Si no se encuentra una transición, rechazar la cadena
            if (siguiente == null) {
                return false;
            }

            estadoActual = siguiente;
        }

        // Aceptar si terminamos en un estado de aceptación
        return estadosAceptacion.contains(estadoActual);
    }

    // Métodos para obtener el número de estados.
    public int obtenerNumeroDeEstados() {
        return estados.size();
    }

    public int obtenerNumeroDeEstadosDeAceptacion() {
        return estadosAceptacion.size();
    }

    // Método para obtener los estado de aceptación
    public boolean EstadoAceptacion(Estado estado) {
        return estadosAceptacion.contains(estado);
    }

    // Método para obtener el token del estado de aceptación
    public String ObtenerToken(Estado estado) {
        return EstadoAceptacion(estado) ? String.valueOf(estado.getToken1()) : "-1";
    }
}