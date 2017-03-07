package aqa;

import aqa.util.PropertyLoader;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.selenium.factory.WebDriverFactory;
import ru.stqa.selenium.factory.WebDriverFactoryMode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Base class for TestNG-based test classes
 */
public class BaseTest {

  protected static String gridHubUrl;
  protected static String baseUrl;
  protected static Capabilities capabilities;
  protected static Integer timeToTimeout;

  private String getLoggerName= "-=WebDriverFactory=-";
  private Logger logger = LoggerFactory.getLogger(getLoggerName);

  protected WebDriver driver;


  @BeforeSuite
  public void initTestSuite() throws IOException {
    baseUrl = PropertyLoader.loadProperty("site.url");
    gridHubUrl = PropertyLoader.loadProperty("grid.url");
    timeToTimeout= Integer.valueOf(PropertyLoader.loadProperty("timeToTimeout"));
    if ("".equals(gridHubUrl)) {
      gridHubUrl = null;
    }
    capabilities = PropertyLoader.loadCapabilities();
    WebDriverFactory.setMode(WebDriverFactoryMode.THREADLOCAL_SINGLETON);
  }

  @BeforeMethod
  public void initWebDriver() {
    driver = WebDriverFactory.getDriver(gridHubUrl, capabilities);

//    System.setProperty("webdriver.chrome.driver","C:\\corevalue\\env\\chromedriver.exe");
//    ChromeOptions options = new ChromeOptions();
//    options.addArguments("--start-maximized");
//    options.addArguments("--lang=en-US");
//    DesiredCapabilities capability = DesiredCapabilities.chrome();
//    capability.setBrowserName("chrome");
//    capability.setCapability(ChromeOptions.CAPABILITY, options);
//    driver = new ChromeDriver(capability);
//    driver.manage().window().maximize();

    driver.manage().timeouts().implicitlyWait(timeToTimeout, TimeUnit.SECONDS);
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() {
    WebDriverFactory.dismissAll();
  }

  public void printUsingEnvInfo(){
    Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
    String browserName = cap.getBrowserName();
    String os = cap.getPlatform().toString();
    String browserVersion = cap.getVersion();
    logger.debug("Tests run on  platform = " + os + " with Browser = " + browserName + " "+ browserVersion);
  }

}
