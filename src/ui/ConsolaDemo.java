package ui;

import model.*;
import service.*;
import repository.CatalogoRepository;
import config.Parametros;

import java.util.List;
import java.util.Scanner;

/**
 * Consola interactiva para demostrar el sistema de gestión de órdenes
 */
public class ConsolaDemo {
    private final Scanner scanner;
    private final CatalogoRepository catalogo;
    private final HistorialService historialService;
    private final OrdenService ordenService;
    private final KPIService kpiService;
    private final TemporizadorService temporizadorService;
    private final Parametros parametros;

    private Usuario usuarioActual;

    public ConsolaDemo() {
        this.scanner = new Scanner(System.in);
        this.catalogo = new CatalogoRepository();
        this.historialService = new HistorialService();
        this.ordenService = new OrdenService(historialService);
        this.kpiService = new KPIService(ordenService);
        this.parametros = new Parametros(15, 45, EstadoParcial.VENCIDA);
        this.temporizadorService = new TemporizadorService(ordenService, parametros, historialService);
    }

    public void iniciar() {
        mostrarBanner();
        catalogo.inicializarDatosDemo();

        // Seleccionar usuario
        seleccionarUsuario();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> crearOrden();
                case "2" -> listarOrdenes();
                case "3" -> verDetalleOrden();
                case "4" -> gestionarOrden();
                case "5" -> mostrarKPIs();
                case "6" -> verHistorial();
                case "7" -> gestionarTemporizador();
                case "8" -> configurarParametros();
                case "9" -> cambiarUsuario();
                case "0" -> salir = true;
                default -> System.out.println("❌ Opción inválida");
            }
        }

        temporizadorService.detener();
        System.out.println("\n👋 ¡Hasta luego!");
    }

    private void mostrarBanner() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║       🏢 SISTEMA DE GESTIÓN DE ÓRDENES - MVP             ║");
        System.out.println("║          Conecta Seguros - Actividad 1                   ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    private void mostrarMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.println("│ Usuario actual: " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")");
        System.out.println("│ Temporizador: " + (temporizadorService.estaEjecutando() ? "✅ ACTIVO" : "⏸️  PAUSADO"));
        System.out.println("│ Parámetros: " + parametros);
        System.out.println("├─────────────────────────────────────────────────────────┤");
        System.out.println("│ 1. Crear orden                                          │");
        System.out.println("│ 2. Listar órdenes                                       │");
        System.out.println("│ 3. Ver detalle de orden                                 │");
        System.out.println("│ 4. Gestionar orden (asignar, cambiar estado)            │");
        System.out.println("│ 5. Ver KPIs y tablero                                   │");
        System.out.println("│ 6. Ver historial                                        │");
        System.out.println("│ 7. Gestionar temporizador                               │");
        System.out.println("│ 8. Configurar parámetros                                │");
        System.out.println("│ 9. Cambiar usuario                                      │");
        System.out.println("│ 0. Salir                                                │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }

    private void seleccionarUsuario() {
        System.out.println("\n👤 Seleccione un usuario:");
        List<Usuario> usuarios = catalogo.obtenerTodosLosUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println((i + 1) + ". " + u.getNombre() + " - " + u.getRol());
        }
        System.out.print("Opción: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            if (opcion > 0 && opcion <= usuarios.size()) {
                usuarioActual = usuarios.get(opcion - 1);
                System.out.println("✅ Usuario seleccionado: " + usuarioActual.getNombre());
            } else {
                usuarioActual = usuarios.get(0);
                System.out.println("⚠️  Usuario por defecto: " + usuarioActual.getNombre());
            }
        } catch (Exception e) {
            usuarioActual = usuarios.get(0);
            System.out.println("⚠️  Usuario por defecto: " + usuarioActual.getNombre());
        }
    }

    private void cambiarUsuario() {
        seleccionarUsuario();
    }

    private void crearOrden() {
        if (!puedeCrearOrdenes()) {
            System.out.println("❌ No tiene permisos para crear órdenes");
            return;
        }

        System.out.println("\n📝 CREAR NUEVA ORDEN");
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Orden orden = ordenService.crearOrden(titulo, descripcion, usuarioActual);
        System.out.println("✅ Orden creada: " + orden.getId());

        // Preguntar si desea asignar áreas
        System.out.print("¿Desea asignar áreas ahora? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            asignarAreasAOrden(orden.getId());
        }
    }

    private void asignarAreasAOrden(String ordenId) {
        List<Area> areas = catalogo.obtenerTodasLasAreas();
        System.out.println("\nÁreas disponibles:");
        for (int i = 0; i < areas.size(); i++) {
            System.out.println((i + 1) + ". " + areas.get(i).getNombre());
        }

        System.out.print("Ingrese números de áreas separados por comas (ej: 1,2,3): ");
        String input = scanner.nextLine().trim();

        for (String num : input.split(",")) {
            try {
                int idx = Integer.parseInt(num.trim()) - 1;
                if (idx >= 0 && idx < areas.size()) {
                    ordenService.asignarArea(ordenId, areas.get(idx), usuarioActual);
                    System.out.println("✅ Área asignada: " + areas.get(idx).getNombre());
                }
            } catch (Exception e) {
                // Ignorar entradas inválidas
            }
        }
    }

    private void listarOrdenes() {
        System.out.println("\n📋 LISTADO DE ÓRDENES");
        System.out.println("1. Todas las órdenes");
        System.out.println("2. Órdenes activas");
        System.out.println("3. Filtrar por estado");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();
        List<Orden> ordenes = switch (opcion) {
            case "2" -> ordenService.obtenerOrdenesActivas();
            case "3" -> filtrarPorEstado();
            default -> ordenService.obtenerTodasLasOrdenes();
        };

        if (ordenes.isEmpty()) {
            System.out.println("No hay órdenes para mostrar");
            return;
        }

        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        for (Orden orden : ordenes) {
            System.out.printf("│ %-10s │ %-25s │ %12s │%n",
                orden.getId(),
                truncar(orden.getTitulo(), 25),
                orden.getEstadoGlobal());
            System.out.printf("│            │ Asignaciones: %-2d  Tiempo: %5ds       │%n",
                orden.getAsignaciones().size(),
                orden.getTiempoTotalSegundos());
        }
        System.out.println("└────────────────────────────────────────────────────────────┘");
    }

    private List<Orden> filtrarPorEstado() {
        System.out.println("\nEstados globales:");
        EstadoGlobal[] estados = EstadoGlobal.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i]);
        }
        System.out.print("Seleccione estado: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < estados.length) {
                return ordenService.filtrarPorEstadoGlobal(estados[idx]);
            }
        } catch (Exception e) {
            // Ignorar
        }
        return ordenService.obtenerTodasLasOrdenes();
    }

    private void verDetalleOrden() {
        System.out.print("\n🔍 Ingrese ID de orden: ");
        String ordenId = scanner.nextLine().trim().toUpperCase();

        Orden orden = ordenService.obtenerOrden(ordenId);
        if (orden == null) {
            System.out.println("❌ Orden no encontrada");
            return;
        }

        System.out.println(orden.toStringDetallado());
    }

    private void gestionarOrden() {
        System.out.print("\n⚙️  Ingrese ID de orden: ");
        String ordenId = scanner.nextLine().trim().toUpperCase();

        Orden orden = ordenService.obtenerOrden(ordenId);
        if (orden == null) {
            System.out.println("❌ Orden no encontrada");
            return;
        }

        System.out.println("\n" + orden.toStringDetallado());

        System.out.println("1. Asignar nueva área");
        System.out.println("2. Cambiar estado de asignación");
        System.out.println("3. Asignar agente a área");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1" -> asignarAreasAOrden(ordenId);
            case "2" -> cambiarEstadoAsignacion(orden);
            case "3" -> asignarAgenteAArea(orden);
        }
    }

    private void cambiarEstadoAsignacion(Orden orden) {
        if (orden.getAsignaciones().isEmpty()) {
            System.out.println("❌ No hay asignaciones en esta orden");
            return;
        }

        System.out.println("\nAsignaciones:");
        List<AsignacionArea> asignaciones = orden.getAsignaciones();
        for (int i = 0; i < asignaciones.size(); i++) {
            System.out.println((i + 1) + ". " + asignaciones.get(i));
        }

        System.out.print("Seleccione asignación: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < asignaciones.size()) {
                AsignacionArea asignacion = asignaciones.get(idx);

                System.out.println("\nNuevo estado:");
                System.out.println("1. EN_PROGRESO");
                System.out.println("2. PENDIENTE");
                System.out.println("3. COMPLETADA");
                System.out.println("4. CERRADA_SIN_SOLUCION");
                System.out.print("Opción: ");

                String estadoOp = scanner.nextLine().trim();
                EstadoParcial nuevoEstado = switch (estadoOp) {
                    case "1" -> EstadoParcial.EN_PROGRESO;
                    case "2" -> EstadoParcial.PENDIENTE;
                    case "3" -> EstadoParcial.COMPLETADA;
                    case "4" -> EstadoParcial.CERRADA_SIN_SOLUCION;
                    default -> null;
                };

                if (nuevoEstado != null) {
                    ordenService.cambiarEstadoAsignacion(orden.getId(),
                        asignacion.getArea(), nuevoEstado, usuarioActual);
                    System.out.println("✅ Estado actualizado");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al procesar");
        }
    }

    private void asignarAgenteAArea(Orden orden) {
        if (orden.getAsignaciones().isEmpty()) {
            System.out.println("❌ No hay asignaciones en esta orden");
            return;
        }

        System.out.println("\nAsignaciones:");
        List<AsignacionArea> asignaciones = orden.getAsignaciones();
        for (int i = 0; i < asignaciones.size(); i++) {
            System.out.println((i + 1) + ". " + asignaciones.get(i).getArea().getNombre());
        }

        System.out.print("Seleccione asignación: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < asignaciones.size()) {
                AsignacionArea asignacion = asignaciones.get(idx);

                List<Usuario> agentes = catalogo.obtenerUsuariosPorRol(Rol.AGENTE_AREA);
                System.out.println("\nAgentes disponibles:");
                for (int i = 0; i < agentes.size(); i++) {
                    System.out.println((i + 1) + ". " + agentes.get(i).getNombre());
                }

                System.out.print("Seleccione agente: ");
                int agenteIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (agenteIdx >= 0 && agenteIdx < agentes.size()) {
                    ordenService.asignarAgente(orden.getId(),
                        asignacion.getArea(), agentes.get(agenteIdx));
                    System.out.println("✅ Agente asignado");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al procesar");
        }
    }

    private void mostrarKPIs() {
        System.out.println(kpiService.mostrarTableroKPIs());
    }

    private void verHistorial() {
        System.out.println("\n📜 HISTORIAL");
        System.out.println("1. Historial completo");
        System.out.println("2. Historial de una orden");
        System.out.println("3. Últimos 10 cambios");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();
        List<CambioHistorial> cambios = switch (opcion) {
            case "2" -> {
                System.out.print("ID de orden: ");
                String id = scanner.nextLine().trim().toUpperCase();
                yield historialService.obtenerHistorialOrden(id);
            }
            case "3" -> historialService.obtenerUltimosCambios(10);
            default -> historialService.obtenerHistorialCompleto();
        };

        System.out.println(historialService.mostrarHistorial(cambios));
    }

    private void gestionarTemporizador() {
        System.out.println("\n⏱️  GESTIÓN DE TEMPORIZADOR");
        System.out.println("1. Iniciar temporizador");
        System.out.println("2. Detener temporizador");
        System.out.println("3. Forzar tick manual");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1" -> {
                temporizadorService.iniciar();
                System.out.println("✅ Temporizador iniciado");
            }
            case "2" -> {
                temporizadorService.detener();
                System.out.println("✅ Temporizador detenido");
            }
            case "3" -> {
                temporizadorService.forzarTick();
                System.out.println("✅ Tick ejecutado manualmente");
            }
        }
    }

    private void configurarParametros() {
        if (usuarioActual.getRol() != Rol.ADMINISTRADOR &&
            usuarioActual.getRol() != Rol.DESPACHADOR_OPERADOR) {
            System.out.println("❌ No tiene permisos para configurar parámetros");
            return;
        }

        System.out.println("\n⚙️  CONFIGURAR PARÁMETROS");
        System.out.println("Actual: " + parametros);

        System.out.print("Nuevo N_seg (segundos entre ticks) [" + parametros.getNSeg() + "]: ");
        String nSegStr = scanner.nextLine().trim();
        if (!nSegStr.isEmpty()) {
            try {
                parametros.setNSeg(Integer.parseInt(nSegStr));
            } catch (Exception e) {
                // Mantener valor actual
            }
        }

        System.out.print("Nuevo SLA_seg (SLA en segundos) [" + parametros.getSlaSeg() + "]: ");
        String slaStr = scanner.nextLine().trim();
        if (!slaStr.isEmpty()) {
            try {
                parametros.setSlaSeg(Integer.parseInt(slaStr));
            } catch (Exception e) {
                // Mantener valor actual
            }
        }

        System.out.println("Estado al vencer timeout:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. VENCIDA");
        System.out.print("Opción [actual: " + parametros.getEstadoTimeout() + "]: ");
        String estadoOp = scanner.nextLine().trim();
        if (estadoOp.equals("1")) {
            parametros.setEstadoTimeout(EstadoParcial.PENDIENTE);
        } else if (estadoOp.equals("2")) {
            parametros.setEstadoTimeout(EstadoParcial.VENCIDA);
        }

        System.out.println("✅ Parámetros actualizados: " + parametros);

        if (temporizadorService.estaEjecutando()) {
            System.out.println("⚠️  Reinicie el temporizador para aplicar cambios en N_seg");
        }
    }

    private boolean puedeCrearOrdenes() {
        return usuarioActual.getRol() == Rol.DESPACHADOR_OPERADOR ||
               usuarioActual.getRol() == Rol.ADMINISTRADOR;
    }

    private String truncar(String texto, int longitud) {
        if (texto.length() <= longitud) {
            return texto;
        }
        return texto.substring(0, longitud - 3) + "...";
    }
}

