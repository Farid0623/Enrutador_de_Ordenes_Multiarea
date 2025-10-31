# Diagramas del Sistema

## Diagrama de Clases Simplificado

```
┌─────────────────────┐
│      Usuario        │
├─────────────────────┤
│ - id: String        │
│ - nombre: String    │
│ - rol: Rol          │
└─────────────────────┘
          △
          │
          │
┌─────────────────────┐
│        Rol          │
├─────────────────────┤
│ DESPACHADOR         │
│ AGENTE_AREA         │
│ ADMINISTRADOR       │
└─────────────────────┘


┌─────────────────────────────────────┐
│           Orden                     │
├─────────────────────────────────────┤
│ - id: String                        │
│ - titulo: String                    │
│ - descripcion: String               │
│ - creador: Usuario                  │
│ - fechaCreacion: LocalDateTime      │
│ - estadoGlobal: EstadoGlobal        │
│ - asignaciones: List<AsignacionArea>│
├─────────────────────────────────────┤
│ + agregarAsignacion()               │
│ + estaCerrada()                     │
│ + getTiempoTotalSegundos()          │
└─────────────────────────────────────┘
          │ 1
          │
          │ *
          ▼
┌─────────────────────────────────────┐
│       AsignacionArea                │
├─────────────────────────────────────┤
│ - area: Area                        │
│ - agenteAsignado: Usuario           │
│ - estado: EstadoParcial             │
│ - fechaAsignacion: LocalDateTime    │
│ - tiempoTranscurridoSegundos: long  │
├─────────────────────────────────────┤
│ + incrementarTiempo()               │
│ + estaActiva()                      │
└─────────────────────────────────────┘
          │
          │ 1
          ▼
┌─────────────────────┐
│       Area          │
├─────────────────────┤
│ - id: String        │
│ - nombre: String    │
│ - agentes: List     │
└─────────────────────┘
```

## Diagrama de Estados

### Estados Parciales (AsignacionArea)

```
    ┌──────────┐
    │  NUEVA   │
    └────┬─────┘
         │
         ▼
    ┌──────────┐
    │ ASIGNADA │◄─────────────┐
    └────┬─────┘              │
         │                    │
         ▼                    │
  ┌─────────────┐             │
  │ EN_PROGRESO │             │
  └──────┬──────┘             │
         │                    │
    ┌────┴────┐              │
    │         │              │
    ▼         ▼              │
┌─────────┐ ┌───────────┐   │
│PENDIENTE│ │  VENCIDA  │───┤
└────┬────┘ └─────┬─────┘   │
     │            │          │
     └────┬───────┘          │
          │                  │
     ┌────┴─────────────┐    │
     │                  │    │
     ▼                  ▼    │
┌────────────┐  ┌─────────────────────┐
│ COMPLETADA │  │CERRADA_SIN_SOLUCION │
└────────────┘  └─────────────────────┘
   (FINAL)              (FINAL)
```

### Estados Globales (Orden)

```
┌────────────────────────────────────────┐
│  Cálculo basado en estados parciales  │
└────────────────────────────────────────┘
                 │
      ┌──────────┼───────────┬──────────┐
      │          │           │          │
      ▼          ▼           ▼          ▼
  ┌───────┐ ┌──────────┐ ┌──────┐ ┌─────────┐
  │ NUEVA │ │EN_PROCESO│ │COMPLE│ │CERRADA_ │
  └───────┘ └──────────┘ │TADA  │ │SIN_SOL  │
                         └──────┘ └─────────┘
                             │         │
      ┌──────────────────────┴─────────┘
      │
      ▼
┌─────────────────┐  ┌──────────────────┐
│PARCIALMENTE_    │  │TOTALMENTE_       │
│VENCIDA          │  │VENCIDA           │
└─────────────────┘  └──────────────────┘
```

## Diagrama de Flujo Principal

