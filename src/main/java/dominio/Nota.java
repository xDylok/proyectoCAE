package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Nota {
    private String texto;
    private LocalDateTime fecha;

    public Nota(String texto) {
        this.texto = texto;
        this.fecha = LocalDateTime.now();
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String toString() {
        return "[" + fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "] " + texto;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nota nota = (Nota) obj;
        return texto.equals(nota.texto);

    }
}
