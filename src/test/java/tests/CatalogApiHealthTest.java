package tests;

import api.CatalogApiClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ReportLogger;

/**
 * Pure API check — no browser. Safe to run in sanity on every build.
 */
public class CatalogApiHealthTest {

    @Test(groups = {"sanity", "api"},
            description = "Catalog API returns HTTP 200 and a restaurant name")
    public void verifyCatalogApiIsHealthy() {
        ReportLogger.step("Call catalog API");
        Response apiResponse = CatalogApiClient.fetchCatalog();
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Catalog API did not return 200");

        String restaurantName = CatalogApiClient.getRestaurantName(apiResponse);
        ReportLogger.info("API restaurant name: " + restaurantName);
        Assert.assertFalse(restaurantName == null || restaurantName.isBlank(),
                "API should return a restaurant / brand name");
        ReportLogger.pass("Catalog API is healthy");
    }
}
