pipeline {
    agent any
    tools{
        maven 'maven3.9.9'
    }
    options {
        timeout(time: 10, unit: 'MINUTES')
        ansiColor('xterm')
    }
    triggers {
     cron('H/30 * * * *')
    }
    stages {
        stage('Integration Tests') {
            steps {
                sh 'mvn clean test -Dstyle.color=always -ntp'
                junit 'target/surefire-reports/*.xml'
            }
        }
    }
}