```
        ┌──────────────┐
        │   INICIO     │
        └──────┬───────┘
               │
               ▼
    ┌──────────────────┐
    │ Seleccionar      │
    │ Usuario          │
    └──────┬───────────┘
           │
           ▼
    ┌──────────────────┐
    │  Mostrar Menú    │
    └──────┬───────────┘
           │
      ┌────┴────┬─────────────┬──────────┐
      │         │             │          │
      ▼         ▼             ▼          ▼
┌─────────┐ ┌────────┐ ┌──────────┐ ┌────────┐
│ Crear   │ │Listar  │ │ Gestionar│ │Ver KPIs│
│ Orden   │ │Órdenes │ │  Orden   │ │        │
└─────────┘ └────────┘ └──────────┘ └────────┘
      │         │             │          │
      └─────────┴─────────────┴──────────┘
                    │
                    ▼
            ┌──────────────┐
            │ ¿Continuar?  │
            └──────┬───────┘
                   │
            ┌──────┴──────┐
            │ Sí          │ No
            ▼             ▼
        (Volver)     ┌─────────┐
                     │   FIN   │
                     └─────────┘
```

## Diagrama de Secuencia - Crear y Completar Orden

```
Despachador    ConsolaDemo    OrdenService    EstadoGlobalService    HistorialService
     │              │               │                  │                    │
     │─crear orden─>│               │                  │                    │
     │              │─crearOrden()─>│                  │                    │
     │              │               │                  │                    │
     │              │               │─registrar────────────────────────────>│
     │              │               │  cambio                               │
     │              │<─────orden────│                  │                    │
     │<─confirmación│               │                  │                    │
     │              │               │                  │                    │
     │─asignar área>│               │                  │                    │
     │              │─asignarArea()->│                  │                    │
     │              │               │─calcular─────────>│                    │
     │              │               │  estado global   │                    │
     │              │               │<─estado──────────│                    │
     │              │               │─registrar────────────────────────────>│
     │              │<─────ok───────│                  │                    │
     │<─confirmación│               │                  │                    │
     │              │               │                  │                    │
     
Agente         ConsolaDemo    OrdenService    TemporizadorService
     │              │               │                  │
     │─iniciar trabajo>             │                  │
     │              │─cambiarEstado>│                  │
     │              │               │                  │
     │              │               │<─tick────────────│
     │              │               │  (automático)    │
     │              │               │─incrementar──────│
     │              │               │  tiempo          │
     │              │               │─verificar SLA────│
     │              │<─estado───────│                  │
     │<─actualización                │                  │
     │              │               │                  │
     │─completar────>               │                  │
     │              │─completar()───>                  │
     │              │               │─actualizar estado│
     │              │               │─registrar cambio │
     │              │<─ok───────────│                  │
     │<─confirmación│               │                  │
```

## Arquitectura de Servicios

```
┌────────────────────────────────────────────────┐
│              ConsolaDemo (UI)                  │
│  - Menú interactivo                            │
│  - Navegación                                  │
│  - Formateo de salida                          │
└───────────────────┬────────────────────────────┘
                    │
        ┌───────────┼───────────┬─────────────┐
        │           │           │             │
        ▼           ▼           ▼             ▼
┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
│  Orden   │ │ Historial│ │   KPI    │ │Tempori-  │
│ Service  │ │ Service  │ │ Service  │ │zador     │
└────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘
     │            │            │            │
     │            │            │            │
     └────────────┴────────────┴────────────┘
                    │
          ┌─────────┴─────────┐
          │                   │
          ▼                   ▼
┌──────────────────┐  ┌─────────────────┐
│ EstadoGlobal     │  │   Catálogo      │
│ Service          │  │   Repository    │
└──────────────────┘  └─────────────────┘
          │                   │
          └─────────┬─────────┘
                    │
                    ▼
        ┌───────────────────────┐
        │   Model (Entities)    │
        │  - Orden              │
        │  - AsignacionArea     │
        │  - Usuario            │
        │  - Area               │
        │  - CambioHistorial    │
        └───────────────────────┘
```

