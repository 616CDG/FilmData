package Helper;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cdn on 17/6/9.
 */
public class DatabaseHelper {

    public static Connection con;
    public static Statement statement;
    String baseurl = "/Users/user/Documents/code/FilmData/src/main/file/";

    public DatabaseHelper() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://115.159.106.212:3306/filmdata?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "";
        try {
            con = DriverManager.getConnection(url, user, password);
            statement = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlatform(String name) {

        try {
            String query = "INSERT INTO platforms (platform) VALUES ('" + name + "');";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getD(String mid) {
        int ret = 0;
        char d = '0';
        ArrayList<String> infos = fileHelper.getStrings("/Users/user/Documents/code/FilmData/src/main/file/baidu_film_details_" + mid + ".txt");
        if (infos.size() > 0)
            d = infos.get(0).split(",")[5].charAt(0);
        return d - '0';
    }

    public void saveFilms() {
        ArrayList<String> films = fileHelper.getStrings("/Users/user/Documents/code/FilmData/src/main/file/platform_baidu_films.txt");
        ArrayList<String> names = fileHelper.getStrings("/Users/user/Documents/code/FilmData/src/main/file/baidu_films_show.txt");
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> nas = new ArrayList<String>();
        for (String n : names) {
            String id = n.split(",")[1];
            String na = n.split(",")[0];
            ids.add(id);
            nas.add(na);
        }

        for (int i = 0; i < films.size(); i++) {
            String film = films.get(i);
            String[] infos = film.split(",,");
            String name = infos[0];
            String first_run = infos[8].substring(5, infos[8].length() - 2);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date parsed = null;
            try {
                parsed = format.parse(first_run);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.sql.Date sqldate = new java.sql.Date(parsed.getTime());
            String language = "";
            String id = "-1";

            for (int j = 0; j < nas.size(); j++) {

                if (nas.get(j).equals(name)) {
                    System.out.println(nas.get(j) + " " + name);
                    id = ids.get(j);
                    break;
                }
            }
            int d = getD(id);
            String area = infos[6].substring(3, infos[6].length());
            String length = infos[7].substring(3, infos[7].length() - 2);
            String director = infos[3].substring(3, infos[3].length());
            String main_c = infos[4].substring(3, infos[4].length());


            try {
                String query = "INSERT INTO films (name,first_run,language,d,area,length,director,main_characters) " +
                        "VALUES ('" + name + "','" + sqldate + "','" + language + "'" +
                        ",'" + d + "','" + area + "','" + length + "','" + director + "','" + main_c + "');";

//                String update = "UPDATE films SET area='"+ area +"' WHERE name='"+ name +"'";
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    private void insert(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void savePlatformFilm() {
        int baiduId = 1;

        ArrayList<String> infos = fileHelper.getStrings("/Users/user/Documents/code/FilmData/src/main/file/platform_baidu_films.txt");
        for (int i = 0; i < infos.size(); i++) {
            String[] info = infos.get(i).split(",,");
            String name = info[0];
            int fid = -1;
            try {
                String query = "SELECT id from films f WHERE f.name = '" + name + "'";
                ResultSet re = statement.executeQuery(query);
                if (re.next())
                    fid = re.getInt("id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (fid != -1) {
                String des = info[5].substring(3, info[5].length());
                String score = info[1];
                String type = info[9];

                String sql = "INSERT INTO platform_films (film_id,platform_id,description,score,type) VALUES" +
                        "('" + fid + "','" + baiduId + "','" + des + "'" +
                        ",'" + score + "','" + type + "')";

                try {
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void saveTheatres(String name, String address, String phone, int id) {
        String sql = "INSERT INTO theatres (name,address,phone,platform_id) VALUES" +
                "('" + name + "','" + address + "','" + phone + "','" + id + "')";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getBaiduTheatreId(String id) {

        ArrayList<String> cs = fileHelper.getStrings(baseurl + "baidu_cinemas.txt");
        for (String c : cs) {
            if (id.equals(c.split(",")[1])) {
                String name = c.split(",")[0];
                try {
                    String query = "SELECT id from theatres t WHERE t.name = '" + name + "'";
                    ResultSet re = statement.executeQuery(query);
                    if (re.next())
                        return re.getInt("id");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return -1;
    }

    public int getFilmId(String name) {
        try {
            String query = "SELECT id from films f WHERE f.name = '" + name + "'";
            ResultSet re = statement.executeQuery(query);
            if (re.next())
                return re.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveShowInfo() {
        int baiduId = 1;

        ArrayList<String> films = fileHelper.getStrings(baseurl + "baidu_films_show.txt");
        for (int i = 0; i < 1; i++) {
            String name = films.get(i).split(",")[0];
            String id = films.get(i).split(",")[1];
            String filename = baseurl + "baidu_film_details_" + id + ".txt";
            ArrayList<String> infos = fileHelper.getStrings(filename);
            System.out.println("size " + infos.size());
            for (int j = 0; j < infos.size(); j++) {
                String info = infos.get(j);
                String[] ss = info.split(",");

                //10345,812,1497024000000,20:45,22:12散场,2D,5号厅,39
                Date date = new Date(Long.valueOf(ss[2]) + 45 * 1000 * 60 + 20 * 60 * 60 * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(date);

                String price = ss[7];
                String hall = ss[6];
                int tid = getBaiduTheatreId(ss[1]);

                int fid = getFilmId(name);
//                System.out.println(tid);
//                System.out.println(fid);
                try {
                    String query = "INSERT INTO show_infos (film_id,theatre_id,video_hall,time,platform_id,price) " +
                            "VALUES ('" + fid + "','" + tid + "','" + hall + "'" +
                            ",'" + time + "','" + baiduId + "','" + price + "');";
                    statement.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(i + " " + name + "finish");
        }


    }

    public void updateFilmImg(String name, String url) {
        try {
            String query = "UPDATE films f SET f.url = '" + url + "' WHERE f.name = '" + name + "'";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void handleGWLData() {
        GWLDataHelper helper = new GWLDataHelper();
        ArrayList<String> infos = helper.getTheatres();
        for (int i = 0; i < infos.size() / 3; i++) {
            String name = infos.get(i);
            String add = infos.get(i + 1);
            String phone = infos.get(i + 2);
        }
    }

    public void gwlTheatres() {
        GWLDataHelper helper2 = new GWLDataHelper();
        //gwl theatre
//        ArrayList<String> infos = helper2.getTheatres();
//
//        for (int i = 0;i < infos.size()/3;i++){
//            String name = infos.get(3*i);
//            String add = infos.get(3*i + 1);
//            String phone = infos.get(3*i + 2);
//            saveTheatres(name,add,phone,2);
//            System.out.println(name + "," + add + "," + phone);
//            System.out.println("-----");
//        }
    }

    public void gwlFilms() {
        GWLDataHelper helper2 = new GWLDataHelper();

        int len = 11;
        //gwl films
        ArrayList<String> films = helper2.getFilms();
        for (int i = 0; i < films.size() / len; i++) {

            try {

                String check = "SELECT COUNT(*) `cf` FROM films f WHERE f.name='" + films.get(i * len) + "'";
                ResultSet rs = statement.executeQuery(check);
                rs.next();
                int rowCount = rs.getInt("cf");
                if (rowCount == 0) {


                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date parsed = null;
                    try {
                        parsed = format.parse(films.get(i * len + 1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    java.sql.Date sqldate = new java.sql.Date(parsed.getTime());

                    String query = "INSERT INTO films (name,first_run,language,d,area,length,director,main_characters) " +
                            "VALUES ('" + films.get(i * len + 0) + "','" + sqldate + "','" + films.get(i * len + 2) + "'" +
                            ",'" + films.get(i * len + 3) + "','" + films.get(i * len + 4) + "','" + films.get(i * len + 5) + "','" + films.get(i * len + 6) + "','" + films.get(i * len + 7) + "');";
                    statement.executeUpdate(query);

                } else {
//                    System.out.println(rowCount + " " + films.get(i*len));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void gwlPFilms() {
        GWLDataHelper helper2 = new GWLDataHelper();
        int len = 11;
        int gwlId = 2;
        //gwl films
        ArrayList<String> films = helper2.getFilms();
        for (int i = 0; i < films.size() / len; i++) {
            int fid = getFilmId(films.get(i*len));
            String type = helper2.getType(films.get(i*len));
            type = type.substring(0,type.length() - 1);
            String des = films.get(i*len + 9).substring(5,films.get(i*len + 9).length());

            String sql = "INSERT INTO platform_films (film_id,platform_id,description,score,type) VALUES" +
                    "('" + fid + "','" + gwlId + "','" + des + "'" +
                    ",'" + films.get(i*len + 10) + "','" + type + "')";

            try {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            helper2.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void gwlShowInfos(){
        GWLDataHelper helper2 = new GWLDataHelper();
        int len = 6;
        int gwlId = 2;
        ArrayList<String> infos = helper2.getShowInfos();
        for (int i = 0; i < infos.size() / len; i++) {
            int fid = getFilmId(infos.get(i*len));

            System.out.println(fid + " ");
            for (int j = 0;j < len;j++){
                System.out.print(infos.get(i*len + j) + " ");
            }
//            System.out.println();

            String tname = infos.get(i*len + 1);
            int tid = 0;

            String sql = "SELECT t.id FROM theatres t WHERE " +
                    "t.name = '" + tname + "' AND t.platform_id=" + gwlId;
            try {
                ResultSet re = statement.executeQuery(sql);
                if (re.next()){
                    tid = re.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            System.out.println(tid);
            try {
                String query = "INSERT INTO show_infos (film_id,theatre_id,video_hall,time,platform_id,price) " +
                        "VALUES ('" + fid + "','" + tid + "','" + infos.get(i*len + 3) + "'" +
                        ",'" + infos.get(i*len + 2) + "','" + gwlId + "','" + infos.get(i*len + 5) + "');";
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        try {
            helper2.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseHelper helper = new DatabaseHelper();

//        helper.gwlPFilms();
        helper.gwlShowInfos();
//        helper.saveShowInfo();
//        helper.savePlatform("格瓦拉");
//        helper.saveFilms();
//        helper.savePlatformFilm();
//        helper.handleGWLData();
//
        try {
            helper.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
