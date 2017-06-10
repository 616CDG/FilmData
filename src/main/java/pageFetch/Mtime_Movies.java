
package pageFetch;
import Helper.WEHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chentiange on 2017/6/9.
 */
public class Mtime_Movies {
    private static String url="http://theater.mtime.com/China_Jiangsu_Province_Nanjing/";

    public static void main(String args[]){
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement e = null;
        while(e == null){
            e = WEHelper.getByXPath("//*[@id=\"hotplayMoreLink\"]",driver);
        }
        e.click();

        ArrayList<WebElement> elements1 = new ArrayList<WebElement>();
        //column 1.1
        while(elements1.size() == 0){
            elements1 = WEHelper.getElements("//*[@id=\"hotplayContent\"]/div[1]/div[1]/div[2]/ul[","]/li[1]/dl/dt/a",driver);
        }


        ArrayList<WebElement> elements2 = new ArrayList<WebElement>();
        //column 1.2
        while(elements2.size() == 0){
            elements2 = WEHelper.getElements("//*[@id=\"hotplayContent\"]/div[1]/div[1]/div[2]/ul[","]/li[2]/dl/dt/a",driver);
        }

//        ArrayList<WebElement> elements3 = new ArrayList<WebElement>();
//        //column 2.1
//        while(elements3.size() == 0){
//            elements3 = WEHelper.getElements("//*[@id=\"hotplayContent\"]/div[1]/div[1]/div[2]/ul[","]/li[2]/dl/dt/a",driver);
//        }


//        System.out.println("size: "+size);
//        for (WebElement a: elements){
//            String content = a.getText()+","+a.getAttribute("href").split("/")[3];
//            System.out.println(content);
////            Mtime_Cinemas.appendFile("/Users/chentiange/Desktop/FilmData/src/main/Mtime/mtime_films_show",content);
//
//        }
        driver.quit();
    }
}
