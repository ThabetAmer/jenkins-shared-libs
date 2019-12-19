def getNameByPort(String port) {
    if (!port?.trim()) { return false }
    return sh(label: 'Get container ID by port',
                returnStdout: true,
                script: "docker ps --format '{{.Names}}\t {{.Ports}}' | grep ':${port}' | cut -f1").trim()
}

def getImageName(String containerName) {
    if (!containerName?.trim()) { return false }
    return sh(label: 'Get image name of container',
                returnStdout: true,
                script: "docker inspect --format='{{.Config.Image}}' ${containerName} 2>/dev/null || true").trim()
}

def start(String containerName) {
    if (!containerName?.trim()) { return false }
    sh(label: 'Start Container', script: "docker start ${containerName}")
}

def stop(String containerName) {
    if (!containerName?.trim()) { return false }
    sh(label: 'Stop Running Container', script: "docker stop ${containerName}")
}

def deleteImage(String imageName) {
    if (!imageName?.trim()) { return false }
    sh(label: "Delete image", script: "docker rmi -f ${imageName}")
}

def terminate(String containerName) {
    if (!containerName?.trim()) { return false }
    def imageName = getImageName(containerName)
    sh(label: "Terminate container", script: "docker stop ${containerName} && docker rm -f ${containerName}")
    deleteImage(imageName)
}

def getCurrentState(String port) {
    if (!port?.trim()) { return false }
    def containerName = getNameByPort(port)
    def imageName = getImageName(containerName)
    return [containerName, imageName]
}

def getRecentLogs(String containerName) {
    if (!containerName?.trim()) { return false }
    return sh(label: 'Container logs', returnStdout: true,
                script: "docker logs ${containerName}").trim()
}

def getStatus(String containerName) {
    if (!containerName?.trim()) { return false }
    return sh(label: 'Container Status', returnStdout: true, 
                script:"docker inspect --format '{{.State.Status}}' ${containerName}").trim()
}

def saveVersion(String containerName, String path = "public") {
    if (!containerName?.trim()) { return false }
    buildDate = new Date().format("dd/MM/yyyy HH:mm:ss")
    exec(containerName, "cat > ${path}/version.txt << EOL \n"
        + "JOB: ${env.JOB_NAME} \n"
        + "BUILD NUMBER: ${env.BUILD_NUMBER} \n"
        + "BRANCH: ${env.BRANCH_NAME} \n"
        + "BUILD DATE: ${buildDate} \n"
        + "EOL", "Saving version logs on server"
    )
}

// TODO: auto get container name from env
def exec(String containerName, String command, String label = command) {
    if (!containerName?.trim() || !command?.trim()) { return false }
    sh(label: label, script: "docker exec -i ${containerName} bash -c '${command}'")
}