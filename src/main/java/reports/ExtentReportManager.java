package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.ConfigReader;
import constants.TestData;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * One Extent report for the whole suite.
 * Each test thread gets its own ExtentTest (safe for parallel runs).
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();
    private static final ThreadLocal<Integer> STEP = ThreadLocal.withInitial(() -> 0);

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = System.getProperty("user.dir") + File.separator + "reports";
            new File(reportDir).mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(reportDir + File.separator + "ExtentReport.html");
            spark.config().setDocumentTitle("QSR Automation Report");
            spark.config().setReportName("QSR Selenium — " + TestData.ENVIRONMENT + " Results");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setTimeStampFormat("dd-MMM-yyyy hh:mm:ss a");
            spark.config().setEncoding("utf-8");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Tester", TestData.TESTER_NAME);
            extent.setSystemInfo("Environment", TestData.ENVIRONMENT);
            extent.setSystemInfo("Application", TestData.APPLICATION_NAME);
            extent.setSystemInfo("Browser", TestData.BROWSER);
            extent.setSystemInfo("Headless", ConfigReader.getProperty("headless"));
            extent.setSystemInfo("Base URL", ConfigReader.getProperty("baseUrl"));
            extent.setSystemInfo("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Executed on", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")));
        }
        return extent;
    }

    public static void startTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description == null ? "" : description);
        extentTest.assignAuthor(TestData.TESTER_NAME);
        extentTest.assignDevice(TestData.BROWSER);
        TEST.set(extentTest);
        STEP.set(0);
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void assignCategory(String... groups) {
        if (getTest() == null || groups == null) {
            return;
        }
        for (String group : groups) {
            if (group != null && !group.isBlank()) {
                getTest().assignCategory(group);
            }
        }
    }

    public static void step(String message) {
        if (getTest() == null) {
            return;
        }
        int next = STEP.get() + 1;
        STEP.set(next);
        getTest().log(Status.INFO, MarkupHelper.createLabel("Step " + next + " — " + message, ExtentColor.BLUE));
    }

    public static void info(String message) {
        if (getTest() != null) {
            getTest().log(Status.INFO, message);
        }
    }

    public static void pass(String message) {
        if (getTest() != null) {
            getTest().log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
        }
    }

    public static void fail(String message) {
        if (getTest() != null) {
            getTest().log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
        }
    }

    public static void skip(String message) {
        if (getTest() != null) {
            getTest().log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.ORANGE));
        }
    }

    public static void addScreenCapture(String screenshotPath) {
        if (getTest() == null || screenshotPath == null) {
            return;
        }
        try {
            String relative = toReportRelativePath(screenshotPath);
            getTest().info("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(relative).build());
        } catch (Exception e) {
            getTest().warning("Could not attach screenshot: " + e.getMessage());
        }
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void endTest() {
        TEST.remove();
        STEP.remove();
    }

    private static String toReportRelativePath(String absolutePath) {
        String marker = "reports" + File.separator;
        int index = absolutePath.lastIndexOf(marker);
        if (index >= 0) {
            return absolutePath.substring(index + marker.length());
        }
        return absolutePath;
    }
}
