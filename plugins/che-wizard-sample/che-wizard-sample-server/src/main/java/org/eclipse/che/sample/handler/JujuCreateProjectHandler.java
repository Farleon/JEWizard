package org.eclipse.che.sample.handler;

import static org.eclipse.che.sample.shared.Constants.DEPLOYGOAL;
import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.MASTER_CONFIG_URL;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.sample.commander.UrlCommander;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;
import org.eclipse.che.sample.shared.functions.JujuFunctions;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public class JujuCreateProjectHandler extends MutableProjectConfig implements CreateProjectHandler {

  @Inject private FsManager fsManager;
  protected static final DtoFactory dtoFactory = DtoFactory.getInstance();
  private TechnologyDAO dao;
  private String deployGoal;
  private Technology technology;
  private ProjectType projectType;

  @Inject
  public JujuCreateProjectHandler() {
    dao = TechnologyDAO.getInstance();
    dao.refillDao(JujuFunctions.createJujuSupportMap(UrlCommander.readFile(MASTER_CONFIG_URL)));

    for (Technology t : dao.getTechnologies().values()) {
      for (ProjectType p : t.getProjectTypes().values()) {
        JujuFunctions.extendProjectType(p, UrlCommander.readFile(p.getFileLocation()));
      }
    }
  }

  @Override
  public void onCreateProject(
      String projectPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ConflictException, ServerException {
    deployGoal = attributes.get(DEPLOYGOAL).getString();
    // Get info from DAO
    technology = dao.getTechnologies().get(attributes.get(TECHNOLOGY).getString());
    projectType = technology.getProjectTypes().get(attributes.get(PROJECT_TYPE).getString());
    for (String k : attributes.keySet()) {
      System.err.println(k);
    }
    System.err.println("Workspaceid: " + attributes.get("workspaceid").getString());
    String rootFolder = WsPathUtils.absolutize(projectPath);
    // Create folders
    for (String folder : projectType.getFolders()) {
      try {
        fsManager.createDir(WsPathUtils.resolve(rootFolder, folder));
      } catch (Exception e) {
        System.err.println("Could not create folder: " + folder);
      }
    }

    // Create files
    for (String file : projectType.getFiles().keySet()) {
      try {
        InputStream myFile = UrlCommander.readFilestream(projectType.getFiles().get(file));
        fsManager.createFile(WsPathUtils.resolve(rootFolder, file), myFile);
      } catch (Exception e) {
        System.err.println("Could not create file:" + file);
      }
    }

    // Create configfiles
    for (String file : projectType.getConfigFiles().keySet()) {
      try {
        String configContent = UrlCommander.readFile(projectType.getConfigFiles().get(file));
        String configContentFilled = fillConfig(configContent);
        InputStream stream =
            new ByteArrayInputStream(configContentFilled.getBytes(StandardCharsets.UTF_8));
        fsManager.createFile(WsPathUtils.resolve(rootFolder, file), stream);
      } catch (Exception e) {
        System.err.println("Could not create file:" + file);
      }
    }

    // Create commands
    for (String deploygoalCommands : projectType.getDeployGoals().values()) {
      System.out.println();
      String command = UrlCommander.readFile(deploygoalCommands);
      String prefix =
          "curl -i -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '";
      String suffix =
          "' 'http://10.10.138.133:8080/api/workspace/"
              + attributes.get("workspaceid").getString()
              + "/command'";

      final String[] line = new String[3];
      line[0] = "/bin/bash";
      line[1] = "-cl";
      line[2] = prefix.trim() + command.trim() + suffix.trim();
      try {
        Process pr = Runtime.getRuntime().exec(line);
        String result = UrlCommander.readFromInputStream(pr.getInputStream());
        String result2 = UrlCommander.readFromInputStream(pr.getErrorStream());
        System.err.println(result);
        System.err.println(result2);
      } catch (Exception e) {

      }
    }

    System.err.println("ready");
  }

  private String fillConfig(String configContent) {
    System.err.println("Keyset" + projectType.getConfigVariables().keySet());
    System.err.println("Keyset size" + projectType.getConfigVariables().keySet().size());
    for (String var : projectType.getConfigVariables().keySet()) {
      try {
        System.err.println("::" + var + "::");
        if (projectType.getConfigVariables().get(var).equals("JUJU_AUTOFILL")) {
          //  JujuCommander.fillConfigVariables(deployGoal);
        }

        configContent =
            configContent.replaceAll("::" + var + "::", projectType.getConfigVariables().get(var));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.err.println(configContent);
    return configContent;
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }
}
