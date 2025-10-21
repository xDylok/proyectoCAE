

import controller.Caecontroller;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Caecontroller controlador = new Caecontroller();



    public static void main(String[] args) {

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    controlador.agregarTicket();
                    break;
                case 2:
                    controlador.atenderSiguienteTicket();
                    break;
                case 3:
                    if (controlador.ticketAtencion == null){
                        controlador.atenderSiguienteTicket();
                        if (controlador.ticketsEspera.estaVacia()){
                            System.out.println("==========================================");
                            System.out.println("No hay tickets en espera de ser antendidos");
                            System.out.println("==========================================");
                            opcion = 4;
                            break;
                        }
                    }

                    int opcion1;
                    do {
                        mostrarMenuGestion();
                        opcion1 = leerOpcion();
                        switch (opcion1) {
                            case 1:
                                controlador.agregarNota();
                                break;
                            case 2:
                                controlador.deshacerAccion();
                                break;
                            case 3:
                                controlador.rehacerAccion();
                                break;
                            case 4:
                                controlador.finalizarAtencion();
                                if (controlador.ticketAtencion == null) {
                                    opcion1 = 5;
                                    opcion = 4;
                                    System.out.println("Regresando al menu principal.......");
                                }
                                break;

                            default:
                                System.out.println("Opcion no valida");
                        }
                    } while (opcion1 != 5);
                    break;
                case 4:
                    controlador.imprimirCola();

                    break;
                case 5:
                     controlador.consultarHistorialFinalizados();

                case 6:
                    System.out.println("Gracias por usar nuestro sistema de gestion de Tramites ......");

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
        if (controlador.ticketAtencion != null) {
            System.out.println("Atendiendo ahora: " + controlador.ticketAtencion);
        } else {
            System.out.println("️Ningun ticket en atencion.");
        }
        System.out.print("Seleccione una opcion: ");
    }

    private static void mostrarMenuGestion() {
        System.out.println("\n--- Gestionando Ticket #" + controlador.ticketAtencion.getId() + " ---");
        System.out.println("1. Agregar nota de observación");
        System.out.println("2. Deshacer última acción (Undo)");
        System.out.println("3. Rehacer última acción (Redo)");
        System.out.println("4. Finalizar atención de este ticket");
        System.out.println("\n--- Historial de Notas ---");
        controlador.ticketAtencion.getHistorialNotas().mostrar();
        System.out.println("--------------------------");
        System.out.print("Seleccione una opción: ");
    }



    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
