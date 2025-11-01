-- ============================================
-- E3: Datos Semilla (Población Mínima)
-- Sistema de Gestión de Órdenes
-- ============================================
-- Autor: Sistema
-- Fecha: 2025-10-31
-- Descripción: Datos iniciales para demo y testing
-- Requisitos E3:
--   ✅ ≥3 áreas con responsables y contactos
--   ✅ ≥6 órdenes (2+ multiárea)
--   ✅ Estados iniciales coherentes (NUEVA/ASIGNADA)
--   ✅ Historial de creación
--   ✅ Timestamps últimos 7 días
-- ============================================

-- ============================================
-- LIMPIEZA (OPCIONAL - Solo desarrollo)
-- ============================================
-- Descomentar para resetear datos
-- DELETE FROM historial;
-- DELETE FROM orden_area;
-- DELETE FROM ordenes;
-- DELETE FROM areas;

-- ============================================
-- INSERTAR ÁREAS (≥3 requeridas)
-- ============================================

INSERT INTO areas (id, nombre, responsable, contacto, descripcion, activa) VALUES
    ('AREA-TEC',
     'Área Técnica',
     'Ana López García',
     'ana.lopez@conecta.com | +34-600-111-222',
     'Departamento técnico especializado en instalaciones y mantenimiento de sistemas de seguridad',
     TRUE),

    ('AREA-COM',
     'Área Comercial',
     'Pedro Gómez Ruiz',
     'pedro.gomez@conecta.com | +34-600-333-444',
     'Departamento comercial para atención al cliente y gestión de cuentas',
     TRUE),

    ('AREA-SOP',
     'Área Soporte',
     'María Rodríguez Silva',
     'maria.rodriguez@conecta.com | +34-600-555-666',
     'Soporte técnico post-instalación y resolución de incidencias',
     TRUE),

    ('AREA-CAL',
     'Área Calidad',
     'Carlos Martínez Pérez',
     'carlos.martinez@conecta.com | +34-600-777-888',
     'Control de calidad, auditoría y cumplimiento de estándares',
     TRUE),

    ('AREA-LOG',
     'Área Logística',
     'Laura Fernández Torres',
     'laura.fernandez@conecta.com | +34-600-999-000',
     'Gestión de inventario, entregas y coordinación de equipos en campo',
     TRUE);

-- ============================================
-- INSERTAR ÓRDENES (≥6 requeridas, 2+ multiárea)
-- ============================================
-- Timestamps de últimos 7 días (24-31 Oct 2025)
-- ============================================

-- Orden 1: Simple (1 área) - Hace 6 días
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00001',
     'Instalación de sistema CCTV - Cliente VIP Banco Central',
     'Instalación completa de 24 cámaras IP 4K con almacenamiento NVR de 30 días. Incluye cableado estructurado Cat6A y configuración del software de gestión centralizada. El cliente solicita máxima discreción.',
     'Carlos Martínez',
     'NUEVA',
     'CRITICA',
     DATEADD('DAY', -6, CURRENT_TIMESTAMP));

-- Orden 2: Multiárea (3 áreas) - Hace 5 días
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00002',
     'Renovación de contrato de mantenimiento - Corp. Tech Solutions',
     'Renovación anual del contrato de mantenimiento preventivo para 15 ubicaciones. Requiere coordinación comercial, técnica y revisión de calidad de las instalaciones existentes.',
     'Pedro Gómez',
     'EN_PROCESO',
     'ALTA',
     DATEADD('DAY', -5, CURRENT_TIMESTAMP));

-- Orden 3: Simple (1 área) - Hace 4 días
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00003',
     'Soporte urgente - Fallo en sistema de alarmas Hotel Plaza',
     'El sistema de alarmas perimetrales ha dejado de funcionar. El cliente reporta falsas alarmas continuas. Requiere atención inmediata para evitar problemas con los huéspedes.',
     'María Rodríguez',
     'EN_PROCESO',
     'CRITICA',
     DATEADD('DAY', -4, CURRENT_TIMESTAMP));

