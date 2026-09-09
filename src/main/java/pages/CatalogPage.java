package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CatalogPage extends BasePage {

    private final By searchInput = By.xpath("//input[@placeholder='Search menu...']");
    private final By restaurantName = By.xpath("//h1[contains(@class,'heroRestaurantName')]");
    private final By restaurantAddress = By.xpath("//span[contains(@class,'address')]");
    private final By cartSummaryBar = By.xpath("//div[contains(@class,'cartButtonContainer')]");
    private final By viewCartButton = By.xpath("//div[contains(@class,'viewButton')]");
    private final By cartItemCountText = By.xpath("//span[contains(@class,'itemCount')]");
    private final By customizationCloseBtn = By.xpath("//button[@aria-label='Close']");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public String getRestaurantName() {
        return getText(restaurantName);
    }

    public String getRestaurantAddress() {
        return getText(restaurantAddress);
    }

    public boolean isSearchDisplayed() {
        return isDisplayed(searchInput);
    }

    public void searchMenu(String query) {
        type(searchInput, query);
    }

    public void selectCategory(String categoryName) {
        click(By.xpath(
                "//button[contains(@class,'categoryTab')]//span[normalize-space()='" + categoryName + "']"
        ));
    }

    public boolean isItemDisplayed(String itemName) {
        return isPresent(itemCard(itemName));
    }

    public String getItemPrice(String itemName) {
        return getText(By.xpath(itemCardXpath(itemName) + "//p[contains(@class,'itemPrice')]"));
    }

    public void addItemToCart(String itemName) {
        click(By.xpath(itemCardXpath(itemName) + "//button[contains(@class,'addButton')]"));
    }

    public void increaseQuantity(String itemName) {
        click(By.xpath(itemCardXpath(itemName)
                + "//div[contains(@class,'quantityControl')]//button[normalize-space()='+']"));
    }

    public void decreaseQuantity(String itemName) {
        click(By.xpath(itemCardXpath(itemName)
                + "//div[contains(@class,'quantityControl')]//button[normalize-space()='-']"));
    }

    public String getItemQuantity(String itemName) {
        return getText(By.xpath(itemCardXpath(itemName) + "//span[contains(@class,'quantity')]"));
    }

    public void openItemCustomization(String itemName) {
        click(By.xpath(itemCardXpath(itemName) + "//img[@alt='" + itemName + "']"));
    }

    public String getCartItemCountText() {
        return getText(cartItemCountText);
    }

    public CustomerDetailsPage clickViewCart() {
        scrollIntoView(viewCartButton);
        click(viewCartButton);
        return new CustomerDetailsPage(driver);
    }

    public boolean isCartSummaryBarDisplayed() {
        return isPresent(cartSummaryBar);
    }

    public boolean isItemCustomizable(String itemName) {
        return isPresent(By.xpath(itemCardXpath(itemName) + "//p[contains(@class,'customisable')]"));
    }

    public String getGroupTitle(int groupIndex) {
        return getText(By.xpath(
                "(//div[contains(@class,'customizationGroup')])[" + groupIndex + "]//h3[contains(@class,'groupTitle')]"
        ));
    }

    public void selectCustomizationOption(String optionName) {
        click(By.xpath(
                "//span[contains(@class,'optionName') and normalize-space()='" + optionName + "']"
                        + "/ancestor::label//div[contains(@class,'checkbox')]"
        ));
    }

    public String getOptionPrice(String optionName) {
        return getText(By.xpath(
                "//span[contains(@class,'optionName') and normalize-space()='" + optionName + "']"
                        + "/ancestor::label//span[contains(@class,'optionPrice')]"
        ));
    }

    public void closeCustomizationPopup() {
        click(customizationCloseBtn);
    }

    public void clickAddItemInPopup() {
        click(By.xpath("//button[contains(@class,'addButton') and normalize-space()='Add item']"));
    }

    private By itemCard(String itemName) {
        return By.xpath(itemCardXpath(itemName));
    }

    private String itemCardXpath(String itemName) {
        return "//h3[normalize-space()='" + itemName + "']/ancestor::div[contains(@class,'menuItem')]";
    }
}
