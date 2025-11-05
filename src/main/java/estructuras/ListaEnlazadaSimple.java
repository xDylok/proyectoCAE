package estructuras;

public class ListaEnlazadaSimple<T> {
    private Nodo<T> cabeza;

    // inserta un nuevo elemento al inicio de la lista

    public void insertarAlInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
    }


    // elimina la primera vez que encuentra un dato especifico
    public boolean eliminaPrimeraCoincidencia(T dato) {
        if (cabeza == null) return false; // lista vacía

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            return true;
        }

        return false;
    }

    // muestra los elementos de la lsita
    public void mostrar(){
        Nodo<T> actual = cabeza;
        if(actual == null){
            System.out.println(" (no se encontraron elementos)");
            return;
        }
        int i = 1;
        while(actual != null){
            System.out.println(" " + i++ + " - " + actual.dato.toString());
            actual = actual.siguiente;
        }
    }
    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

}
