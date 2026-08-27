package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import driver.DriverFactory;
import pages.LandingPage;
import utils.QRCodeReader;
import config.ConfigReader;

public class BaseTest {
    protected WebDriver driver;
    protected LandingPage landingPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.getDriver();

        String qrCodeUrl = ConfigReader.getProperty("qrCodeUrl");
        String catalogUrl = QRCodeReader.readQRFromUrl(qrCodeUrl);

        driver.get(catalogUrl);
        landingPage = new LandingPage(driver);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            // Screenshot capture goes here once we build ScreenshotUtils (later step)
            System.out.println("Test Failed: " + result.getName());
        }
        DriverFactory.quitDriver();
    }

}