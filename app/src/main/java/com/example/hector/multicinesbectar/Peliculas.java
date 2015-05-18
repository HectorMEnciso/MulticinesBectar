package com.example.hector.multicinesbectar;

/**
 * Created by Hector on 01/05/2015.
 */
public class Peliculas {

    private int IdPelicula;
    private String ImgPelicula;
    private String Titulo;
    private String Director;
    private String Interpretes;
    private String Genero;
    private String Duracion;
    private String Anyo;
    private String Trailer;

    public String getSinopsis() {
        return Sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        Sinopsis = sinopsis;
    }

    private String Sinopsis;

    public Peliculas() {
    }

    public int getIdPelicula() {
        return IdPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        IdPelicula = idPelicula;
    }

    public String getImgPelicula() {
        return ImgPelicula;
    }

    public void setImgPelicula(String imgPelicula) {
        ImgPelicula = imgPelicula;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getInterpretes() {
        return Interpretes;
    }

    public void setInterpretes(String interpretes) {
        Interpretes = interpretes;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getDuracion() {
        return Duracion;
    }

    public void setDuracion(String duracion) {
        Duracion = duracion;
    }

    public String getAnyo() {
        return Anyo;
    }

    public void setAnyo(String anyo) {
        Anyo = anyo;
    }

    public String getTrailer() {
        return Trailer;
    }

    public void setTrailer(String trailer) {
        Trailer = trailer;
    }
}
