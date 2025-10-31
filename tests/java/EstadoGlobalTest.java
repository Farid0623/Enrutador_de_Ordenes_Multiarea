/**
 * E9: Tests de Reglas de Estado y Temporizador
 * Sistema de Gestión de Órdenes
 *
 * Tests unitarios para verificar:
 * 1. Cálculo de estado global según estados parciales
 * 2. Aplicación de timeout SLA
 * 3. Acumulación de segundos
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Simulación de las clases del dominio (ajustar según implementación real)
class EstadoParcial {
    public static final String NUEVA = "NUEVA";
    public static final String ASIGNADA = "ASIGNADA";
    public static final String EN_PROGRESO = "EN_PROGRESO";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String COMPLETADA = "COMPLETADA";
    public static final String CERRADA_SIN_SOLUCION = "CERRADA_SIN_SOLUCION";
    public static final String VENCIDA = "VENCIDA";
}

class EstadoGlobal {
    public static final String NUEVA = "NUEVA";
    public static final String EN_PROCESO = "EN_PROCESO";
    public static final String COMPLETADA = "COMPLETADA";
    public static final String CERRADA_SIN_SOLUCION = "CERRADA_SIN_SOLUCION";
    public static final String PARCIALMENTE_VENCIDA = "PARCIALMENTE_VENCIDA";
    public static final String TOTALMENTE_VENCIDA = "TOTALMENTE_VENCIDA";
}

class AsignacionArea {
    private String areaId;
    private String estadoParcial;
    private long segAcumulados;

    public AsignacionArea(String areaId, String estadoParcial, long segAcumulados) {
        this.areaId = areaId;
        this.estadoParcial = estadoParcial;
        this.segAcumulados = segAcumulados;
    }

    public String getAreaId() { return areaId; }
    public String getEstadoParcial() { return estadoParcial; }
    public void setEstadoParcial(String estado) { this.estadoParcial = estado; }
    public long getSegAcumulados() { return segAcumulados; }
    public void setSegAcumulados(long seg) { this.segAcumulados = seg; }
}

/**
 * Servicio de cálculo de estado global
 * (Simplificado para tests - la implementación real estaría en src/)
 */
class EstadoGlobalService {

    /**
     * Regla 1: Si todas COMPLETADA → COMPLETADA
     * Regla 2: Si alguna CERRADA_SIN_SOLUCION → CERRADA_SIN_SOLUCION
     * Regla 3: Si todas VENCIDA → TOTALMENTE_VENCIDA
     * Regla 4: Si alguna VENCIDA → PARCIALMENTE_VENCIDA
     * Regla 5: Si alguna EN_PROGRESO o PENDIENTE → EN_PROCESO
     * Regla 6: Si todas NUEVA o ASIGNADA → NUEVA
     */
    public String calcularEstadoGlobal(List<AsignacionArea> asignaciones) {
        if (asignaciones.isEmpty()) {
            return EstadoGlobal.NUEVA;
        }

        int totalAsignaciones = asignaciones.size();
        int completadas = 0;
        int cerradasSinSolucion = 0;
        int vencidas = 0;
        int enProgreso = 0;
        int pendientes = 0;
        int nuevasOAsignadas = 0;

        for (AsignacionArea a : asignaciones) {
            String estado = a.getEstadoParcial();

            switch (estado) {
                case EstadoParcial.COMPLETADA:
                    completadas++;
                    break;
                case EstadoParcial.CERRADA_SIN_SOLUCION:
                    cerradasSinSolucion++;
                    break;
                case EstadoParcial.VENCIDA:
                    vencidas++;
                    break;
                case EstadoParcial.EN_PROGRESO:
                    enProgreso++;
                    break;
                case EstadoParcial.PENDIENTE:
                    pendientes++;
                    break;
                case EstadoParcial.NUEVA:
                case EstadoParcial.ASIGNADA:
                    nuevasOAsignadas++;
                    break;
            }
        }

        // Regla 1: Todas completadas
        if (completadas == totalAsignaciones) {
            return EstadoGlobal.COMPLETADA;
        }

        // Regla 2: Alguna cerrada sin solución
        if (cerradasSinSolucion > 0) {
            return EstadoGlobal.CERRADA_SIN_SOLUCION;
        }

        // Regla 3: Todas vencidas
        if (vencidas == totalAsignaciones) {
            return EstadoGlobal.TOTALMENTE_VENCIDA;
        }

        // Regla 4: Alguna vencida (pero no todas)
        if (vencidas > 0) {
            return EstadoGlobal.PARCIALMENTE_VENCIDA;
        }

        // Regla 5: Alguna en progreso o pendiente
        if (enProgreso > 0 || pendientes > 0) {
            return EstadoGlobal.EN_PROCESO;
        }

        // Regla 6: Todas nuevas o asignadas
        return EstadoGlobal.NUEVA;
    }
}

