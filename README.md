# Selenium UI-тесты YouTrack Server

Проект содержит UI-тесты YouTrack на Java 17, Selenium WebDriver и JUnit 5. ChromeDriver автоматически подбирается Selenium Manager. Каждый тест использует отдельный потокобезопасный экземпляр браузера. При ошибке JUnit сохраняет скриншот в `target/screenshots`; стандартные отчёты Surefire находятся в `target/surefire-reports`.

## Запуск YouTrack

Из корня проекта выполните:

```powershell
docker compose up -d
docker compose logs -f youtrack
```

Откройте `http://localhost:8080` и войдите в YouTrack вручную. Конфигурация и данные сохраняются в именованных Docker volumes. Перезапуск контейнера не удаляет их. Остановить сервис можно командой `docker compose down`; команда `docker compose down -v` удаляет данные.

## Настройка и запуск тестов

Укажите URL, учётные данные пользователя с правами на создание задач и ключ существующего проекта:

```powershell
$env:YOUTRACK_URL = "http://localhost:8080"
$env:YOUTRACK_USERNAME = "ваш_логин"
$env:YOUTRACK_PASSWORD = "ваш_пароль"
$env:YOUTRACK_PROJECT = "TEST"
mvn test
```

Те же параметры можно передать как Maven properties: `mvn test -Dyoutrack.url=http://localhost:8080 -Dyoutrack.username=... -Dyoutrack.password=... -Dyoutrack.project=TEST`. Для видимого окна Chrome укажите `-Dbrowser.headless=false`; по умолчанию Chrome запускается в headless-режиме.

Требуются JDK 17+, Maven и Google Chrome. Параллельное выполнение тестов JUnit включено в `junit-platform.properties`. Токен YouTrack для этих UI-сценариев не требуется.

## Тест-кейсы

| № | Сценарий | Проверка |
|---|---|---|
| 1 | Вход с корректными учётными данными | После авторизации отображается интерфейс YouTrack |
| 2 | Вход с неверным паролем | Интерфейс показывает отказ авторизации |
| 3 | Просмотр списка проектов | На странице проектов виден заданный проект |
| 4 | Создание задачи | Новая задача создаётся в проекте и появляется в интерфейсе |
| 5 | Поиск задачи | Поиск по заголовку возвращает созданную задачу |

Данные входа и задачи находятся в `src/test/resources/testdata/*.csv`. Заголовки задач получают уникальный суффикс для параллельных запусков.
