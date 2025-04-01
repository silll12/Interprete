/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parte1;

/**
 *
 * @author silvi
 */
import javax.swing.*;
import java.util.*;

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
    public HashSet<Estado> CerraduraEpsilon(Estado e){
        HashSet<Estado> R= new HashSet<Estado>();//conjunto de esstados
        Stack<Estado> S= new Stack<Estado>();//pil de estados
        Estado aux, Edo;
        //dejamos vacios el connjunto de estados y la pila
        R.clear();
        S.clear();
        //estado del que quiero calcular la cerradura
        S.push(e);
        //mientras no este vacia
        while(!S.isEmpty()){
            aux = S.pop();//sacamos elemento de la pila
            R.add(aux);//lo agrego al conjunto de estados dado que faltan por revisarse
            for(Transicion t: aux.getTrans1())
                if((Edo = t.getEdoTrans(SimbolosEspeciales.EPSILON))!=null)
                    if(!R.contains(Edo))
                        S.push(Edo);
        }
        return R;
    }
    public HashSet<Estado> CerraduraEpsilon(HashSet<Estado> ConjEdos){
        HashSet<Estado> R= new HashSet<Estado>();//conjunto de esstados
        Stack<Estado> S= new Stack<Estado>();//pil de estados
        Estado aux, Edo;
        //dejamos vacios el connjunto de estados y la pila
        R.clear();
        S.clear();
        for(Estado e: ConjEdos)
            S.push(e);
        while(!S.isEmpty()){
            aux=S.pop();
            R.add(aux);
            for(Transicion t: aux.getTrans1())
                if((Edo=t.getEdoTrans(SimbolosEspeciales.EPSILON)) !=null)
                    if(!R.contains(Edo))
                        S.push(Edo);
        }
        return R;
    }
    public HashSet<Estado> Mover(Estado Edo, char Simb){
        HashSet<Estado> C= new HashSet<Estado>();
        Estado Aux;
        C.clear();
        //Para cada transicion obtengo para que estado es la transicion
        for(Transicion t: Edo.getTrans1()){
            Aux=t.getEdoTrans(Simb);//comprueba si hay transicion con ese simbolo
            if(Aux!= null) //muestra si hubo una transicion con es simbolo
                C.add(Aux);
        }
        return C;
    }
    public HashSet<Estado> Mover(HashSet<Estado> Edos, char Simb){
        HashSet<Estado> C= new HashSet<Estado>();
        Estado Aux;
        C.clear();
        //Barrer el conjunto de estados
        for(Estado Edo: Edos)
            //exactamente lo mismo que lo anterior
            for (Transicion t: Edo.getTrans1()){
                Aux=t.getEdoTrans(Simb);//comprueba si hay transicion con ese simbolo
                if(Aux!= null) //muestra si hubo una transicion con es simbolo
                    C.add(Aux);
            }
        return C;
    }
    public HashSet<Estado> Ir_A(HashSet<Estado> Edos, char Simb){
        HashSet<Estado> C= new HashSet<Estado>();
        C.clear();
        HashSet<Estado> MoverEstados= Mover(Edos, Simb);
        C=CerraduraEpsilon(MoverEstados);
        return C;
    }

    public AFD ConvertirAFNaAFD() {
        AFD afd = new AFD();

        // Estructuras para el algoritmo
        HashMap<Set<Estado>, Estado> estadosAFD = new HashMap<>();
        Queue<Set<Estado>> pendientes = new LinkedList<>();

        // Obtener cerradura epsilon del estado inicial del AFN
        Set<Estado> cerraduraInicial = CerraduraEpsilon(this.EdoIni);

        // Crear el estado inicial del AFD
        Estado estadoInicialAFD = new Estado();
        afd.estadoInicial = estadoInicialAFD;
        afd.estados.add(estadoInicialAFD);

        // Marcar si el estado inicial del AFD es de aceptación
        for (Estado e : cerraduraInicial) {
            if (e.getEdoAcept()) {
                estadoInicialAFD.setEdoAcept(true);
                afd.estadosAceptacion.add(estadoInicialAFD);
                break;
            }
        }

        // Asociar la cerradura inicial con el estado inicial del AFD
        estadosAFD.put(cerraduraInicial, estadoInicialAFD);
        pendientes.add(cerraduraInicial);

        // Copiar el alfabeto del AFN al AFD
        afd.alfabeto.addAll(this.Alfabeto);

        // Procesar todos los conjuntos de estados pendientes
        while (!pendientes.isEmpty()) {
            Set<Estado> conjuntoActual = pendientes.poll();
            Estado estadoActualAFD = estadosAFD.get(conjuntoActual);

            // Para cada símbolo del alfabeto
            for (Character simbolo : this.Alfabeto) {
                // Ignorar el símbolo epsilon
                if (simbolo == SimbolosEspeciales.EPSILON) {
                    continue;
                }

                // Calcular Ir_A(conjuntoActual, simbolo)
                Set<Estado> conjuntoDestino = Ir_A(new HashSet<>(conjuntoActual), simbolo);

                // Si el resultado no es vacío
                if (!conjuntoDestino.isEmpty()) {
                    // Verificar si ya existe este conjunto de estados
                    if (!estadosAFD.containsKey(conjuntoDestino)) {
                        // Crear nuevo estado para el AFD
                        Estado nuevoEstadoAFD = new Estado();

                        // Verificar si el nuevo estado debe ser de aceptación
                        for (Estado e : conjuntoDestino) {
                            if (e.getEdoAcept()) {
                                nuevoEstadoAFD.setEdoAcept(true);
                                afd.estadosAceptacion.add(nuevoEstadoAFD);
                                break;
                            }
                        }

                        // Agregar el nuevo estado al AFD
                        afd.estados.add(nuevoEstadoAFD);
                        estadosAFD.put(conjuntoDestino, nuevoEstadoAFD);
                        pendientes.add(conjuntoDestino);
                    }

                    // Agregar la transición desde el estado actual al estado destino con el símbolo actual
                    Estado estadoDestinoAFD = estadosAFD.get(conjuntoDestino);
                    Transicion nuevaTransicion = new Transicion(simbolo, estadoDestinoAFD);
                    estadoActualAFD.agregarTransicion(nuevaTransicion);
                }
            }
        }

        return afd;
    }

}