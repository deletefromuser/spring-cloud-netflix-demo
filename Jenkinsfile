pipeline {
    agent any
    triggers {
        githubPush()
    }
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                echo '#### java $$$$'
                sh 'pwd'
                sh 'cd config-server'
                sh 'ls -lrt'
                sh 'whoami'
                sh 'uname -a'
                sh 'lsb_release -a'
                sh 'docker ps'
                def mvnHome = tool name: 'Apache Maven 3.6.0', type: 'maven'
                sh '(cd gateway-server/ && ${mvnHome}/bin/mvn spring-boot:build-image -DskipTests)'
            }
        }
    }
}