package controller;

import dominio.*;
import estructuras.*;
import java.util.Scanner;

public class CaeController {

    private static final Scanner sc = new Scanner(System.in);

    public static final Cola<Ticket> ticketsEspera = new Cola<>();
    private static final ListaEnlazadaSimple<Ticket>  ticketsFinalizados = new ListaEnlazadaSimple<>();

    public static Ticket ticketAtencion;
    private static Pila<Accion> undoStack = new Pila<>();
    private static Pila<Accion> redoStack = new Pila<>();

    private static Ticket crearTicket() {
        System.out.print("Agrega el nombre del estudiante: ");
        String estudiante = sc.nextLine();
        System.out.print("Agrega el nro del tramite: ");
        String tramite = sc.nextLine();

        return new Ticket(estudiante, tramite);
    }

    public static void agregarTicket() {
        Ticket nuevo = crearTicket();
        ticketsEspera.enqueue(nuevo);
        System.out.println("Ticket agregado a la cola: " + nuevo);
    }

     public static void atenderSiguienteTicket() {
        if (ticketAtencion != null) {
            System.out.println("Ya hay un ticket en atención: " + ticketAtencion);
            return;
        }

        ticketAtencion = ticketsEspera.dequeue();

        if (ticketAtencion == null) {
            System.out.println("No hay tickets en espera.");
            return;

        }

        ticketAtencion.setEstado(EstadoTicket.EN_ATENCION);
        undoStack = new Pila<>();
        redoStack = new Pila<>();

        System.out.println("Atendiendo ahora el " + ticketAtencion);
    }

    //Opciones submenu de gestion

     public static void agregarNota() {
        System.out.print("Escriba la nota: ");
        String textoNota = sc.nextLine();
        Nota nuevaNota = new Nota(textoNota);

        ticketAtencion.getHistorialNotas().insertarAlInicio(nuevaNota);

        // Guarda la accion para poder deshacerla (Undo)
        Accion accion = new Accion(TipoAccion.AGREGAR_NOTA, null, nuevaNota);
        undoStack.push(accion);
        // Una nueva acción borra el historial de "rehacer" (Redo)
        redoStack = new Pila<>();

        System.out.println("Nota agregada");
    }



public static void deshacerAccion() {
        Accion accion = undoStack.pop();
        if (accion == null) {
            System.out.println("No hay acciones para deshacer");
            return;
        }

        switch(accion.getTipo()) {
            case AGREGAR_NOTA:
                ticketAtencion.getHistorialNotas().eliminaPrimeraCoincidencia((Nota) accion.getValorNuevo());
                System.out.println("Nota eliminada");
                break;
            case CAMBIAR_ESTADO:
                ticketAtencion.setEstado((EstadoTicket) accion.getValorAnterior());
                System.out.println("Estado restaurado a " + accion.getValorAnterior());
                break;
        }
        redoStack.push(accion);
    }

    public static void rehacerAccion() {
        Accion accion = redoStack.pop();
        if (accion == null) {
            System.out.println("No hay acciones para rehacer");
            return;
        }

        switch(accion.getTipo()) {
            case AGREGAR_NOTA:
                ticketAtencion.getHistorialNotas().insertarAlInicio((Nota) accion.getValorNuevo());
                System.out.println("Redo: Nota re-agregada.");
                break;
            case CAMBIAR_ESTADO:
                ticketAtencion.setEstado((EstadoTicket) accion.getValorNuevo());
                System.out.println("Redo: Estado cambiado a " + accion.getValorNuevo());
                break;
        }
        undoStack.push(accion);
    }

    public static void finalizarAtencion() {
        if (ticketAtencion.getEstado() != EstadoTicket.COMPLETADO && ticketAtencion.getEstado() != EstadoTicket.CANCELADO) {
            System.out.println("Advertencia: El ticket no esta en estado COMPLETADO o CANCELADO.");
            System.out.print("Desea finalizarlo? (s/n): ");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Finalización cancelada");
                return;
            }
            ticketAtencion.setEstado(EstadoTicket.COMPLETADO);
        }

        ticketsFinalizados.insertarAlInicio(ticketAtencion); // Guardamos en el historial
        System.out.println("Ticket #" + ticketAtencion.getId() + " finalizado y archivado");
        ticketAtencion = null;
    }

    //menu

    public static void imprimirCola(){
        ticketsEspera.imprimir();
    }

    public static void consultarHistorialFinalizados() {
        System.out.println("\n--- Historial de Tickets Finalizados ---");

        if (ticketsFinalizados.estaVacia()) {
            System.out.println(" (no hay tickets finalizados)");
            return;
        }

        Nodo<Ticket> actual = ticketsFinalizados.getCabeza();
        int i = 1;

        while (actual != null) {
            Ticket t = actual.dato;
            System.out.println("\n" + i++ + ") " + t);
            System.out.println("   📋 Historial de notas:");
            t.getHistorialNotas().mostrar();
            actual = actual.siguiente;
        }

        System.out.println("------------------------------------------");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
