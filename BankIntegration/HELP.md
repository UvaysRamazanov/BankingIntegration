# HELP.md

## Описание проекта

**BankingIntegration** — это приложение на Java с использованием Spring Boot, которое интегрируется с API для конвертации валют.
Приложение использует PostgreSQL в качестве базы данных и включает в себя функционал миграции базы данных с помощью Flyway.

## Стек технологий

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Flyway**
- **Swagger**
- **Docker**
- **Kubernetes**

## Установка и запуск локально

### Предварительные требования

- Установленный [JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- Установленный [Maven](https://maven.apache.org/download.cgi)
- Установленный [Docker](https://www.docker.com/get-started)
- Установленный [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- Установленный [Docker Compose](https://docs.docker.com/compose/install/)

### Сборка проекта

1. Клонируйте репозиторий:
   git clone <URL_репозитория>
   cd <имя_папки_проекта>

2. Соберите проект с помощью Maven:
   mvn clean package
### Запуск локально с Docker

1. Создайте образ:

docker build -t banking-integration .

text

1. Запустите контейнеры с помощью Docker Compose:

docker-compose up

text

### Настройка базы данных

Убедитесь, что у вас есть PostgreSQL, работающий на `localhost:5432`, и база данных `BankDB` создана. Вы можете изменить параметры подключения в файле `application.yml`.

## Развертывание в Kubernetes

### Подготовка секретов

Перед развертыванием создайте секреты для хранения конфиденциальной информации:

kubectl create secret generic db-secret --from-literal=password=<your_db_password>
kubectl create secret generic twelvedata-secret --from-literal=api-key=<your_twelvedata_api_key>
kubectl create secret generic app-secrets --from-file=secrets.yaml=./secrets.yaml

### Применение манифестов Kubernetes

1. Примените манифесты:

kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

1. Проверьте состояние развертывания:

kubectl get deployments
kubectl get services

1. Получите внешний IP-адрес сервиса (если используется LoadBalancer):

kubectl get services banking-integration-service

## Полезные команды

- **Сборка образа**: `docker build -t <имя_образа> .`
- **Запуск контейнеров**: `docker-compose up`
- **Список запущенных контейнеров**: `docker ps`
- **Остановка контейнеров**: `docker-compose down`
- **Применение изменений в Kubernetes**: `kubectl apply -f <имя_файла.yaml>`

## Документация API

Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui.html` (после запуска приложения).

## Контрибуция

Если вы хотите внести изменения или улучшения в проект, пожалуйста, создайте pull request или откройте issue.

## Лицензия

Этот проект лицензирован под MIT License. Пожалуйста, ознакомьтесь с файлом LICENSE для получения дополнительной информации.