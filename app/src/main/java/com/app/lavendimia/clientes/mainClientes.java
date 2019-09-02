package com.app.lavendimia.clientes;

public class mainClientes {
    private int c_id;
    private String c_nombre;
    private String c_apaterno;
    private String c_amaterno;
    private String c_rfc;

    public mainClientes (int id, String nombre, String apaterno, String amaterno, String rfc) {
        this.c_id = id;
        this.c_nombre = nombre;
        this.c_apaterno = apaterno;
        this.c_amaterno = amaterno;
        this.c_rfc = rfc;
    }

    public int getC_id() {
        return c_id;
    }

    public String getC_apaterno() {
        return c_apaterno;
    }

    public String getC_amaterno() {
        return c_amaterno;
    }

    public String getC_nombre() {
        return c_nombre;
    }

    public String getC_rfc() {
        return c_rfc;
    }
}
