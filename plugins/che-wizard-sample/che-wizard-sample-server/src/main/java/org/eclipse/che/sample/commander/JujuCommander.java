package org.eclipse.che.sample.commander;

import java.util.ArrayList;
import org.eclipse.che.api.core.util.CommandLine;
import org.eclipse.che.api.core.util.ShellFactory;
import org.eclipse.che.sample.shared.logic.JujuApplication;

public class JujuCommander {

  private static ArrayList<JujuApplication> apps;

  public static ArrayList<JujuApplication> getJujuApplications() {
    apps = new ArrayList<>();
    CommandLine cmd = new CommandLine().add("juju", "status");
    final String[] line = new ShellFactory.StandardLinuxShell().createShellCommand(cmd);
    try {
      Process pr = Runtime.getRuntime().exec(line);
      String result = UrlCommander.readFromInputStream(pr.getInputStream());
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
          JujuApplication jujuApp = new JujuApplication(app, password, username, ip, port);
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
