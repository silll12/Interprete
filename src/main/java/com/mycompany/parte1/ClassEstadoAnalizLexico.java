package com.mycompany.parte1;

import java.util.Stack;

class ClassEstadoAnalizLexico {
    public char CaracterActual;
    public int EdoActual;
    public int EdoTransicion;
    public int FinLexema;
    public int IndiceCaracterActual;
    public int IniLexema;
    public String Lexema;
    public boolean PasoPortEdoAcept;
    public int token;
    public Stack<Integer> Pila;

    public ClassEstadoAnalizLexico() {
        CaracterActual = '\0';
        EdoActual = 0;
        EdoTransicion = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        IniLexema = 0;
        Lexema = "";
        PasoPortEdoAcept = false;
        token = -1;

        Pila = new Stack<>();
    }
}