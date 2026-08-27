package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;

public class LandingPage extends BasePage {

    private By startYourOrderBtn = By.xpath("//button[normalize-space()='Start Your Order']");
    private By restaurantWelcomeText = By.xpath("//h2[contains(@class,'styles_headline')]");
    private By secondPaginationDot = By.xpath("(//div[contains(@class,'paginationDots')]//*)[2]");


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

        return driver.findElements(restaurantWelcomeText).size() > 0;
    }
}
