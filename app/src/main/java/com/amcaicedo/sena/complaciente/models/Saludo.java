package com.amcaicedo.sena.complaciente.models;

import android.net.Uri;

/**
 * Created by asus on 19/07/2017.
 */

public class Saludo {

    private String emisor;
    private String receptor;
    private String detalle;
    private String url;

    public Saludo() {
    }

    public Saludo(String emisor, String receptor, String detalle) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.detalle = detalle;
    }

    //region Getters and Setters
    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //endregion
}
