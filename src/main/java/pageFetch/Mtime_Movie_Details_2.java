package pageFetch;

import Helper.WEHelper;
import Helper.fileHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chentiange on 2017/6/9.
 */
public class Mtime_Movie_Details_2 implements Runnable{
    private static String url="http://theater.mtime.com/";



    public static void main(String args[]){
        ChromeDriverManager.getInstance().setup();
        for (int i = 0; i < 1; i++) {
            new Thread(new Mtime_Movie_Details_2()).start();
        }

    }

    private static void getPieceInfo(String infourl, WebDriver driver, int piece, int date, String cinemaid) {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        //change piece
        if (piece != 0){
            WebElement e = null;
            while(e == null){
                e = WEHelper.getByXPath("//*[@id=\"movieItemListRegion\"]/b["+piece+"]/a",driver);
            }
            try {
                e.click();
            }catch (Exception ex){
                    boolean breakIt = true;
                    while (true) {
                        breakIt = true;
                        try {
                            WEHelper.getByXPath("//*[@id=\"movieNext\"]", driver).click();
                        } catch (Exception ex1) {
                            breakIt = false;
                        }
                        if (breakIt) {
                            break;
                        }
                }
            }
        }
        //get halls
        ArrayList<WebElement> halls = WEHelper.getElements("//*[@id=\"showtimesRegion\"]/table/tbody/tr[","]",driver);
        int hallnum = halls.size();
        String movie = WEHelper.getByXPath("//*[@id=\"movieDetailRegion\"]/h2/a",driver).getAttribute("href");
        String movieid = movie.substring(23);
        movieid  = movieid.substring(0, movieid.length()-1);
        for (int i = 1;i <= hallnum; ++i){
            String first = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr["+i+"]",driver).getAttribute("class");
            if (first!= null && first.length() != 0) {
                String starttime = new String();
                try {
                    starttime = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[1]/div/p[1]", driver).getText();
                } catch (Exception e1) {
                    boolean breakIt = true;
                    while (true) {
                        breakIt = true;
                        try {
                            starttime = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[1]/div/p[1]", driver).getText();
                        } catch (Exception ex1) {
                            breakIt = false;
                        }
                        if (breakIt) {
                            break;
                        }
                    }
                }
                String end = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[1]/div/p[2]", driver).getText();
                String endtime = end.substring(2, 7);
                String threed = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[2]/p[1]", driver).getText();
                String hallname = new String();
                boolean breakIt = true;
                while (true) {
                    breakIt = true;
                    try {
                        hallname = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[3]/div/p[1]", driver).getText();
                    } catch (Exception ex1) {
                        breakIt = false;
                    }
                    if (breakIt) {
                        break;
                    }
                }

                String price = new String();
                WebElement price_ele = WEHelper.getByXPath("//*[@id=\"showtimesRegion\"]/table/tbody/tr[" + i + "]/td[4]/p[1]", driver);
                if (price_ele != null){
                    price = price_ele.getText();
                }
                if (price.length() > 0) {
                    price = price.substring(1);
                }
                String content = movieid + "," + cinemaid + "," + date + "," + starttime + "," + endtime + "," + threed + "," + hallname + "," + price;
                System.out.println(content);
                if (map.get(movieid) == null){
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(content);
                    map.put(movieid,list);
                }else {
                    ArrayList<String> list = map.get(movieid);
                    list.add(content);
                    map.put(movieid,list);
                }
//                Mtime_Cinemas.appendFile("/Users/chentiange/Desktop/FilmData/src/main/Mtime/mtime_film_details_"+movieid,content);
            }

        }
        fileHelper.appendDetails(map);

    }

    private static int getPageSize(String infourl, WebDriver driver) {
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        try {
            driver.get(infourl);
//        }catch (Exception ex){
//            driver.get(infourl);
//        }
        ArrayList<WebElement> movies = WEHelper.getElements("//*[@id=\"movieItemListRegion\"]/b[","]/a",driver);
        int size = movies.size();
        return size;
    }


    public void run() {
        WebDriver driver = new ChromeDriver();
        ArrayList<String> cinemas = fileHelper.getStrings("/Users/chentiange/Desktop/FilmData/src/main/Mtime/mtime_cinemas");
        for (int j = 19;j < cinemas.size(); ++j){
            String cinema = cinemas.get(j);
            String[] infos = cinema.split(",");
            String cinemaid = infos[1];
            String stringid = infos[2];
            for (int date = 20170610; date <= 20170615; ++date) {
                String infourl = url + stringid + "/" + cinemaid + "/?d=" + date;
                System.out.println(infourl);
                int size = getPageSize(infourl, driver);
                for (int i = 0; i < size; ++i){
                    getPieceInfo(infourl, driver, i, date, cinemaid);
                }
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
        driver.quit();
    }
}
