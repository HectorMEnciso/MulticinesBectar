package com.example.hector.multicinesbectar;

/**
 * Created by Hector on 02/05/2015.
 */
public class Usuarios {
    public int IdUsuario;
    public String DNI;
    public String ImgUsuario;
    public String Nombre;
    public String Apellidos;
    public String Email;
    public String UserName;
    public String Pass;
    public String T_Credito;
    public String Language;

    public Usuarios() {
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getImgUsuario() {
        return ImgUsuario;
    }

    public void setImgUsuario(String imgUsuario) {
        ImgUsuario = imgUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getT_Credito() {
        return T_Credito;
    }

    public void setT_Credito(String t_Credito) {
        T_Credito = t_Credito;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }
}
