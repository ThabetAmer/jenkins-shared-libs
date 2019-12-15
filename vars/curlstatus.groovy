def call(String url) {
    assert url != null
    int status = sh(label: 'Domain Check',
                    returnStdout: true,
                    script: "curl -sLI -w '%{http_code}' ${url} -o /dev/null"
                )
    if (status != 200) {
        error("Returned status code = $status when calling $url")
    }
    return "success"
}