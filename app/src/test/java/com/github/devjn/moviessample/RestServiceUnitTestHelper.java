package com.github.devjn.moviessample;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by @author Jahongir on 01-May-17
 * devjn@jn-arts.com
 * RestServiceTestHelper
 */

class RestServiceUnitTestHelper {

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        final InputStream stream = RestServiceUnitTestHelper.class.getClassLoader().getResourceAsStream(filePath);
        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }

    public static <T> T load(String filePath, Class<T> classOf) throws Exception {
        final InputStream stream = RestServiceUnitTestHelper.class.getClassLoader().getResourceAsStream(filePath);
        String file = convertStreamToString(stream);
        stream.close();
        Gson gson = new Gson();
        return gson.fromJson(file, classOf);
    }

}