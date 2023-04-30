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
                sh 'cd config-server'
                sh 'ls -lrt'
                sh 'whoami'
            }
        }
    }
}