/**
 * Servicio de temporizador
 */
class TemporizadorService {
    private final int nSeg;
    private final int slaSeg;
    private final String estadoTimeout;

    public TemporizadorService(int nSeg, int slaSeg, String estadoTimeout) {
        this.nSeg = nSeg;
        this.slaSeg = slaSeg;
        this.estadoTimeout = estadoTimeout;
    }

    /**
     * Ejecutar tick del temporizador
     */
    public void ejecutarTick(List<AsignacionArea> asignaciones) {
        for (AsignacionArea a : asignaciones) {
            String estado = a.getEstadoParcial();

            // Solo incrementar tiempo en estados activos
            if (estado.equals(EstadoParcial.EN_PROGRESO) ||
                estado.equals(EstadoParcial.PENDIENTE)) {

                // Incrementar tiempo
                a.setSegAcumulados(a.getSegAcumulados() + nSeg);

                // Verificar SLA (solo para EN_PROGRESO)
                if (estado.equals(EstadoParcial.EN_PROGRESO) &&
                    a.getSegAcumulados() >= slaSeg) {
                    a.setEstadoParcial(estadoTimeout);
                }
            }
        }
    }
}

/**
 * E9: Tests de Reglas de Estado
 */
@DisplayName("E9: Tests de Reglas de Estado y Temporizador")
public class EstadoGlobalTest {

    private EstadoGlobalService estadoService;
    private TemporizadorService temporizadorService;

    @BeforeEach
    void setUp() {
        estadoService = new EstadoGlobalService();
        temporizadorService = new TemporizadorService(15, 60, EstadoParcial.VENCIDA);
    }

    // ========================================
    // CASO 1: Dos áreas - una COMPLETADA, otra EN_PROGRESO
    // ========================================

