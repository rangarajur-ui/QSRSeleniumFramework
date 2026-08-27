package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderCompletionPage extends BasePage {



    private By brandname = By.xpath("//h3[@class='styles_accountName__ARhuv']");
    private By viewBill = By.xpath("//button[text()='View bill details']");
    private By orderId = By.xpath("//p[contains(@class,'styles_counterOrderID__2OpRL')]");


    public OrderCompletionPage(WebDriver driver) {
        super(driver);
    }

    public String getBrandName() {

            String brand = getText(brandname);
        return brand;
    }
    public String getOrderId() {
        String orderid = getText(orderId);
        return orderid;
    }

    public void openBillDetails() {

        click(viewBill);
    }


}
