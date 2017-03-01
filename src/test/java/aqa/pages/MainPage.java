package aqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by dbolgarov on 2/28/2017.
 */
public class MainPage extends Page {

    @FindBy(how = How.ID, using = "topLangMenuItem")
    public WebElement language;

    public MainPage(WebDriver driver) {
        super(driver);
    }
}
