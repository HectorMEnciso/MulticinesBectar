package com.example.hector.multicinesbectar;

/**
 * Created by Hector on 28/04/2015.
 */
public class Cines {
    private int IdCine;
    private String ImgCine;
    private String Direccion;
    private String NombreCine;


    public void setIdCine(int idCine) {
        IdCine = idCine;
    }

    public int getIdCine() {
        return IdCine;
    }

    public String getImgCine() {
        return ImgCine;
    }

    public void setImgCine(String imgCine) {
        ImgCine = imgCine;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getNombreCine() {
        return NombreCine;
    }

    public void setNombreCine(String nombreCine) {
        NombreCine = nombreCine;
    }
}
