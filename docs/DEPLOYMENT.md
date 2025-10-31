# Guía de Despliegue

## 🚀 Entornos de Despliegue

### Desarrollo (Local)
```bash
# Clonar y ejecutar
git clone https://github.com/tu-usuario/actividad_1.git
cd actividad_1
cp .env.example .env
./run.sh
```

### Testing (QA)
```bash
# Configurar entorno de testing
export APP_ENVIRONMENT=testing
export DB_TYPE=h2
export TIMER_INTERVAL_SEC=5
export TIMER_SLA_SEC=15

# Ejecutar con datos de prueba
java --enable-preview -cp out Main
```

### Producción
```bash
# Configurar variables de producción
export APP_ENVIRONMENT=production
export DB_TYPE=postgresql
export DB_HOST=prod-db.example.com
export TIMER_INTERVAL_SEC=60
export TIMER_SLA_SEC=3600

# Ejecutar como servicio
java --enable-preview -cp out Main
```

## 🐳 Despliegue con Docker (Futuro)

### Dockerfile (Ejemplo)
```dockerfile
FROM openjdk:21-slim

WORKDIR /app

COPY out/ /app/
COPY .env /app/

EXPOSE 8080

CMD ["java", "--enable-preview", "-cp", ".", "Main"]
```

### Docker Compose
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=postgres
      - DB_NAME=ordenes_db
    depends_on:
      - postgres

  postgres:
    image: postgres:15
    environment:
      - POSTGRES_DB=ordenes_db
      - POSTGRES_PASSWORD=secure_password
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data:
```

## ☁️ Despliegue en la Nube

### AWS
- EC2 con Java 21
- RDS para PostgreSQL
- CloudWatch para logs

### Azure
- App Service
- Azure Database for PostgreSQL
- Application Insights

### Google Cloud
- Compute Engine
- Cloud SQL
- Cloud Logging

## 📦 Empaquetado

### JAR Ejecutable
```bash
# Crear JAR con Maven (futuro)
mvn clean package

# Ejecutar JAR
java -jar target/ordenes-app-1.0.0.jar
```

### Distribución
```bash
# Crear paquete de distribución
tar -czf ordenes-app-1.0.0.tar.gz \
  src/ \
  run.sh \
  .env.example \
  README.md \
  LICENSE
```

## 🔒 Checklist de Producción

- [ ] Variables de entorno configuradas
- [ ] Base de datos migrada
- [ ] Logs configurados
- [ ] Backups automatizados
- [ ] Monitoreo activo
- [ ] SSL/TLS habilitado (futuro)
- [ ] Firewall configurado
- [ ] Secrets management
- [ ] Health checks
- [ ] Rollback plan

---

**Última actualización**: 31 de Octubre, 2025

