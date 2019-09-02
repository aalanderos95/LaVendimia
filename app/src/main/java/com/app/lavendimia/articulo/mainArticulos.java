package com.app.lavendimia.articulo;

public class mainArticulos {
    private int ar_id;
    private String ar_descripcion;
    private String ar_Modelo;
    private double ar_precio;
    private int ar_existencia;

    public mainArticulos (int id, String descripcion, String modelo, double precio, int existencia) {
        this.ar_id = id;
        this.ar_descripcion = descripcion;
        this.ar_Modelo = modelo;
        this.ar_precio = precio;
        this.ar_existencia = existencia;
    }

    public int getAr_id() {
        return ar_id;
    }

    public double getAr_precio() {
        return ar_precio;
    }

    public String getAr_descripcion() {
        return ar_descripcion;
    }

    public String getAr_Modelo() {
        return ar_Modelo;
    }

    public int getAr_existencia() {
        return ar_existencia;
    }
}
