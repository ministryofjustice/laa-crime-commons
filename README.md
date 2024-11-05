# Laa-Crime-Commons

This repository contains shared libraries that support laa crime microservices.

It currently contains a single module (**crime-commons-spring-boot-starter**) that autoconfigures web clients for
communicating with other services and also provides several related utility classes.

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/ministryofjustice/laa-crime-commons/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/ministryofjustice/laa-crime-commons/tree/main)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ministryofjustice_laa-crime-commons&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ministryofjustice_laa-crime-commons)

[![repo standards badge](https://img.shields.io/endpoint?labelColor=231f20&color=005ea5&style=for-the-badge&label=MoJ%20Compliant&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fendpoint%2Fgithub-repository-standards&logo=data:image/png;base64,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)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-github-repositories.html#laa-crime-commons)

## Getting Started

### Pre-requisites

- JDK 17
- Spring Boot 3.0.4

Install dependencies and build modules with Gradle:

```
./gradlew clean build
```

### Usage

To use a published package from GitHub Packages, add the package as a dependency and add the repository to your project
For more information, see "[Declaring dependencies](https://docs.gradle.org/current/userguide/declaring_dependencies.html)" in the Gradle documentation.

1. Authenticate to GitHub Packages
2. Add the package dependencies to your build.gradle file

```groovy
dependencies {
    implementation 'com.example:package'
}
```

3. Add the repository to your build.gradle file

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
        credentials {
            password = project.findProperty("github-token") ?: System.getenv("GITHUB_TOKEN")
            username = project.findProperty("github-username") ?: System.getenv("GITHUB_USERNAME")
        }
    }
}
```

### GitHub Authentication

You will need to create a `gradle.properties` file, specifying values for the `github-username` and `github-token` properties.
These can also be set as environment variables.

### Coverage

The Jacoco Gradle plugin is used to generate coverage reports:

```
./gradlew jacocoTestReport
```

These are used by SonarCloud to evaluate code quality and maintainability. Jacoco coverage reports are auto generated when builds
are performed through CircleCI or new changes are merged into the main branch.

Click the SonarCloud badge above to view these metrics.

## Releases

### Publishing Artefacts

Each time changes are merged into the main branch, a new release is tagged and published to GitHub Packages.
To perform a manual release, you will need to authenticate with GitHub (see above).

This enables you to run the following command:

```
./gradlew publish
```

Releases can also be published from branches, however, the version number will be auto-generated and requires manual approval through CircleCI.
See below for more information on our versioning strategy for published modules.

### Version Management

The Semver Gradle Plugin is used to implement [Semantic versioning](https://semver.org/).

Once the Jar files for each module have been built in CircleCI, the latest commit will be tagged with
a new version number, by default, the minor version number is incremented. However, It is possible to manually bump other parts of the version string:

#### For 'crime-commons-spring-boot-starter-rest-client' module:

- bump the major number: `./gradlew "-Psemver.stage=final" "-Psemver.scope=major" "-Psemver.tagPrefix=rest-client-"`
- bump the patch number: `./gradlew "-Psemver.stage=final" "-Psemver.scope=patch" "-Psemver.tagPrefix=rest-client-"`

#### For 'crime-commons-classes' module:

- bump the major number: `./gradlew "-Psemver.stage=final" "-Psemver.scope=major" "-Psemver.tagPrefix=crime-commons-classes-"`
- bump the patch number: `./gradlew "-Psemver.stage=final" "-Psemver.scope=patch" "-Psemver.tagPrefix=crime-commons-classes-"`

New releases will be automatically tagged and published when changes are merged into the main branch. Artifacts can also be published
from feature branches, however, these require manual approval through CircleCI.

### Publishing Locally

You can publish crime commons sub modules to a local maven repository, if you only wish to test any changes made locally. This can be quicker and easier and avoid duplicate versioning issues for remote repositories. To publish locally you will need to perform the following steps:

1. Add the following configuration to disable signing for local publishing to the `build.gradle` of the sub module you wish to publish:

```groovy
tasks.withType(Sign) {
    onlyIf { !gradle.taskGraph.hasTask(":${project.name}:publishMavenPublicationToMavenLocal") }
}
```

2. Run the following command to generate a snapshot version and publish a sub module to the local Maven repository (replace `<sub_module_name>` with the name of the sub module being published):

```shell
./gradlew pushSemverTag "-PisSnapshot=true" "-Psemver.stage=snapshot" "-Psemver.scope=patch" "-Psemver.tagPrefix=<sub_module_name>-" :<sub_module_name>:publishToMavenLocal
```

3. Once the above script has been run, in order for a micro service to then use this locally published dependency add `mavenLocal()` to the list of repositories in the `build.gradle` file and update the version of the dependency.

## Contributing

Bug reports and pull requests are welcome. However, please make sure your changes are covered by tests and follow modern software development best practices.
Otherwise, it is likely that your changes will be rejected.

1. Clone the project (`git clone https://github.com/ministryofjustice/laa-crime-commons.git`)
2. Create a feature branch (`git checkout -b my-new-feature`)
3. Commit until you are happy with your contribution (`git commit -am 'Add some feature'`)
4. Push the branch (`git push origin my-new-feature`)
5. Open a new pull request, specifying your changes in detail.
