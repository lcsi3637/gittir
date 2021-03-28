package testclasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.Keys.ENTER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DemoTestClass {

    public ExtentHtmlReporter htmlReporter;
    public ExtentReports extent;
    public ExtentTest extentTest;
    WebDriver driver;

    @BeforeClass
    public void beforeClass(){
        htmlReporter=new ExtentHtmlReporter("./reports/extent.html");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setDocumentTitle("Automation Reports");
        htmlReporter.config().setReportName("Automation Test Results");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent=new ExtentReports();
        extent.setSystemInfo("Organization","Let's code it");
        extent.attachReporter(htmlReporter);

    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
//        WebDriverManager.firefoxdriver().setup();
//        driver=new FirefoxDriver();

        driver.manage().window().maximize();

        driver.get("http://live.demoguru99.com/");

        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
    @Test(priority = 1) // day1
    public void sortByName() {

        extentTest=extent.createTest("test sortByName");
        extentTest.log(Status.PASS,"Succesfully passed!!!");

        assertTrue(driver.getTitle().equals("Home page"));

        //  demoButoton
        WebElement demo = driver.findElement(By.xpath("//div[@class='page-title']/h2"));
        assertTrue(demo.getText().contains("THIS IS DEMO SITE FOR"));

        //mobile Button
        WebElement mobileButton = driver.findElement(By.className("level0"));
        mobileButton.click();

        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

        assertTrue(driver.getTitle().equals("Mobile"));


        // Select Drop Down
        WebElement selectDropDown = driver.findElement(By.xpath("//select[@title='Sort By']"));

        Select select = new Select(selectDropDown);

        select.selectByVisibleText("Name");
        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

        //ul[@class='products-grid products-grid--max-4-col first last odd']/li/a

        List<WebElement> items= driver.findElements(By.xpath("//li[@class='item last']"));

        String [] itemsArr = new String[items.size()];
        String [] itemsArr2 = new String[items.size()];

        for (int i=0;i< items.size();i++){
            itemsArr[i]=items.get(i).getText();
            itemsArr2[i]=items.get(i).getText();
        }


        Arrays.sort(itemsArr);

        for (int i = 0; i < itemsArr.length; i++) {
            assertEquals(itemsArr[i], (itemsArr2[i]));
        }



        assertTrue((items.get(0).getText().contains("IPHONE")));


    }

    @Test (priority = 2)// day 2
    public void priceCheck(){
        extentTest=extent.createTest("test priceCheck");
        extentTest.log(Status.PASS,"Succesfully passed!!!");

        //mobile Button
        WebElement mobileButton = driver.findElement(By.className("level0"));
        mobileButton.click();

        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

        assertTrue(driver.getTitle().equals("Mobile"));

        WebElement sonyXperiaPrice =driver.findElement(By.xpath("//ul[@class='products-grid products-grid--max-4-col first last odd']/li/div/div[1]"));

        String OutherPriceXperia = sonyXperiaPrice.getText();

        WebElement itemSonyXperia= driver.findElement(By.xpath("//img[@alt='Xperia'] "));
        itemSonyXperia.click();

        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

        WebElement sonyHeader=driver.findElement(By.xpath("//span[@class='h1']"));

        String expectedHeaderSony=sonyHeader.getText();
        System.out.println(expectedHeaderSony);

        assertTrue(sonyHeader.getText().equals("SONY XPERIA"));

        WebElement InnerPriceXperia=driver.findElement(By.xpath("//span[@class='price']"));

        assertEquals(OutherPriceXperia,InnerPriceXperia.getText());


    }

    @Test(priority = 3) //day3
    public void errorMessage(){

        extentTest=extent.createTest("test errorMessage");
        extentTest.log(Status.FAIL,"test failed!!! !!!");

        WebElement mobileButton = driver.findElement(By.className("level0"));
        mobileButton.click();

        driver.manage().timeouts().implicitlyWait(4000, TimeUnit.SECONDS);

        assertTrue(driver.getTitle().equals("Mobile"));

        WebElement sonyXperiaPrice =driver.findElement(By.xpath("//ul[@class='products-grid products-grid--max-4-col first last odd']/li/div/div[3]/button"));
        sonyXperiaPrice.click();

        WebElement idQty = driver.findElement(By.xpath("//input[@class='input-text qty']"));
        idQty.sendKeys("1000"+ENTER);

        String expectedErrorMessage="The requested quantity for \"Sony Experia\" is not avaliable.";

        WebElement errorMessage=driver.findElement(By.xpath("//li[@class=\"error-msg\"]/ul/li/span"));

        SoftAssert softAssert= new SoftAssert();
        softAssert.assertTrue(errorMessage.getText().contains(expectedErrorMessage));

        WebElement emptyButton=driver.findElement(By.xpath("//button[@title=\"Empty Cart\"]"));
        emptyButton.click();

        assertTrue(driver.findElement(By.xpath("//div[@class='page-title']")).getText().contains("EMPTY"));
        Assert.fail("Executing failed test method.xml");

    }



}
