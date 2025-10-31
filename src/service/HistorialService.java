package service;

import model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de historial de cambios
 */
public class HistorialService {
    private final List<CambioHistorial> historial;

    public HistorialService() {
        this.historial = new ArrayList<>();
    }

    /**
     * Registrar un cambio en el historial
     */
    public void registrarCambio(String ordenId, String descripcion, Usuario usuario,
                               String detalleAnterior, String detalleNuevo) {
        CambioHistorial cambio = new CambioHistorial(
            ordenId, descripcion, usuario, detalleAnterior, detalleNuevo
        );
        historial.add(cambio);
    }

    /**
     * Obtener historial completo
     */
    public List<CambioHistorial> obtenerHistorialCompleto() {
        return new ArrayList<>(historial);
    }

    /**
     * Obtener historial de una orden específica
     */
    public List<CambioHistorial> obtenerHistorialOrden(String ordenId) {
        return historial.stream()
                .filter(c -> c.getOrdenId().equals(ordenId))
                .collect(Collectors.toList());
    }

    /**
     * Obtener últimos N cambios
     */
    public List<CambioHistorial> obtenerUltimosCambios(int cantidad) {
        int size = historial.size();
        int inicio = Math.max(0, size - cantidad);
        return new ArrayList<>(historial.subList(inicio, size));
    }

    /**
     * Mostrar historial formateado
     */
    public String mostrarHistorial(List<CambioHistorial> cambios) {
        if (cambios.isEmpty()) {
            return "No hay cambios registrados";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n═══════════════ HISTORIAL ═══════════════\n");
        for (CambioHistorial cambio : cambios) {
            sb.append(cambio).append("\n");
        }
        sb.append("═══════════════════════════════════════════\n");
        return sb.toString();
    }
}

