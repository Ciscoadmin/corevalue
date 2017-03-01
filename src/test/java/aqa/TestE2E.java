package aqa;

import aqa.pages.IndividualsPage;
import aqa.pages.MainPage;
import aqa.pages.MortgagePage;
import aqa.pages.MortgagePaymentCalculatorPage;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

public class TestE2E extends BaseTest {

  private IndividualsPage individualsPage;
  private MainPage mainPage;
  private MortgagePage mortgagePage;
  private MortgagePaymentCalculatorPage mortgagePaymentCalculatorPage;

  @BeforeMethod
  public void initPageObjects() {
    mainPage = PageFactory.initElements(driver,MainPage.class);
    individualsPage = PageFactory.initElements(driver, IndividualsPage.class);
    mortgagePage = PageFactory.initElements(driver, MortgagePage.class);
    mortgagePaymentCalculatorPage = PageFactory.initElements(driver, MortgagePaymentCalculatorPage.class);
  }

  @Parameters({"language","partOfTitleInIndividualsPage"})
  @Test(priority = 1)
  public void openMainPageWithCorrectLanguage(String lang, String partOfTitleInIndividualsPage) {
    driver.get(baseUrl);

    //Choosing  correct language if it needs
    if (lang.equals(mainPage.language.getText())) {
      mainPage.language.click();
    }

    assertThat("Probably opened wrong page - Invalid Title",individualsPage.getTitle().toLowerCase(), containsString(partOfTitleInIndividualsPage.toLowerCase()));
  }

    @Parameters({"partOfTitleInMortagePaymentCalculatorPage"})
    @Test(priority = 2 , dependsOnMethods = {"openMainPageWithCorrectLanguage"})
    public void switchToMortagePaymentCalculatorPage(String partOfTitleInMortagePaymentCalculatorPage ){

    individualsPage.loans.click();
    individualsPage.mortageLink.click();
    mortgagePage.mortgagePaymentCalculatorButton.click();

    assertThat("Probably opened wrong page - Invalid Title",mortgagePaymentCalculatorPage.getTitle().toLowerCase(), containsString(partOfTitleInMortagePaymentCalculatorPage.toLowerCase()));
    }

  @Parameters({"downPayment","amortization","paymentFrequency","interestRate","monthlyPaymentsResult"})
  @Test(priority = 3, dependsOnMethods = {"switchToMortagePaymentCalculatorPage"})
  public void checkMortgagePaymentCalculator(String downPayment, String amortization, String paymentFrequency, String interestRate, String monthlyPaymentsResult){

    boolean sliderTestResult = mortgagePaymentCalculatorPage.checkSlider();

    mortgagePaymentCalculatorPage.purchasePriceInput.clear();
    mortgagePaymentCalculatorPage.plusPurchaseButton.click();
    mortgagePaymentCalculatorPage.plusPurchaseButton.click();

    mortgagePaymentCalculatorPage.downPaymentInput.clear();
    mortgagePaymentCalculatorPage.downPaymentInput.sendKeys("\b"+downPayment);

    new Select(mortgagePaymentCalculatorPage.paymentFrequencySelect).selectByVisibleText(paymentFrequency);
    new Select(mortgagePaymentCalculatorPage.amortizationSelect).selectByVisibleText(amortization);

    mortgagePaymentCalculatorPage.interestRateInput.clear();
    mortgagePaymentCalculatorPage.interestRateInput.sendKeys(interestRate);

    mortgagePaymentCalculatorPage.calculateButton.click();

    new WebDriverWait(driver, 5, 500).until(ExpectedConditions.visibilityOf(mortgagePaymentCalculatorPage.monthlyPaymentsResult));

    assertThat("Slider test was failed!! ", sliderTestResult);
    assertThat("Monthly Payments Result is  incorrect !",mortgagePaymentCalculatorPage.monthlyPaymentsResult.getText().split(" ")[1] ,is(equalTo(monthlyPaymentsResult)));
  }
}
