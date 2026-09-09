package base;

import config.ConfigReader;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import constants.TestData;
import pages.CatalogPage;
import pages.CustomerDetailsPage;
import pages.LandingPage;
import pages.PaymentSummaryPage;
import utils.QRCodeReader;
import utils.ReportLogger;

/**
 * Every UI test extends this class.
 * Opens the catalog from the table QR before each test and closes the browser after.
 */
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
        DriverFactory.quitDriver();
    }

    /**
     * Welcome screen only appears when the table has no active order.
     * Use this instead of repeating the if/else in every test.
     */
    protected CatalogPage openCatalog() {
        if (landingPage.isWelcomeScreenDisplayed()) {
            ReportLogger.info("Welcome screen is shown — starting a new order");
            return landingPage.clickStartYourOrder();
        }
        ReportLogger.info("Welcome screen not shown — table already has an active session, opening catalog");
        return new CatalogPage(driver);
    }

    protected PaymentSummaryPage goToPaymentSummary() {
        CatalogPage catalog = openCatalog();
        catalog.addItemToCart(TestData.MENU_ITEM);
        CustomerDetailsPage details = catalog.clickViewCart();
        details.fillCustomerDetails(TestData.CUSTOMER_NAME, TestData.CUSTOMER_MOBILE);
        return details.clickProceed();
    }

    protected void logStep(String message) {
        ReportLogger.step(message);
    }
}
