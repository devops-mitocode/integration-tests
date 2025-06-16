pipeline {
    agent any
    tools{
        maven 'maven3.9.9'
    }
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['default', 'test', 'qa'], description: 'Selecciona el entorno de pruebas')
        string(name: 'TAGS', defaultValue: 'not @obtenerTiposMascotaPorId', description: 'Dejar vacío para ejecutar todas las pruebas')
    }
    options {
        timeout(time: 10, unit: 'MINUTES')
        ansiColor('xterm')
    }

//     triggers {
//         cron('H/30 * * * *')
//     }
    stages {
        stage('Integration Tests') {
            steps {
                script {
                    def tagsOption = TAGS?.trim() ? "-Dcucumber.filter.tags='${TAGS}'" : ""
                    sh 'mvn clean test -ntp'
                    junit 'target/surefire-reports/*.xml'
//                    mvn test -Dtest=ClinicServiceTests
                }
            }
        }
    }
//    post {
//        always {
//            cleanWs()
//        }
//    }
}
