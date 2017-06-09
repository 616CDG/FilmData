package pageFetch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by chentiange on 2017/6/9.
 */
public class Mtime_Movie_Links {
    private static String url="http://www.bignox.com/";



    public static void main(String args[]){
//        System.setProperty("webdriver.chrome.driver", "C:\\browser\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        String targeturl=driver.getCurrentUrl();
        System.out.println(targeturl);
        WebElement element =  driver.findElement(By.xpath("//*[@id='index-page']/footer/div[3]/ul/li[2]/a/span"));
        String aa = element.getAttribute("id");
        String a2 = element.getText();
        System.out.println(aa);
        System.out.println(a2);
        driver.quit();



    }
}
