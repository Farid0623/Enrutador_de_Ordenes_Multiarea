package service;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para calcular el estado global de una orden basado en los estados parciales
 */
public class EstadoGlobalService {

    /**
     * Calcula el estado global de una orden según las reglas de negocio
     */
    public EstadoGlobal calcularEstadoGlobal(Orden orden) {
        List<AsignacionArea> asignaciones = orden.getAsignaciones();

        if (asignaciones.isEmpty()) {
            return EstadoGlobal.NUEVA;
        }

        // Contar estados parciales
        long completadas = contarEstado(asignaciones, EstadoParcial.COMPLETADA);
        long cerradasSinSolucion = contarEstado(asignaciones, EstadoParcial.CERRADA_SIN_SOLUCION);
        long vencidas = contarEstado(asignaciones, EstadoParcial.VENCIDA);
        long enProgreso = contarEstado(asignaciones, EstadoParcial.EN_PROGRESO);
        long pendientes = contarEstado(asignaciones, EstadoParcial.PENDIENTE);
        long asignadas = contarEstado(asignaciones, EstadoParcial.ASIGNADA);

        int totalAsignaciones = asignaciones.size();

        // Regla 1: Todas completadas → COMPLETADA
        if (completadas == totalAsignaciones) {
            return EstadoGlobal.COMPLETADA;
        }

        // Regla 2: Todas cerradas sin solución → CERRADA_SIN_SOLUCION
        if (cerradasSinSolucion == totalAsignaciones) {
            return EstadoGlobal.CERRADA_SIN_SOLUCION;
        }

        // Regla 3: Alguna completada y resto cerrado sin solución → CERRADA_SIN_SOLUCION
        if (completadas + cerradasSinSolucion == totalAsignaciones && cerradasSinSolucion > 0) {
            return EstadoGlobal.CERRADA_SIN_SOLUCION;
        }

        // Regla 4: Todas vencidas → TOTALMENTE_VENCIDA
        if (vencidas == totalAsignaciones) {
            return EstadoGlobal.TOTALMENTE_VENCIDA;
        }

        // Regla 5: Alguna vencida pero no todas → PARCIALMENTE_VENCIDA
        if (vencidas > 0) {
            return EstadoGlobal.PARCIALMENTE_VENCIDA;
        }

        // Regla 6: Al menos una en progreso, asignada o pendiente → EN_PROCESO
        if (enProgreso > 0 || pendientes > 0 || asignadas > 0) {
            return EstadoGlobal.EN_PROCESO;
        }

        // Default
        return EstadoGlobal.EN_PROCESO;
    }

    private long contarEstado(List<AsignacionArea> asignaciones, EstadoParcial estado) {
        return asignaciones.stream()
                .filter(a -> a.getEstado() == estado)
                .count();
    }

    /**
     * Obtiene un resumen de los estados parciales de una orden
     */
    public String obtenerResumenEstados(Orden orden) {
        Map<EstadoParcial, Long> conteo = orden.getAsignaciones().stream()
                .collect(Collectors.groupingBy(AsignacionArea::getEstado, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        for (EstadoParcial estado : EstadoParcial.values()) {
            long count = conteo.getOrDefault(estado, 0L);
            if (count > 0) {
                sb.append(estado).append(": ").append(count).append("  ");
            }
        }
        return sb.toString();
    }
}

