# Guía de Uso del Sistema de Gestión de Órdenes

## 🎯 Casos de Uso Principales

### 1. Crear y Gestionar una Orden Completa

#### Paso 1: Crear la Orden (Despachador)
```
Usuario: Carlos Martínez (Despachador/Operador)
Menú → Opción 1: Crear orden

Título: Instalación sistema de seguridad
Descripción: Cliente nuevo requiere instalación completa
¿Asignar áreas? Sí
Áreas seleccionadas: 1,3 (Técnica, Soporte)
```

#### Paso 2: Ver Detalle de la Orden
```
Menú → Opción 3: Ver detalle de orden
ID: ORD-00001

Verás:
- Información básica
- Estado global: EN_PROCESO
- Asignaciones por área con estados parciales
- Tiempo transcurrido
```

#### Paso 3: Asignar Agentes (Despachador)
```
Menú → Opción 4: Gestionar orden
ID: ORD-00001
Opción 3: Asignar agente a área

Seleccionar área: Área Técnica
Seleccionar agente: Ana López
```

#### Paso 4: Trabajar como Agente
```
Cambiar usuario (Opción 9)
Usuario: Ana López (Agente de Área)

Menú → Opción 4: Gestionar orden
ID: ORD-00001
Opción 2: Cambiar estado de asignación
Seleccionar: Área Técnica
Nuevo estado: 1 (EN_PROGRESO)
```

#### Paso 5: Monitorear con Temporizador
```
Menú → Opción 7: Gestionar temporizador
Opción 1: Iniciar temporizador

El sistema automáticamente:
- Incrementa tiempo cada N_seg
- Marca como VENCIDA si excede SLA_seg
- Actualiza estado global
```

#### Paso 6: Ver KPIs
```
Menú → Opción 5: Ver KPIs y tablero

Tablero mostrará:
- Total órdenes: 1
- En proceso: 1
- Tiempo promedio
- Porcentajes por estado
```

#### Paso 7: Completar Trabajo
```
Como agente Ana López:
Menú → Opción 4: Gestionar orden
Opción 2: Cambiar estado
Nuevo estado: 3 (COMPLETADA)

Si todas las áreas completan → Estado global: COMPLETADA
```

#### Paso 8: Ver Historial
```
Menú → Opción 6: Ver historial
Opción 2: Historial de una orden
ID: ORD-00001

Verás cronología completa:
[timestamp] ORD-00001 - Orden creada: null → NUEVA (Carlos Martínez)
[timestamp] ORD-00001 - Área asignada: null → Área Técnica (Carlos Martínez)
[timestamp] ORD-00001 - Agente asignado: null → Ana López (Ana López)
[timestamp] ORD-00001 - Cambio de estado: ASIGNADA → EN_PROGRESO (Ana López)
[timestamp] ORD-00001 - SLA excedido: EN_PROGRESO → VENCIDA (SISTEMA)
[timestamp] ORD-00001 - Cambio de estado: VENCIDA → COMPLETADA (Ana López)
[timestamp] ORD-00001 - Estado global actualizado: EN_PROCESO → COMPLETADA (SISTEMA)
```

---

## 💡 Escenarios de Demostración

### Escenario 1: Orden Completada Exitosamente

**Objetivo**: Demostrar flujo completo exitoso

1. Crear orden "Mantenimiento preventivo"
2. Asignar Área Técnica y Área Calidad
3. Iniciar temporizador con SLA_seg = 60
4. Ambas áreas trabajan y completan a tiempo
5. Estado global: COMPLETADA
6. Revisar KPIs y historial

**Resultado esperado**: 
- ✅ Orden completada sin vencer SLA
- Historial con trazabilidad completa
- KPIs actualizados

---

### Escenario 2: Orden Parcialmente Vencida

**Objetivo**: Demostrar manejo de SLA excedido

