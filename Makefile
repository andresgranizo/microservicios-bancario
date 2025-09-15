# Makefile Simple - Microservicios Bancario
# Autor: Andrés Granizo

.PHONY: help start stop build test clean

# Comandos principales
help: ## Mostrar ayuda
	@echo "Comandos disponibles:"
	@echo "  make start    - Iniciar todos los servicios"
	@echo "  make stop     - Detener servicios"
	@echo "  make build    - Construir proyecto"
	@echo "  make test     - Ejecutar pruebas"
	@echo "  make clean    - Limpiar contenedores"
	@echo "  make logs     - Ver logs"

start: ## Iniciar servicios con Docker
	@echo "Iniciando microservicios..."
	docker-compose up -d
	@echo "   Servicios iniciados:"
	@echo "   Cliente Service: http://localhost:8081"
	@echo "   Cuenta Service:  http://localhost:8082"
	@echo "   RabbitMQ:        http://localhost:15672"
	@echo "   Swagger Cliente: http://localhost:8081/swagger-ui/index.html"
	@echo "   Swagger Cuenta:  http://localhost:8082/swagger-ui/index.html

stop: ## Detener servicios
	@echo " Deteniendo servicios..."
	docker-compose stop

build: ## Construir proyecto
	@echo "Construyendo servicios..."
	cd cliente-service && mvn clean package -DskipTests
	cd cuenta-service && mvn clean package -DskipTests
	@echo " Construyendo imágenes Docker..."
	docker-compose build

test: ## Ejecutar pruebas
	@echo "Ejecutando pruebas..."
	cd cliente-service && mvn test
	cd cuenta-service && mvn test

clean: ## Limpiar contenedores
	@echo "Limpiando..."
	docker-compose down -v
	docker system prune -f

logs: ## Ver logs
	docker-compose logs -f

# Comandos de desarrollo
dev: ## Modo desarrollo con logs
	docker-compose up

restart: stop start ## Reiniciar servicios
