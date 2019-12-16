def getNameByPort(String port) {
    assert port != null
    return sh(label: 'Get container ID by port',
                returnStdout: true,
                script: "docker ps --format '{{.Names}}\t {{.Ports}}' | grep ':${port}' | cut -f1")
}

def getImageName(String containerName) {
    assert containerName != null
    return sh(label: 'Get image name of container',
                returnStdout: true,
                script: "docker inspect --format='{{.Config.Image}}' ${containerName} 2>/dev/null || true")
}

def start(String containerName) {
    assert containerName != null
    sh(label: 'Start Container', script: "docker start ${containerName}")
}

def stop(String containerName) {
    assert containerName != null
    sh(label: 'Stop Running Container', script: "docker stop ${containerName}")
}

def deleteImage(String imageName) {
    assert imageName != null
    sh(label: "Delete image", script: "docker rmi -f ${imageName}")
}

def terminate(String containerName) {
    assert containerName != null
    def imageName = getImageName(containerName)
    sh(label: "Terminate container", script: "docker stop ${containerName} \&\& docker rm -f ${containerName}")
    deleteImage(imageName)
}

def getCurrentState(String port) {
    assert port != null
    def containerName = getNameByPort(port)
    def imageName = getImageName(containerName)
    return [containerName, imageName]
}

def saveVersion(String container) {
    assert container != null
    exec(container, "cat > version.txt << EOL \n"
        + "JOB: ${env.JOB_NAME} \n"
        + "BUILD NUMBER: ${env.BUILD_NUMBER} \n"
        + "BRANCH: ${env.BRANCH_NAME} \n"
        + "EOL", "Saving version logs on server"
    )
}

// TODO: auto get container name from env
def exec(String container, String command, String label = command) {
    assert container != null
    assert command != null
    sh(label: label, script: "docker exec -i ${container} bash -c '${command}'")
}