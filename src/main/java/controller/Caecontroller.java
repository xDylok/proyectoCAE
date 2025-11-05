package controller;

import dominio.*;
import estructuras.*;
import java.util.Scanner;

public class Caecontroller {

    private static Scanner sc = new Scanner(System.in);

    public static final Cola<Ticket> ticketsEspera = new Cola<>();
    private static final ListaEnlazadaSimple<Ticket>  ticketsFinalizados = new ListaEnlazadaSimple<>();

    public static Ticket ticketAtencion;
    public static Ticket ticketEspera;
    private static Pila<Accion> undoStack = new Pila<>();
    private static Pila<Accion> redoStack = new Pila<>();


    /**
     * Metodo para Agregar el ticket creado a la cola
     */
    public static void agregarTicket() throws Exception{

        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione el proceso a realizar: ");
        System.out.println("1. Homologacion");
        System.out.println("2. Consulta");
        System.out.println("3. Info");
        System.out.println("4. Matricula");
        System.out.println("Ingrese el numero del proceso a realizar: ");
        int opcion = sc.nextInt();


        Procesos proceso;
        switch (opcion) {
            case 1:
                proceso = Procesos.HOMOLOGACION;
                break;

            case 2:
                proceso = Procesos.CONSULTA;
                break;
            case 3:
                proceso = Procesos.INFO;
                break;
            case 4:
                proceso = Procesos.MATRICULA;
                break;
            default:
                throw new Exception(" Numero no valido: ");

        }
        sc.nextLine();

        Ticket nuevo = new Ticket(proceso);
        ticketsEspera.enqueue(nuevo);
        System.out.println("Ticket agregado a la cola: " + nuevo);
    }

    /***
     * Metodo para realizar la atencion de un ticket en espera
     */
    public static void atenderSiguienteTicket() throws Exception {
        Scanner sc = new Scanner(System.in);
        if (ticketAtencion != null) {
            System.out.println("Ya hay un ticket en atención: " + ticketAtencion);
            return;
        }

        ticketAtencion = ticketsEspera.dequeue();

        System.out.println("Ingrese e nombre del usuario");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el apellido del usuario");
        String apellido = sc.nextLine();

        //Para verificar si mi cedula son numeros caso contrario error
        boolean cedulavalida = false;
        int cedula = 0;
        while (!cedulavalida) {
            try {
                System.out.println("Ingrese el cedula del usuario");
                cedula = sc.nextInt();
                cedulavalida = true;
            } catch (Exception e) {
                System.out.println("Solo se aceptan numeros");
                sc.nextLine();
            }
        }


        Persona persona = new Persona(nombre, apellido, cedula);
        ticketAtencion.setPersona(persona);

        if (ticketAtencion == null) {
            System.out.println("No hay tickets en espera.");
            return;
        }

        ticketAtencion.setEstado(EstadoTicket.EN_ATENCION);
        undoStack = new Pila<>();
        redoStack = new Pila<>();

        System.out.println("Atendiendo ahora el " + ticketAtencion);
    }


    /***
     * Metodo para agregar notas al ticket en atencion
     */
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


    /***
     * Metodo para deshacer acciones como notas del ticket que esta siendo atendido
     */
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

    /***
     * Metodo para recuperar la accion borrada en el ticket de atencion
     */
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

    /***
     * Metodo utilizado para realizar la finalizacion de la atencion del ticket y limpiar el ticketAtencion
     */
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

    /***
     * Metodo que llama al metodo de imprimir de la cola
     */
    public static void imprimirCola(){
        ticketsEspera.imprimir();
    }

    /***
     * Metodo que realiza la busqueda de la lista de los tickets finalizadoos
     */
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
            System.out.println("\n" + i++ + ") " + t );
            System.out.println("   📋 Historial de notas:");
            t.getHistorialNotas().mostrar();
            actual = actual.siguiente;
        }

        System.out.println("------------------------------------------");
    }




}