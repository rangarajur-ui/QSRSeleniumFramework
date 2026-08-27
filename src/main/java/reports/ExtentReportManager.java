package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("QSR Automation Report");
            sparkReporter.config().setReportName("QSR Selenium Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System info shown at the top of the report dashboard
            extent.setSystemInfo("Tester", "Rangaraju R");
            extent.setSystemInfo("Environment", "QA / Staging");
            extent.setSystemInfo("Browser", config.ConfigReader.getProperty("browser"));
            extent.setSystemInfo("Application", "QSR Ordering - Soren Brand");
        }
        return extent;
    }

    public static void startTest(String testName) {
        ExtentTest extentTest = getInstance().createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void endTest(int status) {
        getInstance().flush();
    }

    public static void addScreenCapture(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            System.out.println("Could not attach screenshot to report: " + e.getMessage());
        }
    }
}