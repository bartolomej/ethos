package net.client;

import config.Constants;
import config.SystemConfig;
import net.PeerNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;

public class HTTPClient {

    private static URL getRemoteUrl() {
        URL url = null;
        try {
            url = new URL(SystemConfig.REMOTE_SERVER);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getStaticPeers() {
        String peers = "";
        try {
            HttpURLConnection con = (HttpURLConnection) getRemoteUrl().openConnection();
            con.setRequestMethod("GET");;
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.connect();
            Reader streamReader = null;

            if (con.getResponseCode() > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            peers = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return peers;
    }

}
