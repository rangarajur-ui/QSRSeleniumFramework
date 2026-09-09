package driver;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Creates one WebDriver per test thread.
 * Browser and headless can be overridden from Jenkins: -Dbrowser=chrome -Dheadless=true
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            String browser = ConfigReader.getProperty("browser").toLowerCase();
            WebDriver driver;

            switch (browser) {
                case "chrome" -> {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(chromeOptions());
                }
                case "firefox" -> {
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions());
                }
                case "edge" -> {
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                }
                default -> throw new RuntimeException("Browser not supported: " + browser);
            }

            int width = ConfigReader.getInt("deviceWidth");
            int height = ConfigReader.getInt("deviceHeight");
            driver.manage().window().setSize(new Dimension(width, height));

            int implicitWait = ConfigReader.getInt("implicitWait");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            DRIVER.set(driver);
        }
        return DRIVER.get();
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--window-size="
                + ConfigReader.getProperty("deviceWidth") + ","
                + ConfigReader.getProperty("deviceHeight"));

        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("-headless");
        }
        return options;
    }

    public static WebDriver getDriverIfPresent() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
