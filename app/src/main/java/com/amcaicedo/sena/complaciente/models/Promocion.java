package com.amcaicedo.sena.complaciente.models;

import android.content.Context;

import com.orm.SugarContext;
import com.orm.SugarRecord;

/**
 * Created by asus on 24/05/2017.
 */

public class Promocion extends SugarRecord{

    private String bar;
    private String imagen;
    private String titulo;
    private String descripcion;

    public Promocion() {
    }

    //region Getters and Setters
    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    //endregion

    //region Sql
    public static void init(Context context){
        SugarContext.init(context);

        if(count(Promocion.class)<1) {
            Promocion promo = new Promocion();
            promo.setBar("CHILANGO");
            promo.setImagen("https://goo.gl/6olNhK");
            promo.setTitulo("Fechas especiales");
            promo.setDescripcion("En esa fecha especial te tenemos sorpresas para ti y tus amigos. Puedes enviar tus canciones favoritas al administrador, votar por otras canciones, enviar saludos a tus amigos en la fiesta, subir fotos y mucho más.");
            promo.save();

            promo = new Promocion();
            promo.setBar("WOKIN");
            promo.setImagen("https://goo.gl/n4qqnm");
            promo.setTitulo("Mariachis");
            promo.setDescripcion("Los jueves tenemos mariachis para disfrutar.");
            promo.save();

            promo = new Promocion();
            promo.setBar("LAND");
            promo.setImagen("http://goo.gl/V7irxd");
            promo.setTitulo("Descuentos");
            promo.setDescripcion("Con este cupón recibes los mejores descuentos.");
            promo.save();
        }
    }
    //endregion
}
