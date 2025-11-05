package dominio;

public class GenerarIdTicket {
    private static int contadorhomologacion = 0;
    private static int contadorconsulta = 0;
    private static int contadorinfo = 0;
    private static int contadormatricula = 0;

    public static String generarIdTicket(Procesos proceso) {
        switch(proceso) {
            case HOMOLOGACION:
                contadorhomologacion++;
                return proceso.getInicial() + String.format("%04d",contadorhomologacion);
            case CONSULTA:
                contadorconsulta++;
                return proceso.getInicial() + String.format("%04d",contadorconsulta);
            case INFO :
                contadorinfo++;
                return proceso.getInicial() + String.format("%04d",contadorinfo);
            case MATRICULA :
                contadormatricula++;
                return proceso.getInicial() + String.format("%04d",contadormatricula);
        }
        return "";
    }

}
