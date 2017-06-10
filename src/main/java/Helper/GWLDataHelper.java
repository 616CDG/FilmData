package Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cdn on 17/6/10.
 */
public class GWLDataHelper {

    public Connection con;
    public Statement statement;
    String baseurl = "/Users/user/Documents/code/FilmData/src/main/file/";

    public GWLDataHelper() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:8889/movie?useUnicode=true&characterEncoding=UTF-8";
        String user = "user";
        String password = "123321";
        try {
            con = DriverManager.getConnection(url, user, password);
            statement = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getType(String name){

        String ret = "";
        try {
            String query = "SELECT t.name FROM films f, film_label fl, type t " +
                    "WHERE f.name = '" + name + "'" +
                    "AND f.id = fl.fid AND fl.typeid = t.id";
            ResultSet re = statement.executeQuery(query);
            while(re.next()){
                ret += re.getString("name") + "/";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;

    }

    public ArrayList<String> getFilms(){

        ArrayList<String> ret = new ArrayList<String>();
        try {

            String query = "SELECT * FROM `films`";
            ResultSet re = statement.executeQuery(query);

            while (re.next()){
                String name = re.getString("name");
                String first_run = re.getString("firstrun");
                String language = "";
                String d = String.valueOf(Integer.valueOf(re.getString("is3d")) + 2);
                String area = re.getString("area");
                String length = "0";
                String director = re.getString("director");
                String mc = re.getString("mc");
                String url = "";
                String descrtption = re.getString("description");
                String score = re.getString("scores");
//                String score =

                ret.add(name);
                ret.add(first_run);
                ret.add(language);
                ret.add(d);
                ret.add(area);
                ret.add(length);
                ret.add(director);
                ret.add(mc);
                ret.add(url);
                ret.add(descrtption);
                ret.add(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public ArrayList<String> getTheatres(){
        ArrayList<String> ret = new ArrayList<String>();
        try {

            String query = "SELECT * FROM `theatres`";
            ResultSet re = statement.executeQuery(query);

            while (re.next()){
                String name = re.getString("name");
                String address = re.getString("address");
                String phone = re.getString("phone");
                ret.add(name);
                ret.add(address);
                ret.add(phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public ArrayList<String> getShowInfos(){
        //fid,tid,hall,time,pid,price,0
        String gwlId = "2";
        ArrayList<String> ret = new ArrayList<String>();
        try {
            String query = "SELECT * FROM show_infos";
            ResultSet re = statement.executeQuery(query);
            while (re.next()){
//                System.out.println("in");
                int fid = re.getInt("fid");
                int tid = re.getInt("tid");
                String fname = "";
                String tname = "";
                //fname0
                Statement s2 = con.createStatement();
                String q1 = "SELECT f.name FROM films f " +
                        "WHERE f.fid = " + fid;
                ResultSet re1 = s2.executeQuery(q1);
                if (re1.next()){
                    fname = re1.getString("name");
                }

                //tname1
                String q2 = "SELECT t.name FROM theatres t " +
                        "WHERE t.tid = " + tid;
                ResultSet re2 = s2.executeQuery(q2);
                if (re2.next())
                    tname = re2.getString("name");


                if (tname.length() > 0 && fname.length() > 0){
                    ret.add(fname);
                    ret.add(tname);
                    //time2
                    ret.add(re.getString("start"));
                    //hall3,pid4,price5
                    ret.add(re.getString("hall"));
                    ret.add(gwlId);
                    ret.add(String.valueOf(re.getInt("price")));
                }
//                if (ret.size()%6 != 0){
//                    System.out.println("err: " + fid + " " + tid + " ");
//                    break;
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }



    public static void main(String[] args){

    }




}
