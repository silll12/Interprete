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

    // Método para obtener el estado actual del analizador léxico
    public ClassEstadoAnalizLexico GetEdoAnalizadorLexico() {
        ClassEstadoAnalizLexico EdoActualAnaliz = new ClassEstadoAnalizLexico();
        EdoActualAnaliz.CaracterActual = CaracterActual;
        EdoActualAnaliz.EdoActual = EdoActual;
        EdoActualAnaliz.EdoTransicion = EdoTransicion;
        EdoActualAnaliz.FinLexema = FinLexema;
        EdoActualAnaliz.IndiceCaracterActual = IndiceCaracterActual;
        EdoActualAnaliz.IniLexema = IniLexema;
        EdoActualAnaliz.Lexema = Lexema;
        EdoActualAnaliz.PasoPortEdoAcept = PasoEdoAcept;
        EdoActualAnaliz.token = token;
        EdoActualAnaliz.Pila = (Stack<Integer>) Pila.clone();
        return EdoActualAnaliz;
    }

    // Método para establecer el estado del analizador léxico
    public boolean SetEdoAnalizadorLexico(ClassEstadoAnalizLexico e) {
        CaracterActual = e.CaracterActual;
        EdoActual = e.EdoActual;
        EdoTransicion = e.EdoTransicion;
        FinLexema = e.FinLexema;
        IndiceCaracterActual = e.IndiceCaracterActual;
        IniLexema = e.IniLexema;
        Lexema = e.Lexema;
        PasoEdoAcept = e.PasoPortEdoAcept;
        token = e.token;
        Pila = (Stack<Integer>) e.Pila.clone();
        return true;
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

    public AFD getAFD() {
        return AutomataFD;
    }
}