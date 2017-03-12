package aqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

    @FindBy(how = How.XPATH, using = "//*[@for='Amortissement']/following-sibling::*/.//*[@class='selectric']/p")
    public WebElement amortizationSelect;

    @FindBy(how = How.XPATH, using = "//*[@for='FrequenceVersement']/following-sibling::*/.//*[@class='selectric']/p")
    public WebElement paymentFrequencySelect;

    @FindBys({@FindBy(xpath =  "//*[@for='Amortissement']/following-sibling::*/.//li")})
    public List<WebElement> selectAmortizationItems;

    @FindBys({@FindBy(xpath =  "//*[@for='FrequenceVersement']/following-sibling::*/.//li")})
    public List<WebElement> selectPaymentFrequencyItems;

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

    /** Calculate Mortgage Payment
     *
     * By default  Purchase price set to 500000
     *
     * @param downPayment
     * @param amortization
     * @param paymentFrequency
     * @param interestRate
     */
    public void fillDataToTheMortgagePaymentCalculator(String downPayment, String amortization, String paymentFrequency, String interestRate){
        plusPurchaseButton.click();
        plusPurchaseButton.click();

        downPaymentInput.clear();
        downPaymentInput.sendKeys("\b"+downPayment);

        selectDropdownMenu(paymentFrequencySelect ,selectPaymentFrequencyItems, paymentFrequency);
        selectDropdownMenu(amortizationSelect ,selectAmortizationItems, amortization);

//old style  dropdown menu list  - worked before 11.03.17
//        new Select(paymentFrequencySelect).selectByVisibleText(paymentFrequency);
//        new Select(amortizationSelect).selectByVisibleText(amortization);

        interestRateInput.clear();
        interestRateInput.sendKeys(interestRate);

        calculateButton.click();
    }


    /**
     *  Select  Item  in dropdown  menu list
     *
     * @param parent    WebElement that we should click on for opening dropdown menu list
     * @param selectItems List<WebElement> - dropdown menu list
     * @param itemToSelect  String for selection
     */
    public void selectDropdownMenu (WebElement parent, List<WebElement> selectItems, String itemToSelect){

        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", parent);   //            parent.click(); // doesn't work here correctly with latest Chrome version, that's why I chose JS way for click on the WebElement

        for(WebElement el : selectItems){
//            System.out.println("Found "+el.getText());  // for debug purposes only
            if (itemToSelect.equals(el.getText())){
                el.click();
                break;
            }
        }

    }

    public MortgagePaymentCalculatorPage(WebDriver driver) {
        super(driver);
    }
}
