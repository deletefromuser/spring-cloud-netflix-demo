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
                sh 'docker ps'
            }
        }
    }
}