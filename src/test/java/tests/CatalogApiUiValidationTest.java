package tests;

import api.CatalogApiClient;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;

public class CatalogApiUiValidationTest extends BaseTest {

    @Test
    public void verifyItemPriceMatchesBetweenApiAndUi() {
        Response apiResponse = CatalogApiClient.fetchCatalog();
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Catalog API did not return 200");

        String itemName = "Chicken Tikka Biryani (Boneless)";
        Double apiPrice = CatalogApiClient.getItemPrice(apiResponse, itemName);
        System.out.println("API price: " + apiPrice);

        if (landingPage.isWelcomeScreenDisplayed()) {
            landingPage.clickStartYourOrder();
        }
        CatalogPage catalogPage = new CatalogPage(driver);
        String uiPriceText = catalogPage.getItemPrice(itemName); // e.g. "AED 33.00"
        System.out.println("UI price text: " + uiPriceText);

        Double uiPrice = Double.valueOf(uiPriceText.replaceAll("[^0-9.]", ""));
        Assert.assertEquals(uiPrice, apiPrice, "UI price does not match API price for " + itemName);
    }

    @Test
    public void verifyCustomizableFlagMatchesUi() {
        Response apiResponse = CatalogApiClient.fetchCatalog();

        String itemName = "Chicken Boneless Special Biryani"; // known is_customizable:true in your JSON
        Boolean apiCustomizable = CatalogApiClient.isItemCustomizable(apiResponse, itemName);
        System.out.println("API says customizable: " + apiCustomizable);

        if (landingPage.isWelcomeScreenDisplayed()) {
            landingPage.clickStartYourOrder();
        }
        CatalogPage catalogPage = new CatalogPage(driver);
        boolean uiCustomizable = catalogPage.isItemCustomizable(itemName);
        System.out.println("UI shows 'customisable' label: " + uiCustomizable);

        Assert.assertEquals(uiCustomizable, apiCustomizable, "Customizable flag mismatch for " + itemName);
    }
}