package estructuras;

public class Pila<T> {
    private Nodo<T> cima;

    public boolean esVacia(){
        return cima == null;
    }

    public void push(T dato){
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
    }

    public T pop(){
        if(esVacia()){
            return null;
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        return dato;
    }
}
