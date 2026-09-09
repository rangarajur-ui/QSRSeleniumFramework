package utils;

import org.openqa.selenium.WebDriver;
import reports.ExtentReportManager;

/**
 * Thin wrapper so tests can log steps without touching Extent APIs.
 *
 *   ReportLogger.step("Add item to cart");
 *   ReportLogger.pass("Quantity updated to 1");
 *   ReportLogger.screenshot(driver, "CartAfterAdd");
 */
public final class ReportLogger {

    private ReportLogger() {
    }

    public static void step(String message) {
        ExtentReportManager.step(message);
    }

    public static void info(String message) {
        ExtentReportManager.info(message);
    }

    public static void pass(String message) {
        ExtentReportManager.pass(message);
    }

    public static void fail(String message) {
        ExtentReportManager.fail(message);
    }

    public static void screenshot(WebDriver driver, String name) {
        String path = ScreenshotUtils.captureScreenshot(driver, name);
        ExtentReportManager.addScreenCapture(path);
    }
}
