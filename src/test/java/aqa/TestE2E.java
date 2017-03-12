package aqa;

import aqa.pages.IndividualsPage;
import aqa.pages.MainPage;
import aqa.pages.MortgagePage;
import aqa.pages.MortgagePaymentCalculatorPage;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.SeverityLevel;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

@Features("Corevalue test task")
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

  @Stories("Checking transition to  Mortgage Payment Calculator")
  @Title("Check starting page")
  @Description("- Открываем начальную страницу и переходим на нужную  локаль")
  @Parameters({"language","partOfTitleInIndividualsPage"})
  @Test(priority = 1)
  public void openMainPageWithCorrectLanguage( @Parameter ("используемый язык для теста") String lang,@Parameter ("часть заголовка для  проверки")   String partOfTitleInIndividualsPage) {
    printUsingEnvInfo();
    mainPage.openMainPageAndSelectLanguage(baseUrl,lang);
    assertThat("Probably opened wrong page - Invalid Title",individualsPage.getTitle().toLowerCase(), containsString(partOfTitleInIndividualsPage.toLowerCase()));
  }


  @Stories("Checking transition to  Mortgage Payment Calculator")
  @Title("Check  transition to  Mortgage Payment Calculator Page")
  @Description("- Открываем меню , переходим на Mortgage Page, затем открываем Mortgage Payment Calculator Page")
  @Parameters({"partOfTitleInMortagePaymentCalculatorPage"})
  @Test(priority = 2 , dependsOnMethods = {"openMainPageWithCorrectLanguage"})
  public void switchToMortagePaymentCalculatorPage(@Parameter ("часть заголовка для  проверки") String partOfTitleInMortagePaymentCalculatorPage ){
    individualsPage.openMortgagePageByClickingOnLinkInMenu();
    mortgagePage.switchToPaymentCalculatorPage();
    assertThat("Probably opened wrong page - Invalid Title",mortgagePaymentCalculatorPage.getTitle().toLowerCase(), containsString(partOfTitleInMortagePaymentCalculatorPage.toLowerCase()));
  }

  @TestCaseId("TestSet-1")
  @Issue("1")
  @Stories("Checking Mortgage Payment Calculator")
  @Severity(SeverityLevel.CRITICAL)
  @Title("Check logic in Mortgage Payment Calculator")
  @Description("- проверяем  slider, затем  на основании параметризированных данных, формируем, генерируем и проверяем  тестовый ипотечный месячный платеж")
  @Parameters({"downPayment","amortization","paymentFrequency","interestRate","monthlyPaymentsResult"})
  @Test(priority = 3, dependsOnMethods = {"switchToMortagePaymentCalculatorPage"})
  public void checkMortgagePaymentCalculator(@Parameter ("задаток ") String downPayment, @Parameter ("амортизация ") String amortization, @Parameter ("частота выплат ") String paymentFrequency,@Parameter ("процентная ставка ") String interestRate, @Parameter ("конечный результат! ") String monthlyPaymentsResult){
    checkSlider();
    checkLogicInPaymentCalculator( downPayment,  amortization,  paymentFrequency,  interestRate,  monthlyPaymentsResult);
    }


  @Step("Check slider by drag and drop slider handle")
  public void checkSlider(){
    boolean sliderTestResult =mortgagePaymentCalculatorPage.checkSlider();
    mortgagePaymentCalculatorPage.purchasePriceInput.clear();
    assertThat("Slider test was failed!! ", sliderTestResult);
  }


  @Step("Main check of this E2E. Monthly Payments Result  should be {4} ")
  public void checkLogicInPaymentCalculator( String downPayment, String amortization, String paymentFrequency, String interestRate, String monthlyPaymentsResult){
    mortgagePaymentCalculatorPage.fillDataToTheMortgagePaymentCalculator( downPayment,  amortization,  paymentFrequency,  interestRate);
    new WebDriverWait(driver, 5, 500).until(ExpectedConditions.visibilityOf(mortgagePaymentCalculatorPage.monthlyPaymentsResult));
    saveTextLog("PaymentCalculatorData.txt","We use following data: Purchase price - 500 000, Down payment - "+downPayment+", Amortization - "+amortization+", Payment frequency - "+paymentFrequency+" and Interest rate - "+interestRate+"% . As result we receive Weekly payments - "+ mortgagePaymentCalculatorPage.monthlyPaymentsResult.getText().split(" ")[1] ); // for demo purpose  only
    makeScreenshot("CalculationResult.png"); // for demo purpose  only
    assertThat("Monthly Payments Result is  incorrect !",mortgagePaymentCalculatorPage.monthlyPaymentsResult.getText().split(" ")[1] ,is(equalTo(monthlyPaymentsResult)));
  }

  //For demo purpose only ;)
  @Attachment(value = "{0}", type = "text/plain")
  public static String saveTextLog(String attachName, String message) {
    return message;
  }

  // for demo purpose  only
  @Attachment(value = "{0}", type = "image/png")
  private byte[] makeScreenshot(String attachName) {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }
}
