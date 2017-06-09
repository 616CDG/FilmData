package pageFetch;

import Helper.fileHelper;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by cdn on 17/6/8.
 */
public class BaiduParser {


    public void parsePage1(String html){
        Document doc = Jsoup.parse(html);
        Elements oList1 = doc
                .select("div.flex-viewport")
                .get(0)
                .select(".item");
        Elements oList2 = doc
                .select("div.flex-viewport")
                .get(1)
                .select(".item");
        ArrayList<String> shows = getName(oList1);
        ArrayList<String> noshows = getName(oList2);
        fileHelper.writeIn("baidu_films_show",shows);
        fileHelper.writeIn("baidu_films_noshow",noshows);
    }


    public static ArrayList<String> getName(Elements oList){
        ArrayList<String> ret = new ArrayList<String>();
        for (Element o: oList) {
            String name = o.select("p").get(0).text();
            JSONObject movieId = new JSONObject(o.select(".img").get(0).attr("data-data"));
            int id = movieId.getInt("movieId");
            String temp = name + "," + id;
            ret.add(temp);
        }
        return ret;
    }
}
