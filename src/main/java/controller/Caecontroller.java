package controller;

import dominio.*;
import estructuras.*;
import util.Persistencia;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Caecontroller {

    private static Scanner sc = new Scanner(System.in);

    public static final Cola<Ticket> ticketsEspera = new Cola<>();
    public static final Cola<Ticket> ticketsUrgente = new Cola<>();
    public static final Cola<Ticket> ticketsPendiente = new Cola<>();
    public static final ListaEnlazadaSimple<Ticket> ticketsFinalizados = new ListaEnlazadaSimple<>();

    public static Ticket ticketAtencion;
    private static Pila<Accion> undoStack = new Pila<>();
    private static Pila<Accion> redoStack = new Pila<>();

    /**
     * Método para agregar un nuevo ticket.
     */
    public static void agregarTicket() throws Exception {
        Scanner sc = new Scanner(System.in);
        Procesos proceso = null;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.println("Seleccione el proceso a realizar: ");
                System.out.println("1. Homologacion");
                System.out.println("2. Consulta");
                System.out.println("3. Info");
                System.out.println("4. Matricula");
                System.out.print("Ingrese el número del proceso: ");
                int opcion = sc.nextInt();

                switch (opcion) {
                    case 1 -> { proceso = Procesos.HOMOLOGACION; valido = true; }
                    case 2 -> { proceso = Procesos.CONSULTA; valido = true; }
                    case 3 -> { proceso = Procesos.INFO; valido = true; }
                    case 4 -> { proceso = Procesos.MATRICULA; valido = true; }
                    default -> System.out.println("Solo se aceptan opciones de 1 a 4.");
                }
            } catch (Exception e) {
                System.out.println("Opción no válida.");
                sc.nextLine();
            }
        }

        boolean ticketUrgOpcion = false;
        while (!ticketUrgOpcion) {
            try {
                System.out.println("¿Es un ticket urgente?");
                System.out.println("1. Sí");
                System.out.println("2. No");
                System.out.print("Opción: ");
                int opcionurg = sc.nextInt();
                switch (opcionurg) {
                    case 1 -> {
                        Ticket nuevo_urg = new Ticket(proceso);
                        nuevo_urg.setEstado(EstadoTicket.URGENTE);
                        ticketsUrgente.enqueue(nuevo_urg);
                        System.out.println("Ticket agregado a la cola: " + nuevo_urg);
                        Persistencia.guardarColas(ticketsUrgente, ticketsEspera, ticketsPendiente);
                        ticketUrgOpcion = true;
                    }
                    case 2 -> {
                        Ticket nuevo = new Ticket(proceso);
                        ticketsEspera.enqueue(nuevo);
                        System.out.println("Ticket agregado a la cola: " + nuevo);
                        Persistencia.guardarColas(ticketsUrgente, ticketsEspera, ticketsPendiente);
                        ticketUrgOpcion = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Solo se aceptan 1 o 2.");
                sc.nextLine();
            }
        }
    }

    /**
     * Guardar ticket pendiente + registrar acción de estado.
     */
    public static void guardarTicketPendiente() {
        if (ticketAtencion != null) {
            EstadoTicket anterior = ticketAtencion.getEstado();
            ticketAtencion.setEstado(EstadoTicket.PENDIENTE);

            Nota notaCambio = new Nota("Cambio de estado: " + anterior + " → PENDIENTE");
            ticketAtencion.getHistorialNotas().insertarAlInicio(notaCambio);

            Accion accion = new Accion(TipoAccion.CAMBIAR_ESTADO, anterior, notaCambio);
            undoStack.push(accion);
            redoStack = new Pila<>();

            ticketsPendiente.enqueue(ticketAtencion);
            ticketAtencion = null;
            System.out.println("Ticket movido a pendiente.");
            Persistencia.guardarColas(ticketsPendiente, ticketsEspera, ticketsUrgente);
        }
    }

    /**
     * Retomar ticket pendiente.
     */
    public static void atenderTicketPendiente() {
        if (ticketAtencion != null) {
            System.out.println("Ya hay un ticket en atención: " + ticketAtencion);
            return;
        }

        ticketAtencion = ticketsPendiente.dequeue();
        if (ticketAtencion == null) {
            System.out.println("No hay tickets pendientes.");
            return;
        }

        EstadoTicket anterior = ticketAtencion.getEstado();
        ticketAtencion.setEstado(EstadoTicket.EN_ATENCION);

        Nota notaCambio = new Nota("Cambio de estado: " + anterior + " → EN_ATENCION");
        ticketAtencion.getHistorialNotas().insertarAlInicio(notaCambio);

        Accion accion = new Accion(TipoAccion.CAMBIAR_ESTADO, anterior, notaCambio);
        undoStack.push(accion);
        redoStack = new Pila<>();

        System.out.println("Atendiendo ticket pendiente: " + ticketAtencion);
        Persistencia.guardarColas(ticketsUrgente, ticketsEspera, ticketsPendiente);
    }

    /**
     * Atender siguiente ticket (cola general o urgente).
     */
    public static void atenderSiguienteTicket() throws Exception {
        if (ticketAtencion != null) {
            System.out.println("Ya hay un ticket en atención: " + ticketAtencion);
            return;
        }

        if (!ticketsUrgente.estaVacia()) {
            ticketAtencion = ticketsUrgente.dequeue();
        } else {
            ticketAtencion = ticketsEspera.dequeue();
        }

        if (ticketAtencion == null) {
            System.out.println("No hay tickets en espera.");
            return;
        }

        sc.nextLine();
        System.out.print("Ingrese el nombre del usuario: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese el apellido del usuario: ");
        String apellido = sc.nextLine();

        boolean cedulaValida = false;
        int cedula = 0;
        while (!cedulaValida) {
            try {
                System.out.print("Ingrese la cédula del usuario: ");
                cedula = sc.nextInt();
                int digitos = String.valueOf(cedula).length();
                if (digitos == 10) {
                    cedulaValida = true;
                } else {
                    System.out.println("Debe tener 10 dígitos.");
                }
            } catch (Exception e) {
                System.out.println("Solo se aceptan números.");
                sc.nextLine();
            }
        }

        Persona persona = new Persona(nombre, apellido, cedula);
        ticketAtencion.setPersona(persona);

        EstadoTicket anterior = ticketAtencion.getEstado();
        ticketAtencion.setEstado(EstadoTicket.EN_ATENCION);

        Nota notaCambio = new Nota("Cambio de estado: " + anterior + " → EN_ATENCION");
        ticketAtencion.getHistorialNotas().insertarAlInicio(notaCambio);

        undoStack = new Pila<>();
        redoStack = new Pila<>();
        undoStack.push(new Accion(TipoAccion.CAMBIAR_ESTADO, anterior, notaCambio));

        System.out.println("Atendiendo ahora el " + ticketAtencion);
    }



    // Métodos auxiliares
    public static void imprimirCola() {
        ticketsUrgente.imprimir("Urgente");
        ticketsEspera.imprimir("General");
        ticketsPendiente.imprimir("Pendiente");
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

    public static void buscarTicketsPorCedula(int cedula) {
        System.out.println("\n--- Búsqueda por CÉDULA ---");

        if (ticketsFinalizados.estaVacia()) {
            System.out.println("No hay tickets finalizados");
            return;
        }

        Nodo<Ticket> actual = ticketsFinalizados.getCabeza();
        boolean encontrado = false;

        while (actual != null) {
            Ticket t = actual.dato;
            if (t.getPersona() != null && t.getPersona().getCedula() == cedula) {
                encontrado = true;
                System.out.println("\n" + t);
                System.out.println("   Historial de notas:");
                t.getHistorialNotas().mostrar();
            }
            actual = actual.siguiente;
        }

        if (!encontrado) {
            System.out.println("No se encontraron tickets con la cédula: " + cedula);
        }
    }


    public static void buscarTicketsPorEstado(EstadoTicket estado) {
        System.out.println("\n--- Búsqueda por ESTADO (" + estado + ") ---");

        if (ticketsFinalizados.estaVacia()) {
            System.out.println("No hay tickets finalizados");
            return;
        }

        Nodo<Ticket> actual = ticketsFinalizados.getCabeza();
        boolean encontrado = false;

        while (actual != null) {
            Ticket t = actual.dato;
            if (t.getEstado() == estado) {
                encontrado = true;
                System.out.println("\n" + t);
                System.out.println("   Historial de notas:");
                t.getHistorialNotas().mostrar();
            }
            actual = actual.siguiente;
        }

        if (!encontrado) {
            System.out.println("No se encontraron tickets con el estado: " + estado);
        }
    }


    public static void buscarTicketsPorProceso(Procesos procesos) {
        System.out.println("\n--- Búsqueda por Proceso (" + procesos + ") ---");

        if (ticketsFinalizados.estaVacia()) {
            System.out.println("No hay tickets finalizados");
            return;
        }

        Nodo<Ticket> actual = ticketsFinalizados.getCabeza();
        boolean encontrado = false;

        while (actual != null) {
            Ticket t = actual.dato;
            if (t.getProceso() == procesos) {
                encontrado = true;
                System.out.println("\n" + t);
                System.out.println("   Historial de notas:");
                t.getHistorialNotas().mostrar();
            }
            actual = actual.siguiente;
        }

        if (!encontrado) {
            System.out.println("No se encontraron tickets con el trámite: " + procesos);
        }
    }

    public static void mostrarGraficoPorEstado() {
        System.out.println("\n=== Gráfico: Tickets por Estado ===");

        Map<EstadoTicket, Integer> conteo = new HashMap<>();
        for (EstadoTicket e : EstadoTicket.values()) {
            conteo.put(e, 0);
        }

        // Contar tickets en colas y finalizados
        for (Ticket t : ticketsEspera.toList()) conteo.put(t.getEstado(), conteo.get(t.getEstado()) + 1);
        for (Ticket t : ticketsUrgente.toList()) conteo.put(t.getEstado(), conteo.get(t.getEstado()) + 1);
        for (Ticket t : ticketsPendiente.toList()) conteo.put(t.getEstado(), conteo.get(t.getEstado()) + 1);
        for (Ticket t : ticketsFinalizados.toList()) conteo.put(t.getEstado(), conteo.get(t.getEstado()) + 1);

        // Mostrar gráfico con barras ASCII
        for (EstadoTicket e : EstadoTicket.values()) {
            int cantidad = conteo.get(e);
            String barra = "#".repeat(cantidad);
            System.out.printf("%-12s | %s (%d)\n", e.name(), barra, cantidad);
        }
    }


    public static ListaEnlazadaSimple<Ticket> getTicketsFinalizados() {
        return ticketsFinalizados;
    }
}
