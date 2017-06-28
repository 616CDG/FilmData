package Helper;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by cdn on 17/6/28.
 */
public class DouBanHelper {
    public static JSONArray getData(String url) {

        String stubsApiBaseUri = url;
        try {

            HttpClient client = HttpClients.createDefault();


            URIBuilder builder = new URIBuilder(stubsApiBaseUri);

            String listStubsUri = builder.build().toString();

            HttpGet getStubMethod = new HttpGet(listStubsUri);

//            getStubMethod.setHeader(HttpHeaders.AUTHORIZATION,
////                    String.format("Basic %s",
////                            new Object[]{DatatypeConverter.printBase64Binary(
////                                    String.format("%s:%s", new Object[]{"findgithubdata","github1"}).getBytes(Charsets.UTF_8))
////                    }));
//                    String.format("Basic %s",
//                            new Object[]{DatatypeConverter.printBase64Binary(
//                                    String.format("%s:%s", new Object[]{"githubhelper2","chen1233"}).getBytes(Charsets.UTF_8))
//                            }));

            HttpResponse getStubResponse = client.execute(getStubMethod);
            int getStubStatusCode = getStubResponse.getStatusLine()
                    .getStatusCode();
            if (getStubStatusCode < 200 || getStubStatusCode >= 300)

            {
                // Handle non-2xx status code
                return null;
            }

            String responseBody = EntityUtils
                    .toString(getStubResponse.getEntity());

            JSONArray jsonArray = new JSONArray(responseBody);

            return jsonArray;

        } catch (URISyntaxException | IOException e) {
            // Handle errors
        }

        return null;
    }

    public static void main(String[] args){
        String filePath = "/Users/user/Documents/un/s7/应用集成/h3/filmsData/films.csv";
        String url = "https://movie.douban.com/j/chart/top_list?type=28&interval_id=100%3A90&action=&start=0&limit=500";

        int[] types = {11, 24, 5, 13, 17, 25, 10, 19,  20, 1, 23, 6, 26, 14 , 7, 28, 8,  2, 4,  22, 3, 27, 16, 15, 12,29 ,30, 18, 31};

        JSONArray array = getData(url);

        try{
            File f = new File(filePath);
            BufferedWriter bw = new BufferedWriter(new FileWriter(f,true));
            for (int i = 0;i < array.length();i++){
                String movie_url = ((JSONObject)array.get(i)).getString("url");
                movie_url.replace("\\","");
                bw.append(movie_url);
                bw.newLine();
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