    @Test
    @DisplayName("Caso 1: Una completada + una en progreso → Estado global EN_PROCESO")
    void testDosAreas_UnaCompletada_OtraEnProgreso() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.COMPLETADA, 3600));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.EN_PROGRESO, 1800));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.EN_PROCESO, estadoGlobal,
            "Estado global debe ser EN_PROCESO cuando hay al menos una asignación en progreso");
    }

    @Test
    @DisplayName("Caso 1b: Una completada + una pendiente → Estado global EN_PROCESO")
    void testDosAreas_UnaCompletada_OtraPendiente() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.COMPLETADA, 3600));
        asignaciones.add(new AsignacionArea("AREA-SOP", EstadoParcial.PENDIENTE, 900));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.EN_PROCESO, estadoGlobal,
            "Estado global debe ser EN_PROCESO cuando hay asignaciones pendientes");
    }

    // ========================================
    // CASO 2: Timeout SLA
    // ========================================

    @Test
    @DisplayName("Caso 2a: Exceder SLA → Cambia a VENCIDA")
    void testTimeout_ExcedeSLA_CambiaAVencida() {
        // Arrange
        AsignacionArea asignacion = new AsignacionArea("AREA-TEC", EstadoParcial.EN_PROGRESO, 55);
        List<AsignacionArea> asignaciones = List.of(asignacion);

        // Act - Ejecutar tick (incrementa 15s, total 70s > 60s SLA)
        temporizadorService.ejecutarTick(asignaciones);

        // Assert
        assertEquals(EstadoParcial.VENCIDA, asignacion.getEstadoParcial(),
            "Estado debe cambiar a VENCIDA cuando excede SLA");
        assertEquals(70, asignacion.getSegAcumulados(),
            "Segundos acumulados debe ser 55 + 15 = 70");
    }

    @Test
    @DisplayName("Caso 2b: Exceder SLA → Estado global PARCIALMENTE_VENCIDA")
    void testTimeout_EstadoGlobal_ParcialmenteVencida() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.EN_PROGRESO, 55));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.COMPLETADA, 3600));

        // Act - Ejecutar tick
        temporizadorService.ejecutarTick(asignaciones);
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoParcial.VENCIDA, asignaciones.get(0).getEstadoParcial(),
            "Primera área debe estar VENCIDA");
        assertEquals(EstadoGlobal.PARCIALMENTE_VENCIDA, estadoGlobal,
            "Estado global debe ser PARCIALMENTE_VENCIDA cuando hay al menos una vencida");
    }

    @Test
    @DisplayName("Caso 2c: Múltiples ticks acumulan tiempo correctamente")
    void testTimeout_MultiplesTicks_AcumulanTiempo() {
        // Arrange
        AsignacionArea asignacion = new AsignacionArea("AREA-TEC", EstadoParcial.EN_PROGRESO, 0);
        List<AsignacionArea> asignaciones = List.of(asignacion);

        // Act - Ejecutar 5 ticks (5 * 15s = 75s)
        for (int i = 0; i < 5; i++) {
            temporizadorService.ejecutarTick(asignaciones);
        }

        // Assert
        assertEquals(75, asignacion.getSegAcumulados(),
            "Debe acumular correctamente: 5 ticks * 15s = 75s");
        assertEquals(EstadoParcial.VENCIDA, asignacion.getEstadoParcial(),
            "Debe cambiar a VENCIDA después de exceder 60s");
    }

    @Test
    @DisplayName("Caso 2d: PENDIENTE incrementa tiempo pero NO vence")
    void testTimeout_Pendiente_NoVence() {
        // Arrange
        AsignacionArea asignacion = new AsignacionArea("AREA-TEC", EstadoParcial.PENDIENTE, 55);
        List<AsignacionArea> asignaciones = List.of(asignacion);

        // Act - Ejecutar tick
        temporizadorService.ejecutarTick(asignaciones);

        // Assert
        assertEquals(70, asignacion.getSegAcumulados(),
            "Debe incrementar tiempo: 55 + 15 = 70");
        assertEquals(EstadoParcial.PENDIENTE, asignacion.getEstadoParcial(),
            "PENDIENTE NO debe cambiar a VENCIDA aunque exceda SLA");
    }

    // ========================================
    // CASO 3: Todas completadas
    // ========================================

    @Test
    @DisplayName("Caso 3a: Todas completadas → Estado global COMPLETADA")
    void testTodasCompletadas_EstadoGlobalCompletada() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.COMPLETADA, 3600));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.COMPLETADA, 7200));
        asignaciones.add(new AsignacionArea("AREA-SOP", EstadoParcial.COMPLETADA, 1800));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.COMPLETADA, estadoGlobal,
            "Estado global debe ser COMPLETADA cuando todas las áreas están completadas");
    }

    @Test
    @DisplayName("Caso 3b: Progresión completa de estados")
    void testProgresionCompleta_Nueva_EnProgreso_Completada() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.NUEVA, 0));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.NUEVA, 0));

        // Act & Assert - Estado inicial: NUEVA
        assertEquals(EstadoGlobal.NUEVA, estadoService.calcularEstadoGlobal(asignaciones));

        // Cambiar a EN_PROGRESO
        asignaciones.get(0).setEstadoParcial(EstadoParcial.EN_PROGRESO);
        assertEquals(EstadoGlobal.EN_PROCESO, estadoService.calcularEstadoGlobal(asignaciones));

        // Completar primera, segunda aún nueva
        asignaciones.get(0).setEstadoParcial(EstadoParcial.COMPLETADA);
        assertEquals(EstadoGlobal.NUEVA, estadoService.calcularEstadoGlobal(asignaciones));

        // Completar ambas
        asignaciones.get(1).setEstadoParcial(EstadoParcial.COMPLETADA);
        assertEquals(EstadoGlobal.COMPLETADA, estadoService.calcularEstadoGlobal(asignaciones));
    }

    // ========================================
    // CASOS ADICIONALES: Reglas especiales
    // ========================================

    @Test
    @DisplayName("Regla: Alguna CERRADA_SIN_SOLUCION → Estado global CERRADA_SIN_SOLUCION")
    void testAlgunaCerradaSinSolucion_EstadoGlobalCerradaSinSolucion() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.COMPLETADA, 3600));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.CERRADA_SIN_SOLUCION, 1800));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.CERRADA_SIN_SOLUCION, estadoGlobal,
            "Si alguna está cerrada sin solución, estado global debe ser CERRADA_SIN_SOLUCION");
    }

    @Test
    @DisplayName("Regla: Todas VENCIDA → Estado global TOTALMENTE_VENCIDA")
    void testTodasVencidas_EstadoGlobalTotalmenteVencida() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.VENCIDA, 120));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.VENCIDA, 150));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.TOTALMENTE_VENCIDA, estadoGlobal,
            "Si todas están vencidas, estado global debe ser TOTALMENTE_VENCIDA");
    }

    @Test
    @DisplayName("Regla: Una VENCIDA + otras NO → Estado global PARCIALMENTE_VENCIDA")
    void testAlgunaVencida_EstadoGlobalParcialmenteVencida() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.VENCIDA, 120));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.EN_PROGRESO, 30));

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.PARCIALMENTE_VENCIDA, estadoGlobal,
            "Si alguna (pero no todas) está vencida, debe ser PARCIALMENTE_VENCIDA");
    }

    @Test
    @DisplayName("Edge case: Sin asignaciones → Estado global NUEVA")
    void testSinAsignaciones_EstadoGlobalNueva() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();

        // Act
        String estadoGlobal = estadoService.calcularEstadoGlobal(asignaciones);

        // Assert
        assertEquals(EstadoGlobal.NUEVA, estadoGlobal,
            "Sin asignaciones, estado global debe ser NUEVA");
    }

    @Test
    @DisplayName("Edge case: Estados finales NO incrementan tiempo")
    void testEstadosFinales_NoIncrementanTiempo() {
        // Arrange
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.COMPLETADA, 100));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.CERRADA_SIN_SOLUCION, 50));
        asignaciones.add(new AsignacionArea("AREA-SOP", EstadoParcial.VENCIDA, 150));

        // Act
        temporizadorService.ejecutarTick(asignaciones);

        // Assert
        assertEquals(100, asignaciones.get(0).getSegAcumulados(),
            "COMPLETADA no debe incrementar tiempo");
        assertEquals(50, asignaciones.get(1).getSegAcumulados(),
            "CERRADA_SIN_SOLUCION no debe incrementar tiempo");
        assertEquals(150, asignaciones.get(2).getSegAcumulados(),
            "VENCIDA no debe incrementar tiempo");
    }

    // ========================================
    // Test de integración: Escenario completo
    // ========================================

    @Test
    @DisplayName("Integración: Escenario completo de orden multiárea")
    void testEscenarioCompleto_OrdenMultiarea() {
        // Arrange - Orden con 3 áreas
        List<AsignacionArea> asignaciones = new ArrayList<>();
        asignaciones.add(new AsignacionArea("AREA-TEC", EstadoParcial.ASIGNADA, 0));
        asignaciones.add(new AsignacionArea("AREA-COM", EstadoParcial.ASIGNADA, 0));
        asignaciones.add(new AsignacionArea("AREA-CAL", EstadoParcial.ASIGNADA, 0));

        // Estado inicial: NUEVA
        assertEquals(EstadoGlobal.NUEVA, estadoService.calcularEstadoGlobal(asignaciones));

        // Paso 1: Área Técnica inicia trabajo
        asignaciones.get(0).setEstadoParcial(EstadoParcial.EN_PROGRESO);
        assertEquals(EstadoGlobal.EN_PROCESO, estadoService.calcularEstadoGlobal(asignaciones));

        // Paso 2: Tick 1 - incrementa tiempo (15s)
        temporizadorService.ejecutarTick(asignaciones);
        assertEquals(15, asignaciones.get(0).getSegAcumulados());

        // Paso 3: Área Comercial también inicia
        asignaciones.get(1).setEstadoParcial(EstadoParcial.EN_PROGRESO);

        // Paso 4: 4 ticks más - Área Técnica excede SLA (75s > 60s)
        for (int i = 0; i < 4; i++) {
            temporizadorService.ejecutarTick(asignaciones);
        }
        assertEquals(75, asignaciones.get(0).getSegAcumulados());
        assertEquals(EstadoParcial.VENCIDA, asignaciones.get(0).getEstadoParcial());
        assertEquals(EstadoGlobal.PARCIALMENTE_VENCIDA,
            estadoService.calcularEstadoGlobal(asignaciones));

        // Paso 5: Completar Área Comercial
        asignaciones.get(1).setEstadoParcial(EstadoParcial.COMPLETADA);
        assertEquals(EstadoGlobal.PARCIALMENTE_VENCIDA,
            estadoService.calcularEstadoGlobal(asignaciones),
            "Sigue PARCIALMENTE_VENCIDA por Área Técnica");

        // Paso 6: Recuperar Área Técnica
        asignaciones.get(0).setEstadoParcial(EstadoParcial.COMPLETADA);
        assertEquals(EstadoGlobal.NUEVA,
            estadoService.calcularEstadoGlobal(asignaciones),
            "Área Calidad aún ASIGNADA, no iniciada");

        // Paso 7: Completar Área Calidad
        asignaciones.get(2).setEstadoParcial(EstadoParcial.COMPLETADA);
        assertEquals(EstadoGlobal.COMPLETADA,
            estadoService.calcularEstadoGlobal(asignaciones),
            "Todas completadas → Estado global COMPLETADA");
    }
}

