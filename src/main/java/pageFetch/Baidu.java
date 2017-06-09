package pageFetch;

import Helper.fileHelper;
import Helper.urlHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static Helper.WEHelper.getByXPath;
import static Helper.WEHelper.getElements;

/**
 * Created by cdn on 17/6/8.
 */
public class Baidu implements Runnable{

    ArrayList<String> cinemas = new ArrayList<String>();
    ArrayList<String> movies = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    int start;
    int end;

    public Baidu(ArrayList<String> cinemas, ArrayList<String> movies, ArrayList<String> dates, int start, int end) {
        this.cinemas = cinemas;
        this.movies = movies;
        this.dates = dates;
        this.start = start;
        this.end = end;
    }

    public Baidu() {
    }

    public void fetchPage1() {

        WebDriver driver = new ChromeDriver();
        BaiduParser parser = new BaiduParser();
        String url = "https://dianying.nuomi.com/?cityId=315";
//        String url = "https://dianying.nuomi.com/movie/movielist?cityId=315";
        driver.get(url);
        parser.parsePage1(driver.getPageSource());
        driver.quit();
    }

    public void getBaiduDate() {
        String movieId = "63068";
        String url = "https://dianying.nuomi.com/movie/detail?cityId=315&movieId=63068";
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        ArrayList<WebElement> dates = getElements("//*[@id=\"dateList\"]/li[", "]", driver);
        ArrayList<String> baidu_date = new ArrayList<String>();

        for (WebElement date : dates) {
            String day = date.getAttribute("data-id");
            baidu_date.add(day);
        }
        fileHelper.writeIn("baidu_dates", baidu_date);
        driver.quit();
    }

    public void getBaiduCinema() {
        ArrayList<String> cinemas = new ArrayList<String>();
        String url = "https://dianying.nuomi.com/cinema?cityId=315";
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        showAllTheatres(driver);
        System.out.println("finish get");

        ArrayList<WebElement> elements = getElements("//*[@id=\"pageletCinemalist\"]/li[", "]/div[1]/p[1]/span", driver);
        System.out.println(elements.size());
        for (WebElement element : elements) {
            String name = element.getText();
            JSONObject o = new JSONObject(element.getAttribute("data-data"));
            int cinemaId = o.getInt("cinemaId");
            String t = name + "," + cinemaId;
            cinemas.add(t);
        }

        fileHelper.writeIn("baidu_cinemas", cinemas);

        driver.quit();
    }


