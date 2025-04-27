package com.mycompany.parte1;

import java.io.File;
import java.util.Stack;

public class AnalizadorLex {
    AFD AutomataFD;
    String CadenaSigma;
    public String Lexema;
    int IndiceCaracterActual;
    int IniLexema;
    int FinLexema;
    int token, EdoActual, EdoTransicion;
    boolean PasoEdoAcept;
    char CaracterActual;
    Stack<Integer> Pila = new Stack<>();

    public AnalizadorLex() {
        CadenaSigma = "";
        PasoEdoAcept = false;
        IniLexema = -1;
        FinLexema = -1;
        IndiceCaracterActual = -1;
        token = -1;
        Pila.clear();
        AutomataFD = null;
    }

    public AnalizadorLex(String sigma, String FileAFD) {
        AutomataFD = new AFD();
        CadenaSigma = sigma;
        PasoEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
        AutomataFD = AFD.cargarAFDDesdeArchivo(new File(FileAFD));
    }

    public void SetSigma(String sigma) {
        CadenaSigma = sigma;
        PasoEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
    }

    public int yylex() {
        while (true) {
            Pila.push(IndiceCaracterActual);

            if (IndiceCaracterActual >= CadenaSigma.length()) {
                Lexema="";
                return SimbolosEspeciales.FIN;
            }

            IniLexema = IndiceCaracterActual;
            EdoActual = 0;
            PasoEdoAcept = false;
            FinLexema = -1;
            token = -1;

            while (IndiceCaracterActual < CadenaSigma.length()) {
                CaracterActual = CadenaSigma.charAt(IndiceCaracterActual);

                // Asegurarse de que el valor ASCII del carácter esté dentro de los límites
                int valorAscii = (int) CaracterActual;
                if (valorAscii >= 0 && valorAscii < 256 && EdoActual >= 0 && EdoActual < AutomataFD.TablaAFD.length) {
                    EdoTransicion = AutomataFD.TablaAFD[EdoActual][valorAscii];
                } else {
                    EdoTransicion = -1; // Transición no válida
                }

                if (EdoTransicion != -1) {
                    if (EdoTransicion >= 0 && EdoTransicion < AutomataFD.TablaAFD.length &&
                            AutomataFD.TablaAFD[EdoTransicion][256] != -1) {
                        PasoEdoAcept = true;
                        token = AutomataFD.TablaAFD[EdoTransicion][256];
                        FinLexema = IndiceCaracterActual;
                    }
                    IndiceCaracterActual++;
                    EdoActual = EdoTransicion;
                    continue;
                }
                break;
            }

            if (!PasoEdoAcept) {
                IndiceCaracterActual = IniLexema + 1;
                Lexema = CadenaSigma.substring(IniLexema, IniLexema + 1);
                token = SimbolosEspeciales.ERROR;
                return token;
            }

            Lexema = CadenaSigma.substring(IniLexema, FinLexema + 1);
            IndiceCaracterActual = FinLexema + 1;
            if (token == SimbolosEspeciales.OMITIR) {
                continue;
            } else {
                return token;
            }
        }
    }

    public boolean UndoToken() {
        if (Pila.isEmpty())
            return false;
        IndiceCaracterActual = Pila.pop();
        return true;
    }

    /**
     * Returns the AFD used by this analyzer
     */
    public AFD getAFD() {
        return AutomataFD;
    }

    /**
     * Returns the current position in the input string
     */
    public int getPosition() {
        return IndiceCaracterActual;
    }

    /**
     * Resets the analyzer to the beginning of the input
     */
    public void reset() {
        this.IndiceCaracterActual = 0;
        this.Lexema = "";
        this.PasoEdoAcept = false;
        this.IniLexema = 0;
        this.FinLexema = -1;
        this.token = -1;
        this.Pila.clear();
    }
}