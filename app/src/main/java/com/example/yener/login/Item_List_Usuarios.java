package com.example.yener.login;

import android.graphics.Bitmap;

/**
 * Created by LIDA on 08/09/2016.
 */
public class Item_List_Usuarios {

    int id;
    Bitmap imagen;
    String img;
    String frm;
    Bitmap firma;
    String identificacion;
    String nombre;
    String correo;
    String genero;
    String contrasena;

    public Item_List_Usuarios() {
    }

    public Item_List_Usuarios(int id, Bitmap imagen, Bitmap firma,String img,String frm, String identificacion, String nombre, String correo, String genero, String contrasena) {
        this.imagen = imagen;
        this.firma = firma;
        this.img = img;
        this.frm = frm;
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correo = correo;
        this.genero = genero;
        this.contrasena = contrasena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFrm() {
        return frm;
    }

    public void setFrm(String frm) {
        this.frm = frm;
    }

    public Bitmap getFirma() {
        return firma;
    }

    public void setFirma(Bitmap firma) {
        this.firma = firma;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}



