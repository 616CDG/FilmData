package pageFetch;

import Helper.WEHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

/**
 * Created by chentiange on 2017/6/9.
 */
public class Mtime_Movie_Details {
    private static String url="http://theater.mtime.com/China_Jiangsu_Province_Nanjing/movie/207927/20170610/";

    public static void main(String args[]){
        String[] movies;




        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<WebElement> buttons = new ArrayList<WebElement>();
        while(buttons.size() == 0){
            buttons = WEHelper.getElements("//*[@id=\"cinemaListRegion\"]/dl[","]/dd[1]/div[3]/a",driver);
        }
//        for(WebElement button:buttons){
//            button.click();
//        }


        ArrayList<WebElement> cinemas = new ArrayList<WebElement>();
        while(cinemas.size() == 0){
            cinemas = WEHelper.getElements("//*[@id=\"cinemaListRegion\"]/dl[","]",driver);
        }

        for(int i = 1;i <=cinemas.size(); ++i){
            String cinema_id = WEHelper.getByXPath("//*[@id=\"cinemaListRegion\"]/dl["+i+"]/dt/a[2]",driver).getAttribute("cid");
            ArrayList<WebElement> halls = new ArrayList<WebElement>();
            //click moved here
            buttons.get(i-1).click();
            while(halls.size() == 0){

//                halls = WEHelper.getElements("//*[@id=\"cinemaListRegion\"]/dl["+i+"]/dd[2]/ul/li[","]/a",driver);

                int count = 1;
                //get halls
                while(WEHelper.getByXPath("//*[@id=\"cinemaListRegion\"]/dl[" + i + "]/dd[2]/ul/li["+count+"]/a", driver) != null){
                    halls.add(WEHelper.getByXPath("//*[@id=\"cinemaListRegion\"]/dl[" + i + "]/dd[2]/ul/li["+count+"]/a", driver));
                    ++count;
                }

                for (WebElement hall: halls){

                    hall.click();
                    //hall page

                    //click: i know
                    WEHelper.getByXPath("//*[@id=\"contentEnd\"]/div[1]/div/div[2]/div[2]/a",driver).click();

                    String halltitle = WEHelper.getByXPath("//*[@id=\"LeftContentRegion\"]/div/div[1]",driver).getText();
                    String hallname = halltitle.split(" ")[1].split("\n")[0];
                    String starttime = WEHelper.getByXPath("//*[@id=\"htmlTag\"]/body/div[4]/div[2]/div/div[1]/span/strong",driver).getText().split(" ")[2];
                    String threed = WEHelper.getByXPath("//*[@id=\"htmlTag\"]/body/div[4]/div[2]/div/p[2]/strong",driver).getText().split(" ")[0];
                    String price = WEHelper.getByXPath("//*[@id=\"unitprice\"]",driver).getText();

                    System.out.println(hallname);
                    System.out.println(starttime);
                    System.out.println(threed);
                    System.out.println(price);

                    driver.navigate().back();



                    boolean breakIt = true;
                    while (true) {
                        breakIt = true;
                        try {
                            // write your code here
                            buttons.get(i-1).click();
                        } catch (Exception e) {
                            if (e.getMessage().contains("element is not attached")) {
                                breakIt = false;
                            }
                        }
                        if (breakIt) {
                            break;
                        }

                    }

                }
            }

        }


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
