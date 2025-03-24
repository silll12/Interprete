/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parte1;

/**
 *
 * @author silvi
 */
import java.util.HashSet;
import java.util.Set;

public class AFN {
    public static Set<AFN> ConjDeAFNs =  new HashSet<>();
    Estado EdoIni;
    Set<Estado> EdosAFN = new HashSet<Estado>();
    Set<Estado> EdosAcept =  new HashSet<Estado>();
    Set<Character> Alfabeto = new HashSet<>();
    boolean AgregoAFNUnionLexico;
    public int IdAFN;

    public AFN(){
        this.IdAFN=0;
        this.EdoIni=null;
        this.EdosAFN.clear();
        this.EdosAcept.clear();
        this.Alfabeto.clear();
        this.AgregoAFNUnionLexico=false;
    }

    public AFN CrearAFNBasico(char s){
        Transicion t;
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();
        t = new Transicion(s, e2);
        e1.getTrans1().add(t);
        e2.setEdoAcept(true);
        Alfabeto.add(s);
        EdoIni=e1;
        EdosAFN.add(e1);
        EdosAFN.add(e2);
        EdosAcept.add(e2);
        AgregoAFNUnionLexico=false;
        return this;
    }
    public AFN CrearAFNBasico(char s1, char s2){
        Transicion t;
        Estado e1,e2;
        e1=new Estado();
        e2=new Estado();
        t= new Transicion(s1,s2, e2);
        e1.getTrans1().add(t);
        e2.setEdoAcept(true);
        for(char i=s1;i<=s2;i++){
            Alfabeto.add(i);
        }
        EdoIni=e1;
        EdosAFN.add(e1);
        EdosAFN.add(e2);
        EdosAcept.add(e2);
        AgregoAFNUnionLexico=false;
        return this;
    }

    public AFN UnirAFN(AFN f2){
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        //e1 tendra dos transiciones epsilon, una al edo inicial del AFN this y otra al de AFN2
        Transicion t1 = new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni );
        Transicion t2 = new Transicion(SimbolosEspeciales.EPSILON, f2.EdoIni);
        e1.getTrans1().add(t1);
        e2.getTrans1().add(t2);
        //Cada estado de aceptacion de this y f2 tendran una trans epsilon
        //Los de aceptacion dejaran de ser de aceptacion
        for(Estado e:this.EdosAcept){
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON,e2));
            e.setEdoAcept(false);
        }
        for(Estado e: f2.EdosAcept){
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON,e2));
            e.setEdoAcept(false);
        }

        this.EdosAcept.clear();
        f2.EdosAcept.clear();
        this.EdoIni=e1;
        e2.setEdoAcept(true);
        this.EdosAcept.add(e2);
        this.EdosAFN.addAll(f2.EdosAFN);
        this.EdosAcept.add(e1);
        this.EdosAcept.add(e2);
        this.Alfabeto.addAll(f2.Alfabeto);
        return this;
    }
    public AFN ConcAFN(AFN f2){
        // Para cada estado de aceptación de `this`, añadimos una transición epsilon hacia el estado inicial de `f2`
        for(Estado e : this.EdosAcept){
                e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON,f2.EdoIni));
                e.setEdoAcept(false);
            }
        //Eliminamos el estado inicial de f2 de su lista de estados
        f2.EdosAFN.remove(f2.EdoIni);
        //Unimos los estados de transicion de f2 en this
        this.EdosAcept = f2.EdosAcept;
        this.EdosAFN.addAll(f2.EdosAFN);
        this.Alfabeto.addAll(f2.Alfabeto);
        return this;
    }

    public AFN Opcional(){
        AFN f= new AFN();
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();
        e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni));
        e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON,e2));

        for(Estado e: this.EdosAcept){
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.setEdoAcept(false);
        }

        e2.setEdoAcept(true);
        f.EdoIni=e1;
        f.Alfabeto.addAll(this.Alfabeto);
        f.EdosAFN.addAll(this.EdosAFN);
        f.EdosAFN.add(e1);
        f.EdosAFN.add(e2);
        f.EdosAcept.add(e2);
        return f;
    }
    public AFN CerraduraKleene(){
        AFN f= new AFN();
        Estado e1, e2;
        e1=new Estado();
        e2=new Estado();
        e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni));
        e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));

        for(Estado e: this.EdosAcept){
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni));
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.setEdoAcept(false);
        }
        e2.setEdoAcept(true);
        f.EdoIni=  e1;
        f.EdosAFN.add(e1);
        f.EdosAFN.add(e2);
        f.EdosAcept.add(e2);
        return f;
    }
    public AFN CerraduraPositiva(){
        AFN f= new AFN();
        Estado e1, e2;
        e1=new Estado();
        e2=new Estado();
        e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni));

        for(Estado e: this.EdosAcept){
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.EdoIni));
            e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.setEdoAcept(false);
        }
        e2.setEdoAcept(true);
        f.EdoIni=  e1;
        f.EdosAFN.add(e1);
        f.EdosAFN.add(e2);
        f.EdosAcept.add(e2);
        return f;
    }

}