package com.amcaicedo.sena.complaciente.retrofitmodels;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by asus on 29/10/2017.
 */

public interface Api {

    String BASE_URL = "http://192.168.0.13:8080/slim/api.php/";

    @GET("autores")
    Call<List<Autor>> getAutores();

    @GET("canciones")
    Call<List<Cancion>> getCanciones();

}
