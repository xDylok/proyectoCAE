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
        if (cabeza != null) return false;

        // si el dato a eliminar es el primero
        if(cabeza.dato.equals(dato)){
            cabeza = cabeza.siguiente;
            return  true;
        }

        // el nodo esta en otra posicion
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }
        if (actual.siguiente == null) {
            actual.siguiente = actual.siguiente.siguiente; //salta el nodo a eliminar
            return true;
        }
        return false; // no encontro el dato
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

}
