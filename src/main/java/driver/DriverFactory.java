package driver;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver(){
        if(driverThread.get()== null){
            String brwoser = ConfigReader.getProperty("browser").toLowerCase();
            WebDriver driver;

            switch (brwoser){
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(getChromeOptions());
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new RuntimeException("Browser not supported"+brwoser);
            }

            driver.manage().window().maximize();
            int implicitWait = Integer.parseInt(ConfigReader.getProperty("implicitWait"));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            driverThread.set(driver);
        }
        return driverThread.get();
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        return options;
    }

    public static void quitDriver(){
        WebDriver driver = driverThread.get();
        if(driver != null){
            driver.quit();
            driverThread.remove();
        }
    }
}