package dominio;

public class Accion {
    private TipoAccion tipo;
    private Object valorAnterior;
    private Object valorNuevo;

    public Accion(TipoAccion tipo, Object valorAnterior, Object valorNuevo) {
        this.tipo = tipo;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
    }

    public TipoAccion getTipo() {
        return tipo;
    }

    public Object getValorAnterior() {
        return valorAnterior;
    }

    public Object getValorNuevo() {
        return valorNuevo;
    }
}
