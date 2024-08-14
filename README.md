# anti-fraud-test-automation
Repository for test automation of anti-fraud

# Used tech stack
- ui tests for backoffice:
    - junit
    - playwright

- api tests for backoffice:
  - rest
    - junit
    - okhttp

- api tests for core project:
- rest
    - junit
    - okhttp
-


# Hot to run the uiTests:
    # ./gradlew test - runs all the tests

    # ./gradlew allureServe - build allure report

    # ./gradlew allureReport --depends-on-tests - run all tests and generate reports

    # allure open - open allure report