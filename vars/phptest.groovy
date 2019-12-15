def phpLint() {
    sh(label: 'PHP linting',
        returnStdout: true,
        script: 'find . -name "*.php" -not -path "./vendor/*" -print0 | xargs -0 -n1 php -l'
    )
}

def unitTestComposer() {
    sh(label: 'PHP unit testing',
        returnStdout: true,
        script: 'composer test'
    )
}

def unitTest() {
    sh(label: 'PHP native unit testing',
        returnStdout: true,
        script: 'vendor/phpunit/phpunit/phpunit --bootstrap build/bootstrap.php --configuration phpunit-coverage.xml'
    )
}

def publishHTML() {
    publishHTML (target: [
        allowMissing: false,
        alwaysLinkToLastBuild: false,
        keepAll: true,
        reportDir: 'build/coverage',
        reportFiles: 'index.html',
        reportName: "Coverage Report"
    ])
}


