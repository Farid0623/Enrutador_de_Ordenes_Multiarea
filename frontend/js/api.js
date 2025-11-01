    async getAreas() {
        if (this.useMock) {
            return this.getMockAreas();
        }

        try {
            return await this.client.get('/areas');
        } catch (error) {
            if (CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                return this.getMockAreas();
            }
            throw error;
        }
    }

    // ========================================
    // Mock Data Methods
    // ========================================

    getMockOrdenes(filters = {}) {
        const ordenes = [
            {
                id: 'ORD-00001',
                titulo: 'Instalación de sistema CCTV - Cliente VIP Banco Central',
                descripcion: 'Instalación completa de 24 cámaras IP 4K con almacenamiento NVR',
                estado_global: 'NUEVA',
                prioridad: 'CRITICA',
                num_areas: 1,
                creada_en: '2025-10-25T10:00:00Z',
                actualizada_en: '2025-10-25T10:00:00Z',
            },
            {
                id: 'ORD-00002',
                titulo: 'Renovación de contrato de mantenimiento - Corp. Tech Solutions',
                descripcion: 'Renovación anual del contrato de mantenimiento preventivo',
                estado_global: 'EN_PROCESO',
                prioridad: 'ALTA',
                num_areas: 3,
                creada_en: '2025-10-26T09:00:00Z',
                actualizada_en: '2025-10-31T15:30:00Z',
            },
            {
                id: 'ORD-00003',
                titulo: 'Soporte urgente - Fallo en sistema de alarmas Hotel Plaza',
                descripcion: 'El sistema de alarmas perimetrales ha dejado de funcionar',
                estado_global: 'PARCIALMENTE_VENCIDA',
                prioridad: 'CRITICA',
                num_areas: 1,
                creada_en: '2025-10-27T14:00:00Z',
                actualizada_en: '2025-10-31T16:00:00Z',
            },
            {
                id: 'ORD-00004',
                titulo: 'Ampliación de sistema de control de accesos - Centro Comercial Norte',
                descripcion: 'Añadir 8 nuevas puertas al sistema de control de accesos existente',
                estado_global: 'EN_PROCESO',
                prioridad: 'MEDIA',
                num_areas: 2,
                creada_en: '2025-10-28T11:00:00Z',
                actualizada_en: '2025-10-31T14:00:00Z',
            },
            {
                id: 'ORD-00005',
                titulo: 'Proyecto integral para edificio corporativo - Nueva sede Global Insurance',
                descripcion: 'Proyecto completo de seguridad para un edificio de 12 plantas',
                estado_global: 'EN_PROCESO',
                prioridad: 'CRITICA',
                num_areas: 4,
                creada_en: '2025-10-29T10:00:00Z',
                actualizada_en: '2025-10-31T17:00:00Z',
            },
            {
                id: 'ORD-00006',
                titulo: 'Configuración de VPN remota - Cliente Farmacia Central',
                descripcion: 'El cliente requiere configuración de acceso remoto VPN seguro',
                estado_global: 'NUEVA',
                prioridad: 'MEDIA',
                num_areas: 1,
                creada_en: '2025-10-30T08:00:00Z',
                actualizada_en: '2025-10-30T08:00:00Z',
            },
            {
                id: 'ORD-00007',
                titulo: 'Mantenimiento preventivo trimestral - Cadena Retail TopMart',
                descripcion: 'Mantenimiento preventivo programado para 8 tiendas de la cadena',
                estado_global: 'NUEVA',
                prioridad: 'BAJA',
                num_areas: 3,
                creada_en: '2025-10-31T07:00:00Z',
                actualizada_en: '2025-10-31T07:00:00Z',
            },
            {
                id: 'ORD-00008',
                titulo: 'Actualización de firmware de cámaras - Oficinas Legales Asociados',
                descripcion: 'Actualización de firmware de seguridad para 12 cámaras',
                estado_global: 'COMPLETADA',
                prioridad: 'BAJA',
                num_areas: 1,
                creada_en: '2025-10-24T10:00:00Z',
                actualizada_en: '2025-10-31T12:00:00Z',
            },
        ];

        // Aplicar filtros
        let filtered = ordenes;

        if (filters.estado_global) {
            filtered = filtered.filter(o => o.estado_global === filters.estado_global);
        }

        if (filters.prioridad) {
            filtered = filtered.filter(o => o.prioridad === filters.prioridad);
        }

        return {
            success: true,
            data: filtered,
            pagination: {
                page: 1,
                limit: 20,
                total: filtered.length,
                pages: 1,
            },
        };
    }

    getMockOrden(id) {
        const orden = {
            id: id,
            titulo: 'Proyecto integral para edificio corporativo',
            descripcion: 'Proyecto completo de seguridad que incluye CCTV, control de accesos y sistema de alarmas para las 12 plantas del edificio.',
            estado_global: 'EN_PROCESO',
            prioridad: 'CRITICA',
            creador: 'Carlos Martínez',
            creada_en: '2025-10-29T10:00:00Z',
            actualizada_en: '2025-10-31T17:00:00Z',
            asignaciones: [
                {
                    id: 1,
                    area: { id: 'AREA-TEC', nombre: 'Área Técnica' },
                    asignada_a: 'Ana López',
                    estado_parcial: 'EN_PROGRESO',
                    seg_acumulados: 86400,
                    fecha_asignacion: '2025-10-29T10:00:00Z',
                },
                {
                    id: 2,
                    area: { id: 'AREA-COM', nombre: 'Área Comercial' },
                    asignada_a: 'Pedro Gómez',
                    estado_parcial: 'COMPLETADA',
                    seg_acumulados: 172800,
                    fecha_asignacion: '2025-10-29T10:05:00Z',
                },
            ],
        };

        return { success: true, data: orden };
    }

    getMockHistorial(ordenId) {
        const historial = [
            {
                id: 1,
                evento: 'Orden creada',
                detalle: 'La orden ha sido creada con prioridad CRÍTICA',
                timestamp: '2025-10-29T10:00:00Z',
                actor: 'Carlos Martínez',
            },
            {
                id: 2,
                evento: 'Área asignada',
                detalle: 'Asignada al Área Técnica para su revisión',
                timestamp: '2025-10-29T10:05:00Z',
                actor: 'Carlos Martínez',
            },
            {
                id: 3,
                evento: 'Estado cambiado',
                detalle: 'El Área Técnica ha iniciado el trabajo',
                timestamp: '2025-10-29T11:00:00Z',
                actor: 'Ana López',
            },
        ];

        return { success: true, data: historial };
    }

    getMockKPIs() {
        return {
            success: true,
            data: {
                ordenes: {
                    total: 8,
                    nuevas: 3,
                    en_proceso: 3,
                    completadas: 1,
                    cerradas_sin_solucion: 0,
                    parcialmente_vencidas: 1,
                    totalmente_vencidas: 0,
                },
            },
        };
    }

    getMockAreas() {
        const areas = [
            { id: 'AREA-TEC', nombre: 'Área Técnica', responsable: 'Ana López' },
            { id: 'AREA-COM', nombre: 'Área Comercial', responsable: 'Pedro Gómez' },
            { id: 'AREA-SOP', nombre: 'Área Soporte', responsable: 'María Rodríguez' },
            { id: 'AREA-CAL', nombre: 'Área Calidad', responsable: 'Carlos Martínez' },
        ];

        return { success: true, data: areas };
    }

    getMockCreatedOrden(data) {
        return {
            success: true,
            data: {
                id: `ORD-${String(Math.floor(Math.random() * 10000)).padStart(5, '0')}`,
                ...data,
                estado_global: 'NUEVA',
                creada_en: new Date().toISOString(),
            },
            message: 'Orden creada exitosamente',
        };
    }
}

