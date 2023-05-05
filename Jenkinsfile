pipeline {
    agent any
    tools { 
        maven 'Maven 3.9.1' 
        jdk 'openjdk11' 
    }
    triggers {
        githubPush()
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }
        }
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