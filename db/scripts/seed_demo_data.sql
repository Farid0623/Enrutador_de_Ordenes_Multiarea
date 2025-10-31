-- ============================================
-- Script: Datos de Demostración
-- Sistema de Gestión de Órdenes
-- ============================================
-- Descripción: Inserta usuarios y áreas de ejemplo
-- Uso: Ejecutar después de V001__initial_schema.sql
-- ============================================

-- ============================================
-- LIMPIAR DATOS EXISTENTES (Opcional)
-- ============================================
-- DELETE FROM historial_cambios;
-- DELETE FROM asignaciones_area;
-- DELETE FROM ordenes;
-- DELETE FROM usuario_area;
-- DELETE FROM areas;
-- DELETE FROM usuarios;

-- ============================================
-- INSERTAR USUARIOS
-- ============================================

-- Despachadores/Operadores
INSERT INTO usuarios (id, nombre, rol) VALUES
    ('U001', 'Carlos Martínez', 'DESPACHADOR_OPERADOR'),
    ('U002', 'Laura Sánchez', 'DESPACHADOR_OPERADOR');

-- Agentes de Área
INSERT INTO usuarios (id, nombre, rol) VALUES
    ('U003', 'Ana López', 'AGENTE_AREA'),
    ('U004', 'Pedro Gómez', 'AGENTE_AREA'),
    ('U005', 'María Rodríguez', 'AGENTE_AREA'),
    ('U006', 'Luis Fernández', 'AGENTE_AREA'),
    ('U007', 'Carmen Torres', 'AGENTE_AREA'),
    ('U008', 'José Ramírez', 'AGENTE_AREA');

-- Administradores
INSERT INTO usuarios (id, nombre, rol) VALUES
    ('U009', 'Laura Admin', 'ADMINISTRADOR'),
    ('U010', 'Roberto Admin', 'ADMINISTRADOR');

-- ============================================
-- INSERTAR ÁREAS
-- ============================================

INSERT INTO areas (id, nombre, descripcion) VALUES
    ('A001', 'Área Técnica', 'Departamento técnico especializado'),
    ('A002', 'Área Comercial', 'Departamento de ventas y atención al cliente'),
    ('A003', 'Área Soporte', 'Soporte técnico y mantenimiento'),
    ('A004', 'Área Calidad', 'Control de calidad y auditoría'),
    ('A005', 'Área Logística', 'Gestión de entregas y almacén');

-- ============================================
-- ASIGNAR AGENTES A ÁREAS
-- ============================================

-- Área Técnica
INSERT INTO usuario_area (usuario_id, area_id) VALUES
    ('U003', 'A001'),  -- Ana López
    ('U004', 'A001');  -- Pedro Gómez

-- Área Comercial
INSERT INTO usuario_area (usuario_id, area_id) VALUES
    ('U005', 'A002'),  -- María Rodríguez
    ('U006', 'A002');  -- Luis Fernández

-- Área Soporte
INSERT INTO usuario_area (usuario_id, area_id) VALUES
    ('U007', 'A003'),  -- Carmen Torres
    ('U003', 'A003');  -- Ana López (también en soporte)

-- Área Calidad
INSERT INTO usuario_area (usuario_id, area_id) VALUES
    ('U008', 'A004'),  -- José Ramírez
    ('U005', 'A004');  -- María Rodríguez (también en calidad)

-- Área Logística
INSERT INTO usuario_area (usuario_id, area_id) VALUES
    ('U006', 'A005');  -- Luis Fernández

-- ============================================
-- INSERTAR ÓRDENES DE EJEMPLO (Opcional)
-- ============================================

-- Orden 1: Instalación cliente VIP
INSERT INTO ordenes (id, titulo, descripcion, creador_id, estado_global) VALUES
    ('ORD-00001',
     'Instalación sistema de seguridad - Cliente VIP',
     'Cliente corporativo requiere instalación completa de sistema de seguridad en nueva sede',
     'U001',
     'NUEVA');

-- Asignaciones para Orden 1
INSERT INTO asignaciones_area (orden_id, area_id, estado_parcial) VALUES
    ('ORD-00001', 'A001', 'ASIGNADA'),  -- Área Técnica
    ('ORD-00001', 'A003', 'ASIGNADA');  -- Área Soporte

-- Historial inicial
INSERT INTO historial_cambios (orden_id, descripcion, usuario_id, detalle_nuevo) VALUES
    ('ORD-00001', 'Orden creada', 'U001', 'NUEVA');

-- Orden 2: Mantenimiento preventivo
INSERT INTO ordenes (id, titulo, descripcion, creador_id, estado_global) VALUES
    ('ORD-00002',
     'Mantenimiento preventivo mensual',
     'Revisión y mantenimiento preventivo de equipos instalados',
     'U001',
     'NUEVA');

-- Asignaciones para Orden 2
INSERT INTO asignaciones_area (orden_id, area_id, estado_parcial) VALUES
    ('ORD-00002', 'A001', 'ASIGNADA'),  -- Área Técnica
    ('ORD-00002', 'A004', 'ASIGNADA');  -- Área Calidad

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Contar registros insertados
SELECT 'Usuarios' as Tabla, COUNT(*) as Total FROM usuarios
UNION ALL
SELECT 'Áreas', COUNT(*) FROM areas
UNION ALL
SELECT 'Asignaciones Usuario-Área', COUNT(*) FROM usuario_area
UNION ALL
SELECT 'Órdenes', COUNT(*) FROM ordenes
UNION ALL
SELECT 'Asignaciones de Área', COUNT(*) FROM asignaciones_area
UNION ALL
SELECT 'Historial', COUNT(*) FROM historial_cambios;

-- ============================================
-- FIN DEL SCRIPT DE DATOS DE DEMO
-- ============================================

