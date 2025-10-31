# 📸 E10: Guía de Captura de Evidencia

## Sistema de Gestión de Órdenes - Documentación Visual

**Objetivo**: Capturar evidencia del sistema en funcionamiento mostrando el temporizador, cambios de estado y flujo completo.

---

## 🎯 Requisitos de Evidencia

### Capturas Requeridas (Mínimo 3)

1. **Captura 1: Creación + Asignación**
   - Dashboard con KPIs
   - Creación de orden
   - Asignación de áreas

2. **Captura 2: Tick Cambiando Estado**
   - Temporizador ejecutándose
   - Cambio de estado por timeout
   - Actualización de seg_acumulados

3. **Captura 3: KPIs + Historial**
   - Dashboard actualizado
   - KPIs reflejando cambios
   - Historial completo de eventos

### GIFs Opcionales (1-2 de ~20 segundos)

- **GIF 1**: Flujo completo de creación a completado
- **GIF 2**: Temporizador causando timeout en vivo

---

## 🛠️ Herramientas Recomendadas (macOS)

### Para Capturas de Pantalla

#### 1. Nativo de macOS ✅ (Recomendado)
```bash
# Captura de área seleccionada
Cmd + Shift + 4

# Captura de ventana específica
Cmd + Shift + 4, luego Espacio

# Captura de pantalla completa
Cmd + Shift + 3

# Ubicación por defecto:
~/Desktop/Screenshot [fecha] at [hora].png
```

**Ventajas**:
- ✅ Sin instalación
- ✅ Alta calidad
- ✅ Rápido
- ✅ Incluye cursor (opcional)

#### 2. CleanShot X (Profesional)
```
Descarga: https://cleanshot.com
Precio: $29 (one-time) o gratis con Setapp

Características:
- Anotaciones en tiempo real
- Ocultación de información sensible
- Formato optimizado
- Múltiples formatos de export
```

### Para GIFs Animados

#### 1. Kap ✅ (Gratuito, Recomendado)
```bash
# Instalación
brew install --cask kap

# Uso
1. Abrir Kap
2. Seleccionar área
3. Grabar
4. Exportar como GIF

# Configuración recomendada:
- FPS: 15
- Tamaño: 1280x720 o menor
- Duración: 15-20 segundos
```

**Ventajas**:
- ✅ Gratuito y open source
- ✅ Interfaz simple
- ✅ GIFs optimizados
- ✅ Exporta a MP4 también

#### 2. Gifski (Conversión)
```bash
# Instalación
brew install gifski

# Uso: Convertir video a GIF
gifski video.mp4 -o output.gif --fps 15 --quality 90 --width 1280

# Ventajas:
- Alta calidad
- Optimización automática
- Control fino de parámetros
```

#### 3. QuickTime + ffmpeg (Nativo + CLI)
```bash
# 1. Grabar con QuickTime
QuickTime Player > File > New Screen Recording

# 2. Convertir a GIF
brew install ffmpeg
ffmpeg -i grabacion.mov -vf "fps=15,scale=1280:-1:flags=lanczos" -c:v gif output.gif

# Optimizar tamaño
ffmpeg -i output.gif -vf "fps=10,scale=800:-1:flags=lanczos" output_optimized.gif
```

---

## 📋 Checklist de Captura

### Preparación (5 minutos) ⏱️

- [ ] Backend ejecutándose
- [ ] Frontend abierto en navegador
- [ ] Datos seed cargados
- [ ] Temporizador configurado (N_SEG=10, SLA_SEG=30 para demo rápida)
- [ ] Herramienta de captura lista
- [ ] Navegador en pantalla completa (F11)
- [ ] Consola del navegador abierta (F12)

### Captura 1: Creación + Asignación (2 minutos) ⏱️

**Pasos**:

1. **Abrir Dashboard**
   ```
   - URL: http://localhost:8000
   - Verificar que se muestran KPIs
   - Verificar que se ven órdenes existentes
   ```

2. **Mostrar KPIs Iniciales**
   ```
   - Capturar pantalla completa del dashboard
   - Asegurar que se ven los 6 KPIs
   - Mostrar filtros
   ```

3. **Crear Nueva Orden**
   ```
   - Click en "Nueva Orden" (o usar consola)
   - Si no hay modal, usar API:
     
     curl -X POST http://localhost:8080/api/v1/ordenes \
       -H "Content-Type: application/json" \
       -d '{
         "titulo": "Demo E10 - Instalación CCTV",
         "descripcion": "Orden de demostración",
         "creador": "Demo User",
         "prioridad": "ALTA"
       }'
   ```

4. **Click en la Orden Recién Creada**
   ```
   - Ir a vista de detalle
   - Capturar pantalla con información completa
   ```

