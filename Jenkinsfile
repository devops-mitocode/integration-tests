pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['default', 'dev', 'soap'], description: 'ENVIRONMENT')
        string(name: 'TAGS', defaultValue: '@wip', description: 'TAGS (default: @wip')
    }
    stages {
        stage('Integration Tests') {
            steps {
                sh "docker run --rm -v ${WORKSPACE}:/usr/src/app -w /usr/src/app maven:3.8.8-eclipse-temurin-17 mvn clean verify -Denvironment=${WORKSPACE} -Dcucumber.filter.tags=${TAGS} -B -ntp"
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
