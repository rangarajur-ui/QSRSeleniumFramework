package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String path = System.getProperty("user.dir")
                    + File.separator + "reports"
                    + File.separator + "screenshots"
                    + File.separator + safeName + "_" + LocalDateTime.now().format(TIME) + ".png";
            File dest = new File(path);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot for: " + testName, e);
        }
    }
}
