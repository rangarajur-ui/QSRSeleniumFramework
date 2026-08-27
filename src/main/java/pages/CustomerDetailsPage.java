package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;

public class CustomerDetailsPage extends BasePage {

    private By nameInput = By.id("userdetail-name");
    private By phoneInput = By.id("userdetail-phone");
    private By countryCodeButton = By.xpath("//button[contains(@class,'countryCodeButton')]");
    private By countrySearchInput = By.xpath("//input[contains(@class,'searchInput')]");
    private By proceedButton = By.xpath("//button[contains(@class,'proceedButton')]");

    public CustomerDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void enterName(String name) {
        type(nameInput, name);
    }

    public void enterMobileNumber(String number) {
        type(phoneInput, number);
    }

    public void selectCountryCode(String code) {
        click(countryCodeButton);
        type(countrySearchInput, code);

        By matchingOption = By.xpath(
                "//div[contains(@class,'countryList')]//button[normalize-space()='" + code + "']"
        );
        click(matchingOption);
    }

    public boolean isProceedEnabled() {
        return driver.findElement(proceedButton).isEnabled();
    }

    public PaymentSummaryPage clickProceed() {
        click(proceedButton);
        return new PaymentSummaryPage(driver);
    }
}