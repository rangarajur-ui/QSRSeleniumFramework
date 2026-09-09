# QSR Selenium Framework

Selenium + Java + TestNG framework for the QSR table-ordering flow.
Page Object Model (POM): tests call page methods, pages hold locators.

**New to this repo?** Open the classroom guide in a browser (double-click the file):

[`docs/framework-guide.html`](docs/framework-guide.html)

It explains every folder, class, method, test case, and the exact path from a TestNG XML to Chrome.

## Folder structure

```
QSRSeleniumFramework/
├── config.properties                 # browser, URLs, waits, tester, test data
├── testng.xml                        # full suite (sanity + regression + e2e)
├── Jenkinsfile
├── src/main/java/
│   ├── base/                         # BasePage, BaseTest
│   ├── config/                       # ConfigReader (-D overrides work)
│   ├── constants/                    # TestData (reads config)
│   ├── driver/                       # DriverFactory
│   ├── listeners/                    # TestNG → Extent report
│   ├── pages/                        # Landing, Catalog, Customer, Payment, Order
│   ├── reports/                      # ExtentReportManager
│   ├── api/                          # Catalog API client
│   └── utils/                        # screenshots, QR reader, ReportLogger
├── src/test/java/tests/              # one class per area
└── src/test/resources/suites/        # sanity.xml, regression.xml, e2e.xml
```

## How to run

```bash
# All tests
mvn clean test

# Sanity only
mvn clean test -DsuiteXmlFile=src/test/resources/suites/sanity.xml

# Regression (includes card payment e2e)
mvn clean test -DsuiteXmlFile=src/test/resources/suites/regression.xml

# Payment e2e only
mvn clean test -DsuiteXmlFile=src/test/resources/suites/e2e.xml

# Override config from the command line (Jenkins does this)
mvn clean test -Dbrowser=chrome -Dheadless=true -DtesterName="Rangaraju R" -Denvironment=QA
```

## Report

After a run, open:

`reports/ExtentReport.html`

The dashboard shows tester name, environment, browser, OS, Java, and each test’s steps plus screenshots.

## Suites (TestNG groups)

| Group        | What it covers                                      |
|--------------|-----------------------------------------------------|
| `sanity`     | Welcome, add to cart, customer form, payment total, API 200 |
| `regression` | Qty +/-, search, tip, bill, change payment, API vs UI, full order |
| `e2e`        | Card payment through to order confirmation          |

UI tests run **one after another** because they share the same table QR.

## Jenkins

1. New Pipeline job → Pipeline script from SCM → this repo → `Jenkinsfile`.
2. Install plugins: **Pipeline**, **HTML Publisher**, **JUnit**, **AnsiColor**.
3. Configure Maven and JDK 17 on the agent (or put `mvn` / `java` on PATH).
4. Build with Parameters: suite (`sanity`, `regression`, `e2e`, `full-suite`), browser, tester name, environment.

## Config

Edit `config.properties` for URLs, menu items, customer details, and the sandbox card.
Any key can be overridden with `-Dkey=value`.
