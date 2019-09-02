package com.app.lavendimia.ventas;

public class mainarticuloAgregado {
    private int id;
    private String descripcion;
    private String modelo;
    private int cantidad;
    private double precio;
    private double importe;
    private int existencia;

    public mainarticuloAgregado(int id, String des, String modelo, int cant, double precio, double importe, int existencia) {
        this.id = id;
        this.descripcion = des;
        this.modelo = modelo;
        this.cantidad = cant;
        this.precio = precio;
        this.importe = importe;
        this.existencia = existencia;
    }

    public int getExistencia() {
        return existencia;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getImporte() {
        return importe;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getModelo() {
        return modelo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
