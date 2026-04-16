package com.ejemplo.demo.domain.model;

public class EstadoManual {

    // POJO sin anotaciones Spring: cada "new EstadoManual()" inicia desde cero.
    private int valor = 0;

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
