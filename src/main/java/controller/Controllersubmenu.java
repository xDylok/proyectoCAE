package controller;

import dominio.*;
import estructuras.ListaEnlazadaSimple;
import estructuras.Pila;
import util.Persistencia;
import static controller.Caecontroller.ticketAtencion;
import static controller.Caecontroller.ticketsFinalizados;


import java.util.Scanner;


public class Controllersubmenu {

    private static Scanner sc = new Scanner(System.in);

    private static Pila<Accion> undoStack = new Pila<>();
    private static Pila<Accion> redoStack = new Pila<>();


    public static void buscarPorCedula (){
        int ced = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print("Ingrese la cédula: ");
            String cedula_text = sc.nextLine().trim();
            try {
                ced = Integer.parseInt(cedula_text);
                if (String.valueOf(ced).length() == 10) {
                    valido = true;
                } else {
                    System.out.println("La cédula debe tener 10 dígitos.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar solo números.");
            }
        }
        Caecontroller.buscarTicketsPorCedula(ced);
    }
    public static void buscarPorEstado (){
        System.out.println("\n--- Busqueda por Estado---");
        System.out.println("Seleccione el estado a buscar");
        EstadoTicket[] estados = EstadoTicket.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i].name());
        }

        int seleccion = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print("Opción: ");
            try {
                seleccion = Integer.parseInt(sc.nextLine());
                if (seleccion >= 1 && seleccion <= estados.length) {
                    EstadoTicket estadoSeleccionado = estados[seleccion - 1];
                    Caecontroller.buscarTicketsPorEstado(estadoSeleccionado);
                    valido = true;
                } else {
                    System.out.println("Opción fuera de rango. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }
    public static void buscarPorProceso (){
        System.out.println("\n--- Busqueda por Proceso ---");
        Procesos[] tramites = Procesos.values();
        for (int i = 0; i < tramites.length; i++) {
            System.out.println((i + 1) + ". " + tramites[i].name());
        }

        int seleccion = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print("Opción: ");
            try {
                seleccion = Integer.parseInt(sc.nextLine());
                if (seleccion >= 1 && seleccion <= tramites.length) {
                    Procesos procesoSeleccionado = tramites[seleccion - 1];
                    Caecontroller.buscarTicketsPorProceso(procesoSeleccionado);
                    valido = true;
                } else {
                    System.out.println("Opción fuera de rango. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    /**
     * Agregar nota + registrar acción.
     */
    public static void agregarNota() {
        System.out.print("Escriba la nota: ");
        String textoNota = sc.nextLine();
        Nota nuevaNota = new Nota(textoNota);
        ticketAtencion.getHistorialNotas().insertarAlInicio(nuevaNota);

        Accion accion = new Accion(TipoAccion.AGREGAR_NOTA, null, nuevaNota);
        undoStack.push(accion);
        redoStack = new Pila<>();

        System.out.println("Nota agregada correctamente.");
    }

    /**
     * Deshacer acción.
     */
    public static void deshacerAccion() {
        Accion accion = undoStack.pop();
        if (accion == null) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }

        switch (accion.getTipo()) {
            case AGREGAR_NOTA -> {
                ticketAtencion.getHistorialNotas().eliminaPrimeraCoincidencia((Nota) accion.getValorNuevo());
                System.out.println("Deshecho: nota eliminada.");
            }
            case CAMBIAR_ESTADO -> {
                EstadoTicket anterior = (EstadoTicket) accion.getValorAnterior();
                Nota notaCambio = (Nota) accion.getValorNuevo();

                // Eliminar la nota del cambio de estado
                ticketAtencion.getHistorialNotas().eliminaPrimeraCoincidencia(notaCambio);

                // Restaurar el estado anterior
                EstadoTicket estadoActual = ticketAtencion.getEstado();
                ticketAtencion.setEstado(anterior);

                System.out.println("Deshecho: estado restaurado de " + estadoActual + " → " + anterior);
            }
        }

        redoStack.push(accion);
    }

    /**
     * Rehacer acción.
     */
    public static void rehacerAccion() {
        Accion accion = redoStack.pop();
        if (accion == null) {
            System.out.println("No hay acciones para rehacer.");
            return;
        }

        switch (accion.getTipo()) {
            case AGREGAR_NOTA -> {
                ticketAtencion.getHistorialNotas().insertarAlInicio((Nota) accion.getValorNuevo());
                System.out.println("Rehecho: nota re-agregada.");
            }
            case CAMBIAR_ESTADO -> {
                Nota notaCambio = (Nota) accion.getValorNuevo();
                EstadoTicket nuevoEstado = null;

                // Intentar deducir el nuevo estado desde el texto de la nota
                String texto = notaCambio.getTexto();
                for (EstadoTicket est : EstadoTicket.values()) {
                    if (texto.contains("→ " + est.name())) {
                        nuevoEstado = est;
                        break;
                    }
                }

                if (nuevoEstado == null) {
                    System.out.println("No se pudo determinar el estado a rehacer.");
                    return;
                }

                EstadoTicket estadoAnterior = ticketAtencion.getEstado();
                ticketAtencion.setEstado(nuevoEstado);
                ticketAtencion.getHistorialNotas().insertarAlInicio(notaCambio);

                System.out.println("Rehecho: estado restaurado de " + estadoAnterior + " → " + nuevoEstado);
            }
        }

        undoStack.push(accion);
    }

    /**
     * Finalizar ticket y registrar acción.
     */
    public static void finalizarAtencion() {
        if (ticketAtencion == null) {
            System.out.println("No hay ticket en atención.");
            return;
        }

        EstadoTicket anterior = ticketAtencion.getEstado();
        if (ticketAtencion.getEstado() != EstadoTicket.COMPLETADO && ticketAtencion.getEstado() != EstadoTicket.CANCELADO) {
            System.out.print("¿Desea finalizarlo como COMPLETADO? (s/n): ");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Finalización cancelada.");
                return;
            }
            ticketAtencion.setEstado(EstadoTicket.COMPLETADO);
        }

        Nota notaCambio = new Nota("Cambio de estado: " + anterior + " → COMPLETADO");
        ticketAtencion.getHistorialNotas().insertarAlInicio(notaCambio);
        undoStack.push(new Accion(TipoAccion.CAMBIAR_ESTADO, anterior, notaCambio));
        redoStack = new Pila<>();

        ticketsFinalizados.insertarAlInicio(ticketAtencion);
        System.out.println("Ticket #" + ticketAtencion.getId() + " finalizado y archivado.");

        Persistencia.guardarFinalizados(ticketsFinalizados);

        ticketAtencion = null;
    }

    public static ListaEnlazadaSimple<Ticket> getTicketsFinalizados() {
        return ticketsFinalizados;
    }


}
