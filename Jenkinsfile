pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['default', 'test', 'qa'], description: 'Selecciona el entorno de pruebas')
        string(name: 'TAGS', defaultValue: 'not @obtenerTiposMascotaPorId', description: 'Dejar vacío para ejecutar todas las pruebas')
    }
//     triggers {
//         cron('H/30 * * * *')
//     }
    stages {
        stage('Integration Tests') {
            steps {
                script {
                    def tagsOption = TAGS?.trim() ? "-Dcucumber.filter.tags='${TAGS}'" : ""
                    sh """
                        docker run --rm \
                          -v \$(pwd):/workspace \
                          -w /workspace \
                          maven:3.8.8-eclipse-temurin-17-alpine \
                          mvn clean verify -Denvironment=${params.ENVIRONMENT} ${tagsOption} -B -ntp
                    """
                }
            }
            post {
                success {
                    publishHTML(
                            target: [
                                    reportName           : 'Integration Tests Report',
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
    post {
        always {
            cleanWs()
        }
    }
}
