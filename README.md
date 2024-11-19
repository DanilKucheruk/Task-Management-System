# Task-Management-System

## Описание

# Инфраструктурные требования

## 1. **PostgreSQL**
   - **Version**: 17.1
   - **Docker Image**: `postgres:17.1`

## 2. **Spring Boot**
   - **Version**: 3.3.2

## 3. **Java**
   - **Version**: 17

## 4. **Docker**
   - **Version**: Any version compatible with Docker Compose files

## 5. **Dependencies Versions**:
   - **PostgreSQL JDBC Driver**: 42.7.4
   - **JWT**: 0.11.2
   - **Springdoc OpenAPI**: 2.3.0
   - **SLF4J**: 2.0.13
   - **MapStruct**: 1.6.3
   - **Lombok**: 1.18.34
   - **JUnit**: 5.8.2 

**Task Management System** – это RESTful API для управления задачами, разработанное на **Java** с использованием **Spring Boot**. Система позволяет пользователям создавать, редактировать, удалять и просматривать задачи и комментарии к ним. Для обеспечения безопасности используется **JWT-аутентификация**, а данные хранятся в базе данных **PostgreSQL**. API документировано с помощью **Swagger UI**.

## Функциональные возможности

- **Аутентификация и авторизация пользователей** по email и паролю через **JWT**.
  
- **Управление задачами**:
  - Создание, редактирование, удаление задач.
  - Просмотр списка задач с фильтрацией и пагинацией.
  - Изменение статуса задачи (например, "в процессе", "завершено").
  - Назначение исполнителя для задачи.

- **Комментарии к задачам**:
  - Возможность добавления и просмотра комментариев для задач.

- **Просмотр задач** других пользователей (с ограниченными правами для изменения задач).

- **Ролевая система**:
  - **Администратор** может управлять всеми задачами: создавать, редактировать, просматривать, удалять, изменять статус и приоритет, назначать исполнителей, оставлять комментарии.
  - **Пользователи** могут управлять только своими задачами и оставлять комментарии к ним.

- **JWT аутентификация** для обеспечения безопасного доступа к API.

- **Валидация данных и обработка ошибок** с понятными сообщениями.

- **Документация API** через **Swagger UI**.

## Установка и запуск

### Требования

1. **Docker** – для запуска приложения и базы данных.
2. **Git** – для клонирования репозитория.

### Шаги для локального запуска

1. **Клонирование репозитория**

  Клонируйте репозиторий с GitHub на ваш локальный компьютер:
  
  ```bash
  git clone https://github.com/DanilKucheruk/Task-Management-System
  ```
2. **Запуск с помощью Docker Compose**
  Перейдите в директорию проекта:
  ```bash
  cd Task-Management-System
  ```
  Создайте и запустите сервисы с помощью Docker Compose:
  ```bash
  docker-compose up --build
  ```
  После успешного запуска все сервисы будут работать, и приложение будет доступно по адресу:
  ```
  http://localhost:8085
  ```
## Структура проекта

- `src/main/java/com/tsm` – исходный код приложения.
  - `configs/` – конфигурационные файлы (например, для безопасности и CORS).
  - `controllers/` – контроллеры REST для обработки запросов.
  - `dto/` – Data Transfer Object для обмена данными между клиентом и сервером.
  - `entity/` – сущности для работы с базой данных.
  - `exceptions/` – классы исключений и обработчики ошибок.
  - `mapper/` – мапперы для преобразования объектов.
  - `repository/` – интерфейсы для работы с базой данных.
  - `service/` – бизнес-логика приложения.
  - `util/` – вспомогательные утилиты, например, для работы с JWT.

## Тестирование

Проект включает базовые тесты для проверки основных функций:

- Проверка аутентификации и авторизации.
- Тестирование CRUD операций для задач и комментариев.
- Проверка пагинации и фильтрации задач.

Для запуска тестов выполните команду:

```bash
./mvnw test
```
