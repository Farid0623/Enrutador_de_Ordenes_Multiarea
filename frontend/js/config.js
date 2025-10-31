/**
 * Configuración Global del Sistema
 * Sistema de Gestión de Órdenes
 */

const CONFIG = {
    // API Configuration
    API: {
        BASE_URL: 'http://localhost:8080/api/v1',
        TIMEOUT: 10000, // 10 segundos
        RETRY_ATTEMPTS: 3,
        RETRY_DELAY: 1000, // 1 segundo
    },

    // Temporizador Configuration (E7)
    TEMPORIZADOR: {
        N_SEG: 15,              // Intervalo de actualización (segundos)
        SLA_SEG: 60,            // Tiempo límite SLA (segundos)
        ESTADO_TIMEOUT: 'VENCIDA', // Estado al exceder timeout
        ENABLED: true,          // Habilitar temporizador
        AUTO_START: true,       // Iniciar automáticamente
    },

    // UI Configuration
    UI: {
        ITEMS_PER_PAGE: 20,
        REFRESH_INTERVAL: 30000, // 30 segundos
        ANIMATION_DURATION: 300,
        DEBOUNCE_DELAY: 500,
    },

    // Estados
    ESTADOS: {
        GLOBAL: {
            NUEVA: { label: 'Nueva', color: '#8b5cf6', icon: '🆕' },
            EN_PROCESO: { label: 'En Proceso', color: '#3b82f6', icon: '🔄' },
            COMPLETADA: { label: 'Completada', color: '#10b981', icon: '✅' },
            CERRADA_SIN_SOLUCION: { label: 'Cerrada sin Solución', color: '#ef4444', icon: '❌' },
            PARCIALMENTE_VENCIDA: { label: 'Parcialmente Vencida', color: '#f59e0b', icon: '⚠️' },
            TOTALMENTE_VENCIDA: { label: 'Totalmente Vencida', color: '#dc2626', icon: '🔴' },
        },
        PARCIAL: {
            NUEVA: { label: 'Nueva', color: '#8b5cf6' },
            ASIGNADA: { label: 'Asignada', color: '#06b6d4' },
            EN_PROGRESO: { label: 'En Progreso', color: '#3b82f6' },
            PENDIENTE: { label: 'Pendiente', color: '#eab308' },
            COMPLETADA: { label: 'Completada', color: '#10b981' },
            CERRADA_SIN_SOLUCION: { label: 'Cerrada sin Solución', color: '#ef4444' },
            VENCIDA: { label: 'Vencida', color: '#f59e0b' },
        },
    },

    // Prioridades
    PRIORIDADES: {
        CRITICA: { label: 'Crítica', color: '#dc2626', icon: '🔴' },
        ALTA: { label: 'Alta', color: '#f97316', icon: '🟠' },
        MEDIA: { label: 'Media', color: '#eab308', icon: '🟡' },
        BAJA: { label: 'Baja', color: '#3b82f6', icon: '🔵' },
    },

    // Formatos
    FORMATS: {
        DATE: 'DD/MM/YYYY',
        TIME: 'HH:mm:ss',
        DATETIME: 'DD/MM/YYYY HH:mm',
    },

    // Mensajes
    MESSAGES: {
        ERROR: {
            NETWORK: 'Error de conexión. Por favor, verifica tu conexión a internet.',
            SERVER: 'Error del servidor. Por favor, intenta más tarde.',
            NOT_FOUND: 'Recurso no encontrado.',
            VALIDATION: 'Datos inválidos. Por favor, verifica los campos.',
            UNAUTHORIZED: 'No autorizado. Por favor, inicia sesión.',
            TIMEOUT: 'La solicitud ha excedido el tiempo límite.',
        },
        SUCCESS: {
            CREATED: 'Creado exitosamente',
            UPDATED: 'Actualizado exitosamente',
            DELETED: 'Eliminado exitosamente',
        },
    },

    // Feature Flags
    FEATURES: {
        ENABLE_MOCK_DATA: true,  // Usar datos mock si API no disponible
        ENABLE_AUTO_REFRESH: true,
        ENABLE_NOTIFICATIONS: true,
        ENABLE_ANALYTICS: false,
    },

    // Debug
    DEBUG: true,
};

// Función helper para logging
const log = (...args) => {
    if (CONFIG.DEBUG) {
        console.log('[Sistema]', ...args);
    }
};

const logError = (...args) => {
    if (CONFIG.DEBUG) {
        console.error('[Sistema ERROR]', ...args);
    }
};

// Exportar para uso global
window.CONFIG = CONFIG;
window.log = log;
window.logError = logError;

log('Configuración cargada:', CONFIG);

