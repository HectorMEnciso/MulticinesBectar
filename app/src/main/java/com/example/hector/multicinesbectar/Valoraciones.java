package com.example.hector.multicinesbectar;

/**
 * Created by Hector on 16/06/2015.
 */
public class Valoraciones {

    public int IdValoracion;
    public int IdUsuario;
    public int IdPelicula;
    public String TextoValoracion;
    public String ValorRatingBar;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String UserName;

    public int getIdValoracion() {
        return IdValoracion;
    }

    public void setIdValoracion(int idValoracion) {
        IdValoracion = idValoracion;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public int getIdPelicula() {
        return IdPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        IdPelicula = idPelicula;
    }

    public String getTextoValoracion() {
        return TextoValoracion;
    }

    public void setTextoValoracion(String textoValoracion) {
        TextoValoracion = textoValoracion;
    }

    public String getValorRatingBar() {
        return ValorRatingBar;
    }

    public void setValorRatingBar(String valorRatingBar) {
        ValorRatingBar = valorRatingBar;
    }

    public Valoraciones() {

    }
}
