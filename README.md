# Jenkins Shared Library

Contains groovy scripts and auxilliary files to support Jenkins pipelines with global methods and variables. Aimed to act as Shared Library in Jenkins.

* `slackutils.groovy` to send notifications via Slack with custom imojis per build result: success or anything else.
  
* `dockerutils.groovy` Docker commands to build/run/read containers during builds.
  
* `phptest.groovy` utility functions for testing PHP builds; linting and unit-testing.
  
* `utils.groovy` Reads envvars from a file, interpret build/deploy parameters.

### Instructions and Notes

* To make vars documentation readable on _Global Variables Reference_, you need to select `Safe html` in `Markup Formatter` in `Configure Global Security` for Jenkins.
  
* Slack notifications requires [Jenkins plugin for Slack](https://plugins.jenkins.io/slack)

* To read vars from a property file, you may follow the sample file `jenkins.properties` in repo here under resources.
  