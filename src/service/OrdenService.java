package service;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio central de gestión de órdenes (CRUD y operaciones)
 */
public class OrdenService {
    private final Map<String, Orden> ordenes;
    private final EstadoGlobalService estadoGlobalService;
    private final HistorialService historialService;

    public OrdenService(HistorialService historialService) {
        this.ordenes = new HashMap<>();
        this.estadoGlobalService = new EstadoGlobalService();
        this.historialService = historialService;
    }

    /**
     * Crear una nueva orden
     */
    public Orden crearOrden(String titulo, String descripcion, Usuario creador) {
        Orden orden = new Orden(titulo, descripcion, creador);
        ordenes.put(orden.getId(), orden);

        historialService.registrarCambio(
            orden.getId(),
            "Orden creada",
            creador,
            null,
            "NUEVA"
        );

        return orden;
    }

    /**
     * Asignar un área a una orden
     */
    public void asignarArea(String ordenId, Area area, Usuario usuario) {
        Orden orden = obtenerOrden(ordenId);
        if (orden == null || orden.estaCerrada()) {
            return;
        }

        AsignacionArea asignacion = new AsignacionArea(area);
        orden.agregarAsignacion(asignacion);

        actualizarEstadoGlobal(orden);

        historialService.registrarCambio(
            ordenId,
            "Área asignada",
            usuario,
            null,
            area.getNombre()
        );
    }

    /**
     * Asignar un agente específico a un área de una orden
     */
    public void asignarAgente(String ordenId, Area area, Usuario agente) {
        Orden orden = obtenerOrden(ordenId);
        if (orden == null || orden.estaCerrada()) {
            return;
        }

        AsignacionArea asignacion = orden.obtenerAsignacionPorArea(area);
        if (asignacion != null) {
            asignacion.setAgenteAsignado(agente);

            historialService.registrarCambio(
                ordenId,
                "Agente asignado a " + area.getNombre(),
                agente,
                null,
                agente.getNombre()
            );
        }
    }

    /**
     * Cambiar el estado de una asignación de área
     */
    public void cambiarEstadoAsignacion(String ordenId, Area area, EstadoParcial nuevoEstado, Usuario usuario) {
        Orden orden = obtenerOrden(ordenId);
        if (orden == null) {
            return;
        }

        AsignacionArea asignacion = orden.obtenerAsignacionPorArea(area);
        if (asignacion != null) {
            EstadoParcial estadoAnterior = asignacion.getEstado();
            asignacion.setEstado(nuevoEstado);

            actualizarEstadoGlobal(orden);

            historialService.registrarCambio(
                ordenId,
                "Cambio de estado en " + area.getNombre(),
                usuario,
                estadoAnterior.toString(),
                nuevoEstado.toString()
            );
        }
    }

    /**
     * Iniciar trabajo en una asignación
     */
    public void iniciarAsignacion(String ordenId, Area area, Usuario agente) {
        cambiarEstadoAsignacion(ordenId, area, EstadoParcial.EN_PROGRESO, agente);
    }

    /**
     * Pausar trabajo en una asignación
     */
    public void pausarAsignacion(String ordenId, Area area, Usuario agente) {
        cambiarEstadoAsignacion(ordenId, area, EstadoParcial.PENDIENTE, agente);
    }

    /**
     * Completar una asignación
     */
    public void completarAsignacion(String ordenId, Area area, Usuario agente) {
        cambiarEstadoAsignacion(ordenId, area, EstadoParcial.COMPLETADA, agente);
    }

    /**
     * Cerrar una asignación sin solución
     */
    public void cerrarSinSolucion(String ordenId, Area area, Usuario agente) {
        cambiarEstadoAsignacion(ordenId, area, EstadoParcial.CERRADA_SIN_SOLUCION, agente);
    }

    /**
     * Actualizar descripción de una orden
     */
    public void actualizarDescripcion(String ordenId, String nuevaDescripcion, Usuario usuario) {
        Orden orden = obtenerOrden(ordenId);
        if (orden != null && !orden.estaCerrada()) {
            String anterior = orden.getDescripcion();
            orden.setDescripcion(nuevaDescripcion);

            historialService.registrarCambio(
                ordenId,
                "Descripción actualizada",
                usuario,
                anterior,
                nuevaDescripcion
            );
        }
    }

    /**
     * Obtener una orden por su ID
     */
    public Orden obtenerOrden(String ordenId) {
        return ordenes.get(ordenId);
    }

    /**
     * Obtener todas las órdenes
     */
    public List<Orden> obtenerTodasLasOrdenes() {
        return new ArrayList<>(ordenes.values());
    }

    /**
     * Filtrar órdenes por estado global
     */
    public List<Orden> filtrarPorEstadoGlobal(EstadoGlobal estado) {
        return ordenes.values().stream()
                .filter(o -> o.getEstadoGlobal() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Filtrar órdenes activas (no cerradas)
     */
    public List<Orden> obtenerOrdenesActivas() {
        return ordenes.values().stream()
                .filter(o -> !o.estaCerrada())
                .collect(Collectors.toList());
    }

    /**
     * Actualizar el estado global de una orden
     */
    public void actualizarEstadoGlobal(Orden orden) {
        EstadoGlobal estadoAnterior = orden.getEstadoGlobal();
        EstadoGlobal nuevoEstado = estadoGlobalService.calcularEstadoGlobal(orden);

        if (estadoAnterior != nuevoEstado) {
            orden.setEstadoGlobal(nuevoEstado);

            // Si se cierra, registrar fecha
            if (orden.estaCerrada() && orden.getFechaCierre() == null) {
                orden.setFechaCierre(java.time.LocalDateTime.now());
            }

            historialService.registrarCambio(
                orden.getId(),
                "Estado global actualizado",
                null,
                estadoAnterior.toString(),
                nuevoEstado.toString()
            );
        }
    }

    /**
     * Actualizar todas las órdenes activas (recalcular estados)
     */
    public void actualizarTodasLasOrdenes() {
        obtenerOrdenesActivas().forEach(this::actualizarEstadoGlobal);
    }
}
