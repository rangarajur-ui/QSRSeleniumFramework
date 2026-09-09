package tests;

import base.BaseTest;
import constants.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import pages.CustomerDetailsPage;
import pages.OrderCompletionPage;
import pages.PaymentSummaryPage;
import utils.ReportLogger;

public class FullOrderFlowTest extends BaseTest {

    @Test(groups = {"e2e", "regression", "payment"},
            description = "End-to-end: catalog → customer details → tip → card payment → order confirmation")
    public void verifyFullOrderFlowEndToEnd() {
        logStep("Open catalog from QR / welcome screen");
        CatalogPage catalogPage = openCatalog();
        ReportLogger.screenshot(driver, "01_LandingOrCatalog");

        logStep("Add " + TestData.MENU_ITEM + " to cart");
        String price = catalogPage.getItemPrice(TestData.MENU_ITEM);
        ReportLogger.info("Item price: " + price);
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        String qty = catalogPage.getItemQuantity(TestData.MENU_ITEM);
        Assert.assertEquals(qty, "1", "Quantity did not update after adding item");
        ReportLogger.pass("Cart quantity is 1");
        ReportLogger.screenshot(driver, "02_Catalog_ItemAdded");

        logStep("Open customer details from View Cart");
        CustomerDetailsPage detailsPage = catalogPage.clickViewCart();
        Assert.assertFalse(detailsPage.isProceedEnabled(), "Proceed should start disabled");

        logStep("Enter customer name and mobile");
        detailsPage.fillCustomerDetails(TestData.CUSTOMER_NAME, TestData.CUSTOMER_MOBILE);
        Assert.assertTrue(detailsPage.isProceedEnabled(), "Proceed should enable after valid input");
        ReportLogger.pass("Customer details accepted");
        ReportLogger.screenshot(driver, "03_CustomerDetails");

        logStep("Open payment summary and bill details");
        PaymentSummaryPage paymentPage = detailsPage.clickProceed();
        paymentPage.openBillDetails();
        String totalPay = paymentPage.getTotalPay();
        ReportLogger.info("Total pay before tip: " + totalPay);
        Assert.assertFalse(totalPay.isBlank(), "Total pay should be visible");
        paymentPage.closeBillDetails();

        logStep("Add AED 10 tip");
        paymentPage.selectTenDirhamTip();
        ReportLogger.screenshot(driver, "04_Payment_TipAdded");

        logStep("Pay with test debit/credit card");
        paymentPage.clickChangePayment();
        paymentPage.selectDebitOrCreditCard();
        paymentPage.switchToPaymentFrame();
        paymentPage.enterCardDetails(TestData.CARD_NUMBER, TestData.CARD_EXPIRY, TestData.CARD_CVV);
        ReportLogger.screenshot(driver, "05_Payment_CardEntered");
        paymentPage.clickPayButton();
        paymentPage.switchBackToMainPage();

        logStep("Verify order confirmation");
        OrderCompletionPage orderPage = new OrderCompletionPage(driver);
        Assert.assertTrue(orderPage.waitForOrderCompletion(), "Order confirmation did not appear after payment");
        String brandName = orderPage.getBrandName();
        String orderId = orderPage.getOrderId();
        ReportLogger.info("Brand: " + brandName);
        ReportLogger.info("Order ID: " + orderId);
        Assert.assertFalse(brandName.isBlank(), "Brand name should be shown on confirmation");
        Assert.assertFalse(orderId.isBlank(), "Order ID should be shown on confirmation");
        orderPage.openBillDetails();
        ReportLogger.pass("Full order flow completed. Order ID: " + orderId);
        ReportLogger.screenshot(driver, "06_OrderCompletion");
        ReportLogger.info("Current URL: " + driver.getCurrentUrl());
    }
}
