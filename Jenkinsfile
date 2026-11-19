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
                checkout scm
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
