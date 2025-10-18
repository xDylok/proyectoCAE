package dominio;

import estructuras.ListaEnlazadaSimple;

public class Ticket {
    private static int contador = 0;
    private int id;
    private String nombreEstudiante;
    private String tramite;
    private EstadoTicket estado;
    private ListaEnlazadaSimple<Nota> historialNotas;

    public Ticket(String nombreEstudiante, String tramite) {
        this.id = ++contador;
        this.nombreEstudiante = nombreEstudiante;
        this.tramite = tramite;
        this.estado = EstadoTicket.EN_COLA; // estado inicial default
        this.historialNotas = new ListaEnlazadaSimple<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public ListaEnlazadaSimple<Nota> getHistorialNotas() {
        return historialNotas;
    }

    public void setHistorialNotas(ListaEnlazadaSimple<Nota> historialNotas) {
        this.historialNotas = historialNotas;
    }

    @Override
    public String toString() {
        return "Ticket #" + id +
                " | Estudiante: " + nombreEstudiante +
                " | Tramite: " + tramite +
                " | Estado: " + estado;
    }
}
