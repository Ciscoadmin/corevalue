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


    public void  openMainPageAndSelectLanguage(String url, String lang){
        driver.get(url);

        //Choosing  correct language if it needs
        if (lang.equals(language.getText())) {
            language.click();
        }
    }

    public MainPage(WebDriver driver) {
        super(driver);
    }
}