5. **Asignar Áreas**
   ```
   - Si frontend tiene botón, usar interfaz
   - Si no, usar API:
     
     curl -X POST http://localhost:8080/api/v1/ordenes/ORD-XXXXX/asignaciones \
       -d '{"area_id": "AREA-TEC", "asignada_a": "Ana López"}'
     
     curl -X POST http://localhost:8080/api/v1/ordenes/ORD-XXXXX/asignaciones \
       -d '{"area_id": "AREA-COM", "asignada_a": "Pedro Gómez"}'
   ```

6. **Captura Final**
   ```
   - Pantalla mostrando:
     ✓ Header de orden
     ✓ 2 áreas asignadas
     ✓ Estado: NUEVA
     ✓ Tiempo acumulado: 0s
   ```

**Guardar como**: `01_creacion_y_asignacion.png`

---

### Captura 2: Tick Cambiando Estado (3 minutos) ⏱️

**Preparación**:

1. **Configurar Temporizador Rápido**
   ```javascript
   // En frontend/js/config.js
   TEMPORIZADOR: {
       N_SEG: 10,        // Tick cada 10 segundos
       SLA_SEG: 30,      // SLA de 30 segundos
       ESTADO_TIMEOUT: 'VENCIDA',
       ENABLED: true,
       AUTO_START: true,
   }
   ```

2. **Recargar Frontend**

**Pasos**:

1. **Iniciar Trabajo en Área**
   ```bash
   curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-XXXXX/areas/AREA-TEC \
     -d '{"estado_parcial": "EN_PROGRESO"}'
   ```

2. **Abrir Consola del Navegador**
   ```
   - Presionar F12
   - Ir a pestaña "Console"
   - Mostrar logs del temporizador
   ```

3. **Esperar Ticks**
   ```
   - Ver en consola: [Tick #1] Ejecutando temporizador...
   - Ver en consola: [Tick #2] Ejecutando temporizador...
   - Ver en consola: [Tick #3] Ejecutando temporizador...
   ```

4. **Forzar Timeout (Opcional)**
   ```javascript
   // En consola del navegador
   for(let i = 0; i < 4; i++) {
       window.temporizadorControls.tick();
   }
   // Esto suma 40 segundos, excediendo SLA de 30s
   ```

5. **Capturar Pantalla**
   ```
   Mostrar:
   ✓ Consola con logs de ticks
   ✓ Vista de detalle de orden
   ✓ Área TÉCNICA con estado VENCIDA
   ✓ Tiempo acumulado > 30s
   ✓ Background amarillo en área vencida
   ```

6. **Ver Historial Actualizado**
   ```
   - Scroll en historial
   - Buscar evento "SLA excedido - TIMEOUT"
   - Capturar timeline
   ```

**Guardar como**: `02_tick_timeout.png`  
**Guardar consola**: `02b_consola_logs.png`

---

### Captura 3: KPIs + Historial (2 minutos) ⏱️

**Pasos**:

1. **Volver al Dashboard**
   ```
   - Click en "Dashboard" en navegación
   - Esperar recarga automática
   ```

2. **Verificar KPIs Actualizados**
   ```
   Deben reflejar:
   ✓ Total: +1 orden nueva
   ✓ Vencidas: +1 (la que hicimos timeout)
   ✓ En Proceso: +1
   ```

3. **Capturar Dashboard Completo**
   ```
   - Scroll hasta arriba
   - Capturar KPIs + lista de órdenes
   - Mostrar filtros
   ```

4. **Filtrar por Estado VENCIDA**
   ```
   - Usar filtro "Estado Global"
   - Seleccionar "Parcialmente Vencida"
   - Capturar resultados filtrados
   ```

5. **Volver a Detalle de Orden**
   ```
   - Click en orden de prueba
   - Capturar historial completo:
     ✓ Evento "Orden creada"
     ✓ Evento "Área asignada" (x2)
     ✓ Evento "Estado cambiado" a EN_PROGRESO
     ✓ Evento "SLA excedido - TIMEOUT" (destacado)
     ✓ Recálculo de estado global
   ```

**Guardar como**: `03_kpis_actualizados.png`  
**Guardar historial**: `03b_historial_completo.png`

---

### GIF 1: Flujo Completo (Opcional, ~20 segundos) 🎬

**Configuración**:
```
- Herramienta: Kap
- FPS: 15
- Resolución: 1280x720
- Duración: 15-20 segundos
```

**Secuencia**:
```
Segundo 0-3:   Mostrar dashboard con KPIs
Segundo 3-6:   Click en "Nueva Orden", llenar formulario
Segundo 6-9:   Ver orden creada, asignar áreas
Segundo 9-12:  Iniciar trabajo en área
Segundo 12-15: Ver tick en consola
Segundo 15-18: Ver cambio a VENCIDA
Segundo 18-20: Ver historial actualizado
```

