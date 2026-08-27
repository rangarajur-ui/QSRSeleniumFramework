package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import base.BasePage;
import org.openqa.selenium.WebElement;

public class CatalogPage extends BasePage {

    // --- Static, page-level locators (only one instance on the page) ---
    private By searchInput = By.xpath("//input[@placeholder='Search menu...']");
    private By restaurantName = By.xpath("//h1[contains(@class,'heroRestaurantName')]");
    private By restaurantAddress = By.xpath("//span[contains(@class,'address')]");
    private By cartSummaryBar = By.xpath("//div[contains(@class,'cartButtonContainer')]");
    private By viewCartButton = By.xpath("//div[contains(@class,'viewButton')]");
    private By cartItemCountText = By.xpath("//span[contains(@class,'itemCount')]");

    // Customization popup (only one open at a time)
    private By customizationCloseBtn = By.xpath("//button[@aria-label='Close']");
    private By popupAddItemBtn = By.xpath("//button[contains(@class,'addButton') and normalize-space()='Add item']");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    // ---------- Page-level actions ----------

    public String getRestaurantName() {
        return getText(restaurantName);
    }

    public void searchMenu(String query) {
        type(searchInput, query);
    }

    public void selectCategory(String categoryName) {
        By categoryTab = By.xpath(
                "//button[contains(@class,'categoryTab')]//span[normalize-space()='" + categoryName + "']"
        );
        click(categoryTab);
    }

    // ---------- Per-item, dynamic locators ----------

    private By itemCard(String itemName) {
        return By.xpath(
                "//h3[normalize-space()='" + itemName + "']/ancestor::div[contains(@class,'menuItem')]"
        );
    }

    public String getItemPrice(String itemName) {
        By priceLocator = By.xpath(
                itemCard(itemName).toString().replace("By.xpath: ", "")
                        + "//p[contains(@class,'itemPrice')]"
        );
        return getText(priceLocator);
    }

    public void addItemToCart(String itemName) {
        By addButton = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//button[contains(@class,'addButton')]"
        );
        click(addButton);
    }

    public void increaseQuantity(String itemName) {
        By plusButton = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//div[contains(@class,'quantityControl')]//button[normalize-space()='+']"
        );
        click(plusButton);
    }

    public void decreaseQuantity(String itemName) {
        By minusButton = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//div[contains(@class,'quantityControl')]//button[normalize-space()='-']"
        );
        click(minusButton);
    }

    public String getItemQuantity(String itemName) {
        By quantitySpan = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//span[contains(@class,'quantity')]"
        );
        return getText(quantitySpan);
    }

    // Opens the customization popup by clicking the item's image
    public void openItemCustomization(String itemName) {
        By itemImage = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//img[@alt='" + itemName + "']"
        );
        click(itemImage);
    }





    // ---------- Cart summary bar ----------

    public String getCartItemCountText() {
        return getText(cartItemCountText);
    }

    public CustomerDetailsPage clickViewCart() {
        scrollIntoView(viewCartButton);
        click(viewCartButton);
        return new CustomerDetailsPage(driver);
    }

    public boolean isCartSummaryBarDisplayed() {
        try {
            return driver.findElements(cartSummaryBar).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Customization: check if an item supports it ---
    public boolean isItemCustomizable(String itemName) {
        By customisableLabel = By.xpath(
                "//h3[normalize-space()='" + itemName + "']"
                        + "/ancestor::div[contains(@class,'menuItem')]"
                        + "//p[contains(@class,'customisable')]"
        );
        return driver.findElements(customisableLabel).size() > 0;
    }

    // --- Customization popup: group + option level ---
    public String getGroupTitle(int groupIndex) {
        By groupTitle = By.xpath(
                "(//div[contains(@class,'customizationGroup')])[" + groupIndex + "]//h3[contains(@class,'groupTitle')]"
        );
        return getText(groupTitle);
    }

    public void selectCustomizationOption(String optionName) {
        By optionCheckbox = By.xpath(
                "//span[contains(@class,'optionName') and normalize-space()='" + optionName + "']"
                        + "/ancestor::label//div[contains(@class,'checkbox')]"
        );
        click(optionCheckbox);
    }

    public String getOptionPrice(String optionName) {
        By optionPrice = By.xpath(
                "//span[contains(@class,'optionName') and normalize-space()='" + optionName + "']"
                        + "/ancestor::label//span[contains(@class,'optionPrice')]"
        );
        return getText(optionPrice);
    }

    public void closeCustomizationPopup() {
        click(customizationCloseBtn); // already defined earlier
    }

    // Popup's own "Add item" button — different footer than the main list's Add
    public void clickAddItemInPopup() {
        By addItemBtn = By.xpath("//button[contains(@class,'addButton') and normalize-space()='Add item']");
        click(addItemBtn);
    }
}