package com.app.lavendimia.Utilidades;

import android.util.Log;

import com.app.lavendimia.ventas.mainarticuloAgregado;

import java.util.ArrayList;
import java.util.List;

public class Utilidades {
    public static boolean tieneConfig = false;
    public static int plazo = 0;
    public static double tasa = 0.0;
    public static int enganche = 0;
    public static boolean puedeRegresar = false;

    public static int existenciaActual=0;
    public static int cantidadSumaResta=0;
    public static List<Integer> listaExistenciaEmpezo = new ArrayList<>();


    public static List<mainarticuloAgregado> listArtAgregados = new ArrayList<>();

    // FORUMULAS Ventas

    public static double obtenerPrecio(double precioarticulo) {
        double preciofinal = 0.0;
        preciofinal = precioarticulo * (1 + (tasa * plazo) / 100);
        return preciofinal;
    }

    public static double obtenerEnganche (double importe) {
        double enganchefinal = 0.0;
        double dbEnganche = Double.valueOf(enganche);
        enganchefinal = (dbEnganche / 100) * importe;
        return enganchefinal;
    }

    public static double obtenerBonificacionEnganche (double engancheobtenido) {
        double bonificacionfinal = 0.0;
        bonificacionfinal = engancheobtenido * ((tasa * plazo) / 100);
        return bonificacionfinal;
    }

    public static double obtenerPrecioContado (double totaladeudo) {
        double preciocontadofinal = 0.0;
        preciocontadofinal = totaladeudo / (1 + (tasa * plazo) / 100);
        return preciocontadofinal;
    }

    public static double obtenerTotalPagar (double preciocontado, int plazoobtenido) {
        double totalpagarfinal = 0.0;
        totalpagarfinal = preciocontado * (1 + (tasa * plazoobtenido) / 100);
        return totalpagarfinal;
    }

    public static double obtenerImporteAbono (double totalpagar, int plazoobtenido) {
        double importeabonofinal = 0.0;
        importeabonofinal = totalpagar / plazoobtenido;
        return importeabonofinal;
    }

    public static double obtenerImporteAhorro(double totaladeudo, double totalpagar) {
        double importeabono = 0.0;
        importeabono = totaladeudo - totalpagar;
        return importeabono;
    }

}
