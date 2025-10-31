package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa una orden de trabajo en el sistema
 */
public class Orden {
    private static long contadorId = 1;

    private final String id;
    private final String titulo;
    private String descripcion;
    private final Usuario creador;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
    private final List<AsignacionArea> asignaciones;
    private EstadoGlobal estadoGlobal;

    public Orden(String titulo, String descripcion, Usuario creador) {
        this.id = "ORD-" + String.format("%05d", contadorId++);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.creador = creador;
        this.fechaCreacion = LocalDateTime.now();
        this.asignaciones = new ArrayList<>();
        this.estadoGlobal = EstadoGlobal.NUEVA;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getCreador() {
        return creador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public List<AsignacionArea> getAsignaciones() {
        return new ArrayList<>(asignaciones);
    }

    public void agregarAsignacion(AsignacionArea asignacion) {
        asignaciones.add(asignacion);
    }

    public AsignacionArea obtenerAsignacionPorArea(Area area) {
        return asignaciones.stream()
                .filter(a -> a.getArea().equals(area))
                .findFirst()
                .orElse(null);
    }

    public EstadoGlobal getEstadoGlobal() {
        return estadoGlobal;
    }

    public void setEstadoGlobal(EstadoGlobal estadoGlobal) {
        this.estadoGlobal = estadoGlobal;
    }

    public boolean estaCerrada() {
        return estadoGlobal == EstadoGlobal.COMPLETADA ||
               estadoGlobal == EstadoGlobal.CERRADA_SIN_SOLUCION;
    }

    public long getTiempoTotalSegundos() {
        return asignaciones.stream()
                .mapToLong(AsignacionArea::getTiempoTranscurridoSegundos)
                .sum();
    }

    @Override
    public String toString() {
        return id + " - " + titulo + " [" + estadoGlobal + "]";
    }

    public String toStringDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("Orden: ").append(id).append("\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Descripción: ").append(descripcion).append("\n");
        sb.append("Creador: ").append(creador.getNombre()).append("\n");
        sb.append("Fecha creación: ").append(fechaCreacion).append("\n");
        sb.append("Estado global: ").append(estadoGlobal).append("\n");
        sb.append("Tiempo total: ").append(getTiempoTotalSegundos()).append("s\n");
        sb.append("\nAsignaciones:\n");
        for (AsignacionArea asig : asignaciones) {
            sb.append("  • ").append(asig).append("\n");
        }
        if (fechaCierre != null) {
            sb.append("\nFecha cierre: ").append(fechaCierre).append("\n");
        }
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        return sb.toString();
    }
}

