package com.wffwebdemo.minimalproductionsample.server.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;

public class HeartBeatUtil {

    private static final Logger LOGGER = Logger
            .getLogger(HeartBeatUtil.class.getName());

    public static void ping(final String sessionId) {

        if (ServerConstants.CONTEXT_PATH == null) {
            return;
        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String url = ServerConstants.DOMAIN_URL
                            .concat(ServerConstants.CONTEXT_PATH)
                            .concat("/heart-beat");

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj
                            .openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
                    con.connect();

                    int responseCode = con.getResponseCode();

                    LOGGER.info("responseCode " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    LOGGER.info("heartbeat response " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

}