    //cid city id // -1 超时 //id 日期问题
    public int getFileDetail(String cinemaid, String mid, String date, String cid) {
        String url = "https://dianying.nuomi.com/cinema/cinemadetail?cityId=315&cinemaId=" + cinemaid +
                "&movieId=" + mid + "&date=" + date;

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        try {
            driver.get(url);
        } catch (TimeoutException e) {
            driver.quit();
            return -1;
        }

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //check date

        String url_date = url.substring(url.length() - 13, url.length());
        System.out.println(mid);

        ArrayList<WebElement> elements = getElements("//div[@data-movieid=\""+ mid +"\"]/div[1]/span[", "]", driver);
        if (elements.size() == 0){
            System.out.println("do not have");
            driver.quit();
            return Integer.valueOf(cinemaid);
        }

//        System.out.println(elements.size());
        for (WebElement element : elements) {
//            System.out.println(element.getAttribute("class"));
            if (element.getAttribute("class").contains("active")) {
//                System.out.println(element.getText());
                String t_d = element.getAttribute("data-id");
                if (!t_d.equals(url_date)) {
                    driver.quit();
                    System.out.println("return cid :" + cinemaid + " " + t_d + " " + url_date);
                    return Integer.valueOf(cinemaid);
                } else
                    break;
            }
        }

        boolean lencheck = false;
        int lens[] = new int[5];
        //check size
        ArrayList<WebElement> starts = new ArrayList<WebElement>();
        ArrayList<WebElement> ends = new ArrayList<WebElement>();
        ArrayList<WebElement> threeDs = new ArrayList<WebElement>();
        ArrayList<WebElement> halls = new ArrayList<WebElement>();
        ArrayList<WebElement> prices = new ArrayList<WebElement>();

        while (!lencheck) {
            starts = getElements("#datelist > div.date.active.hide > div.session-list.hide.active > ul > li:nth-child(", ") > div.time.fl > p.start", driver);
            ends = getElements("#datelist > div.date.active.hide > div.session-list.hide.active > ul > li:nth-child(", ") > div.time.fl > p.end", driver);
            threeDs = getElements("#datelist > div.date.active.hide > div.session-list.hide.active > ul > li:nth-child(", ") > div.type.fl.font14", driver);
            halls = getElements("#datelist > div.date.active.hide > div.session-list.hide.active > ul > li:nth-child(", ") > div.hall.fl.font14", driver);
            prices = getElements("#datelist > div.date.active.hide > div.session-list.hide.active > ul > li:nth-child(", ") > div.price.fl > p > span.num.nuomi-red", driver);

            lens[0] = starts.size();
            lens[1] = ends.size();
            lens[2] = threeDs.size();
            lens[3] = halls.size();
            lens[4] = prices.size();

            boolean pass = true;
            for (int i = 1; i < lens.length; i++) {
                if (lens[i] != lens[0]) {
                    pass = false;
                    System.out.println("lens check fail try again");
                }
            }
            if (pass) {
                lencheck = true;
                System.out.println("pass len check");
            }
        }


        //get result
        ArrayList<String> infos = new ArrayList<String>();
        for (int i = 0; i < starts.size(); i++) {
            String start = starts.get(i).getText();
            String end = ends.get(i).getText();
            String threeD = threeDs.get(i).getText();
            String hall = halls.get(i).getText();
            String price = prices.get(i).getText();

            String info = mid + "," + cinemaid + "," + date + ","
                    + start + "," + end + "," + threeD + "," + hall + "," + price;

            infos.add(info);
        }

        fileHelper.writeIn("baidu_film_details_" + mid, infos);

        driver.quit();
        return 0;
    }


    public void showAllTheatres(WebDriver driver) {
        String finish = "没有更多结果了";
        boolean done = false;
        String more = "_";
        while (((!(more).equals(finish)) || (!more.equals(""))) && (!done)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebElement morebtn = getByXPath("//*[@id=\"moreCinema\"]", driver);
            more = morebtn.getText();
            if (!morebtn.getAttribute("class").equals("more hide"))
                morebtn.click();
            else done = true;
        }
    }

    public void getDetails() {
        for (int i = start; i <= end; i++) {
            String movie = movies.get(i);
            String mid = movie.split(",")[1];
            System.out.println("------movie " + movie + " start-----------");
            for (String cinema : cinemas) {
                String cinemaid = cinema.split(",")[1];
                for (String date : dates) {

                    int getState = -1;
                    while (getState == -1) {
//                        System.err.println("try again: " + mid + " " + cinema + " " + date);
                        getState = getFileDetail(cinemaid, mid, date, "0");

                    }
                    if (getState == Integer.valueOf(cinemaid)) {
//                        System.out.println(getState);
                        System.out.println(movie + "------dates finish-------");
                        break;
                    }

                    System.out.println(movie + "------date " + date + " done------");
                }
                System.out.println(movie + "------cinema " + cinema + " done---------");
            }
            System.out.println("------movie " + movie + " done-----------");

        }
    }

    public static void main(String[] args) {

        ChromeDriverManager.getInstance().setup();
        String cinemas_url = "/Users/user/Documents/code/FilmData/src/main/file/baidu_cinemas.txt";
        String movies_url = "/Users/user/Documents/code/FilmData/src/main/file/baidu_films_show.txt";
        String dates_url = "/Users/user/Documents/code/FilmData/src/main/file/baidu_dates.txt";
        ArrayList<String> cinemas = fileHelper.getStrings(cinemas_url);
        ArrayList<String> movies = fileHelper.getStrings(movies_url);
        ArrayList<String> dates = fileHelper.getStrings(dates_url);

        for (int i = 23;i < 32;i++){
            new Thread(new Baidu(cinemas, movies, dates,i,i)).start();
        }
    }

    public void run() {
        getDetails();
    }
}
