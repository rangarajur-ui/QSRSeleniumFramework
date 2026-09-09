package listeners;

import com.aventstack.extentreports.Status;
import config.ConfigReader;
import constants.TestData;
import driver.DriverFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentReportManager;
import utils.ScreenshotUtils;

/**
 * Writes every TestNG result into the Extent report:
 * tester, environment, groups (sanity/regression/e2e), steps, and failure screenshots.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getInstance();
        context.setAttribute("tester", TestData.TESTER_NAME);
        context.setAttribute("environment", TestData.ENVIRONMENT);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentReportManager.startTest(result.getMethod().getMethodName(), description);
        ExtentReportManager.assignCategory(result.getMethod().getGroups());
        ExtentReportManager.info("Tester: " + TestData.TESTER_NAME);
        ExtentReportManager.info("Environment: " + TestData.ENVIRONMENT);
        ExtentReportManager.info("Browser: " + ConfigReader.getProperty("browser"));
        ExtentReportManager.info("Class: " + result.getTestClass().getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.pass("Test passed — " + result.getMethod().getMethodName());
        }
        ExtentReportManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            if (result.getThrowable() != null) {
                ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());
            } else {
                ExtentReportManager.fail("Test failed");
            }
            try {
                if (DriverFactory.getDriverIfPresent() != null) {
                    String screenshotPath = ScreenshotUtils.captureScreenshot(
                            DriverFactory.getDriverIfPresent(), result.getMethod().getMethodName() + "_FAILURE");
                    ExtentReportManager.addScreenCapture(screenshotPath);
                }
            } catch (Exception e) {
                ExtentReportManager.info("Could not capture failure screenshot: " + e.getMessage());
            }
        }
        ExtentReportManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.skip("Test skipped — " + result.getMethod().getMethodName());
        if (result.getThrowable() != null && ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.SKIP, result.getThrowable());
        }
        ExtentReportManager.endTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }
}
