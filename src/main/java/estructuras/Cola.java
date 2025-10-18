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
}
