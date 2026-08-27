package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import pages.CustomerDetailsPage;
import pages.PaymentSummaryPage;
import utils.ScreenshotUtils;

public class CatalogNavigationTest extends BaseTest {

    @Test
    public void verifyLandingPageAndNavigateToCatalog() {
        if (landingPage.isWelcomeScreenDisplayed()) {
            String welcomeMessage = landingPage.getWelcomeMessage();
            System.out.println("Welcome message: " + welcomeMessage);
            Assert.assertTrue(welcomeMessage.contains("table"), "Landing page did not show table info");
            landingPage.clickStartYourOrder();
        } else {
            System.out.println("Welcome screen not shown — table likely already has an active order. Skipping welcome assertion.");
        }

        ScreenshotUtils.captureScreenshot(driver, "LandingOrCatalogPage");
    }

    @Test
    public void verifyCanAddItemToCart() {
        landingPage.clickStartYourOrder();
        CatalogPage catalogPage = new CatalogPage(driver);

        String itemName = "Chicken Tikka Biryani (Boneless)";
        String price = catalogPage.getItemPrice(itemName);
        System.out.println("Price: " + price);

        catalogPage.addItemToCart(itemName);
        String qty = catalogPage.getItemQuantity(itemName);
        System.out.println("Quantity after add: " + qty);

        Assert.assertEquals(qty, "1", "Quantity did not update after adding item");

        ScreenshotUtils.captureScreenshot(driver, "AfterAddToCart");
    }

    @Test
    public void verifyFullFlowToPaymentSummary() {
        if (landingPage.isWelcomeScreenDisplayed()) {
            landingPage.clickStartYourOrder();
        }

        CatalogPage catalogPage = new CatalogPage(driver);
        String itemName = "Chicken Tikka Biryani (Boneless)";
        catalogPage.addItemToCart(itemName);

        CustomerDetailsPage detailsPage = catalogPage.clickViewCart();

        Assert.assertFalse(detailsPage.isProceedEnabled(), "Proceed should start disabled");
        detailsPage.enterName("Rangaraju R");
        detailsPage.enterMobileNumber("501234567");
        Assert.assertTrue(detailsPage.isProceedEnabled(), "Proceed should enable after valid input");

        PaymentSummaryPage paymentPage = detailsPage.clickProceed();

        ScreenshotUtils.captureScreenshot(driver, "PaymentSummaryLanding");
    }
}