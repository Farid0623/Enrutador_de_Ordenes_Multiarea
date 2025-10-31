# 🌳 Árbol Completo del Proyecto

## Estructura de Archivos

```
actividad_1/
│
├── 📄 .env.example                      ← Plantilla de variables de entorno (50+ vars)
├── 📄 .gitignore                        ← Exclusiones Git (200+ líneas, 11 secciones)
├── 📄 LICENSE                           ← Licencia MIT
├── 📄 README.md                         ← Documentación principal (≤5 pasos) ⭐
├── 📄 DIAGRAMAS.md                      ← Diagramas del sistema
├── 📄 GUIA_USO.md                       ← Guía de uso detallada
├── 📄 RESUMEN.md                        ← Resumen ejecutivo
│
├── 🔧 run.sh                            ← Script ejecución Linux/Mac
├── 🔧 run-java17.sh                     ← Script Java 17+ Linux/Mac
├── 🔧 run.bat                           ← Script ejecución Windows
│
├── 📁 src/                              ← CÓDIGO FUENTE (18 clases)
│   │
│   ├── 📄 Main.java                     ← Punto de entrada principal
│   │
│   ├── 📁 model/                        ← Modelos de dominio (8 clases)
│   │   ├── Area.java                    ← Áreas de trabajo
│   │   ├── AsignacionArea.java          ← Relación orden-área con tiempo
│   │   ├── CambioHistorial.java         ← Registro de auditoría
│   │   ├── EstadoGlobal.java            ← 6 estados globales (enum)
│   │   ├── EstadoParcial.java           ← 7 estados parciales (enum)
│   │   ├── Orden.java                   ← Orden de trabajo principal
│   │   ├── Rol.java                     ← Roles del sistema (enum)
│   │   └── Usuario.java                 ← Usuarios del sistema
│   │
│   ├── 📁 service/                      ← Lógica de negocio (5 servicios)
│   │   ├── EstadoGlobalService.java     ← Cálculo de estados con reglas
│   │   ├── HistorialService.java        ← Trazabilidad completa
│   │   ├── KPIService.java              ← Métricas y tablero
│   │   ├── OrdenService.java            ← CRUD y gestión de órdenes
│   │   └── TemporizadorService.java     ← Timer automático con SLA
│   │
│   ├── 📁 repository/                   ← Acceso a datos (1 repositorio)
│   │   └── CatalogoRepository.java      ← Catálogo de usuarios/áreas
│   │
│   ├── 📁 config/                       ← Configuración (1 clase)
│   │   └── Parametros.java              ← Parámetros del sistema
│   │
│   └── 📁 ui/                           ← Interfaz de usuario (1 clase)
│       └── ConsolaDemo.java             ← Demo interactiva (400+ líneas)
│
├── 📁 docs/                             ← DOCUMENTACIÓN TÉCNICA
│   ├── 📄 ARQUITECTURA.md               ← Estructura y diseño del sistema
│   ├── 📄 DEPLOYMENT.md                 ← Guía de despliegue
│   └── 📄 E1_JUSTIFICACION.md          ← Justificación entregable E1 ⭐
│
├── 📁 db/                               ← BASE DE DATOS
│   ├── 📁 migrations/                   ← Scripts de migración SQL
│   │   └── 📄 V001__initial_schema.sql  ← Schema inicial (6 tablas)
│   │
│   ├── 📁 scripts/                      ← Scripts auxiliares SQL
│   │   └── 📄 seed_demo_data.sql        ← Datos de demostración
│   │
│   └── 📁 data/                         ← Datos persistentes (gitignored)
│
├── 📁 tests/                            ← TESTS (estructura preparada)
│   ├── 📁 unit/                         ← Tests unitarios (futuro)
│   ├── 📁 integration/                  ← Tests de integración (futuro)
│   └── 📁 e2e/                          ← Tests end-to-end (futuro)
│
├── 📁 logs/                             ← Logs de aplicación (gitignored)
│
└── 📁 out/                              ← Archivos compilados (gitignored)
```

---

## 📊 Estadísticas del Proyecto

### Archivos por Tipo

| Tipo | Cantidad | Descripción |
|------|----------|-------------|
| 📄 Java | 18 | Clases del sistema |
| 📄 Markdown | 7 | Documentación |
| 📄 SQL | 2 | Migrations y scripts |
| 🔧 Scripts | 3 | Ejecución multiplataforma |
| 📄 Config | 3 | .env.example, .gitignore, LICENSE |
| **Total** | **33** | **Archivos principales** |

### Clases por Paquete

| Paquete | Clases | Propósito |
|---------|--------|-----------|
| `model` | 8 | Entidades de dominio |
| `service` | 5 | Lógica de negocio |
| `repository` | 1 | Acceso a datos |
| `config` | 1 | Configuración |
| `ui` | 1 | Interfaz de usuario |
| `root` | 1 | Main |
| **Total** | **18** | **Clases Java** |

### Líneas de Código Aproximadas

| Componente | Líneas | Porcentaje |
|------------|--------|------------|
| Código Java | ~1,800 | 50% |
| Documentación | ~1,500 | 42% |
| SQL | ~300 | 8% |
| **Total** | **~3,600** | **100%** |

---

## 🎯 Archivos Clave del Entregable E1

### ⭐ Obligatorios (Criterios de Aceptación)