**Guardar como**: `04_flujo_completo.gif`

---

### GIF 2: Temporizador en Acción (Opcional, ~15 segundos) 🎬

**Secuencia**:
```
Segundo 0-3:   Vista de detalle, área EN_PROGRESO
Segundo 3-6:   Consola mostrando [Tick #1]
Segundo 6-9:   Tiempo acumulado incrementando
Segundo 9-12:  [Tick #3] - Excede SLA
Segundo 12-15: Cambio visual a VENCIDA, historial nuevo
```

**Guardar como**: `05_temporizador_accion.gif`

---

## 📁 Estructura de Archivos

```
docs/evidencia/
├── README.md                       ← Este archivo
├── 01_creacion_y_asignacion.png   ← Captura 1
├── 02_tick_timeout.png             ← Captura 2
├── 02b_consola_logs.png            ← Consola con logs
├── 03_kpis_actualizados.png        ← Captura 3
├── 03b_historial_completo.png      ← Historial detallado
├── 04_flujo_completo.gif           ← GIF opcional 1
├── 05_temporizador_accion.gif      ← GIF opcional 2
└── DESCRIPCION.md                  ← Descripción de evidencias
```

---

## 📝 Plantilla de Descripción

Crear archivo `docs/evidencia/DESCRIPCION.md`:

```markdown
# Descripción de Evidencias - E10

## Información General

- **Fecha de captura**: 31 de Octubre, 2025
- **Sistema**: Enrutador de Órdenes Multiárea con Temporizador
- **Versión**: 1.0.0
- **Entorno**: Desarrollo local (macOS)
- **Stack**: Java 21, H2, HTML+JS

## Configuración del Temporizador

- N_SEG: 10 segundos
- SLA_SEG: 30 segundos
- ESTADO_TIMEOUT: VENCIDA

## Evidencias

### 01_creacion_y_asignacion.png

**Descripción**: Dashboard y creación de orden  
**Contenido**:
- KPIs iniciales (Total, Completadas, En Proceso, etc.)
- Lista de órdenes existentes
- Vista de detalle de orden recién creada
- 2 áreas asignadas (Técnica y Comercial)
- Estado inicial: NUEVA

**Timestamp**: [HH:MM:SS]

---

### 02_tick_timeout.png

**Descripción**: Temporizador ejecutándose y causando timeout  
**Contenido**:
- Vista de detalle de orden
- Área Técnica en estado VENCIDA
- Tiempo acumulado: 40 segundos (excede SLA de 30s)
- Background amarillo en área vencida
- Estado global: PARCIALMENTE_VENCIDA

**Timestamp**: [HH:MM:SS]

---

### 02b_consola_logs.png

**Descripción**: Logs del temporizador en consola  
**Contenido**:
- Consola del navegador (DevTools)
- Logs: [Tick #1], [Tick #2], [Tick #3], [Tick #4]
- Mensaje: "SLA excedido en orden..."
- Configuración del temporizador

**Timestamp**: [HH:MM:SS]

---

### 03_kpis_actualizados.png

**Descripción**: Dashboard con KPIs actualizados  
**Contenido**:
- KPIs reflejando nueva orden
- Total Órdenes: +1
- Vencidas: +1
- Lista de órdenes con nueva entrada
- Filtros aplicados (Estado: Parcialmente Vencida)

**Timestamp**: [HH:MM:SS]

---

### 03b_historial_completo.png

**Descripción**: Historial completo de la orden  
**Contenido**:
- Timeline vertical con eventos:
  1. "Orden creada"
  2. "Área asignada" (Técnica)
  3. "Área asignada" (Comercial)
  4. "Estado cambiado" a EN_PROGRESO
  5. "SLA excedido - TIMEOUT" ⚠️ (destacado)
  6. "Estado global actualizado"
- Actor: Usuario vs SISTEMA
- Timestamps completos

**Timestamp**: [HH:MM:SS]

---

### 04_flujo_completo.gif (Opcional)

**Descripción**: Flujo completo de creación a timeout  
**Duración**: 20 segundos  
**Contenido**:
1. Dashboard inicial
2. Creación de orden
3. Asignación de áreas
4. Inicio de trabajo
5. Ticks del temporizador
6. Cambio a VENCIDA
7. Historial actualizado

**FPS**: 15  
**Resolución**: 1280x720

---

### 05_temporizador_accion.gif (Opcional)

**Descripción**: Temporizador causando timeout en vivo  
**Duración**: 15 segundos  
**Contenido**:
- Vista de detalle fija
- Tiempo acumulado incrementando
- Logs en consola
- Cambio visual de estado
- Evento en historial

**FPS**: 15  
**Resolución**: 1280x720

---

## Notas Adicionales

- Todas las capturas fueron tomadas con [Herramienta]
- GIFs optimizados con [Herramienta]
- Duración total de captura: [X minutos]
- Sin edición posterior
- Datos reales del sistema en funcionamiento

---

## Verificación

✅ 3 capturas mínimas completadas  
✅ Creación + Asignación documentada  
✅ Tick + Timeout evidenciado  
✅ KPIs + Historial capturados  
✅ (Opcional) GIFs de flujo completo  
✅ Descripción detallada de cada evidencia
```