## Flujo del Temporizador

```
     ┌─────────────────────┐
     │ Iniciar Timer       │
     └──────────┬──────────┘
                │
                ▼
     ┌─────────────────────┐
     │ Esperar N_seg       │
     └──────────┬──────────┘
                │
                ▼
     ┌─────────────────────┐
     │ Para cada Orden     │
     │ Activa              │
     └──────────┬──────────┘
                │
                ▼
     ┌─────────────────────┐
     │ Para cada           │
     │ Asignación Activa   │
     └──────────┬──────────┘
                │
                ▼
     ┌─────────────────────┐
     │ Incrementar         │
     │ tiempo += N_seg     │
     └──────────┬──────────┘
                │
                ▼
     ┌─────────────────────┐
     │ ¿Tiempo > SLA_seg?  │
     └──────┬────────┬─────┘
            │ Sí     │ No
            ▼        │
  ┌─────────────────┐│
  │ Cambiar estado  ││
  │ a estado_timeout││
  └─────────┬───────┘│
            │        │
            ▼        │
  ┌─────────────────┐│
  │ Registrar en    ││
  │ Historial       ││
  └─────────┬───────┘│
            │        │
            ▼        │
  ┌─────────────────┐│
  │ Recalcular      ││
  │ Estado Global   ││
  └─────────┬───────┘│
            │        │
            └────────┴────┐
                          │
                          ▼
              ┌─────────────────────┐
              │ Siguiente           │
              │ Asignación          │
              └──────────┬──────────┘
                         │
                         ▼
                    (Repetir)
```

## Matriz de Permisos

```
┌──────────────────┬─────────────┬─────────────┬──────────────┐
│   Acción         │ Despachador │ Agente      │ Administrador│
├──────────────────┼─────────────┼─────────────┼──────────────┤
│ Crear orden      │     ✅      │     ❌      │      ✅      │
│ Asignar área     │     ✅      │     ❌      │      ✅      │
│ Asignar agente   │     ✅      │     ❌      │      ✅      │
│ Cambiar estado   │     ✅      │  ✅ (su área│      ✅      │
│ Ver KPIs         │     ✅      │     ✅      │      ✅      │
│ Ver historial    │     ✅      │     ✅      │      ✅      │
│ Config parámetros│     ✅      │     ❌      │      ✅      │
│ Gestionar timer  │     ✅      │     ❌      │      ✅      │
│ Ver todas órdenes│     ✅      │     ✅      │      ✅      │
└──────────────────┴─────────────┴─────────────┴──────────────┘
```

## Ciclo de Vida de una Orden

```
   ┌────────────┐
   │   CREAR    │ ← Despachador ingresa datos
   └─────┬──────┘
         │
         ▼
   ┌────────────┐
   │  ASIGNAR   │ ← Despachador asigna áreas
   │   ÁREAS    │
   └─────┬──────┘
         │
         ▼
   ┌────────────┐
   │  ASIGNAR   │ ← Despachador asigna agentes
   │  AGENTES   │
   └─────┬──────┘
         │
         ▼
   ┌────────────┐
   │  TRABAJAR  │ ← Agentes cambian estados
   │            │   Temporizador corre
   └─────┬──────┘
         │
    ┌────┴────┬────────┐
    │         │        │
    ▼         ▼        ▼
┌────────┐ ┌─────┐ ┌───────┐
│COMPLETA│ │VENCÍ│ │CERRAR │
│        │ │DA   │ │SIN SOL│
└────────┘ └─────┘ └───────┘
    │         │        │
    └─────────┴────────┘
              │
              ▼
        ┌──────────┐
        │  CIERRE  │
        │  FINAL   │
        └──────────┘
              │
              ▼
        ┌──────────┐
        │HISTORIAL │
        │COMPLETO  │
        └──────────┘
```

