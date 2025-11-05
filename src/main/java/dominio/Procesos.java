package dominio;

public enum Procesos {
    HOMOLOGACION('H'),
    CONSULTA('C'),
    INFO('I'),
    MATRICULA('M');

    private final char inicial;

    Procesos(char inicial){
        this.inicial = inicial;
    }

    public char getInicial(){
        return inicial;
    }
}
