/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parte1;

/**
 *
 * @author silvi
 */
public class Transicion {
    private char SimbInf1;
    private char SimbSup1;
    private Estado Edo;
    public Transicion(char simb, Estado e){
        this.SimbInf1=simb;
        this.SimbSup1=simb;
        this.Edo= e;
    }

    public Transicion(char simb1, char simb2, Estado e){
        this.SimbInf1=simb1;
        this.SimbSup1 =simb2;
        this.Edo = e;
    }
    public Transicion(){
        this.Edo= null;
    }
    // Metodo para actualizar la transición con dos símbolos
    public void setTransicion(char s1, char s2, Estado e){
        this.SimbInf1 = s1;
        this.SimbSup1 = s2;
        this.Edo = e;
    }
    // Metodo para actualizar la transición con un símbolo
    public void setTransicion(char s1, Estado e){
        this.SimbInf1 = s1;
        this.SimbSup1 = s1;
        this.Edo = e;
    }

    public char getSimbInf1() {return SimbInf1;}
    public void setSimbInf1(char simbInf1) {this.SimbInf1 = simbInf1;}

    public char getSimbSup1() {return SimbSup1;}
    public void setSimbSup1(char simbSup1) {this.SimbSup1 = simbSup1;}


    //Va recibir un caracter y si bajo ese caracter indicara si tengo una transicion a un estado, sino, se regresa null
    public Estado getEdoTrans(char s) {
        if(this.SimbInf1<=s && s<=this.SimbSup1)
            return Edo;
        return null;
    }
}
