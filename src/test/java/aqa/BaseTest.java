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
import org.testng.ReporterConfig;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.selenium.factory.WebDriverFactory;
import ru.stqa.selenium.factory.WebDriverFactoryMode;
import ru.yandex.qatools.allure.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Base class for TestNG-based test classes
 */
public class BaseTest {

  @Parameter("Grid hub url")
  protected static String gridHubUrl;

  @Parameter("Base web driver url")
  protected static String baseUrl;

  protected static Capabilities capabilities;

  @Parameter("Timeout in sec for WebDriver")
  protected static Integer timeToTimeout;

  private String getLoggerName= "-=WebDriverFactory=-";
  private Logger logger = LoggerFactory.getLogger(getLoggerName);
  private Properties env = new Properties();

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
    saveEnvironmentsToFileForAllureReport();
  }


  public void printUsingEnvInfo(){
    Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

    env.setProperty("Browser Name",cap.getBrowserName());
    env.setProperty("Browser Version",cap.getVersion());
    env.setProperty("Platform",cap.getPlatform().toString());
    logger.debug("Tests run on  platform = " + env.getProperty("Platform") + " with Browser = " + env.getProperty("Browser Name") + " "+ env.getProperty("Browser Version"));

//    String browserName = cap.getBrowserName();
//    String os = cap.getPlatform().toString();
//    String browserVersion = cap.getVersion();
//    logger.debug("Tests run on  platform = " + os + " with Browser = " + browserName + " "+ browserVersion);

  }

  public void saveEnvironmentsToFileForAllureReport(){

    File file = Paths.get(System.getProperty("user.dir"),"/target/allure-results").toAbsolutePath().toFile();

    if (!file.exists()){
      System.out.println("Created dirs: "+file.mkdirs());
    }

    try (FileWriter out = new FileWriter("./target/allure-results/environment.properties")) {
        env.store(out,"Environment variables for Allure report");
    }catch(IOException e){
        System.out.println(e.getMessage());
      }

  }


}
