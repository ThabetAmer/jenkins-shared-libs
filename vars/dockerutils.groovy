def stopByPort(String port) {
    assert port != null
    sh(label: 'Stop Running Containers',
        script: "docker ps --format '{{.ID}}\t {{.Ports}}' | grep ':${port}' | \
            cut -f1 | xargs -r docker stop || true"
    )
}

def saveVersion() {
    exec("cat > version.txt << EOL "
                + "JOB: ${env.JOB_NAME} \n"
                + "BUILD NUMBER: ${env.BUILD_NUMBER} \n"
                + "BRANCH: ${env.BRANCH_NAME} \n"
                + "EOL", "Saving version logs on server")
    )
}

def exec(String command, String label = command) {
    assert command != null
    sh(label: label,
        script: "docker exec -i ${env.DOCKER_CONTAINER} bash -c '${command}'"
    )
}