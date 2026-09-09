package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CustomerDetailsPage extends BasePage {

    private final By nameInput = By.id("userdetail-name");
    private final By phoneInput = By.id("userdetail-phone");
    private final By countryCodeButton = By.xpath("//button[contains(@class,'countryCodeButton')]");
    private final By countrySearchInput = By.xpath("//input[contains(@class,'searchInput')]");
    private final By proceedButton = By.xpath("//button[contains(@class,'proceedButton')]");

    public CustomerDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isNameFieldDisplayed() {
        return isDisplayed(nameInput);
    }

    public void enterName(String name) {
        type(nameInput, name);
    }

    public void enterMobileNumber(String number) {
        type(phoneInput, number);
    }

    public void fillCustomerDetails(String name, String mobile) {
        enterName(name);
        enterMobileNumber(mobile);
    }

    public void selectCountryCode(String code) {
        click(countryCodeButton);
        type(countrySearchInput, code);
        click(By.xpath("//div[contains(@class,'countryList')]//button[normalize-space()='" + code + "']"));
    }

    public boolean isProceedEnabled() {
        return driver.findElement(proceedButton).isEnabled();
    }

    public PaymentSummaryPage clickProceed() {
        click(proceedButton);
        return new PaymentSummaryPage(driver);
    }
}
