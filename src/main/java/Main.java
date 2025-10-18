import dominio.*;
import estructuras.*;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Cola<Ticket> ticketsEspera = new Cola<>();
    private static final ListaEnlazadaSimple<Ticket>  ticketsFinalizados = new ListaEnlazadaSimple<>();

    private static Ticket ticketAtencion = null;
    private static Pila<Accion> undoStack = new Pila<>();
    private static Pila<Accion> redoStack = new Pila<>();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuGestion();
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    agregarNota();
                    break;
                case 2:
                    cambiarEstado();
                    break;
                case 3:
                    deshacerAccion();
                    break;
                case 4:
                    rehacerAccion();
                    break;
                case 5:
                    finalizarAtencion();
                    opcion = 6; break; // Forzar salida del bucle
                case 6:
                    System.out.println("Regresando al menu principal. ");
                break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 6);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Centro de Atencion al Estudiante (CAE) ---");
        System.out.println("1. Recibir nuevo ticket de estudiante");
        System.out.println("2. Atender siguiente ticket en la cola");
        System.out.println("3. Gestionar ticket en atencion actual");
        System.out.println("4. Tickets en espera");
        System.out.println("5. Consultar historial de un ticket finalizado");
        System.out.println("6. Salir");
        System.out.println("-------------------------------------------------");
        if (ticketAtencion != null) {
            System.out.println("Atendiendo ahora: " + ticketAtencion);
        } else {
            System.out.println("️Ningun ticket en atencion.");
        }
        System.out.print("Seleccione una opcion: ");
    }

    private static void mostrarMenuGestion() {
        System.out.println("\n--- Gestionando Ticket #" + ticketAtencion.getId() + " ---");
        System.out.println("1. Agregar nota de observación");
        System.out.println("2. Cambiar estado del ticket");
        System.out.println("3. Deshacer última acción (Undo)");
        System.out.println("4. Rehacer última acción (Redo)");
        System.out.println("5. Finalizar atención de este ticket");
        System.out.println("6. Volver al menú principal");
        System.out.println("\n--- Historial de Notas ---");
        ticketAtencion.getHistorialNotas().mostrar();
        System.out.println("--------------------------");
        System.out.print("Seleccione una opción: ");
    }

    private static void agregarNota() {
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

    private static void cambiarEstado() {
        System.out.println("Seleccione el nuevo estado:");
        int i = 1;
        for (EstadoTicket est : EstadoTicket.values()) {
            System.out.println(i++ + ". " + est);
        }
        int opcion = leerOpcion();
        if (opcion > 0 && opcion < i) {
            EstadoTicket estadoAnterior = ticketAtencion.getEstado();
            EstadoTicket nuevoEstado = EstadoTicket.values()[opcion - 1];
            ticketAtencion.setEstado(nuevoEstado);

            Accion accion = new Accion(TipoAccion.CAMBIAR_ESTADO, estadoAnterior, nuevoEstado);
            undoStack.push(accion);
            redoStack = new Pila<>();

            System.out.println("Estado cambiado a " + nuevoEstado);
        } else {
            System.out.println("La opcion de estado no es correcta");
        }
    }

    private static void deshacerAccion() {
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

    private static void rehacerAccion() {
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

    private static void finalizarAtencion() {
        if (ticketAtencion.getEstado() != EstadoTicket.COMPLETADO && ticketAtencion.getEstado() != EstadoTicket.CANCELADO) {
            System.out.println("Advertencia: El ticket no esta en estado COMPLETADO o CANCELADO.");
            System.out.print("Desea finalizarlo? (s/n): ");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Finalización cancelada");
                return;
            }
        }
        ticketsFinalizados.insertarAlInicio(ticketAtencion); // Guardamos en el historial
        System.out.println("Ticket #" + ticketAtencion.getId() + " finalizado y archivado");
        ticketAtencion = null;
    }

    private static void consultarTicketsEnEspera() {
        System.out.println("\n--- Tickets en Cola de Espera ---");
        if (ticketsEspera.estaVacia()) {
            System.out.println(" (no hay tickets en espera)");
            return;
        }
        Cola<Ticket> aux = new Cola<>();
        Ticket t;
        while ((t = ticketsEspera.dequeue()) != null) {
            System.out.println("- " + t);
            aux.enqueue(t);
        }
        while ((t = aux.dequeue()) != null) {
            ticketsEspera.enqueue(t);
        }
    }

    private static void consultarHistorialFinalizados() {
        System.out.println("\n--- Historial de Tickets Finalizados ---");
        ticketsFinalizados.mostrar();
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
