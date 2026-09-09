package tests;

import base.BaseTest;
import constants.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import pages.CustomerDetailsPage;
import utils.ReportLogger;

public class CustomerDetailsTest extends BaseTest {

    @Test(groups = {"sanity", "customer"},
            description = "Proceed stays disabled until name and mobile are filled")
    public void verifyProceedDisabledUntilDetailsFilled() {
        CatalogPage catalogPage = openCatalog();

        logStep("Add item and open customer details");
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        CustomerDetailsPage detailsPage = catalogPage.clickViewCart();
        Assert.assertTrue(detailsPage.isNameFieldDisplayed(), "Name field should be visible");
        Assert.assertFalse(detailsPage.isProceedEnabled(), "Proceed should start disabled");
        ReportLogger.pass("Proceed is disabled on an empty form");

        logStep("Fill valid customer details");
        detailsPage.fillCustomerDetails(TestData.CUSTOMER_NAME, TestData.CUSTOMER_MOBILE);
        Assert.assertTrue(detailsPage.isProceedEnabled(), "Proceed should enable after valid input");
        ReportLogger.pass("Proceed enabled after name + mobile");
        ReportLogger.screenshot(driver, "CustomerDetails_Filled");
    }

    @Test(groups = {"regression", "customer"},
            description = "Name alone is not enough to enable Proceed")
    public void verifyNameAloneDoesNotEnableProceed() {
        CatalogPage catalogPage = openCatalog();

        logStep("Open customer details with one item in cart");
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        CustomerDetailsPage detailsPage = catalogPage.clickViewCart();

        logStep("Enter name only");
        detailsPage.enterName(TestData.CUSTOMER_NAME);
        Assert.assertFalse(detailsPage.isProceedEnabled(),
                "Proceed should stay disabled when mobile is empty");
        ReportLogger.pass("Proceed still disabled after name only");
        ReportLogger.screenshot(driver, "CustomerDetails_NameOnly");
    }
}
