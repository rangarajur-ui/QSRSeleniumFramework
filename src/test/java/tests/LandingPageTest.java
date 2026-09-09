package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.CatalogPage;
import utils.ReportLogger;

public class LandingPageTest extends BaseTest {

    @Test(groups = {"sanity", "landing"},
            description = "Welcome screen shows table information when the table is free")
    public void verifyWelcomeMessageShowsTableInfo() {
        logStep("Check whether the welcome / table screen is displayed");
        if (!landingPage.isWelcomeScreenDisplayed()) {
            throw new SkipException("Welcome screen not shown — table already has an active order");
        }

        String welcomeMessage = landingPage.getWelcomeMessage();
        ReportLogger.info("Welcome message: " + welcomeMessage);
        Assert.assertTrue(welcomeMessage.toLowerCase().contains("table"),
                "Landing page did not show table info. Actual: " + welcomeMessage);
        ReportLogger.pass("Welcome message contains table info");
        ReportLogger.screenshot(driver, "Landing_Welcome");
    }

    @Test(groups = {"sanity", "landing"},
            description = "Start Your Order opens the restaurant catalog")
    public void verifyStartOrderOpensCatalog() {
        logStep("Open catalog from landing page");
        CatalogPage catalogPage = openCatalog();

        logStep("Verify restaurant name is visible on catalog");
        String restaurantName = catalogPage.getRestaurantName();
        ReportLogger.info("Restaurant name: " + restaurantName);
        Assert.assertFalse(restaurantName.isBlank(), "Restaurant name should be visible after starting the order");
        ReportLogger.pass("Catalog opened — restaurant: " + restaurantName);
        ReportLogger.screenshot(driver, "Landing_CatalogOpened");
    }
}
