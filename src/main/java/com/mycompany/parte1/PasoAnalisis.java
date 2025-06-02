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