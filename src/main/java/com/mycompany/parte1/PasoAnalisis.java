package com.mycompany.parte1;

import java.awt.Color;

public class PasoAnalisis {
    private String lexema;
    private Color color;
    private int token;

    // Campos adicionales para análisis sintáctico
    public String pila;
    public String entrada;
    public String accion;

    public PasoAnalisis(String lexema, Color color, int token) {
        this.lexema = lexema;
        this.color = color;
        this.token = token;
    }

    public PasoAnalisis() {
        // Constructor vacío para uso flexible
    }

    public String getLexema() {
        return lexema;
    }

    public Color getColor() {
        return color;
    }

    public int getToken() {
        return token;
    }

    // Setters añadidos para pila, entrada, acción, lexema, color y token
    public void setPila(String pila) {
        this.pila = pila;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setToken(String tokenStr) {
        try {
            this.token = Integer.parseInt(tokenStr);
        } catch (NumberFormatException e) {
            this.token = -1; // Valor por defecto en caso de error
        }
    }
}
