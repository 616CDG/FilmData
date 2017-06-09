package Helper;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by cdn on 17/6/8.
 */
public class fileHelper {

    public static void appendFile(String filename, String content) {
        try {
            File file = new File("/Users/user/Documents/code/FilmData/src/main/file/" + filename + ".txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.append(content);
            bw.newLine();

            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeIn(String filename, ArrayList<String> ss) {

        try {
            File file = new File("/Users/user/Documents/code/FilmData/src/main/file/" + filename + ".txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            for (int i = 0; i < ss.size(); i++) {
                bw.append(ss.get(i));
                bw.newLine();
            }
            bw.close();
            System.out.println("---write--- " + filename);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeIn(String filename, String content) {

        try {
            File file = new File("/Users/user/Documents/code/FilmData/src/main/file/" + filename + ".txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.write(content);

            bw.close();
            System.out.println("---write---");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getStrings(String filePath) {
        ArrayList<String> ret = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line = br.readLine()) != null) {
                ret.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
