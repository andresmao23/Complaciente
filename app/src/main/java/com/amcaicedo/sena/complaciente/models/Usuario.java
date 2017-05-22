package com.amcaicedo.sena.complaciente.models;

import android.content.Context;

import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by asus on 22/05/2017.
 */

public class Usuario extends SugarRecord {

    private String nombre, usr, pass, urlusr, urlbanner;

    public Usuario() {
    }

    //region Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUrlusr() {
        return urlusr;
    }

    public void setUrlusr(String urlusr) {
        this.urlusr = urlusr;
    }

    public String getUrlbanner() {
        return urlbanner;
    }

    public void setUrlbanner(String urlbanner) {
        this.urlbanner = urlbanner;
    }
    //endregion

    //region SqlLite
    public static void init(Context context){
        SugarContext.init(context);
        if(count(Usuario.class)<1) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Andres Caicedo");
            usuario.setUsr("mao");
            usuario.setPass("123");
            usuario.setUrlusr("https://goo.gl/1IjNWP");
            usuario.setUrlusr("http://goo.gl/V7irxd");

            usuario.save();
        }
    }

    public static Usuario findUsuarioByUsrAndPass(String usr, String pass){
        Usuario usuario = null;

        List<Usuario> result = find(Usuario.class, "usr = ? AND pass = ?", usr, pass);

        if(result.size()>0)
            usuario = result.get(0);

        return usuario;
    }
    //endregion
}
