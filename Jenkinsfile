pipeline {
    agent any

    parameters {
        choice(
            name: 'SUITE',
            choices: ['sanity.xml', 'regression.xml', 'e2e.xml', 'full-suite.xml'],
            description: 'Which TestNG suite to run'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser'
        )
        string(name: 'TESTER', defaultValue: 'Jenkins CI', description: 'Name shown on the Extent report')
        choice(
            name: 'ENVIRONMENT',
            choices: ['QA', 'Staging'],
            description: 'Environment label on the report'
        )
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run tests') {
            steps {
                script {
                    def mavenCmd = "mvn clean test -DsuiteXmlFile=src/test/resources/suites/${params.SUITE} -Dbrowser=${params.BROWSER} -Dheadless=true -DtesterName=\"${params.TESTER}\" -Denvironment=${params.ENVIRONMENT}"
                    if (isUnix()) {
                        sh mavenCmd
                    } else {
                        bat mavenCmd
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
            script {
                try {
                    publishHTML(target: [
                        allowMissing         : true,
                        alwaysLinkToLastBuild: true,
                        keepAll              : true,
                        reportDir            : 'reports',
                        reportFiles          : 'ExtentReport.html',
                        reportName           : 'QSR Extent Report'
                    ])
                } catch (ignored) {
                    echo 'HTML Publisher plugin not installed. Open reports/ExtentReport.html from the archived artifacts.'
                }
            }
        }
    }
}