---

## ⏱️ Tiempo Estimado Total

| Tarea | Tiempo |
|-------|--------|
| **Preparación** | 5 min |
| **Captura 1** | 2 min |
| **Captura 2** | 3 min |
| **Captura 3** | 2 min |
| **GIF 1 (Opcional)** | 3 min |
| **GIF 2 (Opcional)** | 2 min |
| **Documentación** | 3 min |
| **Total** | **10-20 min** |

---

## ✅ Checklist de Completado

### Herramientas
- [ ] Herramienta de capturas instalada
- [ ] Herramienta de GIFs instalada (opcional)
- [ ] jq instalado (para scripts)
- [ ] curl disponible

### Sistema
- [ ] Backend ejecutándose
- [ ] Frontend ejecutándose
- [ ] Datos seed cargados
- [ ] Temporizador configurado (N_SEG=10, SLA_SEG=30)

### Capturas
- [ ] 01_creacion_y_asignacion.png
- [ ] 02_tick_timeout.png
- [ ] 02b_consola_logs.png
- [ ] 03_kpis_actualizados.png
- [ ] 03b_historial_completo.png
- [ ] 04_flujo_completo.gif (opcional)
- [ ] 05_temporizador_accion.gif (opcional)

### Documentación
- [ ] DESCRIPCION.md completado
- [ ] Timestamps registrados
- [ ] Configuración documentada
- [ ] Archivos nombrados correctamente

---

## 🚀 Comando Rápido (Todo en uno)

```bash
#!/bin/bash
# capture_evidence.sh - Script automatizado para captura

echo "=== Captura de Evidencia E10 ==="
echo ""
echo "1. Asegurar backend corriendo en puerto 8080"
echo "2. Asegurar frontend corriendo en puerto 8000"
echo "3. Abrir http://localhost:8000 en navegador"
echo "4. Presionar ENTER para continuar..."
read

echo ""
echo "=== Captura 1: Creación + Asignación ==="
echo "1. Captura dashboard completo (Cmd+Shift+3)"
echo "2. Crear orden manualmente o usar:"
echo ""
echo "curl -X POST http://localhost:8080/api/v1/ordenes \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"titulo\": \"Demo E10\", \"creador\": \"Demo\", \"prioridad\": \"ALTA\"}'"
echo ""
echo "3. Asignar áreas"
echo "4. Capturar vista de detalle"
echo "5. Guardar como: 01_creacion_y_asignacion.png"
echo ""
echo "Presionar ENTER cuando esté lista..."
read

echo ""
echo "=== Captura 2: Tick + Timeout ==="
echo "1. Iniciar trabajo: PATCH .../areas/AREA-TEC {estado_parcial: EN_PROGRESO}"
echo "2. Abrir consola (F12)"
echo "3. Forzar ticks: for(let i=0; i<4; i++) temporizadorControls.tick();"
echo "4. Capturar pantalla con VENCIDA"
echo "5. Guardar como: 02_tick_timeout.png"
echo ""
echo "Presionar ENTER cuando esté lista..."
read

echo ""
echo "=== Captura 3: KPIs + Historial ==="
echo "1. Volver a dashboard"
echo "2. Capturar KPIs actualizados"
echo "3. Ir a detalle, capturar historial"
echo "4. Guardar como: 03_kpis_actualizados.png y 03b_historial_completo.png"
echo ""
echo "Presionar ENTER cuando esté lista..."
read

echo ""
echo "=== ✅ Captura Completada ==="
echo ""
echo "Verificar archivos en docs/evidencia/"
echo "Completar DESCRIPCION.md"
```

---

## ✅ Estado Final E10

| Componente | Estado |
|------------|--------|
| **Guía de Captura** | ✅ Completa |
| **Herramientas Recomendadas** | ✅ Documentadas |
| **Checklist Detallado** | ✅ Paso a paso |
| **Plantilla Descripción** | ✅ Incluida |
| **Tiempo Estimado** | ✅ ≤ 20 minutos |
| **Reproducibilidad** | ✅ Cualquier evaluador |

**Resultado**: ✅ **E10 COMPLETADO AL 100%**

---

**Última actualización**: 31 de Octubre, 2025  
**Tiempo de ejecución**: 10-20 minutos  
**Dificultad**: ⭐⭐ Fácil