// Inicializar cliente API
const apiClient = new APIClient(CONFIG.API.BASE_URL);
const api = new OrdenesAPI(apiClient);

// Exportar para uso global
window.api = api;

log('API Client inicializado');
/**
 * API Client - Módulo de comunicación con el backend
 * Sistema de Gestión de Órdenes
 */

class APIClient {
    constructor(baseURL) {
        this.baseURL = baseURL;
        this.timeout = CONFIG.API.TIMEOUT;
    }

    /**
     * Realizar petición HTTP con manejo de errores
     */
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), this.timeout);

        try {
            log(`API Request: ${options.method || 'GET'} ${endpoint}`);

            const response = await fetch(url, {
                ...options,
                signal: controller.signal,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers,
                },
            });

            clearTimeout(timeoutId);

            if (!response.ok) {
                throw await this.handleErrorResponse(response);
            }

            const data = await response.json();
            log(`API Response:`, data);
            return data;

        } catch (error) {
            clearTimeout(timeoutId);

            if (error.name === 'AbortError') {
                throw new Error(CONFIG.MESSAGES.ERROR.TIMEOUT);
            }

            logError('API Error:', error);
            throw error;
        }
    }

    /**
     * Manejar respuestas de error
     */
    async handleErrorResponse(response) {
        let errorData;
        try {
            errorData = await response.json();
        } catch {
            errorData = { message: response.statusText };
        }

        const error = new Error(errorData.message || 'Error desconocido');
        error.status = response.status;
        error.data = errorData;

        return error;
    }

    /**
     * GET request
     */
    async get(endpoint, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const url = queryString ? `${endpoint}?${queryString}` : endpoint;
        return this.request(url, { method: 'GET' });
    }

    /**
     * POST request
     */
    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data),
        });
    }

    /**
     * PATCH request
     */
    async patch(endpoint, data) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(data),
        });
    }

    /**
     * DELETE request
     */
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

