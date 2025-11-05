package estructuras;

import dominio.EstadoTicket;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;

    public boolean estaVacia() {
        return frente == null;
    }

    public void enqueue(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = fin = nuevo;
        }  else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
    }

    public T dequeue() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return dato;
    }
    public void imprimir(String nombre_cola) {
        if (estaVacia()) {
            System.out.println("\u001B[1mContenido de la cola "+nombre_cola+":\u001B[0m");
            System.out.println("================================================");
            System.out.println("\t\u001B[34mNo existe tickets "+nombre_cola+" en la cola\u001B[0m");
            System.out.println("================================================\n");
            return;
        }

        Nodo<T> actual = frente;
        System.out.println("\u001B[1mContenido de la cola "+nombre_cola+":\u001B[0m");
        System.out.println("================================================");
        int i = 1;
        while (actual != null) {
            System.out.println("\t\u001B[34m " + i++ + " - " + actual.dato.toString()+"\u001B[0m");
            actual = actual.siguiente;
        }
        System.out.println("================================================\n");
    }

}
