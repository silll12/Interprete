package com.mycompany.parte1;
import java.util.*;
import java.util.stream.Collectors;

public class AnalizadorSintacticoLL1 {
    private final TablaLL1Numerada tabla;
    private final List<Integer> tokens;
    private final Map<Integer, String> tokenToTerminal;
    private final List<PasoAnalisis> pasos = new ArrayList<>();

    public AnalizadorSintacticoLL1(TablaLL1Numerada tabla, List<Integer> tokens, Map<Integer, String> tokenToTerminal) {
        this.tabla = tabla;
        this.tokens = tokens;
        this.tokenToTerminal = tokenToTerminal;
    }

    public List<PasoAnalisis> analizar(String simboloInicial, List<String> lexemasOriginales, Map<String, String> terminalToLexema) {
        Stack<String> pila = new Stack<>();
        pila.push("$");
        pila.push(simboloInicial);

        List<Integer> entrada = new ArrayList<>(tokens);
        entrada.add(0); // token del $

        int posLexema = 0;

        while (!pila.isEmpty()) {
            String topePila = pila.peek();
            int tokenActual = entrada.get(0);
            String terminalActual = tokenToTerminal.getOrDefault(tokenActual, "");

            PasoAnalisis paso = new PasoAnalisis();
            paso.setPila(
                    pila.stream()
                            .map(s -> terminalToLexema.getOrDefault(s, s))
                            .collect(Collectors.joining(" "))
            );

            paso.setEntrada(String.join(" ", lexemasOriginales.subList(posLexema, lexemasOriginales.size())));
            paso.setToken(tokenActual + "");
            paso.setLexema(terminalActual);

            if (topePila.equals(terminalActual)) {
                pila.pop();
                entrada.remove(0);
                posLexema++;
                paso.setAccion("pop");
            } else if (topePila.equals("$")) {
                paso.setAccion(topePila.equals(terminalActual) ? "aceptar" : "error");
                pasos.add(paso);
                break;
            } else if (tabla.esNoTerminal(topePila)) {
                List<String> produccion = tabla.getProduccion(topePila, terminalActual);
                if (produccion == null) {
                    paso.setAccion("error");
                    pasos.add(paso);
                    break;
                } else {
                    pila.pop();
                    if (!produccion.contains("epsilon")) {
                        for (int i = produccion.size() - 1; i >= 0; i--) {
                            pila.push(produccion.get(i));
                        }
                    }
                    paso.setAccion(topePila + " → " + String.join(" ", produccion));
                }
            } else {
                paso.setAccion("error");
                pasos.add(paso);
                break;
            }

            pasos.add(paso);
        }

        return pasos;
    }
}