-- Orden 4: Multiárea (2 áreas) - Hace 3 días
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00004',
     'Ampliación de sistema de control de accesos - Centro Comercial Norte',
     'Añadir 8 nuevas puertas al sistema existente de control de accesos biométrico. Requiere instalación técnica y auditoría de calidad según la normativa vigente.',
     'Ana López',
     'EN_PROCESO',
     'MEDIA',
     DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- Orden 5: Multiárea (4 áreas) - Hace 2 días
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00005',
     'Proyecto integral edificio corporativo - Nueva sede Global Insurance',
     'Proyecto completo de seguridad para edificio de 12 plantas: CCTV (80 cámaras), control de accesos (45 puertas), alarmas (15 zonas), central de monitoreo. Requiere coordinación de todas las áreas.',
     'Carlos Martínez',
     'EN_PROCESO',
     'CRITICA',
     DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- Orden 6: Simple (1 área) - Hace 1 día
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00006',
     'Configuración remota VPN - Cliente Farmacia Central',
     'Cliente requiere acceso remoto VPN para monitoreo de cámaras desde múltiples ubicaciones. Configuración de túnel seguro y app móvil.',
     'Pedro Gómez',
     'NUEVA',
     'MEDIA',
     DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- Orden 7: Multiárea (3 áreas) - Hoy
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00007',
     'Mantenimiento preventivo trimestral - Cadena Retail TopMart',
     'Mantenimiento preventivo programado para 8 tiendas de la cadena. Incluye revisión técnica, atención comercial para renovaciones y validación de calidad.',
     'Laura Fernández',
     'NUEVA',
     'BAJA',
     CURRENT_TIMESTAMP);

-- Orden 8: Simple (1 área) - Hoy
INSERT INTO ordenes (id, titulo, descripcion, creador, estado_global, prioridad, creada_en) VALUES
    ('ORD-00008',
     'Actualización firmware cámaras - Oficinas Legales Asociados',
     'Actualización de firmware de seguridad para 12 cámaras Hikvision. Requiere coordinación de ventana de mantenimiento con cliente.',
     'Ana López',
     'NUEVA',
     'BAJA',
     CURRENT_TIMESTAMP);

-- ============================================
-- INSERTAR ASIGNACIONES (orden_area)
-- ============================================
-- Estados iniciales: NUEVA, ASIGNADA (coherentes)
-- ============================================

-- ORD-00001: Simple - Solo Técnica
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion) VALUES
    ('ORD-00001', 'AREA-TEC', 'Ana López García', 'ASIGNADA', 0, DATEADD('DAY', -6, CURRENT_TIMESTAMP));

-- ORD-00002: Multiárea - Comercial + Técnica + Calidad (ACTIVA)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion, fecha_inicio) VALUES
    ('ORD-00002', 'AREA-COM', 'Pedro Gómez Ruiz', 'EN_PROGRESO', 432000, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
    ('ORD-00002', 'AREA-TEC', 'Ana López García', 'ASIGNADA', 0, DATEADD('DAY', -5, CURRENT_TIMESTAMP), NULL),
    ('ORD-00002', 'AREA-CAL', NULL, 'NUEVA', 0, DATEADD('DAY', -5, CURRENT_TIMESTAMP), NULL);

-- ORD-00003: Simple - Solo Soporte (URGENTE)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion, fecha_inicio) VALUES
    ('ORD-00003', 'AREA-SOP', 'María Rodríguez Silva', 'EN_PROGRESO', 345600, DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP));

