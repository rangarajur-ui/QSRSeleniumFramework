package tests;

import base.BaseTest;
import constants.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import utils.ReportLogger;

public class CatalogPageTest extends BaseTest {

    @Test(groups = {"sanity", "catalog"},
            description = "Menu item can be added to cart and quantity becomes 1")
    public void verifyCanAddItemToCart() {
        CatalogPage catalogPage = openCatalog();

        logStep("Read price for " + TestData.MENU_ITEM);
        String price = catalogPage.getItemPrice(TestData.MENU_ITEM);
        ReportLogger.info("Item price: " + price);
        Assert.assertFalse(price.isBlank(), "Item price should be displayed");

        logStep("Add item to cart");
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        String qty = catalogPage.getItemQuantity(TestData.MENU_ITEM);
        ReportLogger.info("Quantity after add: " + qty);
        Assert.assertEquals(qty, "1", "Quantity did not update after adding item");
        ReportLogger.pass("Item added — quantity is 1");
        ReportLogger.screenshot(driver, "Catalog_ItemAdded");
    }

    @Test(groups = {"regression", "catalog"},
            description = "Plus and minus update the item quantity on the catalog")
    public void verifyQuantityIncreaseAndDecrease() {
        CatalogPage catalogPage = openCatalog();

        logStep("Add item then increase quantity");
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        catalogPage.increaseQuantity(TestData.MENU_ITEM);
        Assert.assertEquals(catalogPage.getItemQuantity(TestData.MENU_ITEM), "2",
                "Quantity should be 2 after plus");

        logStep("Decrease quantity");
        catalogPage.decreaseQuantity(TestData.MENU_ITEM);
        Assert.assertEquals(catalogPage.getItemQuantity(TestData.MENU_ITEM), "1",
                "Quantity should be 1 after minus");
        ReportLogger.pass("Quantity +/- works");
        ReportLogger.screenshot(driver, "Catalog_Quantity");
    }

    @Test(groups = {"regression", "catalog"},
            description = "Cart summary bar appears after an item is added")
    public void verifyCartSummaryBarAfterAdd() {
        CatalogPage catalogPage = openCatalog();

        logStep("Add item and check cart bar");
        catalogPage.addItemToCart(TestData.MENU_ITEM);
        Assert.assertTrue(catalogPage.isCartSummaryBarDisplayed(),
                "Cart summary bar should appear after adding an item");
        String countText = catalogPage.getCartItemCountText();
        ReportLogger.info("Cart count text: " + countText);
        Assert.assertFalse(countText.isBlank(), "Cart item count should be shown");
        ReportLogger.pass("Cart summary bar is visible");
        ReportLogger.screenshot(driver, "Catalog_CartBar");
    }

    @Test(groups = {"regression", "catalog"},
            description = "Search filters the menu by keyword")
    public void verifyMenuSearch() {
        CatalogPage catalogPage = openCatalog();

        logStep("Search menu for " + TestData.SEARCH_KEYWORD);
        Assert.assertTrue(catalogPage.isSearchDisplayed(), "Search box should be visible");
        catalogPage.searchMenu(TestData.SEARCH_KEYWORD);

        logStep("Confirm the known item is still listed");
        Assert.assertTrue(catalogPage.isItemDisplayed(TestData.MENU_ITEM),
                "Searched item should remain visible: " + TestData.MENU_ITEM);
        ReportLogger.pass("Search returned the expected item");
        ReportLogger.screenshot(driver, "Catalog_Search");
    }
}
