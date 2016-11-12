package com.globe3.tno.g3_mobile.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_SERVER_EXT;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_SERVER_INT;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_ADDR;
import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_WEBSERVICE_PATH;
import static com.globe3.tno.g3_mobile.constants.App.HTTP_CONNECT_TIMEOUT;
import static com.globe3.tno.g3_mobile.constants.App.HTTP_READ_TIMEOUT;

public class HttpUtility {
    public static JSONObject requestJSON(String web_func, String param){
        try{
            return testConnection() ? new JSONObject(requestString(GLOBE3_WEBSERVICE_ADDR + web_func, param, HTTP_READ_TIMEOUT, HTTP_CONNECT_TIMEOUT, true)) : null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String requestString(String address, String param, int read_timeout, int conn_timeout, boolean logError){
        try{
            URL url = new URL(address);

            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setReadTimeout(read_timeout);
            httpConnection.setConnectTimeout(conn_timeout);
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);

            OutputStream outStream = httpConnection.getOutputStream();
            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            bufferWriter.write(param);
            bufferWriter.flush();
            bufferWriter.close();
            outStream.close();

            httpConnection.connect();

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader( httpConnection.getInputStream(),"utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String resultLine = null;
            while ((resultLine = bufferReader.readLine()) != null) {
                stringBuilder.append(resultLine + "\n");
            }
            bufferReader.close();

            return stringBuilder.toString();
        }catch (Exception e){
            if(logError){
                e.printStackTrace();
            }
            return "";
        }
    }

    public static boolean testConnection(){
        boolean intServer = requestString(GLOBE3_SERVER_INT + GLOBE3_WEBSERVICE_PATH + "ping", "", 750, 750, false)!="";
        boolean extServer = requestString(GLOBE3_SERVER_EXT + GLOBE3_WEBSERVICE_PATH + "ping", "", 750, 750, false)!="";

        GLOBE3_WEBSERVICE_ADDR = (extServer?GLOBE3_SERVER_EXT:GLOBE3_SERVER_INT) + GLOBE3_WEBSERVICE_PATH;

        return (intServer || extServer);
    }
}
