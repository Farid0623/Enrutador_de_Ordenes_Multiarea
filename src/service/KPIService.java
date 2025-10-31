package service;

import model.*;
import java.util.*;

/**
 * Servicio para calcular y mostrar KPIs del sistema
 */
public class KPIService {
    private final OrdenService ordenService;

    public KPIService(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    /**
     * Calcular KPIs del sistema
     */
    public KPIResumen calcularKPIs() {
        List<Orden> todasOrdenes = ordenService.obtenerTodasLasOrdenes();

        int total = todasOrdenes.size();
        int completadas = 0;
        int cerradasSinSolucion = 0;
        int enProceso = 0;
        int parcialmenteVencidas = 0;
        int totalmenteVencidas = 0;
        int nuevas = 0;

        long tiempoTotalSegundos = 0;
        int ordenesConAsignaciones = 0;

        for (Orden orden : todasOrdenes) {
            switch (orden.getEstadoGlobal()) {
                case COMPLETADA -> completadas++;
                case CERRADA_SIN_SOLUCION -> cerradasSinSolucion++;
                case EN_PROCESO -> enProceso++;
                case PARCIALMENTE_VENCIDA -> parcialmenteVencidas++;
                case TOTALMENTE_VENCIDA -> totalmenteVencidas++;
                case NUEVA -> nuevas++;
            }

            if (!orden.getAsignaciones().isEmpty()) {
                tiempoTotalSegundos += orden.getTiempoTotalSegundos();
                ordenesConAsignaciones++;
            }
        }

        double tiempoPromedioSegundos = ordenesConAsignaciones > 0
            ? (double) tiempoTotalSegundos / ordenesConAsignaciones
            : 0;

        return new KPIResumen(
            total, completadas, cerradasSinSolucion, enProceso,
            parcialmenteVencidas, totalmenteVencidas, nuevas,
            tiempoPromedioSegundos, tiempoTotalSegundos
        );
    }

    /**
     * Mostrar KPIs en formato de tablero
     */
    public String mostrarTableroKPIs() {
        KPIResumen kpis = calcularKPIs();

        StringBuilder sb = new StringBuilder();
        sb.append("\n╔═══════════════════════════════════════════════════════════╗\n");
        sb.append("║               📊 TABLERO DE KPIs                          ║\n");
        sb.append("╠═══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total de órdenes:           %5d                        ║\n", kpis.total));
        sb.append("║───────────────────────────────────────────────────────────║\n");
        sb.append(String.format("║ ✅ Completadas:             %5d  (%5.1f%%)              ║\n",
            kpis.completadas, calcularPorcentaje(kpis.completadas, kpis.total)));
        sb.append(String.format("║ ❌ Cerradas sin solución:   %5d  (%5.1f%%)              ║\n",
            kpis.cerradasSinSolucion, calcularPorcentaje(kpis.cerradasSinSolucion, kpis.total)));
        sb.append(String.format("║ 🔄 En proceso:              %5d  (%5.1f%%)              ║\n",
            kpis.enProceso, calcularPorcentaje(kpis.enProceso, kpis.total)));
        sb.append(String.format("║ ⚠️  Parcialmente vencidas:  %5d  (%5.1f%%)              ║\n",
            kpis.parcialmenteVencidas, calcularPorcentaje(kpis.parcialmenteVencidas, kpis.total)));
        sb.append(String.format("║ 🔴 Totalmente vencidas:     %5d  (%5.1f%%)              ║\n",
            kpis.totalmenteVencidas, calcularPorcentaje(kpis.totalmenteVencidas, kpis.total)));
        sb.append(String.format("║ 🆕 Nuevas:                  %5d  (%5.1f%%)              ║\n",
            kpis.nuevas, calcularPorcentaje(kpis.nuevas, kpis.total)));
        sb.append("║───────────────────────────────────────────────────────────║\n");
        sb.append(String.format("║ ⏱️  Tiempo promedio:        %6.1fs                      ║\n",
            kpis.tiempoPromedioSegundos));
        sb.append(String.format("║ ⏱️  Tiempo total acumulado: %6ds                      ║\n",
            kpis.tiempoTotalSegundos));
        sb.append("╚═══════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    private double calcularPorcentaje(int valor, int total) {
        return total > 0 ? (valor * 100.0 / total) : 0.0;
    }

    /**
     * Clase interna para encapsular los KPIs
     */
    public static class KPIResumen {
        public final int total;
        public final int completadas;
        public final int cerradasSinSolucion;
        public final int enProceso;
        public final int parcialmenteVencidas;
        public final int totalmenteVencidas;
        public final int nuevas;
        public final double tiempoPromedioSegundos;
        public final long tiempoTotalSegundos;

        public KPIResumen(int total, int completadas, int cerradasSinSolucion,
                         int enProceso, int parcialmenteVencidas, int totalmenteVencidas,
                         int nuevas, double tiempoPromedioSegundos, long tiempoTotalSegundos) {
            this.total = total;
            this.completadas = completadas;
            this.cerradasSinSolucion = cerradasSinSolucion;
            this.enProceso = enProceso;
            this.parcialmenteVencidas = parcialmenteVencidas;
            this.totalmenteVencidas = totalmenteVencidas;
            this.nuevas = nuevas;
            this.tiempoPromedioSegundos = tiempoPromedioSegundos;
            this.tiempoTotalSegundos = tiempoTotalSegundos;
        }
    }
}

