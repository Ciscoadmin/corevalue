package aqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by dbolgarov on 2/28/2017.
 */
public class IndividualsPage extends Page {

    @FindBy(how = How.XPATH, using = "//a[@data-utag-name='loans']")
    public WebElement loans;

    @FindBy(how = How.XPATH, using = "//*[@data-utag-name='mortgage_loan']")
    public WebElement mortageLink;



    public IndividualsPage(WebDriver driver) {
        super(driver);
    }
}