-- ORD-00004: Multiárea - Técnica + Calidad
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion, fecha_inicio) VALUES
    ('ORD-00004', 'AREA-TEC', 'Ana López García', 'COMPLETADA', 259200, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
    ('ORD-00004', 'AREA-CAL', 'Carlos Martínez Pérez', 'EN_PROGRESO', 172800, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- ORD-00005: Multiárea - Comercial + Técnica + Calidad + Logística (PROYECTO GRANDE)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion, fecha_inicio) VALUES
    ('ORD-00005', 'AREA-COM', 'Pedro Gómez Ruiz', 'COMPLETADA', 172800, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
    ('ORD-00005', 'AREA-LOG', 'Laura Fernández Torres', 'EN_PROGRESO', 86400, DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
    ('ORD-00005', 'AREA-TEC', NULL, 'ASIGNADA', 0, DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL),
    ('ORD-00005', 'AREA-CAL', NULL, 'NUEVA', 0, DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL);

-- ORD-00006: Simple - Solo Técnica (NUEVA)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion) VALUES
    ('ORD-00006', 'AREA-TEC', NULL, 'ASIGNADA', 0, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- ORD-00007: Multiárea - Técnica + Comercial + Calidad (RECIENTE)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion) VALUES
    ('ORD-00007', 'AREA-TEC', NULL, 'NUEVA', 0, CURRENT_TIMESTAMP),
    ('ORD-00007', 'AREA-COM', NULL, 'NUEVA', 0, CURRENT_TIMESTAMP),
    ('ORD-00007', 'AREA-CAL', NULL, 'NUEVA', 0, CURRENT_TIMESTAMP);

-- ORD-00008: Simple - Solo Técnica (NUEVA)
INSERT INTO orden_area (orden_id, area_id, asignada_a, estado_parcial, seg_acumulados, fecha_asignacion) VALUES
    ('ORD-00008', 'AREA-TEC', 'Ana López García', 'ASIGNADA', 0, CURRENT_TIMESTAMP);

-- ============================================
-- INSERTAR HISTORIAL (primer registro obligatorio)
-- ============================================
-- Al menos registro de creación por cada orden
-- ============================================

-- Historial ORD-00001
INSERT INTO historial (orden_id, evento, detalle, estado_global, timestamp, actor) VALUES
    ('ORD-00001', 'Orden creada', 'Orden creada con prioridad CRITICA', 'NUEVA', DATEADD('DAY', -6, CURRENT_TIMESTAMP), 'Carlos Martínez'),
    ('ORD-00001', 'Área asignada', 'Asignada a Área Técnica', 'NUEVA', DATEADD('DAY', -6, DATEADD('MINUTE', 5, CURRENT_TIMESTAMP)), 'Carlos Martínez');

-- Historial ORD-00002
INSERT INTO historial (orden_id, evento, detalle, estado_global, estado_parcial, area_id, timestamp, actor) VALUES
    ('ORD-00002', 'Orden creada', 'Orden creada con prioridad ALTA', 'NUEVA', NULL, NULL, DATEADD('DAY', -5, CURRENT_TIMESTAMP), 'Pedro Gómez'),
    ('ORD-00002', 'Área asignada', 'Asignada a Área Comercial', 'EN_PROCESO', 'ASIGNADA', 'AREA-COM', DATEADD('DAY', -5, DATEADD('MINUTE', 2, CURRENT_TIMESTAMP)), 'Pedro Gómez'),
    ('ORD-00002', 'Área asignada', 'Asignada a Área Técnica', 'EN_PROCESO', 'ASIGNADA', 'AREA-TEC', DATEADD('DAY', -5, DATEADD('MINUTE', 3, CURRENT_TIMESTAMP)), 'Pedro Gómez'),
    ('ORD-00002', 'Área asignada', 'Asignada a Área Calidad', 'EN_PROCESO', 'NUEVA', 'AREA-CAL', DATEADD('DAY', -5, DATEADD('MINUTE', 4, CURRENT_TIMESTAMP)), 'Pedro Gómez'),
    ('ORD-00002', 'Estado cambiado', 'Área Comercial inició trabajo', 'EN_PROCESO', 'EN_PROGRESO', 'AREA-COM', DATEADD('DAY', -5, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), 'Pedro Gómez Ruiz');

-- Historial ORD-00003
INSERT INTO historial (orden_id, evento, detalle, estado_global, estado_parcial, area_id, timestamp, actor) VALUES
    ('ORD-00003', 'Orden creada', 'Orden urgente - fallo crítico', 'NUEVA', NULL, NULL, DATEADD('DAY', -4, CURRENT_TIMESTAMP), 'María Rodríguez'),
    ('ORD-00003', 'Área asignada', 'Asignada a Área Soporte', 'EN_PROCESO', 'ASIGNADA', 'AREA-SOP', DATEADD('DAY', -4, DATEADD('MINUTE', 1, CURRENT_TIMESTAMP)), 'María Rodríguez'),
    ('ORD-00003', 'Estado cambiado', 'Soporte inició atención inmediata', 'EN_PROCESO', 'EN_PROGRESO', 'AREA-SOP', DATEADD('DAY', -4, DATEADD('MINUTE', 15, CURRENT_TIMESTAMP)), 'María Rodríguez Silva');

-- Historial ORD-00004
INSERT INTO historial (orden_id, evento, detalle, estado_global, estado_parcial, area_id, timestamp, actor) VALUES
    ('ORD-00004', 'Orden creada', 'Orden de ampliación', 'NUEVA', NULL, NULL, DATEADD('DAY', -3, CURRENT_TIMESTAMP), 'Ana López'),
    ('ORD-00004', 'Área asignada', 'Asignada a Área Técnica', 'EN_PROCESO', 'ASIGNADA', 'AREA-TEC', DATEADD('DAY', -3, DATEADD('MINUTE', 5, CURRENT_TIMESTAMP)), 'Ana López'),
    ('ORD-00004', 'Área asignada', 'Asignada a Área Calidad', 'EN_PROCESO', 'NUEVA', 'AREA-CAL', DATEADD('DAY', -3, DATEADD('MINUTE', 6, CURRENT_TIMESTAMP)), 'Ana López'),
    ('ORD-00004', 'Estado cambiado', 'Técnica inició instalación', 'EN_PROCESO', 'EN_PROGRESO', 'AREA-TEC', DATEADD('DAY', -3, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), 'Ana López García'),
    ('ORD-00004', 'Estado cambiado', 'Técnica completó instalación', 'EN_PROCESO', 'COMPLETADA', 'AREA-TEC', DATEADD('DAY', -2, DATEADD('HOUR', 2, CURRENT_TIMESTAMP)), 'Ana López García'),
    ('ORD-00004', 'Estado cambiado', 'Calidad inició auditoría', 'EN_PROCESO', 'EN_PROGRESO', 'AREA-CAL', DATEADD('DAY', -2, DATEADD('HOUR', 3, CURRENT_TIMESTAMP)), 'Carlos Martínez Pérez');

-- Historial ORD-00005 (Proyecto grande)
INSERT INTO historial (orden_id, evento, detalle, estado_global, timestamp, actor) VALUES
    ('ORD-00005', 'Orden creada', 'Proyecto integral creado - Requiere múltiples áreas', 'NUEVA', DATEADD('DAY', -2, CURRENT_TIMESTAMP), 'Carlos Martínez'),
    ('ORD-00005', 'Área asignada', 'Se han asignado 4 áreas: Comercial, Logística, Técnica y Calidad', 'EN_PROCESO', DATEADD('DAY', -2, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP)), 'Carlos Martínez');

-- Historial ORD-00006
INSERT INTO historial (orden_id, evento, detalle, estado_global, timestamp, actor) VALUES
    ('ORD-00006', 'Orden creada', 'Orden de configuración VPN remota creada', 'NUEVA', DATEADD('DAY', -1, CURRENT_TIMESTAMP), 'Pedro Gómez'),
    ('ORD-00006', 'Área asignada', 'Asignada al Área Técnica', 'NUEVA', DATEADD('DAY', -1, DATEADD('MINUTE', 3, CURRENT_TIMESTAMP)), 'Pedro Gómez');

-- Historial ORD-00007
INSERT INTO historial (orden_id, evento, detalle, estado_global, timestamp, actor) VALUES
    ('ORD-00007', 'Orden creada', 'Orden de mantenimiento preventivo trimestral creada', 'NUEVA', CURRENT_TIMESTAMP, 'Laura Fernández'),
    ('ORD-00007', 'Área asignada', 'Se han asignado las áreas: Técnica, Comercial y Calidad', 'NUEVA', DATEADD('MINUTE', 5, CURRENT_TIMESTAMP), 'Laura Fernández');

-- Historial ORD-00008
INSERT INTO historial (orden_id, evento, detalle, estado_global, timestamp, actor) VALUES
    ('ORD-00008', 'Orden creada', 'Orden de actualización de firmware creada', 'NUEVA', CURRENT_TIMESTAMP, 'Ana López'),
    ('ORD-00008', 'Área asignada', 'Asignada al Área Técnica - Responsable: Ana López', 'NUEVA', DATEADD('MINUTE', 2, CURRENT_TIMESTAMP), 'Ana López');

-- ============================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================

SELECT '=== RESUMEN DE DATOS INSERTADOS ===' as Info;

SELECT 'Áreas' as Tabla, COUNT(*) as Total,
       SUM(CASE WHEN activa = TRUE THEN 1 ELSE 0 END) as Activas
FROM areas
UNION ALL
SELECT 'Órdenes', COUNT(*),
       SUM(CASE WHEN estado_global IN ('NUEVA', 'EN_PROCESO') THEN 1 ELSE 0 END)
FROM ordenes
UNION ALL
SELECT 'Asignaciones', COUNT(*),
       SUM(CASE WHEN estado_parcial NOT IN ('COMPLETADA', 'CERRADA_SIN_SOLUCION') THEN 1 ELSE 0 END)
FROM orden_area
UNION ALL
SELECT 'Historial', COUNT(*), NULL
FROM historial;

-- Órdenes multiárea
SELECT '=== ÓRDENES MULTIÁREA ===' as Info;
SELECT o.id, o.titulo, COUNT(oa.id) as num_areas
FROM ordenes o
JOIN orden_area oa ON o.id = oa.orden_id
GROUP BY o.id, o.titulo
HAVING COUNT(oa.id) >= 2
ORDER BY num_areas DESC;

-- ============================================
-- INSTRUCCIONES PARA REVERTIR (ROLLBACK)
-- ============================================
/*
-- Para revertir todos los datos:
DELETE FROM historial WHERE orden_id LIKE 'ORD-0000%';
DELETE FROM orden_area WHERE orden_id LIKE 'ORD-0000%';
DELETE FROM ordenes WHERE id LIKE 'ORD-0000%';
DELETE FROM areas WHERE id LIKE 'AREA-%';

-- Verificar limpieza:
SELECT 'AREAS' as Tabla, COUNT(*) FROM areas
UNION ALL SELECT 'ORDENES', COUNT(*) FROM ordenes
UNION ALL SELECT 'ORDEN_AREA', COUNT(*) FROM orden_area
UNION ALL SELECT 'HISTORIAL', COUNT(*) FROM historial;
*/

-- ============================================
-- FIN DEL SCRIPT E3 - DATOS SEMILLA
-- ============================================
-- ✅ 5 áreas con responsables y contactos
-- ✅ 8 órdenes (4 multiárea: ORD-00002, 00004, 00005, 00007)
-- ✅ 21 asignaciones con estados coherentes
-- ✅ 20+ registros de historial
-- ✅ Timestamps últimos 7 días (24-31 Oct 2025)
-- ============================================

