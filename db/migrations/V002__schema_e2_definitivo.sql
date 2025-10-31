-- ============================================
-- Migración V002: Schema E2 - Modelo Definitivo
-- Sistema de Gestión de Órdenes
-- ============================================
-- Autor: Sistema
-- Fecha: 2025-10-31
-- Versión: 2.0.0
-- Descripción: Modelo de datos para E2/E3/E4
-- Entidades: areas, ordenes, orden_area, historial
-- ============================================

-- ============================================
-- TABLA: AREAS
-- ============================================
-- Descripción: Áreas de trabajo del sistema
-- Relaciones: 1:N con orden_area
-- ============================================

CREATE TABLE IF NOT EXISTS areas (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    responsable VARCHAR(255) NOT NULL,
    contacto VARCHAR(255),
    descripcion TEXT,
    activa BOOLEAN DEFAULT TRUE NOT NULL,
    creada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    actualizada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Índices para areas
CREATE INDEX IF NOT EXISTS idx_areas_activa ON areas(activa);
CREATE INDEX IF NOT EXISTS idx_areas_responsable ON areas(responsable);
CREATE INDEX IF NOT EXISTS idx_areas_nombre ON areas(nombre);

-- ============================================
-- TABLA: ORDENES
-- ============================================
-- Descripción: Órdenes de trabajo principales
-- Relaciones: 1:N con orden_area, 1:N con historial
-- ============================================

CREATE TABLE IF NOT EXISTS ordenes (
    id VARCHAR(50) PRIMARY KEY,
    titulo VARCHAR(500) NOT NULL,
    descripcion TEXT,
    creador VARCHAR(255) NOT NULL,
    estado_global VARCHAR(50) NOT NULL
        CHECK (estado_global IN (
            'NUEVA', 'EN_PROCESO', 'COMPLETADA',
            'CERRADA_SIN_SOLUCION', 'PARCIALMENTE_VENCIDA',
            'TOTALMENTE_VENCIDA'
        )),
    prioridad VARCHAR(20) DEFAULT 'MEDIA' NOT NULL
        CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA')),
    creada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    actualizada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    cerrada_en TIMESTAMP
);

-- Índices para ordenes
CREATE INDEX IF NOT EXISTS idx_ordenes_estado_global ON ordenes(estado_global);
CREATE INDEX IF NOT EXISTS idx_ordenes_creador ON ordenes(creador);
CREATE INDEX IF NOT EXISTS idx_ordenes_creada_en ON ordenes(creada_en);
CREATE INDEX IF NOT EXISTS idx_ordenes_prioridad ON ordenes(prioridad);
CREATE INDEX IF NOT EXISTS idx_ordenes_estado_fecha ON ordenes(estado_global, creada_en);

-- ============================================
-- TABLA: ORDEN_AREA
-- ============================================
-- Descripción: Asignación de órdenes a áreas (relación N:M)
-- Relaciones: N:1 con ordenes, N:1 con areas
-- ============================================

CREATE TABLE IF NOT EXISTS orden_area (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_id VARCHAR(50) NOT NULL,
    area_id VARCHAR(50) NOT NULL,
    asignada_a VARCHAR(255),
    estado_parcial VARCHAR(50) NOT NULL
        CHECK (estado_parcial IN (
            'NUEVA', 'ASIGNADA', 'EN_PROGRESO',
            'PENDIENTE', 'COMPLETADA',
            'CERRADA_SIN_SOLUCION', 'VENCIDA'
        )),
    seg_acumulados BIGINT DEFAULT 0 NOT NULL CHECK (seg_acumulados >= 0),
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_inicio TIMESTAMP,
    fecha_completada TIMESTAMP,
    actualizada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE,
    UNIQUE (orden_id, area_id)
);

-- Índices para orden_area
CREATE INDEX IF NOT EXISTS idx_orden_area_orden ON orden_area(orden_id);
CREATE INDEX IF NOT EXISTS idx_orden_area_area ON orden_area(area_id);
CREATE INDEX IF NOT EXISTS idx_orden_area_estado ON orden_area(estado_parcial);
CREATE INDEX IF NOT EXISTS idx_orden_area_asignada_a ON orden_area(asignada_a);
CREATE INDEX IF NOT EXISTS idx_orden_area_activa ON orden_area(estado_parcial, seg_acumulados);

-- ============================================
-- TABLA: HISTORIAL
-- ============================================
-- Descripción: Auditoría de cambios en órdenes
-- Relaciones: N:1 con ordenes
-- ============================================

CREATE TABLE IF NOT EXISTS historial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_id VARCHAR(50) NOT NULL,
    evento VARCHAR(500) NOT NULL,
    detalle TEXT,
    estado_global VARCHAR(50),
    estado_parcial VARCHAR(50),
    area_id VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    actor VARCHAR(255),

    -- Constraints
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE
);

-- Índices para historial
CREATE INDEX IF NOT EXISTS idx_historial_orden ON historial(orden_id);
CREATE INDEX IF NOT EXISTS idx_historial_timestamp ON historial(timestamp);
CREATE INDEX IF NOT EXISTS idx_historial_actor ON historial(actor);
CREATE INDEX IF NOT EXISTS idx_historial_orden_timestamp ON historial(orden_id, timestamp);

-- ============================================
-- COMENTARIOS DE DOCUMENTACIÓN
-- ============================================

-- Comentarios en H2 (opcional, algunos dialectos no soportan)
-- COMMENT ON TABLE areas IS 'Áreas de trabajo del sistema';
-- COMMENT ON TABLE ordenes IS 'Órdenes de trabajo principales';
-- COMMENT ON TABLE orden_area IS 'Asignación de órdenes a áreas (multiárea)';
-- COMMENT ON TABLE historial IS 'Auditoría de todos los cambios en órdenes';

-- ============================================
-- ESTADÍSTICAS Y VERIFICACIÓN
-- ============================================

-- Verificar creación de tablas
SELECT 'AREAS' as Tabla, COUNT(*) as Total FROM areas
UNION ALL
SELECT 'ORDENES', COUNT(*) FROM ordenes
UNION ALL
SELECT 'ORDEN_AREA', COUNT(*) FROM orden_area
UNION ALL
SELECT 'HISTORIAL', COUNT(*) FROM historial;

-- ============================================
-- FIN DE MIGRACIÓN V002
-- ============================================
-- Próximo paso: Ejecutar script de seed (V003 o seed_data.sql)
-- ============================================

