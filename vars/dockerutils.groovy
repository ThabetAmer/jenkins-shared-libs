def stopByPort(String port) {
    assert port != null
    sh(label: 'Stop Running Containers',
        script: "docker ps --format '{{.ID}}\t {{.Ports}}' | grep ':${port}' | \
            cut -f1 | xargs -r docker stop || true"
    )
}

def saveVersion(String container) {
    assert container != null
    exec(container, "cat > version.txt << EOL "
        + "JOB: ${env.JOB_NAME} \n"
        + "BUILD NUMBER: ${env.BUILD_NUMBER} \n"
        + "BRANCH: ${env.BRANCH_NAME} \n"
        + "EOL", "Saving version logs on server"
    )
}

def exec(String container, String command, String label = command) {
    assert container != null
    assert command != null
    sh(label: label,
        script: "docker exec -i ${container} bash -c '${command}'"
    )
}