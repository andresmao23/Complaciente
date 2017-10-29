package com.amcaicedo.sena.complaciente.retrofitmodels;

/**
 * Created by asus on 29/10/2017.
 */

public class Cancion {
    private String id;
    private String nombre;

    public Cancion() {
    }

    public Cancion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
