package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LandingPage extends BasePage {

    private final By startYourOrderBtn = By.xpath("//button[normalize-space()='Start Your Order']");
    private final By restaurantWelcomeText = By.xpath("//h2[contains(@class,'styles_headline')]");
    private final By secondPaginationDot = By.xpath("(//div[contains(@class,'paginationDots')]//*)[2]");

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public String getWelcomeMessage() {
        return getText(restaurantWelcomeText);
    }

    public CatalogPage clickStartYourOrder() {
        click(startYourOrderBtn);
        return new CatalogPage(driver);
    }

    public void goToWelcomeSlide() {
        click(secondPaginationDot);
    }

    public boolean isWelcomeScreenDisplayed() {
        return isPresent(restaurantWelcomeText);
    }

    public boolean isStartOrderButtonDisplayed() {
        return isDisplayed(startYourOrderBtn);
    }
}
