package tests;

import api.CatalogApiClient;
import base.BaseTest;
import constants.TestData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CatalogPage;
import utils.ReportLogger;

public class CatalogApiTest extends BaseTest {

    @Test(groups = {"regression", "api"},
            description = "UI price matches catalog API price for the same item")
    public void verifyItemPriceMatchesBetweenApiAndUi() {
        logStep("Fetch item price from API");
        Response apiResponse = CatalogApiClient.fetchCatalog();
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Catalog API did not return 200");
        Double apiPrice = CatalogApiClient.getItemPrice(apiResponse, TestData.MENU_ITEM);
        ReportLogger.info("API price: " + apiPrice);
        Assert.assertNotNull(apiPrice, "API did not return a price for " + TestData.MENU_ITEM);

        logStep("Read the same item price from UI");
        CatalogPage catalogPage = openCatalog();
        String uiPriceText = catalogPage.getItemPrice(TestData.MENU_ITEM);
        ReportLogger.info("UI price text: " + uiPriceText);
        Double uiPrice = Double.valueOf(uiPriceText.replaceAll("[^0-9.]", ""));

        Assert.assertEquals(uiPrice, apiPrice, "UI price does not match API price for " + TestData.MENU_ITEM);
        ReportLogger.pass("API and UI prices match: " + uiPrice);
    }

    @Test(groups = {"regression", "api"},
            description = "Customizable flag on UI matches the catalog API")
    public void verifyCustomizableFlagMatchesUi() {
        logStep("Read is_customizable from API");
        Response apiResponse = CatalogApiClient.fetchCatalog();
        Boolean apiCustomizable = CatalogApiClient.isItemCustomizable(apiResponse, TestData.CUSTOMIZABLE_ITEM);
        ReportLogger.info("API customizable: " + apiCustomizable);
        Assert.assertNotNull(apiCustomizable, "API flag missing for " + TestData.CUSTOMIZABLE_ITEM);

        logStep("Check customisable label on UI");
        CatalogPage catalogPage = openCatalog();
        boolean uiCustomizable = catalogPage.isItemCustomizable(TestData.CUSTOMIZABLE_ITEM);
        ReportLogger.info("UI customisable label: " + uiCustomizable);

        Assert.assertEquals(uiCustomizable, apiCustomizable.booleanValue(),
                "Customizable flag mismatch for " + TestData.CUSTOMIZABLE_ITEM);
        ReportLogger.pass("API and UI customizable flags match");
    }
}
