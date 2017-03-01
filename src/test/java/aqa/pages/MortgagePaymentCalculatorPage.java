package aqa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by dbolgarov on 2/28/2017.
 */
public class MortgagePaymentCalculatorPage extends Page {

    @FindBy(how = How.ID, using = "PrixPropriete")
    public WebElement purchasePriceInput;

    @FindBy(how = How.ID, using = "MiseDeFond")
    public WebElement downPaymentInput;

    @FindBy(how = How.ID, using = "TauxInteret")
    public WebElement interestRateInput;

    @FindBy(how = How.ID, using = "btn_calculer")
    public WebElement calculateButton;

    @FindBy(how = How.ID, using = "Amortissement")
    public WebElement amortizationSelect;

    @FindBy(how = How.ID, using = "FrequenceVersement")
    public WebElement paymentFrequencySelect;

    @FindBy(how = How.ID, using = "paiement-resultats")
    public WebElement monthlyPaymentsResult;

    @FindBy(how = How.ID, using = "PrixProprietePlus")
    public WebElement plusPurchaseButton;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'slider-handle') and not (contains(@class,'hide'))]")
    public WebElement sliderHandle;

    @FindBy(how = How.CLASS_NAME, using = "slider-track")
    public WebElement sliderTrack;

    /**
     *  Checking slider by drag and drop slider handle
     *
     *
     * @return true  if 2 checks are passed
     */
    public boolean checkSlider(){

        new Actions(driver).dragAndDropBy(sliderHandle,sliderTrack.getSize().width,0).perform();
        if (Integer.valueOf(purchasePriceInput.getAttribute("value")) == 2000000 ){
            new Actions(driver).dragAndDropBy(sliderHandle,-sliderTrack.getSize().width/2,0).perform();
            if (Integer.valueOf(purchasePriceInput.getAttribute("value")) >= 1000000){
                return true;
            }else return false;
        } else return false;
    }

    public MortgagePaymentCalculatorPage(WebDriver driver) {
       super(driver);
    }
}
