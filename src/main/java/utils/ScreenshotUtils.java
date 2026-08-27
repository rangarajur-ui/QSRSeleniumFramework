package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtils {

    public static String captureScreenshot(WebDriver driver,String testName) {
        try{
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String path = System.getProperty("user.dir")+ "/reports/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        }catch (IOException e){
            throw new RuntimeException("Failed to capture screenshot for: " + testName, e);
        }
    }
}
