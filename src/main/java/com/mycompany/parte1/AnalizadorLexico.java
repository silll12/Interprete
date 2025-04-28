package com.mycompany.parte1;

import java.io.File;
import java.util.Stack;

public class AnalizadorLexico {
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

    public AnalizadorLexico() {
        CadenaSigma = "";
        PasoEdoAcept = false;
        IniLexema = -1;
        FinLexema = -1;
        IndiceCaracterActual = -1;
        token = -1;
        Pila.clear();
        AutomataFD = null;
    }

    public AnalizadorLexico(String sigma, String FileAFD) {
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
            EdoActual=0;
            PasoEdoAcept = false;
            FinLexema = -1;
            token=-1;



            while (IndiceCaracterActual < CadenaSigma.length()) {
                CaracterActual = CadenaSigma.charAt(IndiceCaracterActual);
                EdoTransicion = AutomataFD.TablaAFD[EdoActual] [CaracterActual];

                if (EdoTransicion != -1) {
                    if (AutomataFD.TablaAFD[EdoTransicion] [256] !=-1) {
                        PasoEdoAcept = true;
                        token = AutomataFD.TablaAFD[EdoTransicion] [256];
                        FinLexema = IndiceCaracterActual;
                    }
                    IndiceCaracterActual++;
                    EdoActual = EdoTransicion;
                    continue;
                }
                break;
            }


            if (!PasoEdoAcept) {
                IndiceCaracterActual = IniLexema+1;
                Lexema= CadenaSigma.substring(IniLexema,1);
                token = SimbolosEspeciales.ERROR;
                return token;
            }

            Lexema = CadenaSigma.substring(IniLexema, FinLexema-IniLexema + 1);
            IndiceCaracterActual = FinLexema +1;
            if (token== SimbolosEspeciales.OMITIR) {
                continue;
            }
            else {
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
}
