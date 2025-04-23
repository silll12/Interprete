package com.mycompany.parte1;

import javax.swing.*;
import java.util.*;
import java.util.Queue;

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
        f.Alfabeto.addAll(this.Alfabeto);
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
        f.EdosAFN.addAll(this.EdosAFN);
        f.EdosAFN.add(e1);
        f.EdosAFN.add(e2);
        f.EdosAcept.add(e2);
        f.Alfabeto.addAll(this.Alfabeto);
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
        f.EdoIni = e1;
        f.EdosAFN.addAll(this.EdosAFN);  // Añadido para preservar todos los estados
        f.EdosAFN.add(e1);
        f.EdosAFN.add(e2);
        f.EdosAcept.add(e2);
        f.Alfabeto.addAll(this.Alfabeto);  // Añadido para preservar el alfabeto
        return f;
    }


    //Metodos para pasar de AFN a AFD
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
    public HashSet<Estado> Ir_A(HashSet<Estado> Edos, char Simb) {
        HashSet<Estado> MoverEstados = Mover(Edos, Simb);
        return CerraduraEpsilon(MoverEstados);
    }


    private int IndiceCaracter(char[] ArregloAlfabeto, char c){
        int i;
        for(i=0; i<ArregloAlfabeto.length; i++)
            if(ArregloAlfabeto[i]==c)
                return i;
        return -1;
    }

    public AFD ConvertirAFNaAFD() {
        int CardAlfabeto, NumEdosAFD;
        int i, j, r;
        char[] ArrAlfabeto;
        ConjIj Ij, Ik;
        boolean existe;

        HashSet<Estado> ConjAux = new HashSet<>();
        HashSet<ConjIj> EdosAFD = new HashSet<>();
        Queue<ConjIj> EdosSinAnalizar = new LinkedList<>();

        AFD afd = new AFD();

        CardAlfabeto = Alfabeto.size();
        ArrAlfabeto = new char[CardAlfabeto];
        i = 0;
        for (char c : Alfabeto) {
            ArrAlfabeto[i++] = c;
        }


        j = 0;
        Ij = new ConjIj(CardAlfabeto);
        Ij.ConjI = CerraduraEpsilon(this.EdoIni);
        Ij.j = j;

        EdosAFD.add(Ij);
        EdosSinAnalizar.add(Ij);
        j++;

        while (!EdosSinAnalizar.isEmpty()) {
            Ij = EdosSinAnalizar.poll();

            for (char c : ArrAlfabeto) {
                Ik = new ConjIj(CardAlfabeto);
                Ik.ConjI = Ir_A( Ij.ConjI, c);

                if (Ik.ConjI.isEmpty()) {
                    continue;
                }

                existe = false;
                for (ConjIj I : EdosAFD) {
                    if (I.ConjI.equals(Ik.ConjI)) {
                        existe = true;
                        r = IndiceCaracter(ArrAlfabeto, c);
                        Ij.TransicionesAFD[r] = I.j;
                        break;
                    }
                }

                if (!existe) {
                    Ik.j = j;
                    r = IndiceCaracter(ArrAlfabeto, c);
                    Ij.TransicionesAFD[r] = Ik.j;
                    EdosAFD.add(Ik);
                    EdosSinAnalizar.add(Ik);
                    j++;
                }
            }
        }

        afd.alfabeto = new HashSet<>(this.Alfabeto);

        NumEdosAFD = EdosAFD.size();
        Estado[] estados = new Estado[NumEdosAFD];

        for (ConjIj I : EdosAFD) {
            estados[I.j] = new Estado();
            if (I.ConjuntoAceptacion()) {
                estados[I.j].setEdoAcept(true);
                estados[I.j].setToken1(I.obtenerTokenAceptacion()); // Añade esta línea
                afd.estadosAceptacion.add(estados[I.j]);
            }
            afd.estados.add(estados[I.j]);
        }


        afd.estadoInicial = estados[0];

        for (ConjIj I : EdosAFD) {
            for (i = 0; i < CardAlfabeto; i++) {
                if (I.TransicionesAFD[i] >= 0) {
                    Transicion t = new Transicion(ArrAlfabeto[i], estados[I.TransicionesAFD[i]]);
                    estados[I.j].agregarTransicion(t);
                }
            }
        }

        return afd;
    }
    //Finalizacion de los metodos para pasar de AFN a AFD
    public void UnionEspecialAFNs(AFN f, int Token) {
        Estado e;
        if(this.AgregoAFNUnionLexico) {
            this.EdosAFN.clear();
            this.Alfabeto.clear();
            e = new Estado();
            e.getTrans1().add(new Transicion(SimbolosEspeciales.EPSILON, f.EdoIni));
            this.EdoIni = e;
            this.EdosAFN.add(e);
            this.AgregoAFNUnionLexico = true;
        }
        else {
            this.EdoIni.getTrans1().add(new Transicion(SimbolosEspeciales.EPSILON, f.EdoIni));
        }

        // Establecer el token para los estados de aceptación del AFN que se está uniendo
        for(Estado EdoAcep : f.EdosAcept) {
            EdoAcep.setToken1(Token);
        }

        this.EdosAcept.addAll(f.EdosAcept);
        this.EdosAFN.addAll(f.EdosAFN);
        this.Alfabeto.addAll(f.Alfabeto);
    }
}