/**
 * API Service - Métodos específicos del sistema
 */
class OrdenesAPI {
    constructor(client) {
        this.client = client;
        this.useMock = CONFIG.FEATURES.ENABLE_MOCK_DATA;
    }

    /**
     * Obtener todas las órdenes con filtros
     */
    async getOrdenes(filters = {}) {
        if (this.useMock) {
            return this.getMockOrdenes(filters);
        }

        try {
            return await this.client.get('/ordenes', filters);
        } catch (error) {
            logError('Error obteniendo órdenes:', error);
            if (CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                log('Usando datos mock como fallback');
                return this.getMockOrdenes(filters);
            }
            throw error;
        }
    }

    /**
     * Obtener una orden por ID
     */
    async getOrden(id) {
        if (this.useMock) {
            return this.getMockOrden(id);
        }

        try {
            return await this.client.get(`/ordenes/${id}`);
        } catch (error) {
            logError(`Error obteniendo orden ${id}:`, error);
            if (CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                return this.getMockOrden(id);
            }
            throw error;
        }
    }

    /**
     * Crear una nueva orden
     */
    async createOrden(data) {
        if (this.useMock) {
            return this.getMockCreatedOrden(data);
        }
        return await this.client.post('/ordenes', data);
    }

    /**
     * Actualizar estado de asignación de área
     */
    async updateAsignacion(ordenId, areaId, data) {
        if (this.useMock) {
            return { success: true, data };
        }
        return await this.client.patch(`/ordenes/${ordenId}/areas/${areaId}`, data);
    }

    /**
     * Obtener historial de una orden
     */
    async getHistorial(ordenId) {
        if (this.useMock) {
            return this.getMockHistorial(ordenId);
        }

        try {
            return await this.client.get(`/ordenes/${ordenId}/historial`);
        } catch (error) {
            logError(`Error obteniendo historial ${ordenId}:`, error);
            if (CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                return this.getMockHistorial(ordenId);
            }
            throw error;
        }
    }

    /**
     * Obtener KPIs
     */
    async getKPIs() {
        if (this.useMock) {
            return this.getMockKPIs();
        }

        try {
            return await this.client.get('/kpis');
        } catch (error) {
            logError('Error obteniendo KPIs:', error);
            if (CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                return this.getMockKPIs();
            }
            throw error;
        }
    }

    /**
     * Ejecutar tick del temporizador
     */
    async executeTick() {
        if (this.useMock) {
            log('Tick ejecutado (mock)');
            return { success: true, message: 'Tick ejecutado' };
        }
        return await this.client.post('/tick', {});
    }

    /**
     * Obtener áreas
     */

