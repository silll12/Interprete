package com.mycompany.parte1;

import java.awt.Color;

public class PasoAnalisis {
    private String lexema;
    private Color color;
    private int token;

    public PasoAnalisis(String lexema, Color color, int token) {
        this.lexema = lexema;
        this.color = color;
        this.token = token;
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
}