package com.app.lavendimia.ventas;

public class mainDetalleVenta {
    private int dv_id;
    private int dv_vFolio;
    private int dv_arid;
    private int dv_cantidad;

    public mainDetalleVenta (int id, int vfolio, int varid, int cantidad) {
        this.dv_id = id;
        this.dv_vFolio = vfolio;
        this.dv_arid = varid;
        this.dv_cantidad = cantidad;
    }

    public int getDv_id() {
        return dv_id;
    }

    public int getDv_arid() {
        return dv_arid;
    }

    public int getDv_cantidad() {
        return dv_cantidad;
    }

    public int getDv_vFolio() {
        return dv_vFolio;
    }
}
