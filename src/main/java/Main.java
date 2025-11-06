

import controller.Caecontroller;
import controller.Controllersubmenu;
import util.Persistencia;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Caecontroller controlador = new Caecontroller();
    private static final Controllersubmenu controlador1 = new Controllersubmenu();


    public static void main(String[] args) throws Exception {

        Persistencia.cargarColas(controlador.ticketsUrgente, controlador.ticketsEspera, controlador.ticketsPendiente);
        Persistencia.cargarFinalizados(controlador.getTicketsFinalizados());


        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    controlador.agregarTicket() ;
                    break;
                case 2:
                    controlador.atenderSiguienteTicket();

                    break;
                case 3:
                    controlador.atenderTicketPendiente();
                    break;
                case 4:
                    if (controlador.ticketAtencion == null){
                        controlador.atenderSiguienteTicket();
                        if (controlador.ticketsEspera.estaVacia()){
                            System.out.println("\u001B[35m==========================================\u001B[35m");
                            System.out.println("No hay tickets en espera de ser antendidos          ");
                            System.out.println("\u001B[35m==========================================\u001B[0m");
                            opcion = 5;
                            break;
                        }
                    }

                    int opcion1;
                    do {
                        mostrarMenuGestion();
                        opcion1 = leerOpcion();
                        switch (opcion1) {
                            case 1:
                                controlador1.agregarNota();
                                break;
                            case 2:
                                controlador1.deshacerAccion();
                                break;
                            case 3:
                                controlador1.rehacerAccion();
                                break;
                            case 4:
                                controlador.guardarTicketPendiente();
                                if (controlador.ticketAtencion == null) {
                                    opcion1 = 7;
                                    opcion = 4;
                                    System.out.println("Regresando al menu principal.......");
                                }
                                break;
                            case 5:
                                controlador1.finalizarAtencion();
                                if (controlador.ticketAtencion == null) {
                                    opcion1 = 7;
                                    opcion = 4;
                                    System.out.println("Regresando al menu principal.......");
                                }
                                break;
                            case 6:
                                controlador.agregarTicket() ;
                                break;

                            default:
                                System.out.println("Opcion no valida");
                        }
                    } while (opcion1 != 7);
                    break;
                case 5:
                    controlador.imprimirCola();

                    break;
                case 6:
                     controlador.consultarHistorialFinalizados();
                    break;
                case 7:
                    menuFiltro();
                    break;
                case 8:
                    controlador.mostrarGraficoPorEstado();
                case 9:
                    System.out.println("Gracias por usar nuestro sistema de gestion de Tramites ......");

                    break;

                default:
                    System.out.println("Opcion no valida");

            }
            Persistencia.guardarColas(controlador.ticketsUrgente, controlador.ticketsEspera, controlador.ticketsPendiente);
            Persistencia.guardarFinalizados(controlador.getTicketsFinalizados());

        } while (opcion != 9);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n \u001B[34m-------------------------------------------------\u001B[0m");
        System.out.println("\u001B[32m--- Centro de Atencion al Estudiante (CAE) ---\u001B[0m");
        System.out.println("1. Recibir nuevo ticket de estudiante");
        System.out.println("2. Atender siguiente ticket en la cola");
        System.out.println("3. Atender ticket en pendiente");
        System.out.println("4. Gestionar ticket en atencion actual");
        System.out.println("5. Tickets en espera");
        System.out.println("6. Consultar historial de un ticket finalizado");
        System.out.println("7. Filtrar tickets finalizados");
        System.out.println("8. Imprimir grafico por Estados");
        System.out.println("\u001B[31m9. Salir \u001B[0m");
        System.out.println("\u001B[35m-------------------------------------------------\u001B[0m");
        if (controlador.ticketAtencion != null) {
            System.out.println("\u001B[33mAtendiendo ahora: \u001B[0m" + controlador.ticketAtencion);
        } else {
            System.out.println("️Ningun ticket en atencion.");
        }
        System.out.print("Seleccione una opcion: ");
    }

    private static void mostrarMenuGestion() {

        System.out.println("\n==================================================");
        Caecontroller.imprimirCola();
        System.out.println("==================================================");

        System.out.println("\u001B[35m-------------------------------------------------\u001B[0m");
        System.out.println("\n--- Gestionando Ticket #" + controlador.ticketAtencion.nroTicket + " ---");
        System.out.println("1. Agregar nota de observación");
        System.out.println("2. Deshacer última acción (Undo)");
        System.out.println("3. Rehacer última acción (Redo)");
        System.out.println("4. Guardar ticket en pendiente");
        System.out.println("5. Finalizar atención de este ticket");
        System.out.println("\u001B[36m=====================================\u001B[36m");
        System.out.println("6. Recibir nuevos tickets en espera");
        System.out.println("\u001B[36m=====================================\u001B[36m");
        System.out.println("\u001B[35m-------------------------------------------------\u001B[0m");
        System.out.println("\n\u001B[32m--- Historial de Notas ---\u001B[0m");
        controlador.ticketAtencion.getHistorialNotas().mostrar();
        System.out.println("--------------------------------------");

        System.out.print("\nSeleccione una opción: ");

    }

    public static void menuFiltro (){
        System.out.println("------Buscar Tickets Finalizados-----");
        System.out.println("1. Por cédula");
        System.out.println("2. Por estado");
        System.out.println("3. Por trámite");
        System.out.print("Opción: ");

        int opc = -1;
        try {
            opc = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número válido.");
        }

        switch (opc) {
            case 1 -> {controlador1.buscarPorCedula();}

            case 2 -> {controlador1.buscarPorEstado();}

            case 3 -> {controlador1.buscarPorProceso();}

            default -> System.out.println("Opción no válida.");
        }
    }


    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
