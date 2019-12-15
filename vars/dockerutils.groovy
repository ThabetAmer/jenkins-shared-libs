def stopByPort(Int $port) {
    assert $port != null
    sh(label: 'Stop Running Containers',
        script: "docker ps --format '{{.ID}}\t {{.Ports}}' | grep ':${port}' | \
            cut -f1 | xargs -r docker stop || true"
    )
}

def saveVersion(Map config) {
    sh(label: 'Saving version logs on server',
        script: "docker exec -i ${DOCKER_CONTAINER} bash -c 'cat  > version.txt << EOL "
                + "JOB: $JOB_NAME \n"
                + "BUILD NUMBER: $BUILD_NUMBER \n"
                + "BRANCH: $BRANCH_NAME"
    )
}

def exec(String $command, String $label = $command) {
    assert $command != null
    sh(label: $label,
        script: "docker exec -i ${DOCKER_CONTAINER} bash -c '$command'"
    )
}