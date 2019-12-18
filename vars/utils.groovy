def loadEnvvars(String path) {
    try {
        node {
            checkout scm
            Map envvars = readProperties(interpolate: true, file: path) ?: null
            if (!envvars) { throw new Exception("unable to read properties file " + path) }
            keys= envvars.keySet()
            for(key in keys) {
                value = envvars["${key}"]
                env."${key}" = "${value}"
            }

            env.ENV = getENV()
            env.ENV_NAME = getEnvName()
            env.DOMAIN = getDomain()
            env.BUILD_LABEL = getBuildLabel()
            env.AGENT_LABEL = getAgentLabel()

            env.IMAGE_TAG = "${BUILD_ID}"
            env.CONTAINER_NAME = "${DOCKER_IMAGE}_${IMAGE_TAG}"
            env.LOCAL_IMAGE_NAME = "${DOCKER_IMAGE}:${IMAGE_TAG}"
            env.REGISTRY_IMAGE_NAME = "${DOCKER_REGISTRY}/${LOCAL_IMAGE_NAME}"
            
            env.DOCKER_REGISTRY_URL = "https://${DOCKER_REGISTRY}"
            env.ECR_REGISTRY_CREDENTIAL = "ecr:${AWS_REGION}:${AWS_CREDENTIALS}"

            return envvars
        }
    }
    catch(err) {
        echo "ERROR: caught in loadEnvvars ${err}"
    }
}

def getENV() {
    if (env."ENV_${BRANCH_NAME.toUpperCase()}") {
        return env."ENV_${BRANCH_NAME.toUpperCase()}"
    }
    return env.ENV_DEVELOP
}

def getEnvName() {
    if (env."NAME_${env.ENV}") {
        return env."NAME_${env.ENV}"
    }
    return env.NAME_DEV
}

def getBuildLabel() {
    if (env."AGENT_BUILD") {
        return env."AGENT_BUILD"
    }
    return env.AGENT_DEV
}

def getAgentLabel() {
    if (env."AGENT_${env.ENV}") {
        return env."AGENT_${env.ENV}"
    }
    return env.AGENT_DEV
}

def getDomain() {
    return 'https://' + env."DOMAIN_${env.ENV.toUpperCase()}"
}

def curlstatus(String url) {
    assert url != null
    int status = sh(label: 'Domain Check',
                    returnStdout: true,
                    script: "curl -sLI -w '%{http_code}' ${url} -o /dev/null"
                )
    if (status != 200) {
        error("Returned status code = $status when calling ${url}")
    }
    return "success"
}