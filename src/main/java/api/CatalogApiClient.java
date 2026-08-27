package api;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import java.util.List;

public class CatalogApiClient {

    public static Response fetchCatalog() {
        RestAssured.useRelaxedHTTPSValidation();

        String baseUrl = ConfigReader.getProperty("catalogApiBaseUrl");
        String path = ConfigReader.getProperty("catalogApiPath") + ConfigReader.getProperty("qrScanCode");

        String requestBody = "{"
                + "\"user_name\":\"AutomationUser\","
                + "\"user_mobile_number\":\"501234567\","
                + "\"user_country_code\":\"+971\","
                + "\"skip_item_options\":true,"
                + "\"issue_qr_scan_token\":true"
                + "}";

        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .post(path);
    }

    public static List<String> getAllItemNames(Response response) {
        JsonPath json = response.jsonPath();
        return json.getList("data.categories.items.flatten().name");
    }

    public static Double getItemPrice(Response response, String itemName) {
        JsonPath json = response.jsonPath();
        Float price = json.getFloat(
                "data.categories.items.flatten().find { it.name == '" + itemName + "' }.price"
        );
        return price == null ? null : price.doubleValue();
    }

    public static Boolean isItemCustomizable(Response response, String itemName) {
        JsonPath json = response.jsonPath();
        return json.getBoolean(
                "data.categories.items.flatten().find { it.name == '" + itemName + "' }.is_customizable"
        );
    }

    public static Boolean isItemInStock(Response response, String itemName) {
        JsonPath json = response.jsonPath();
        return json.getBoolean(
                "data.categories.items.flatten().find { it.name == '" + itemName + "' }.in_stock"
        );
    }

    public static String getRestaurantName(Response response) {
        return response.jsonPath().getString("data.merchant.brand_name");
    }

    public static String getTableNumber(Response response) {
        return response.jsonPath().getString("data.table.number");
    }
}