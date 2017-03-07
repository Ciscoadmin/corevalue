package aqa.util;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that extracts properties from the prop file.
 */
public class PropertyLoader {

  private static final String DEBUG_PROPERTIES = "/debug.properties";

  public static Capabilities loadCapabilities() throws IOException {
    return loadCapabilities(System.getProperty("application.properties", DEBUG_PROPERTIES));
  }

  public static Capabilities loadCapabilities(String fromResource) throws IOException {
    Properties props = new Properties();
    props.load(PropertyLoader.class.getResourceAsStream(fromResource));
    String capabilitiesFile = props.getProperty("capabilities");

    boolean chromeOptionsSet=false;
    ChromeOptions options = new ChromeOptions();

    Properties capsProps =    new Properties();
    capsProps.load(PropertyLoader.class.getResourceAsStream(capabilitiesFile));

    DesiredCapabilities capabilities = new DesiredCapabilities();

     if (System.getProperty("ChromeOptions")!=null){// for e.g. mvn  test "-DChromeOptions=--start-maximized --lang=en-US"
          for (String v2 : System.getProperty("ChromeOptions").split(" ")) {
            options.addArguments(v2);
          }
      chromeOptionsSet=true;
    }

    for (String name : capsProps.stringPropertyNames()) {
      String value = capsProps.getProperty(name);
      if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
        capabilities.setCapability(name, Boolean.valueOf(value));
      } else if (value.startsWith("file:")) {
        capabilities.setCapability(name, new File(".", value.substring(5)).getCanonicalFile().getAbsolutePath());
      } else if (name.toLowerCase().startsWith("chromeoptions")){  //for fixing issue in non-English windows

          for (String v : value.split(" ")) {
            options.addArguments(v);
          }

        chromeOptionsSet=true;

      }else {
        capabilities.setCapability(name, value);
      }
    }

    if (chromeOptionsSet){
      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    return capabilities;
  }

  public static String loadProperty(String name) throws IOException {
    return loadProperty(name, System.getProperty("application.properties", DEBUG_PROPERTIES));
  }

  public static String loadProperty(String name, String fromResource) throws IOException {
    Properties props = new Properties();
    props.load(PropertyLoader.class.getResourceAsStream(fromResource));

    return props.getProperty(name);
  }

}