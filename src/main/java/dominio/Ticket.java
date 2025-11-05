package dominio;

import estructuras.ListaEnlazadaSimple;

public class Ticket {
    private static int contador = 0;
    private int id;
    private Procesos proceso;
    private Persona  persona;
    public String nroTicket;
    private EstadoTicket estado;
    private ListaEnlazadaSimple<Nota> historialNotas;

    public Ticket(Procesos proceso) {
        this.id = ++contador;
        this.proceso = proceso;
        this.nroTicket = GenerarIdTicket.generarIdTicket(proceso);
        this.estado = EstadoTicket.EN_COLA; // estado inicial default
        this.historialNotas = new ListaEnlazadaSimple<>();
    }

    public int getId() {
        return id;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public Persona getPersona() {
        return persona;
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

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public String toString() {
        String dato = "Ticket #" + id +
                " | Nro Ticket: "+ nroTicket +
                " | Proceso: "+ proceso.name() +
                " | Estado: " + estado;
        if (EstadoTicket.EN_ATENCION.equals(estado) || EstadoTicket.COMPLETADO.equals(estado)) {
            dato += " | Persona: " + persona;
        } else {
            dato += " | Datos de persona: En espera";
        }
        return dato;

    }
}
