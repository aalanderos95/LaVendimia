package com.app.lavendimia.configuracion;

public class mainConfiguracion {
    private int id;
    private double tasaFinanciamiento;
    private int plazoMaximo;
    private int porcentajeEnganche;

    public mainConfiguracion (int id, double tasaFinanciamiento, int plazoMaximo, int porcentajeEnganche) {
        this.id = id;
        this.tasaFinanciamiento = tasaFinanciamiento;
        this.plazoMaximo = plazoMaximo;
        this.porcentajeEnganche = porcentajeEnganche;
    }

    public int getId() {
        return id;
    }

    public double getTasaFinanciamiento() {
        return tasaFinanciamiento;
    }

    public int getPlazoMaximo() {
        return plazoMaximo;
    }

    public int getPorcentajeEnganche() {
        return porcentajeEnganche;
    }
}
