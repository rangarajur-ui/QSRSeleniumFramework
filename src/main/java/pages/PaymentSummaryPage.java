package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentSummaryPage extends BasePage {

    private final By viewAllCouponsBtn = By.xpath("//button[contains(@class,'viewAllFooter')]");
    private final By viewBillDetailsBtn = By.xpath("//button[contains(@class,'viewBillLink')]");
    private final By totalPayValue = By.xpath("//span[contains(@class,'styles_totalValue__V88sU')]");
    private final By closeViewBill = By.xpath("//button[@class='styles_closeButton__6UghF']");
    private final By tipTenButton = By.xpath("//button[contains(@class,'tipButton')][1]");
    private final By changePayment = By.xpath("//button[@class='styles_changeButton__9OJLM']");
    private final By debitOrCredit = By.xpath("//span[contains(text(),'Debit/Credit Cards')]");
    private final By placeOrderButton = By.xpath("//button[contains(@class,'qsrPlaceOrderButton')]");
    private final By paymentIframe = By.xpath("//iframe[contains(@class,'paymentIframe')]");
    private final By cardNumberInput = By.xpath("//input[@placeholder='1234 1234 1234 1234']");
    private final By expiryInput = By.xpath("//input[@placeholder='MM/YY']");
    private final By cvvInput = By.xpath("//input[@placeholder='CVV']");
    private final By payButton = By.xpath("//button[contains(normalize-space(),'Pay AED')]");

    public PaymentSummaryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isTotalDisplayed() {
        return isDisplayed(totalPayValue);
    }

    public boolean isChangePaymentDisplayed() {
        return isDisplayed(changePayment);
    }

    public void openViewAllCoupons() {
        click(viewAllCouponsBtn);
    }

    public void applyCoupon(String couponName) {
        click(By.xpath(
                "//p[contains(@class,'offerTitle') and normalize-space()='" + couponName + "']"
                        + "/ancestor::div[contains(@class,'offerCard')]//button[contains(@class,'applyButton')]"
        ));
    }

    public void openBillDetails() {
        click(viewBillDetailsBtn);
    }

    public void closeBillDetails() {
        click(closeViewBill);
    }

    public String getTotalPay() {
        return getText(totalPayValue);
    }

    public void selectTenDirhamTip() {
        click(tipTenButton);
    }

    public void clickPlaceOrder() {
        click(placeOrderButton);
    }

    public void clickChangePayment() {
        click(changePayment);
    }

    public void selectDebitOrCreditCard() {
        click(debitOrCredit);
    }

    public void switchToPaymentFrame() {
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt(paymentIframe));
    }

    public void switchBackToMainPage() {
        driver.switchTo().defaultContent();
    }

    public void enterCardDetails(String cardNumber, String expiry, String cvv) {
        type(cardNumberInput, cardNumber);
        type(expiryInput, expiry);
        type(cvvInput, cvv);
    }

    public OrderCompletionPage clickPayButton() {
        click(payButton);
        return new OrderCompletionPage(driver);
    }

    public void payWithCard(String cardNumber, String expiry, String cvv) {
        clickChangePayment();
        selectDebitOrCreditCard();
        switchToPaymentFrame();
        enterCardDetails(cardNumber, expiry, cvv);
        clickPayButton();
        switchBackToMainPage();
    }
}
