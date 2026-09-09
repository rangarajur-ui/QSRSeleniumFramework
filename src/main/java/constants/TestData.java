package constants;

import config.ConfigReader;

/**
 * Single place for test data so tests stay readable.
 * Values come from config.properties (or Jenkins -D overrides).
 */
public final class TestData {

    private TestData() {
    }

    public static final String CUSTOMER_NAME = ConfigReader.getProperty("customerName");
    public static final String CUSTOMER_MOBILE = ConfigReader.getProperty("customerMobile");
    public static final String COUNTRY_CODE = ConfigReader.getProperty("countryCode");

    public static final String MENU_ITEM = ConfigReader.getProperty("menuItem");
    public static final String CUSTOMIZABLE_ITEM = ConfigReader.getProperty("customizableItem");
    public static final String SEARCH_KEYWORD = ConfigReader.getProperty("searchKeyword");

    public static final String CARD_NUMBER = ConfigReader.getProperty("cardNumber");
    public static final String CARD_EXPIRY = ConfigReader.getProperty("cardExpiry");
    public static final String CARD_CVV = ConfigReader.getProperty("cardCvv");

    public static final String TESTER_NAME = ConfigReader.getProperty("testerName");
    public static final String ENVIRONMENT = ConfigReader.getProperty("environment");
    public static final String APPLICATION_NAME = ConfigReader.getProperty("applicationName");
    public static final String BROWSER = ConfigReader.getProperty("browser");
}
