package org.eclipse.che.sample.commander;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.Map;

public class HttpCommander {
    
    public static String readFile(String link){
           try {
      URL url = new URL(link);
      HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
      Map<String, List<String>> headers = httpConnection.getHeaderFields();

      // If URL is getting 301 and 302 redirection HTTP code then get new URL link.
      // This below for loop is totally optional if you are sure that your URL is not getting
      // redirected to anywhere
      for (String header : headers.get(null)) {
        if (header.contains(" 302 ") || header.contains(" 301 ")) {
          link = headers.get("Location").get(0);
          url = new URL(link);
          httpConnection = (HttpURLConnection) url.openConnection();
          headers = httpConnection.getHeaderFields();
        }
      }
      InputStream stream = httpConnection.getInputStream();
      String content = FileCommander.readFromInputStream(stream);
      return content;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
    }
}
