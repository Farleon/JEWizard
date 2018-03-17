package org.eclipse.che.sample.commander;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.che.api.core.util.CommandLine;
import org.eclipse.che.api.core.util.ShellFactory;

public class JujuCommander {

  public static HashMap<String, ArrayList<String>> getJujuSupported() {
        String result = HttpCommander.readFile("https://raw.githubusercontent.com/Farleon/ChePluginConstants/master/deploygoals.config");
        return createJujuSupportMap(result);
  }

  private static HashMap<String, ArrayList<String>> createJujuSupportMap(String str) {

    HashMap<String, ArrayList<String>> supportMap = new HashMap<>();
    String[] lines = str.split("\\r?\\n");
    for (String line : lines) {
      List<String> supported;
      String split[] = line.split(":::");
      String goal = split[0];
      supported = Arrays.asList(split[1].split(","));
      supportMap.put(goal, new ArrayList<>(supported));
    }
    return supportMap;
  }

  public static ArrayList<JujuApplication> getJujuApplications() {
    ArrayList<JujuApplication> apps = new ArrayList<>();

    CommandLine cmd = new CommandLine().add("juju", "status");
    final String[] line = new ShellFactory.StandardLinuxShell().createShellCommand(cmd);
    try {
      Process pr = Runtime.getRuntime().exec(line);
      String result = FileCommander.readFromInputStream(pr.getInputStream());
      String[] lines = result.split("\\r?\\n");
      int i = 0;
      while (i < lines.length && !lines[i].startsWith("Unit")) {
        i++;
      }
      i++;
      while (i < lines.length - 1 && !lines[i].startsWith("Machine")) {
        lines[i] = lines[i].replaceAll("   *", "  ");
        String[] blocks = lines[i].split("  ");
        if (blocks.length > 5) {
          String app = blocks[0];
          String ip = blocks[4];
          String port = "";
          String msg = "";
          if (blocks.length < 7) {
            msg = blocks[5];
          } else {
            port = blocks[5].split("[^\\d.]")[0];
            msg = blocks[6];
          }

          String password = "";
          String username = "";
          if (app.contains("arangodb")) {
            username = "root";
            password = msg.replace("ArangoDB running with root password ", "");
          }
          JujuApplication jujuApp = new JujuApplication(app, password, username, ip, port, "");
          apps.add(jujuApp);
        }

        i++;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return apps;
  }
}
