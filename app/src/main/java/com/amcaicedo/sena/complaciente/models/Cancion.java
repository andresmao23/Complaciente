package com.amcaicedo.sena.complaciente.models;

/**
 * Created by asus on 10/06/2017.
 */

public class Cancion {

    private String id;
    private String nombre;
    private String autor;
    private String album;
    private int votos;

    public Cancion() {
    }

    public Cancion(String nombre, String autor, String album, int votos) {
        this.nombre = nombre;
        this.autor = autor;
        this.album = album;
        this.votos = votos;
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

}