1. Crear orden "Soporte urgente"
2. Asignar 3 áreas: Técnica, Comercial, Soporte
3. Configurar SLA_seg = 30 (corto para demo)
4. Iniciar temporizador
5. Solo 1 área completa a tiempo
6. Otras 2 exceden SLA → VENCIDA
7. Estado global: PARCIALMENTE_VENCIDA

**Resultado esperado**:
- ⚠️ Algunas áreas vencidas, otras activas
- Historial muestra cambios automáticos por SISTEMA
- KPIs reflejan órdenes parcialmente vencidas

---

### Escenario 3: Cerrar Sin Solución

**Objetivo**: Demostrar cierre de orden sin resolver

1. Crear orden "Problema técnico complejo"
2. Asignar Área Técnica
3. Agente inicia trabajo
4. Después de intentos, decide cerrar sin solución
5. Estado: CERRADA_SIN_SOLUCION
6. Estado global: CERRADA_SIN_SOLUCION

**Resultado esperado**:
- ❌ Orden cerrada sin completar
- KPIs muestran órdenes cerradas sin solución
- Historial registra decisión del agente

---

### Escenario 4: Múltiples Órdenes Simultáneas

**Objetivo**: Demostrar capacidad de gestión paralela

1. Crear 5 órdenes diferentes
2. Asignar diferentes áreas a cada una
3. Iniciar temporizador
4. Alternar entre usuarios para trabajar
5. Ver tablero KPIs en tiempo real
6. Filtrar órdenes por estado

**Resultado esperado**:
- 📊 KPIs reflejan distribución de estados
- Temporizador afecta todas las órdenes activas
- Filtros funcionan correctamente

---

## ⚙️ Configuración de Parámetros

### Para Demo Rápida (resultados inmediatos)
```
N_seg = 10 segundos
SLA_seg = 30 segundos
Estado_timeout = VENCIDA
```
Verás cambios rápidamente y órdenes venciendo en 30 segundos.

### Para Demo Realista
```
N_seg = 15 segundos
SLA_seg = 60 segundos
Estado_timeout = PENDIENTE
```
Más tiempo para trabajar, se pausa automáticamente al vencer.

### Para Testing de Larga Duración
```
N_seg = 20 segundos
SLA_seg = 120 segundos
Estado_timeout = VENCIDA
```
Permite explorar funcionalidades con calma.

---

## 📊 Interpretación de Estados

### Estados Parciales

| Estado | Significado | Siguiente Acción |
|--------|-------------|------------------|
| NUEVA | Orden creada pero no asignada | Asignar áreas |
| ASIGNADA | Área asignada pero sin iniciar | Agente debe iniciar |
| EN_PROGRESO | Agente trabajando activamente | Completar o pausar |
| PENDIENTE | Trabajo pausado temporalmente | Reanudar trabajo |
| COMPLETADA | Área finalizó exitosamente | Ninguna (final) |
| CERRADA_SIN_SOLUCION | Área cerró sin resolver | Ninguna (final) |
| VENCIDA | Excedió SLA sin completar | Completar urgente |

### Estados Globales

| Estado | Condición | Acción Recomendada |
|--------|-----------|-------------------|
| NUEVA | Sin asignaciones | Asignar áreas |
| EN_PROCESO | Al menos una área activa | Monitorear progreso |
| COMPLETADA | Todas las áreas completadas | Archivar |
| CERRADA_SIN_SOLUCION | Todas cerradas sin resolver | Revisar causa |
| PARCIALMENTE_VENCIDA | Algunas áreas vencidas | Escalar urgente |
| TOTALMENTE_VENCIDA | Todas las áreas vencidas | Intervención inmediata |

---

## 🔍 Filtros y Búsquedas

### Listar Órdenes

**Opción 1: Todas las órdenes**
- Muestra todas independiente del estado
- Útil para visión general

**Opción 2: Órdenes activas**
- Solo órdenes no cerradas
- Para ver trabajo pendiente

**Opción 3: Filtrar por estado**
- Seleccionar estado global específico
- Análisis por categoría

### Ver Historial

