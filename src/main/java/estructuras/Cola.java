package estructuras;

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
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("(cola vacía)");
            return;
        }

        Nodo<T> actual = frente;
        System.out.println("Contenido de la cola:");
        int i = 1;
        while (actual != null) {
            System.out.println(" " + i++ + " - " + actual.dato.toString());
            actual = actual.siguiente;
        }
    }
}
