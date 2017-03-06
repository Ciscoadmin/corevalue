package aqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by dbolgarov on 2/28/2017.
 */
public class MortgagePage extends Page {

    @FindBy(how = How.XPATH, using = "//*[@class='btn-full' and @data-utag-name='calculate_your_payments']")
    public WebElement mortgagePaymentCalculatorButton;

    public void switchToPaymentCalculatorPage() {
        mortgagePaymentCalculatorButton.click();
    }

    public MortgagePage(WebDriver driver) {
        super(driver);
    }
}
