/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parte1;
import java.awt.Color;


/**
 *
 * @author silvi
 */
// Agrega esta clase interna al final de tu clase (o puede ser fuera también)
class PasoAnalisis {
    private String lexema;
    private Color color;
    private int token;

    // Constructor
    public PasoAnalisis(String lexema, Color color, int token) {
        this.lexema = lexema;
        this.color = color;
        this.token = token;
    }

    // Getters
    public String getLexema() { return lexema; }
    public Color getColor() { return color; }
    public int getToken() { return token; }
}
