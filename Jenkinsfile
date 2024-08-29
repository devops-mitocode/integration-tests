pipeline {
    agent any
    parameters {
        string(name: 'TAGS', defaultValue: '@wip', description: 'TAGS (default: @wip')
    }
    stages {
        stage('Integration Tests') {
            steps {
                sh 'sleep 10s'
                sh "docker run --rm -v ${WORKSPACE}:/usr/src/app -w /usr/src/app maven:3.8.8-eclipse-temurin-17 mvn clean verify -B -ntp"
                publishHTML(
                        target: [
                                reportName           : 'Serenity Report',
                                reportDir            : 'target/site/serenity',
                                reportFiles          : 'index.html',
                                keepAll              : true,
                                alwaysLinkToLastBuild: true,
                                allowMissing         : false
                        ]
                )
            }
        }
    }
}
