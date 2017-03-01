package aqa;

import aqa.util.PropertyLoader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dbolgarov on 3/1/2017.
 */
public class TestNGListener implements ITestListener, ISuiteListener, IExecutionListener {

    private String getLoggerName= "-=TestNG EXECUTOR=-";
    private Logger logger = LoggerFactory.getLogger(getLoggerName);
    private long startTime;

    private WebDriver driver;


    /**
     * Add message to  TestNG report  and to the log file
     *
     * @param message  Message  that  should  be  added  to  TestNG report  and to the log file  at the  same  time
     */
    public  void toLoggerAndTestNGReport(String message){
        logger.debug(message);
        Reporter.log(message,false);
    }



    @Override
    public void onExecutionStart() {
        startTime = System.currentTimeMillis();
        logger.debug("========================================================================= TestNG is going to start ====================================================================================");
    }

    @Override
    public void onExecutionFinish() {
        logger.debug("TestNG has finished, took around " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
        logger.debug("");
        logger.debug("");

    }

    @Override
    public void onStart(ISuite suite) {
        logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        logger.debug(suite.getName() + "  is  starting ..." );
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        logger.debug(suite.getName() +" has FINISHED ..." );
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.debug("................................................................");
        logger.debug(result.getName() + " from "+result.getTestClass()+"  is  starting ..." );
        logger.debug("................................................................");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.debug(result.getName() + " from "+result.getTestClass()+"  PASSED" );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        this.driver = ((BaseTest)result.getInstance()).driver; // obtaining  driver
        toLoggerAndTestNGReport(result.getName() + " from "+result.getTestClass()+"<FONT color=\"red\">  FAILED </FONT>");

        try {
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String dstFileNamePlusPath = PropertyLoader.loadProperty("screenshotsFolder")+ "\\" +new SimpleDateFormat("dd.MM.yyyy_hh.mm_").format(new Date())+result.getName().toString().trim()+".png";
            FileUtils.copyFile(scrFile,  new File(dstFileNamePlusPath));

            toLoggerAndTestNGReport("New screenshot was  created - " + dstFileNamePlusPath);
            Reporter.log("<a href='"+ "../../"+dstFileNamePlusPath + "'> <img src='"+"../../" +dstFileNamePlusPath + "' height='600' width='1024'/> </a>");

        } catch (IOException e) {
            toLoggerAndTestNGReport("<FONT color=\"red\">  New screenshot CANNOT BE  created !!! ERROR - See  stack trace below </FONT>");
            e.printStackTrace();
        }


    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.debug(result.getName() + " from "+result.getTestClass()+"  SKIPPED" );
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
