package org.eclipse.che.sample.commander;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class UrlCommander {

  public static String readFile(String link) {
    try {
      if (link.length() > 5) {
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
        String content = readFromInputStream(stream);
        return content;
      }

    } catch (Exception e) {
      System.err.println("Could not get content from link: " + link);
      e.printStackTrace();
    }
    return null;
  }

  public static InputStream readFilestream(String link) {
    try {
      if (link.length() > 5) {
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
        return stream;
      }

    } catch (Exception e) {
      System.err.println("Could not get content from link: " + link);
      e.printStackTrace();
    }
    return null;
  }

  public static String readFromInputStream(InputStream is) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    try {
      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      buffer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    byte[] byteArray = buffer.toByteArray();

    String text = new String(byteArray);
    return text;
  }
}
