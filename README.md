# Selenium UI-тесты YouTrack Server

Проект содержит UI-тесты YouTrack на Java 17, Selenium WebDriver 4.53.0 и JUnit 5. ChromeDriver автоматически подбирается Selenium Manager. Каждый тест использует отдельный потокобезопасный экземпляр браузера. При ошибке JUnit сохраняет скриншот в `target/screenshots`; стандартные отчёты Surefire находятся в `target/surefire-reports`.

## Запуск YouTrack

Из корня проекта выполните:

```powershell
docker compose up -d
docker compose logs -f youtrack
```

Откройте `http://localhost:8080` и войдите в YouTrack вручную. Конфигурация и данные сохраняются в именованных Docker volumes. Перезапуск контейнера не удаляет их. Остановить сервис можно командой `docker compose down`; команда `docker compose down -v` удаляет данные.

## Настройка и запуск тестов

Заполните локальный файл `.env`: укажите логин и пароль пользователя с правами на создание задач. Файл `.env` исключён из Git. Шаблон для других разработчиков хранится в `.env.example`. Файл необязателен: если он отсутствует или нужное значение в нём пустое, тесты возьмут значение из системной переменной окружения.

Порядок поиска значения: Maven/Java property (`-Dyoutrack.username=...`), системная переменная (`YOUTRACK_USERNAME`), значение из `.env`, затем значение по умолчанию, если оно задано. Запускайте Maven из корня проекта, где лежит `.env`:

```powershell
mvn test
```

Сначала Surefire запускает `PreflightUiTest`: он проверяет вход настроенным пользователем. Остальные UI-тесты запускаются вторым этапом только после успешного preflight. Если он падает, Maven останавливает сборку; тесты второго этапа не запускаются и не отмечаются как skipped. Остальные тесты выполняются параллельно (до двух одновременно).

Вместо `.env` можно задать переменные в PowerShell перед запуском:

```powershell
$env:YOUTRACK_URL = "http://localhost:8080"
$env:YOUTRACK_USERNAME = "ваш_логин"
$env:YOUTRACK_PASSWORD = "ваш_пароль"
$env:YOUTRACK_PROJECT = "DEMO"
mvn test
```

На macOS/Linux используйте `export` в терминале:

```sh
export YOUTRACK_URL="http://localhost:8080"
export YOUTRACK_USERNAME="ваш_логин"
export YOUTRACK_PASSWORD="ваш_пароль"
export YOUTRACK_PROJECT="DEMO"
mvn test
```

Переменные, заданные в терминале, действуют для Maven, запущенного из этого терминала. Для видимого окна Chrome укажите `-Dbrowser.headless=false`; по умолчанию Chrome запускается в headless-режиме.

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

## Jenkins в отдельном Docker-контейнере

Jenkins запускается отдельно от YouTrack. Сначала поднимите YouTrack обычной командой `docker compose up -d`. Затем подготовьте локальные переменные Jenkins и запустите его:

```powershell
Copy-Item .env.jenkins.example .env.jenkins
# Откройте .env.jenkins и укажите данные тестового пользователя YouTrack
docker compose -f docker-compose.jenkins.yml up -d --build
```

Jenkins будет доступен по адресу `http://localhost:8081`. Из Jenkins-контейнера YouTrack доступен по адресу `http://host.docker.internal:8080`; Для остановки `docker compose -f docker-compose.jenkins.yml down`; volume сохраняется. Не добавляйте `.env.jenkins` в Git.

При первом входе пароль администратора (или в консоли) для настройки:

```powershell
docker exec selenium-jenkins sh -c "cat /var/jenkins_home/secrets/initialAdminPassword"
```

В мастере первоначальной настройки установить рекомендуемые плагины. Настроить пайплайн: 
```
pipeline {
    agent any

    triggers {
        cron('TZ=Europe/Moscow\n0 0 * * *')
        pollSCM('H/5 * * * *')
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/vdekhtiarenko/Selenium.git'
            }
        }
        stage('UI tests') {
            steps {
                sh 'mvn -B test'
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/screenshots/**/*', allowEmptyArchive: true
        }
    }
}
```

Еще можно настроить крон или вебхук.

Локальный `Jenkinsfile` подготовлен для последующего хранения pipeline в Git. После отправки `Jenkinsfile` в выбранную ветку можно переключить задачу `test` на **Pipeline script from SCM**: SCM `Git`, Repository URL `https://github.com/vdekhtiarenko/Selenium.git`, Branch Specifier `*/feat/youtrack-selenium-tests`, Script Path `Jenkinsfile`.

`YOUTRACK_URL`, `YOUTRACK_USERNAME`, `YOUTRACK_PASSWORD`, `YOUTRACK_PROJECT` и `BROWSER_HEADLESS` передаются Jenkins из `.env.jenkins`. При смене пароля тестового пользователя обновить локальный файл и пересоздайть контейнер: `docker compose -f docker-compose.jenkins.yml up -d`.