package org.eclipse.che.sample.commander;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCommander {

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
