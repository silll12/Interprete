/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parte1;

/**
 *
 * @author silvi
 */

public class ER_AFN {

    String exprRegular; //sigma
    public AFN result; //automata que corresponde a la er
    public AnalizadorLex L;


    public ER_AFN(String sigma, String FileAFD) {
        this.exprRegular = sigma;
        this.L = new AnalizadorLex(exprRegular, FileAFD);
    }

    public void setExprRegular(String sigma) {
        this.exprRegular = sigma;
        L.SetSigma(sigma);
    }

    public boolean iniConversion() {
        int Token;
        AFN f = new AFN();

        if (E(f)) {
            Token = L.yylex();
            if (Token == 0) {
                this.result = f;
                return true;
            }
        }
        return false;
    }

    public boolean E(AFN f) {
        if (T(f)) {
            return Ep(f);
        }
        return false;
    }

    public boolean Ep(AFN f) {

        int Token;
        AFN f2 = new AFN();

        Token = L.yylex();

        if (Token == 10) { // OR
            if (T(f2)) {
                f.UnirAFN(f2); // unión de f y f2
                if (Ep(f))
                    return true;
            }
            return false;
        }

        L.UndoToken(); // epsilon
        return true;
    }

    public boolean T(AFN f) {
        if (C(f)) {
            if (Tp(f))
                return true;
        }
        return false;
    }

    public boolean Tp(AFN f) {
        int Token;
        AFN f2 = new AFN();
        Token = L.yylex();

        if (Token == 20) { // CONC

            if (C(f2)) {
                f.ConcAFN(f2); // Acción semántica: concatenación
                if (Tp(f)) ;
                return true;
            }
            return false;
        }

        L.UndoToken(); // epsilon
        return true;
    }

    public boolean C(AFN f) {
        if (F(f)) {
            if (Cp(f)) ;
            return true;
        }
        return false;
    }

    private boolean Cp(AFN f) {
        int Token;
        Token = L.yylex();

        switch (Token) {
            case 30: // +
                f.CerraduraPositiva(); // acción semántica
                return Cp(f);
            case 40: // *
                f.CerraduraKleene();// acción semántica
                return Cp(f);
            case 50: // ?
                f.Opcional(); // acción semántica
                return Cp(f);
            default:
                L.UndoToken(); // ε (vacío)
                return true;
        }
    }

    public boolean F(AFN f) {
        int Token;
        char simbolo1, simbolo2;
        Token = L.yylex();

        switch (Token) {
            case 60: // Paréntesis izquierdo "("
                if (E(f)) {
                    Token = L.yylex();
                    if (Token == 70) { // Paréntesis derecho ")"
                        return true;
                    }
                }
                return false;

            case 80: // Cochete IZQ
                Token = L.yylex();
                if (Token == 110) //Simbolo
                {
                    simbolo1 = (L.Lexema.charAt(0) == '\\') ? L.Lexema.charAt(1) : L.Lexema.charAt(0);
                    Token = L.yylex();
                    if (Token == 100) {  //guion
                        Token = L.yylex();
                        if (Token == 110) {  //simb
                            simbolo2 = (L.Lexema.charAt(0) == '\\') ? L.Lexema.charAt(1) : L.Lexema.charAt(0);
                            Token = L.yylex();
                            if (Token == 90) {  //CORCH_DER
                                f.CrearAFNBasico(simbolo1, simbolo2);
                                return true;
                            }
                        }
                    }
                }
                return false;

            case 110: //simbolo
                simbolo1 = (L.Lexema.charAt(0) == '\\') ? L.Lexema.charAt(1) : L.Lexema.charAt(0);
                f.CrearAFNBasico(simbolo1);
                return true;
        }
        return false;

    }



}