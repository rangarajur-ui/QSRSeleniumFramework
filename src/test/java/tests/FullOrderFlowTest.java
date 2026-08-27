package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import pages.CustomerDetailsPage;
import pages.OrderCompletionPage;
import pages.PaymentSummaryPage;
import utils.ScreenshotUtils;

public class FullOrderFlowTest extends BaseTest {

    @Test
    public void verifyFullOrderFlowEndToEnd() {

        // ---------- Step 1: Landing Page ----------
        if (landingPage.isWelcomeScreenDisplayed()) {
            String welcomeMessage = landingPage.getWelcomeMessage();
            System.out.println("Welcome message: " + welcomeMessage);
            Assert.assertTrue(welcomeMessage.contains("table"), "Landing page did not show table info");
            landingPage.clickStartYourOrder();
        } else {
            System.out.println("Welcome screen not shown — table likely has an active order already.");
        }
        ScreenshotUtils.captureScreenshot(driver, "01_LandingPage");

        // ---------- Step 2: Catalog Page ----------
        CatalogPage catalogPage = new CatalogPage(driver);
        String itemName = "Chicken Tikka Biryani (Boneless)";

        String price = catalogPage.getItemPrice(itemName);
        System.out.println("Item price: " + price);

        catalogPage.addItemToCart(itemName);
        String qty = catalogPage.getItemQuantity(itemName);
        System.out.println("Quantity after add: " + qty);
        Assert.assertEquals(qty, "1", "Quantity did not update after adding item");

        ScreenshotUtils.captureScreenshot(driver, "02_CatalogPage_ItemAdded");

        // ---------- Step 3: View Cart -> Customer Details ----------
        CustomerDetailsPage detailsPage = catalogPage.clickViewCart();

        Assert.assertFalse(detailsPage.isProceedEnabled(), "Proceed should start disabled");
        detailsPage.enterName("Rangaraju R");
        detailsPage.enterMobileNumber("501234567");
        Assert.assertTrue(detailsPage.isProceedEnabled(), "Proceed should enable after valid input");

        ScreenshotUtils.captureScreenshot(driver, "03_CustomerDetailsPage_Filled");

        // ---------- Step 4: Proceed -> Payment Summary Page ----------
        PaymentSummaryPage paymentPage = detailsPage.clickProceed();
        paymentPage.openBillDetails();


        String totalPay = paymentPage.getTotalPay();
        System.out.println("Total pay before tip: " + totalPay);

        paymentPage.clickcloseviewbill();

        paymentPage.selectTenDirhamTip();
        ScreenshotUtils.captureScreenshot(driver, "04_PaymentSummaryPage_TipAdded");

        // ---------- Step 5: Place Order -> Payment Gateway ----------
        paymentPage.clickchangepayment();
        paymentPage.clickdebitorcredit();


        paymentPage.switchToPaymentFrame();
        paymentPage.enterCardDetails("4000 0000 0000 0002", "1230", "123");
        ScreenshotUtils.captureScreenshot(driver, "05_PaymentGateway_CardEntered");

        paymentPage.clickPayButton();

        // NOTE: we don't yet know if payment navigates away or updates in-page.
        // Leaving this commented until we confirm actual behavior:
        paymentPage.switchBackToMainPage();

        ScreenshotUtils.captureScreenshot(driver, "06_PaymentCompletion");

        OrderCompletionPage orderpage = new OrderCompletionPage(driver);
        String brandN = orderpage.getBrandName();

        String orderID = orderpage.getOrderId();
        orderpage.openBillDetails();
        System.out.println(orderID);
        System.out.println("Brand " + brandN);

        System.out.println("Full order flow completed. Current URL: " + driver.getCurrentUrl());
    }
}