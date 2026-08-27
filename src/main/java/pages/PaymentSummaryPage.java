package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;

public class PaymentSummaryPage extends BasePage {

    private By viewAllCouponsBtn = By.xpath("//button[contains(@class,'viewAllFooter')]");
    private By applyCouponBtn = By.xpath("//button[contains(@class,'applyButton')]");
    private By viewBillDetailsBtn = By.xpath("//button[contains(@class,'viewBillLink')]");
    // In PaymentSummaryPage.java — fix the locator to point at the always-visible total
    private By totalPayValue = By.xpath("//span[contains(@class,'styles_totalValue__V88sU')]");
    private By closeviewbill = By.xpath("//button[@class='styles_closeButton__6UghF']");
    private By tipTenButton = By.xpath("//button[contains(@class,'tipButton')][1]");
    private By customTipButton = By.xpath("//button[contains(@class,'tipButton')][2]");
    private By changepayment = By.xpath("//button[@class='styles_changeButton__9OJLM']");
    private By debitorcredit = By.xpath("//span[contains(text(),'Debit/Credit Cards')]");
    private By placeOrderButton = By.xpath("//button[contains(@class,'qsrPlaceOrderButton')]");
    private By paymentIframe = By.xpath("//iframe[contains(@class,'paymentIframe')]");
    private By cardNumberInput = By.xpath("//input[@placeholder='1234 1234 1234 1234']");
    private By expiryInput = By.xpath("//input[@placeholder='MM/YY']");
    private By cvvInput = By.xpath("//input[@placeholder='CVV']");
    private By payButton = By.xpath("//button[contains(normalize-space(),'Pay AED')]");


    public PaymentSummaryPage(WebDriver driver) {

        super(driver);
    }

    public void openViewAllCoupons() {
        click(viewAllCouponsBtn);
    }

    public void applyCoupon(String couponName) {
        By couponApplyBtn = By.xpath(
                "//p[contains(@class,'offerTitle') and normalize-space()='" + couponName + "']"
                        + "/ancestor::div[contains(@class,'offerCard')]//button[contains(@class,'applyButton')]"
        );
        click(couponApplyBtn);
    }

    public void openBillDetails() {

        click(viewBillDetailsBtn);
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

    // Switches into the cross-origin payment iframe so subsequent finds target its content
    public void switchToPaymentFrame() {
        driver.switchTo().frame(driver.findElement(paymentIframe));
    }

    public void switchBackToMainPage() {
        driver.switchTo().defaultContent();
    }


    public void enterCardDetails(String cardNumber, String expiry, String cvv) {
        type(cardNumberInput, cardNumber);
        type(expiryInput, expiry);
        type(cvvInput, cvv);
    }
    public void clickcloseviewbill() {
        click(closeviewbill);
    }
    public void clickchangepayment() {
        click(changepayment);
    }
    public void clickdebitorcredit() {
        click(debitorcredit);
    }

    public OrderCompletionPage clickPayButton() {
        click(payButton);
        return new OrderCompletionPage(driver);
    }


}