```
✅ src/                              Código fuente completo
✅ docs/                             Documentación técnica
✅ db/                               Base de datos
   ✅ migrations/                    Scripts de migración
   ✅ scripts/                       Scripts auxiliares
✅ tests/                            Estructura para tests
✅ README.md                         Doc principal (≤5 pasos)
✅ LICENSE                           MIT License
✅ .env.example                      Variables completas
✅ .gitignore                        Adaptado a Java
```

### 📝 Archivos de Configuración

```
.env.example        → 50+ variables documentadas
.gitignore          → 200+ líneas, 11 secciones
LICENSE             → MIT License completa
```

### 📚 Documentación Principal

```
README.md                 → Documentación principal con inicio rápido
GUIA_USO.md              → Casos de uso detallados
RESUMEN.md               → Resumen ejecutivo
DIAGRAMAS.md             → Visualizaciones del sistema
docs/ARQUITECTURA.md     → Estructura técnica
docs/DEPLOYMENT.md       → Guía de despliegue
docs/E1_JUSTIFICACION.md → Justificación del entregable ⭐
```

### 🔧 Scripts de Ejecución

```
run.sh          → Linux/Mac con Java 21
run-java17.sh   → Linux/Mac con Java 17+
run.bat         → Windows
```

### 🗄️ Base de Datos

```
db/migrations/V001__initial_schema.sql  → Schema completo (6 tablas)
db/scripts/seed_demo_data.sql           → Datos de demo (10 usuarios, 5 áreas)
```

---

## 🚀 Navegación Rápida

### Para Empezar
1. Leer: `README.md` (sección "Inicio Rápido")
2. Configurar: `.env.example` → `.env`
3. Ejecutar: `./run.sh` o `run.bat`

### Para Entender el Sistema
1. Arquitectura: `docs/ARQUITECTURA.md`
2. Diagramas: `DIAGRAMAS.md`
3. Casos de uso: `GUIA_USO.md`

### Para Desarrollar
1. Código fuente: `src/`
2. Modelos: `src/model/`
3. Servicios: `src/service/`

### Para Desplegar
1. Guía: `docs/DEPLOYMENT.md`
2. Migrations: `db/migrations/`
3. Variables: `.env.example`

### Para Contribuir
1. Justificación E1: `docs/E1_JUSTIFICACION.md`
2. Resumen: `RESUMEN.md`
3. Licencia: `LICENSE`

---

## 📦 Archivos Ignorados (No en Git)

Definidos en `.gitignore`:

```
out/                    ← Archivos compilados (*.class)
logs/                   ← Logs de la aplicación
db/data/                ← Base de datos local
.env                    ← Variables de entorno locales
.idea/                  ← Configuración de IntelliJ
target/                 ← Build de Maven
build/                  ← Build de Gradle
*.log                   ← Archivos de log
*.tmp                   ← Temporales
.DS_Store               ← macOS
Thumbs.db               ← Windows
```

---

## 🏗️ Estructura de Capas

```
┌──────────────────────────────────────────┐
│        Presentation Layer                 │
│      (ui/ConsolaDemo.java)               │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│         Service Layer                     │
│  ┌──────────────────────────────────┐   │
│  │ OrdenService                      │   │
│  │ KPIService                        │   │
│  │ TemporizadorService               │   │
│  │ HistorialService                  │   │
│  │ EstadoGlobalService               │   │
│  └──────────────────────────────────┘   │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│       Repository Layer                    │
│  (repository/CatalogoRepository.java)    │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│        Domain Layer                       │
│  (model/*.java - 8 clases)               │
└──────────────────────────────────────────┘
```

---

## 🔍 Búsqueda Rápida

### Por Funcionalidad

| Busco... | Ver archivo... |
|----------|----------------|
| Crear orden | `src/service/OrdenService.java` |
| Cambiar estados | `src/service/OrdenService.java` |
| Calcular KPIs | `src/service/KPIService.java` |
| Ver historial | `src/service/HistorialService.java` |
| Temporizador | `src/service/TemporizadorService.java` |
| Estados | `src/model/EstadoParcial.java`, `EstadoGlobal.java` |
| Usuarios | `src/model/Usuario.java` |
| Áreas | `src/model/Area.java` |
| Interfaz | `src/ui/ConsolaDemo.java` |

### Por Tipo de Tarea

| Tarea | Archivos |
|-------|----------|
| Ejecutar | `run.sh` / `run.bat` |
| Configurar | `.env.example` |
| Entender | `README.md`, `docs/ARQUITECTURA.md` |
| Usar | `GUIA_USO.md` |
| Desplegar | `docs/DEPLOYMENT.md` |
| Desarrollar | `src/` |
| Datos demo | `db/scripts/seed_demo_data.sql` |

---

## ✅ Checklist de Archivos E1

- [x] src/ con código fuente completo
- [x] docs/ con documentación técnica
- [x] db/migrations/ con scripts SQL
- [x] db/scripts/ con datos de demo
- [x] tests/ (estructura preparada)
- [x] README.md con ≤5 pasos
- [x] .env.example completo
- [x] .gitignore adaptado
- [x] LICENSE MIT
- [x] Scripts de ejecución (.sh, .bat)
- [x] Justificación técnica (E1_JUSTIFICACION.md)
- [x] Documentación adicional (ARQUITECTURA, DEPLOYMENT)

**Estado**: ✅ **100% COMPLETO**

---

**Última actualización**: 31 de Octubre, 2025  
**Total de archivos**: 33 archivos principales  
**Líneas de código**: ~3,600 líneas  
**Documentación**: 7 archivos Markdown

