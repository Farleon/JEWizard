package org.eclipse.che.sample.handler;

import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.util.CommandLine;
import org.eclipse.che.api.core.util.ShellFactory;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public class JujuCreateProjectHandler extends MutableProjectConfig implements CreateProjectHandler {

  @Inject private FsManager fsManager;
  private TechnologyDAO dao;

  @Override
  public void onCreateProject(
      String projectPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ConflictException, ServerException {

    // Get info from DAO
    dao = TechnologyDAO.getInstance();
    Technology t = dao.getTechnologies().get(attributes.get(TECHNOLOGY).getString());
    ProjectType p = t.getProjectTypes().get(attributes.get(PROJECT_TYPE).getString());
    String location = p.getFileLocation();
    String configString = "";
    // Read extra info from config
    try (InputStream config =
        getClass().getClassLoader().getResourceAsStream(location + "/config")) {
      configString = readFromInputStream(config);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // read configfile + create folders and files
    try {
      String rootFolder = WsPathUtils.absolutize(projectPath);
      String[] lines = configString.split("\\r?\\n");

      String[] folders = lines[0].split(",");

      for (int i = 0; i < folders.length; i++) {
        System.err.println("Create folder: " + folders[i]);
        fsManager.createDir(WsPathUtils.resolve(rootFolder, folders[i]));
      }
      for (int i = 1; i < lines.length; i++) {
        String[] parts = lines[i].split(":");
        String file = parts[0];
        String fileLocation = "";
        if (parts.length == 2) fileLocation = parts[1];
        System.err.println(file + " will be saved in " + fileLocation);
        try (InputStream myfile =
            getClass()
                .getClassLoader()
                .getResourceAsStream(location + "/" + fileLocation + "/" + file)) {
          if (fileLocation != "") {
            fsManager.createFile(
                WsPathUtils.resolve(WsPathUtils.resolve(rootFolder, fileLocation), file), myfile);
          } else {
            fsManager.createFile(WsPathUtils.resolve(rootFolder, file), myfile);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    CommandLine cmd = new CommandLine().add("ls");
    final String[] line = new ShellFactory.StandardLinuxShell().createShellCommand(cmd);
    try {
      Process pr = Runtime.getRuntime().exec(line);
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      StringBuilder builder = new StringBuilder();
      String lin = null;
      while ((lin = in.readLine()) != null) {
        builder.append(lin + "\n");
      }
      String result = builder.toString();
      System.err.println(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }

  public String readFromInputStream(InputStream is) {
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
