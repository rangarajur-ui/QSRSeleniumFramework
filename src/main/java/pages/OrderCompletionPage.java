package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderCompletionPage extends BasePage {

    private final By brandName = By.xpath("//h3[@class='styles_accountName__ARhuv']");
    private final By viewBill = By.xpath("//button[text()='View bill details']");
    private final By orderId = By.xpath("//p[contains(@class,'styles_counterOrderID__2OpRL')]");

    public OrderCompletionPage(WebDriver driver) {
        super(driver);
    }

    public boolean waitForOrderCompletion() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBrandNameDisplayed() {
        return isDisplayed(brandName);
    }

    public String getBrandName() {
        return getText(brandName);
    }

    public String getOrderId() {
        return getText(orderId);
    }

    public void openBillDetails() {
        click(viewBill);
    }
}
