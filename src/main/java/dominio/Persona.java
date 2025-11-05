package dominio;

public class Persona {
    private String nombre;
    private String apellido;
    private int cedula;

    public Persona(String nombre, String apellido, int cedula) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getCedula() {
        return cedula;
    }
    @Override
    public String toString() {
        return
                " Nombre: "+ nombre +
                " | Apellido: "+ apellido +
                " | Cedula: " + cedula;
    }

}
