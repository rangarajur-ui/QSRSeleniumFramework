package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PaymentSummaryPage;
import utils.ReportLogger;

public class PaymentSummaryTest extends BaseTest {

    @Test(groups = {"sanity", "payment"},
            description = "Payment summary shows a total after customer details")
    public void verifyPaymentSummaryShowsTotal() {
        logStep("Reach payment summary with one item");
        PaymentSummaryPage paymentPage = goToPaymentSummary();

        logStep("Read payable total");
        Assert.assertTrue(paymentPage.isTotalDisplayed(), "Total pay should be visible");
        String totalPay = paymentPage.getTotalPay();
        ReportLogger.info("Total pay: " + totalPay);
        Assert.assertFalse(totalPay.isBlank(), "Total pay should not be empty");
        ReportLogger.pass("Payment summary total is displayed: " + totalPay);
        ReportLogger.screenshot(driver, "Payment_Total");
    }

    @Test(groups = {"regression", "payment"},
            description = "Bill details drawer can be opened and closed")
    public void verifyBillDetailsCanBeOpened() {
        PaymentSummaryPage paymentPage = goToPaymentSummary();

        logStep("Open bill details");
        paymentPage.openBillDetails();
        String totalPay = paymentPage.getTotalPay();
        ReportLogger.info("Total inside bill: " + totalPay);
        Assert.assertFalse(totalPay.isBlank(), "Bill details should show a total");

        logStep("Close bill details");
        paymentPage.closeBillDetails();
        ReportLogger.pass("Bill details opened and closed");
        ReportLogger.screenshot(driver, "Payment_BillDetails");
    }

    @Test(groups = {"regression", "payment"},
            description = "AED 10 tip can be selected on payment summary")
    public void verifyTenDirhamTipCanBeAdded() {
        PaymentSummaryPage paymentPage = goToPaymentSummary();

        logStep("Capture total before tip");
        String beforeTip = paymentPage.getTotalPay();
        ReportLogger.info("Total before tip: " + beforeTip);

        logStep("Select AED 10 tip");
        paymentPage.selectTenDirhamTip();
        String afterTip = paymentPage.getTotalPay();
        ReportLogger.info("Total after tip: " + afterTip);
        Assert.assertFalse(afterTip.isBlank(), "Total should still be visible after tip");
        ReportLogger.pass("Tip applied");
        ReportLogger.screenshot(driver, "Payment_Tip");
    }

    @Test(groups = {"regression", "payment"},
            description = "Change payment opens Debit/Credit card option")
    public void verifyChangePaymentToCard() {
        PaymentSummaryPage paymentPage = goToPaymentSummary();

        logStep("Open change payment");
        Assert.assertTrue(paymentPage.isChangePaymentDisplayed(), "Change payment should be visible");
        paymentPage.clickChangePayment();

        logStep("Select Debit/Credit Cards");
        paymentPage.selectDebitOrCreditCard();
        ReportLogger.pass("Card payment option selected");
        ReportLogger.screenshot(driver, "Payment_ChangeMethod");
    }
}
