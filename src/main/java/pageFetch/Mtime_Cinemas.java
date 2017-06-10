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
public class Mtime_Cinemas {
    private static String url="http://theater.mtime.com/China_Jiangsu_Province_Nanjing/";



    public static void appendFile(String filename, String content){
        FileWriter writer= null;
        try {
            writer = new FileWriter(filename,true);
            writer.write(content+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]){
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
//        String targeturl=driver.getCurrentUrl();
//        System.out.println(targeturl);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement e = null;
        while(e == null){
            e = WEHelper.getByXPath("//*[@id=\"ticketSearchDiv\"]/div[2]/a",driver);
        }
        e.click();

        ArrayList<WebElement> elements = new ArrayList<WebElement>();
        while(elements.size() == 0){
            elements = WEHelper.getElements("//*[@id='ticketSearchDiv']/div[5]/div/p[","]",driver);
        }
        int size = elements.size();
        for (int i = 1; i<=size; ++i){
            String path = "//*[@id=\"ticketSearchDiv\"]/div[5]/div/p["+i+"]/a[";
            ArrayList<WebElement> as = new ArrayList<WebElement>();
            while (as.size() == 0) {
                as = WEHelper.getElements(path, "]", driver);
            }
            for(WebElement a: as){
                String content = a.getText()+","+a.getAttribute("value")+","+a.getAttribute("stringid");
                appendFile("/Users/chentiange/Desktop/FilmData/src/main/Mtime/mtime_cinemas",content);
            }
        }
        driver.quit();




    }
}
