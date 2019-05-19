package net;

import config.Constants;
import config.SystemConfig;
import net.PeerNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;

public class HTTPClient {

    private static URL parseToUrl(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String sendMessage(URL url, PeerMessage message) {
        String peers = "";
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");;
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.connect();
            Reader streamReader = new InputStreamReader(con.getInputStream());
            BufferedReader in = new BufferedReader(streamReader);
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
