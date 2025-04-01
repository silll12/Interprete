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
    }

