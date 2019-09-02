package com.app.lavendimia.Utilidades;

import com.app.lavendimia.articulo.apiArticulos;
import com.app.lavendimia.clientes.apiClientes;
import com.app.lavendimia.configuracion.apiConfiguracion;
import com.app.lavendimia.configuracion.apiConfiguraciones;
import com.app.lavendimia.ventas.apiVentas;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Servicios {
    public static final String BASE_URL ="https://animeymas.online/test/";

    @GET("obtenerConfiguracion.php")
    Call<apiConfiguraciones> obtenerConfiguracion();

    @GET("insertarConfiguracion.php")
    Call<apiConfiguraciones> serviceConfiguracion(@Query("tasa") double tasa, @Query("plazo") int plazo,
                                                @Query("enganche") int enganche, @Query("opcion") int opcion);


    @GET("obtenerClientes.php")
    Call<apiClientes> obtenerClientes();

    @GET("obtenerClientesConDato.php")
    Call<apiClientes> obtenerClientesconDato(@Query("dato") String dato);

    @GET("insertarClientes.php")
    Call<apiClientes> serviceClientes(@Query("nombre") String nombre, @Query("apaterno") String apaterno,
                                      @Query("amaterno") String amaterno,@Query("rfc") String rfc,
                                      @Query("id") String id, @Query("opcion") String opcion);


    @GET("obtenerArticulos.php")
    Call<apiArticulos> obtenerArticulos();

    @GET("obtenerArticulosConDato.php")
    Call<apiArticulos> obtenerArticulosconDato(@Query("dato") String dato);

    @GET("actualizarExistencia.php")
    Call<apiArticulos> actualizaExistencia(@Query("id") int id, @Query("cantidad") int cantidad,
                                           @Query("opcion") int opcion);

    @GET("insertarArticulos.php")
    Call<apiArticulos> serviceArticulos(@Query("descripcion") String descripcion, @Query("modelo") String modelo,
                                        @Query("precio") double precio, @Query("existencia") int existencia,
                                        @Query("id") String id, @Query("opcion") String opcion);


    @GET("obtenerVentas.php")
    Call<apiVentas> obtenerVentas();

    @GET("insertarVentas.php")
    Call<apiVentas> insertarVentas(@Query("userid") int userid, @Query("plazo") int plazo, @Query("enganche") double enganche,
                                   @Query("bonificacion") double bonificacion, @Query("total") double total);



}
