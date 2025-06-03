package com.mycompany.parte1;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class DesRecGramDeGram {
    public String Gramatica;
    public AnalizadorLex L;
    public ElementRegla[] ArrReglas = new ElementRegla[100];
    public int NumReglas = 0;

    public HashSet<String> Vn = new HashSet<String>();
    public HashSet<String> Vt = new HashSet<String>();

    // Constructor limpio para uso interno en TablaLL1Numerada
    public DesRecGramDeGram() {
        this.Gramatica = "";
        this.L = null;
        this.ArrReglas = new ElementRegla[100];
        this.NumReglas = 0;
        this.Vn = new HashSet<>();
        this.Vt = new HashSet<>();
    }

    public DesRecGramDeGram(String sigma, String FileAFD) {
        this.Gramatica = sigma;
        this.L = new AnalizadorLex(Gramatica, FileAFD);
        Vn.clear();
        Vt.clear();
    }


    public boolean SetGramatica(String sigma) {
        this.Gramatica = sigma;
        this.L.SetSigma(sigma);
        return true;
    }

    public boolean AnalizarGramatica() {
        int token;
        if (G()) {
            token = L.yylex();
            if (token == 0) {
                IdentificarTerminales();
                return true;
            }
        }
        return false;
    }

    // G -> ListaReglas ;
    private boolean G() {
        if (ListaReglas()) {
            return true;
        }
        return false;
    }

    // ListaReglas -> Reglas PC ListaReglasP
    private boolean ListaReglas() {
        int token;
        if (Reglas()) {
            token = L.yylex();
            if (token == TokensGram_Gram.PC) {
                if (ListaReglasP()) {
                    return true;
                }
            }
        }
        return false;
    }

    // ListaReglasP -> Reglas PC ListaReglasP | epsilon
    private boolean ListaReglasP() {
        int token;
        ClassEstadoAnalizLexico e;
        e = L.GetEdoAnalizadorLexico();

        if (Reglas()) {
            token = L.yylex();
            if (token == TokensGram_Gram.PC) {
                if (ListaReglasP()) {
                    return true;
                }
            }
            return false;
        }

        // epsilon
        L.SetEdoAnalizadorLexico(e);
        return true;
    }

    // Reglas -> LadoIzquierdo FLECHA LadosDerechos
    private boolean Reglas() {
        int token;
        String[] Simbolo = new String[1]; // arreglo mutable de un solo elemento

        if (LadoIzquierdo(Simbolo)) {
            Vn.add(Simbolo[0]);
            token = L.yylex();
            if (token == TokensGram_Gram.FLECHA) {
                if (LadosDerechos(Simbolo[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    // LadoIzquierdo -> SIMBOLO
    private boolean LadoIzquierdo(String[] Simbolo) {
        int token = L.yylex();
        if (token == TokensGram_Gram.SIMBOLO) {
            Simbolo[0] = L.Lexema;
            return true;
        }
        return false;
    }


    // LadosDerechos -> LadoDerecho LadosDerechosP
    private boolean LadosDerechos(String Simbolo) {
        if (LadoDerecho(Simbolo)) {
            if (LadosDerechosP(Simbolo)){
                return true;
            }
        }
        return false;
    }

    // LadosDerechosP -> OR LadoDerecho LadosDerechosP | epsilon
    private boolean LadosDerechosP(String Simbolo) {
        int token;
        token = L.yylex();

        if (token == TokensGram_Gram.OR) {
            if (LadoDerecho(Simbolo)) {
                if (LadosDerechosP(Simbolo)) {
                    return true;
                }
                return false;
            }
        }

        // epsilon
        L.UndoToken();
        return true;
    }

    // LadoDerecho -> SecSimbolos
    private boolean LadoDerecho(String simbolo) {
        ArrayList<ClassNodo> lista = new ArrayList<>();
        if (SecSimbolos(lista)) {
            ArrReglas[NumReglas] = new ElementRegla();
            ArrReglas[NumReglas].InfoSimbolo = new ClassNodo(simbolo, false);
            ArrReglas[NumReglas++].ListaLadoDerecho = lista;

            return true;
        }
        return false;
    }

    // SecSimbolos -> SIMBOLO SecSimbolosP
    private boolean SecSimbolos (List < ClassNodo > Lista)
    {
        int token;
        ClassNodo N;
        token = L.yylex();
        if (token == TokensGram_Gram.SIMBOLO) {
            N = new ClassNodo(L.Lexema, false);
            if (SecSimbolosP(Lista)) {
                Lista.add(0, N);
                return true;
            }
        }
        return false;
    }

    // SecSimbolosP -> SIMBOLO SecSimbolosP | epsilon
    private boolean SecSimbolosP (List < ClassNodo > Lista)
    {
        int token;
        ClassNodo N;
        token = L.yylex();
        if (token == TokensGram_Gram.SIMBOLO) {
            N = new ClassNodo(L.Lexema, false);
            if (SecSimbolosP(Lista)) {
                Lista.add(0, N);
                return true;
            }
        }
        // epsilon
        L.UndoToken();
        return true;
    }

    private void IdentificarTerminales() {
        for (int i = 0; i < NumReglas; i++) {
            for (ClassNodo nodo : ArrReglas[i].ListaLadoDerecho) {
                if (!Vn.contains(nodo.Simbolo) && !nodo.Simbolo.equals("epsilon")) {
                    nodo.Terminal = true;
                    Vt.add(nodo.Simbolo);
                }
            }
        }
    }

    public HashSet<String> First(List<ClassNodo> lista) {
        HashSet<String> R = new HashSet<>();

        for (int j = 0; j < lista.size(); j++) {
            ClassNodo n = lista.get(j);
            if (n.Terminal || n.Simbolo.equals("epsilon")) {
                R.add(n.Simbolo);
                return R;
            }
            for (int i = 0; i < NumReglas; i++) {
                if (ArrReglas[i].InfoSimbolo.Simbolo.equals(n.Simbolo)) {
                    R.addAll(First(ArrReglas[i].ListaLadoDerecho));
                }
            }
            if (R.contains("epsilon")) {
                if (j == lista.size() - 1) continue;
                R.remove("epsilon");
            } else break;
        }
        return R;
    }

    public HashSet<String> Follow(String simbolo) {
        HashSet<String> R = new HashSet<>();

        if (ArrReglas[0].InfoSimbolo.Simbolo.equals(simbolo)) {
            R.add("$");
        }

        for (int i = 0; i < NumReglas; i++) {
            List<ClassNodo> ladoDerecho = ArrReglas[i].ListaLadoDerecho;
            for (int j = 0; j < ladoDerecho.size(); j++) {
                if (ladoDerecho.get(j).Simbolo.equals(simbolo)) {
                    List<ClassNodo> post = new ArrayList<>();
                    for (int k = j + 1; k < ladoDerecho.size(); k++) {
                        post.add(ladoDerecho.get(k));
                    }
                    if (post.isEmpty()) {
                        if (!ArrReglas[i].InfoSimbolo.Simbolo.equals(simbolo)) {
                            R.addAll(Follow(ArrReglas[i].InfoSimbolo.Simbolo));
                        }
                    } else {
                        HashSet<String> aux = First(post);
                        if (aux.contains("epsilon")) {
                            aux.remove("epsilon");
                            R.addAll(aux);
                            if (!ArrReglas[i].InfoSimbolo.Simbolo.equals(simbolo)) {
                                R.addAll(Follow(ArrReglas[i].InfoSimbolo.Simbolo));
                            }
                        } else {
                            R.addAll(aux);
                        }
                    }
                }
            }
        }
        return R;
    }
}