package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentReportManager;
import utils.ScreenshotUtils;
import driver.DriverFactory;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        try {
            String screenshotPath = ScreenshotUtils.captureScreenshot(
                    DriverFactory.getDriver(), result.getMethod().getMethodName() + "_FAILURE"
            );
            ExtentReportManager.addScreenCapture(screenshotPath);
        } catch (Exception e) {
            System.out.println("Could not capture failure screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.endTest(0);
    }
}