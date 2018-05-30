package co.zpdev.bots.jitters.util;

import co.zpdev.bots.core.exception.ExceptionHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Util class for reading and writing JSON files, both locally and via the web.
 *
 * TODO: Move to core
 * TODO: Add read from web support
 *
 * @author zpdev
 * @version 0.9_BETA
 */
public class JSONUtil {

    /**
     * Reads JSON data from a file.
     *
     * @param file the file to read
     * @return the parsed JSONObject
     */
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
            ExceptionHandler.handleException("Reading file", e);
        }
        return data.isEmpty() ? new JSONObject() : new JSONObject(data);
    }

    /**
     * Writes data to a file.
     *
     * @param data the data to write
     * @param file the file to write to
     */
    public static void writeFile(String data, File file) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdir();
            if (!file.exists()) file.createNewFile();

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            wr.write(data);
            wr.flush();
            wr.close();
        } catch (Exception e) {
            ExceptionHandler.handleException("Writing file", e);
        }
    }

}
