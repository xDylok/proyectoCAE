package util;

import dominio.*;
import estructuras.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class Persistencia {

    private static final String PATH_COLAS = "colas.txt";
    private static final String PATH_FINALIZADOS = "finalizados.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // =========================================================
    // GUARDAR DATOS
    // =========================================================
    public static void guardarColas(Cola<Ticket> urgentes, Cola<Ticket> normales, Cola<Ticket> pendientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_COLAS))) {
            bw.write("=== COLA URGENTES ===\n");
            guardarCola(bw, urgentes);
            bw.write("\n=== COLA NORMALES ===\n");
            guardarCola(bw, normales);
            bw.write("\n=== COLA PENDIENTES ===\n");
            guardarCola(bw, pendientes);
            System.out.println("Colas guardadas correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar colas: " + e.getMessage());
        }
    }

    private static void guardarCola(BufferedWriter bw, Cola<Ticket> cola) throws IOException {
        Nodo<Ticket> actual = cola.getFrente();
        while (actual != null) {
            guardarTicket(bw, actual.dato);
            actual = actual.siguiente;
        }
    }

    public static void guardarFinalizados(ListaEnlazadaSimple<Ticket> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_FINALIZADOS))) {
            Nodo<Ticket> actual = lista.getCabeza();
            while (actual != null) {
                guardarTicket(bw, actual.dato);
                actual = actual.siguiente;
            }
            System.out.println("Tickets finalizados guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar tickets finalizados: " + e.getMessage());
        }
    }

    private static void guardarTicket(BufferedWriter bw, Ticket t) throws IOException {
        bw.write("Ticket #" + t.getId() + "\n");
        bw.write("Proceso=" + t.getProceso().name() + "\n");
        bw.write("Estado=" + t.getEstado().name() + "\n");
        bw.write("NroTicket=" + t.nroTicket + "\n");

        if (t.getPersona() != null) {
            Persona p = t.getPersona();
            bw.write("Persona=" + p.getNombre() + "," + p.getApellido() + "," + p.getCedula() + "\n");
        }

        // Guardar notas
        bw.write("Notas:\n");
        Nodo<Nota> actual = t.getHistorialNotas().getCabeza();
        while (actual != null) {
            Nota n = actual.dato;
            bw.write("  - " + n.getTexto().replace("\n", "\\n") + "\n");
            actual = actual.siguiente;
        }
        bw.write("ENDTICKET\n\n");
    }

    // =========================================================
    // CARGAR DATOS
    // =========================================================
    public static void cargarColas(Cola<Ticket> urgentes, Cola<Ticket> normales, Cola<Ticket> pendientes) {
        File file = new File(PATH_COLAS);
        if (!file.exists()) {
            System.out.println("ℹ️ No hay registros previos de colas.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Cola<Ticket> actualCola = null;
            String linea;
            Ticket ticketActual = null;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("=== COLA URGENTES")) actualCola = urgentes;
                else if (linea.startsWith("=== COLA NORMALES")) actualCola = normales;
                else if (linea.startsWith("=== COLA PENDIENTES")) actualCola = pendientes;
                else if (linea.startsWith("Ticket #")) {
                    ticketActual = new Ticket(Procesos.INFO); // valor temporal
                    ticketActual.setHistorialNotas(new ListaEnlazadaSimple<>());
                } else if (linea.startsWith("Proceso=")) {
                    ticketActual = ticketActual == null ? new Ticket(Procesos.valueOf(linea.split("=")[1])) : ticketActual;
                    ticketActual.setEstado(EstadoTicket.EN_COLA);
                } else if (linea.startsWith("Estado=")) {
                    ticketActual.setEstado(EstadoTicket.valueOf(linea.split("=")[1]));
                } else if (linea.startsWith("NroTicket=")) {
                    ticketActual.nroTicket = linea.split("=")[1];
                } else if (linea.startsWith("Persona=")) {
                    String[] datos = linea.split("=")[1].split(",");
                    if (datos.length == 3) {
                        ticketActual.setPersona(new Persona(datos[0], datos[1], Integer.parseInt(datos[2])));
                    }
                } else if (linea.startsWith("- ")) {
                    String texto = linea.replace("- ", "").replace("\\n", "\n");
                    ticketActual.getHistorialNotas().insertarAlInicio(new Nota(texto));
                } else if (linea.equals("ENDTICKET")) {
                    if (actualCola != null && ticketActual != null)
                        actualCola.enqueue(ticketActual);
                    ticketActual = null;
                }
            }

            System.out.println("Colas cargadas correctamente desde archivo.");
        } catch (Exception e) {
            System.out.println("Error al cargar colas: " + e.getMessage());
        }
    }

    public static void cargarFinalizados(ListaEnlazadaSimple<Ticket> lista) {
        File file = new File(PATH_FINALIZADOS);
        if (!file.exists()) {
            System.out.println("No hay registros previos de tickets finalizados.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            Ticket ticketActual = null;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("Ticket #")) {
                    ticketActual = new Ticket(Procesos.INFO);
                    ticketActual.setHistorialNotas(new ListaEnlazadaSimple<>());
                } else if (linea.startsWith("Proceso=")) {
                    ticketActual = new Ticket(Procesos.valueOf(linea.split("=")[1]));
                } else if (linea.startsWith("Estado=")) {
                    ticketActual.setEstado(EstadoTicket.valueOf(linea.split("=")[1]));
                } else if (linea.startsWith("NroTicket=")) {
                    ticketActual.nroTicket = linea.split("=")[1];
                } else if (linea.startsWith("Persona=")) {
                    String[] datos = linea.split("=")[1].split(",");
                    if (datos.length == 3) {
                        ticketActual.setPersona(new Persona(datos[0], datos[1], Integer.parseInt(datos[2])));
                    }
                } else if (linea.startsWith("- ")) {
                    String texto = linea.replace("- ", "").replace("\\n", "\n");
                    ticketActual.getHistorialNotas().insertarAlInicio(new Nota(texto));
                } else if (linea.equals("ENDTICKET")) {
                    if (ticketActual != null)
                        lista.insertarAlInicio(ticketActual);
                    ticketActual = null;
                }
            }

            System.out.println("Tickets finalizados cargados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar tickets finalizados: " + e.getMessage());
        }
    }
}
