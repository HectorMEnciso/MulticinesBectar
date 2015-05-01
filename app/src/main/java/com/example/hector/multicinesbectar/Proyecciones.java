package com.example.hector.multicinesbectar;

/**
 * Created by Hector on 01/05/2015.
 */
public class Proyecciones {

    private int IdProyeccion;
    private int IdCine;
    private int IdPelicula;
    private int IdButaca;
    private int IdSala;
    private String Hora;
    private String Dia;
    private String ButacasDisponibles;
    private String IdCompra;

    public Proyecciones() {
    }

    public int getIdProyeccion() {
        return IdProyeccion;
    }

    public void setIdProyeccion(int idProyeccion) {
        IdProyeccion = idProyeccion;
    }

    public int getIdCine() {
        return IdCine;
    }

    public void setIdCine(int idCine) {
        IdCine = idCine;
    }

    public int getIdPelicula() {
        return IdPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        IdPelicula = idPelicula;
    }

    public int getIdButaca() {
        return IdButaca;
    }

    public void setIdButaca(int idButaca) {
        IdButaca = idButaca;
    }

    public int getIdSala() {
        return IdSala;
    }

    public void setIdSala(int idSala) {
        IdSala = idSala;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public String getButacasDisponibles() {
        return ButacasDisponibles;
    }

    public void setButacasDisponibles(String butacasDisponibles) {
        ButacasDisponibles = butacasDisponibles;
    }

    public String getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(String idCompra) {
        IdCompra = idCompra;
    }
}
