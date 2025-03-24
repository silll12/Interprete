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

public class Estado {
    static int ContadorIdEstado;

    static {
        ContadorIdEstado = 0;
    }

    private int idEstado1;
    private boolean edoAcept1;
    private Set<Transicion> trans1 = new HashSet<>();

    public Estado() {
        this.idEstado1 = ContadorIdEstado++;
        this.edoAcept1 = false;
    }

    public Set<Transicion> getTrans1() {
        return trans1;
    }
    public void setTrans1(Set<Transicion> trans1) {
        this.trans1 = trans1;
    }
    public boolean getEdoAcept() {return edoAcept1;}
    public void setEdoAcept(boolean edoAcept1) {this.edoAcept1 = edoAcept1;}
    public int getIdEstado() {return idEstado1;}
    public void setIdEstado(int idEstado1) {this.idEstado1 = idEstado1;}
    public void agregarTransicion(Transicion t) {this.trans1.add(t);}

}
