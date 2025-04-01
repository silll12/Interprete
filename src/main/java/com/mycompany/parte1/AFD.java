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

    //Metodo para verificar si una cadena es aceptada por este AFD.

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

    //Metodo para obtener el número de estados.

    public int obtenerNumeroDeEstados() {
        return estados.size();
    }

    // Metodo para obtener el número de estados de aceptación.

    public int obtenerNumeroDeEstadosDeAceptacion() {
        return estadosAceptacion.size();
    }
}
