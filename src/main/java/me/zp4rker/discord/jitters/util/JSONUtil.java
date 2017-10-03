package me.zp4rker.discord.jitters.util;

import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author ZP4RKER
 */
public class JSONUtil {

    /*public static JSONObject readFile(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            ExceptionHandler.handleException(e);
        }
        return new JSONObject(result);
    }*/

    public static JSONObject readFile(File file) {
        String data = "";
        try {
            FileReader rd = new FileReader(file);
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = rd.read()) != -1) {
                sb.append((char) c);
            }
            data = sb.toString();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return new JSONObject(data);
    }

    public static void writeFile(String data, File file) {
        try {
            makeFile(file);

            FileWriter wr = new FileWriter(file);
            wr.write(data);
            wr.flush();
            wr.close();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private static void makeFile(File file) throws IOException {
        if (!file.getParentFile().exists()) file.getParentFile().mkdir();
        if (!file.exists()) file.createNewFile();
    }

}
