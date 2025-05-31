package com.mycompany.parte1;

public class ClassNodo {
    public String Simbolo;
    public boolean Terminal;

    // Constructor por defecto
    public ClassNodo() {
        this.Simbolo = "";
        this.Terminal = false;
    }

    // Constructor con parámetros
    public ClassNodo(String simbolo, boolean terminal) {
        this.Simbolo = simbolo;
        this.Terminal = terminal;
    }



    // Método para comparar nodos
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClassNodo)) return false;
        ClassNodo otro = (ClassNodo) obj;
        return this.Simbolo.equals(otro.Simbolo) && this.Terminal == otro.Terminal;
    }

    @Override
    public int hashCode() {
        return Simbolo.hashCode() + (Terminal ? 1 : 0);
    }
}
