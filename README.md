# Cucumber Selenium Automation Demo

A Java 17 test-automation project combining:

- Cucumber for behavior-driven scenarios
- Selenium WebDriver for browser automation
- REST Assured for API testing
- TestNG for suite execution
- Allure for test results
- Jenkins for scheduled CI runs

## Test coverage

- Validate a JSONPlaceholder API response.
- Search YouTube and verify the results page.
- Open a YouTube search result and verify video playback.
- Open a direct YouTube link and verify video playback.

## Run locally

Run the full regression suite:

```bash
mvn test
```

Run headlessly, which is recommended for CI:

```bash
mvn test -Dheadless=true
```

The configured TestNG suite is `src/test/resources/testng.xml`. Allure result
files are written to `target/allure-results`.

## Continuous integration

The included `Jenkinsfile` checks out the repository, runs the Maven test
suite daily, and publishes Allure results when the Jenkins Allure plugin is
available. Jenkins must provide tools named `jdk17` and `maven3`.