**Opción 1: Historial completo**
- Todos los cambios del sistema
- Auditoría general

**Opción 2: Historial de una orden**
- Cronología específica
- Trazabilidad detallada

**Opción 3: Últimos 10 cambios**
- Actividad reciente
- Monitoreo rápido

---

## 🎓 Tips para la Demo

1. **Iniciar con parámetros cortos** (N_seg=10, SLA_seg=30) para ver efectos rápidos

2. **Crear 2-3 órdenes** con diferentes complejidades:
   - Simple: 1 área
   - Media: 2-3 áreas
   - Compleja: 4 áreas

3. **Alternar usuarios** para mostrar roles:
   - Despachador crea y asigna
   - Agentes trabajan
   - Admin configura

4. **Dejar temporizador corriendo** mientras exploras otras funciones

5. **Mostrar historial** para evidenciar trazabilidad completa

6. **Ver KPIs periódicamente** para mostrar actualización en tiempo real

7. **Provocar vencimientos** para demostrar reglas automáticas

8. **Cerrar algunas órdenes** de diferentes maneras (completada, sin solución)

9. **Usar filtros** para mostrar capacidad de análisis

10. **Revisar historial final** para mostrar registro completo de actividad

---

## 🐛 Solución de Problemas Comunes

### El temporizador no incrementa tiempo
- ✅ Verificar que esté iniciado (Opción 7-1)
- ✅ Esperar N_seg segundos
- ✅ Ver detalle de orden para confirmar incremento

### No puedo crear órdenes
- ✅ Verificar rol de usuario (debe ser Despachador o Admin)
- ✅ Cambiar usuario si es necesario (Opción 9)

### El estado global no cambia
- ✅ Verificar que todas las áreas tengan el estado correcto
- ✅ Sistema calcula automáticamente según reglas
- ✅ Ver reglas de cálculo en documentación

### No veo cambios en KPIs
- ✅ Hacer cambios en órdenes (crear, completar, etc.)
- ✅ Actualizar vista (volver a Opción 5)
- ✅ KPIs se calculan en tiempo real

### El historial está vacío
- ✅ Crear al menos una orden primero
- ✅ Verificar filtro seleccionado
- ✅ Todos los cambios se registran automáticamente

---

## 📞 Comandos Rápidos

| Función | Navegación |
|---------|-----------|
| Crear orden | 1 → datos → asignar áreas |
| Ver todas las órdenes | 2 → 1 |
| Ver órdenes activas | 2 → 2 |
| Detalle de orden | 3 → ID |
| Cambiar estado | 4 → ID → 2 → área → estado |
| Asignar agente | 4 → ID → 3 → área → agente |
| Ver KPIs | 5 |
| Ver historial completo | 6 → 1 |
| Historial de orden | 6 → 2 → ID |
| Iniciar temporizador | 7 → 1 |
| Detener temporizador | 7 → 2 |
| Forzar tick | 7 → 3 |
| Configurar params | 8 → valores |
| Cambiar usuario | 9 → número |

---

## ✅ Checklist de Demo Completa

- [ ] Inicializar sistema y ver banner
- [ ] Seleccionar usuario despachador
- [ ] Crear al menos 2 órdenes
- [ ] Asignar múltiples áreas a órdenes
- [ ] Configurar parámetros del sistema
- [ ] Iniciar temporizador
- [ ] Ver KPIs iniciales
- [ ] Cambiar a usuario agente
- [ ] Iniciar trabajo en área
- [ ] Ver estado EN_PROGRESO
- [ ] Esperar vencimiento de SLA
- [ ] Ver cambio automático a VENCIDA
- [ ] Completar área
- [ ] Ver actualización de estado global
- [ ] Revisar historial completo
- [ ] Filtrar órdenes por estado
- [ ] Ver KPIs finales con estadísticas
- [ ] Cambiar parámetros como admin
- [ ] Cerrar orden sin solución
- [ ] Verificar trazabilidad completa

¡Sistema listo para demostración! 🚀

