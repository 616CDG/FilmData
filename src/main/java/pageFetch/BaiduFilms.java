package pageFetch;

import Helper.fileHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

import static Helper.WEHelper.getByXPath;

/**
 * Created by cdn on 17/6/8.
 * 平台-电影信息
 */
public class BaiduFilms{

    ArrayList<String> movies = new ArrayList<String>();

    public BaiduFilms(ArrayList<String> movies) {
        this.movies = movies;
    }

    public void getFilmInfo(String mid){
        String url = "https://dianying.nuomi.com/movie/detail?movieId=" + mid;
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        getByXPath("//*[@id=\"moreBtn\"]",driver).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String name = getByXPath("//*[@id=\"infoCopy\"]/h4/div[1]",driver).getText();
        String score = getByXPath("//*[@id=\"infoCopy\"]/h4/div[2]/span",driver).getText();
        String des = getByXPath("//*[@id=\"infoCopy\"]/div/p[1]",driver).getText();
        String director = getByXPath("//*[@id=\"infoCopy\"]/div/p[2]",driver).getText();
        String main_cs = getByXPath("//*[@id=\"infoCopy\"]/div/p[3]",driver).getText();
        String description = getByXPath("//*[@id=\"infoCopy\"]/div/p[4]",driver).getText();
        String area = getByXPath("//*[@id=\"infoCopy\"]/div/p[5]",driver).getText();
        String length = getByXPath("//*[@id=\"infoCopy\"]/div/p[6]",driver).getText();
        String first_show = getByXPath("//*[@id=\"infoCopy\"]/div/p[7]",driver).getText();

        String info = name + ",," + score + ",," + des + ",," + director + ",," +main_cs
                + ",," + description + ",," + area + ",," + length + ",," + first_show;

        System.out.println(info);
        fileHelper.appendFile("platform_baidu_films",info);
        driver.quit();
    }



    public static void main(String[] args){
        String movies_url = "/Users/user/Documents/code/FilmData/src/main/file/baidu_films_show.txt";
        ArrayList<String> movies = fileHelper.getStrings(movies_url);
        ChromeDriverManager.getInstance().setup();
        BaiduFilms baiduFilms = new BaiduFilms(movies);

        for (int i = 0;i < movies.size();i++){
            baiduFilms.getFilmInfo((movies.get(i)).split(",")[1]);
        }
    }


}
