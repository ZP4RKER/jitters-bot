package co.zpdev.bots.jitters.util;

import co.zpdev.bots.core.exception.ExceptionHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JSONUtil {

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
        return data.isEmpty() ? new JSONObject() : new JSONObject(data);
    }

    public static void writeFile(String data, File file) {
        try {
            makeFile(file);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
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
