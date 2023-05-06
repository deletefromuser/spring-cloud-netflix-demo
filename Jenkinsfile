pipeline {
    agent any
    tools { 
        maven 'Maven 3.9.1' 
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
            }
        }
        stage('build config-server') {
            when { changeset "config-server/*"}
            steps {
                sh '(cd config-server/ && mvn spring-boot:build-image -DskipTests)'
            }
        }
        stage('build service-registry') {
            when { changeset "service-registry/*"}
            steps {
                sh '(cd service-registry/ && mvn spring-boot:build-image -DskipTests)'
            }
        }
        stage('build gateway-server') {
            when { changeset "gateway-server/*"}
            steps {
                sh '(cd gateway-server/ && mvn spring-boot:build-image -DskipTests)'
            }
        }
        stage('build eureka-client') {
            when { changeset "eureka-client/*"}
            steps {
                sh '(cd eureka-client/ && mvn spring-boot:build-image -DskipTests)'
            }
        }
        stage('build eureka-client-consumer') {
            when { changeset "eureka-client-consumer/*"}
            steps {
                sh '(cd eureka-client-consumer/ && mvn spring-boot:build-image -DskipTests)'
            }
        }
    }
}