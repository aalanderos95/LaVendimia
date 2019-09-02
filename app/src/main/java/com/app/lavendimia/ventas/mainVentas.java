package com.app.lavendimia.ventas;
public class mainVentas {
    private int v_folio;
    private int v_cid;
    private String v_fecha;
    private int v_plazo;
    private double v_total;
    private String nombre;
    private String apaterno;
    private String amaterno;

    public mainVentas(int folio, int cid, String nombre, String apaterno, String amaterno,
                      String fecha, int plazo,double total) {
        this.v_folio = folio;
        this.v_cid = cid;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.v_fecha = fecha;
        this.v_plazo = plazo;
        this.v_total = total;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public String getApaterno() {
        return apaterno;
    }

    public double getV_total() {
        return v_total;
    }

    public String getNombre() {
        return nombre;
    }

    public int getV_cid() {
        return v_cid;
    }

    public int getV_folio() {
        return v_folio;
    }

    public int getV_plazo() {
        return v_plazo;
    }

    public String getV_fecha() {
        return v_fecha;
    }
}
