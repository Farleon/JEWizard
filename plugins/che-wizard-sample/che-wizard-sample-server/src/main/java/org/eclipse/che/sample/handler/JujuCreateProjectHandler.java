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
import java.util.Arrays;
import java.util.Map;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.eclipse.che.api.project.server.ProjectManager;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.sample.commander.JujuCommander;
import org.eclipse.che.sample.commander.UrlCommander;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;
import org.eclipse.che.sample.shared.functions.JujuFunctions;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public class JujuCreateProjectHandler extends MutableProjectConfig implements CreateProjectHandler {

  @Inject private FsManager fsManager;
  private final ProjectManager projectManager;
  protected static final DtoFactory dtoFactory = DtoFactory.getInstance();
  private TechnologyDAO dao;
  private String deployGoal;
  private Technology technology;
  private ProjectType projectType;

  @Inject
  public JujuCreateProjectHandler(ProjectManager pm) {
    this.projectManager = pm;
    dao = TechnologyDAO.getInstance();
    dao.refillDao(JujuFunctions.createJujuSupportMap(UrlCommander.readFile(MASTER_CONFIG_URL)));

    for (Technology t : dao.getTechnologies().values()) {
      for (ProjectType p : t.getProjectTypes().values()) {
        System.out.println(t.getName() + p.getName());
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
        InputStream myFile = UrlCommander.readFilestream(p.getFiles().get(file));
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

    /*    // adding commands to the project attributes
    List<String> attrValue = new ArrayList<>(attributes.size());
    // Create test command
    HashMap<String, String> commandAttributes = new HashMap<>();
    commandAttributes.put("goal", "Run");
    commandAttributes.put("previewUrl", "");
    CommandImpl command = new CommandImpl("test123", "echo hello", "custom", commandAttributes);

    // Convert command
    CommandDto commandDto =
        dtoFactory
            .createDto(CommandDto.class)
            .withName(command.getName())
            .withType(command.getType())
            .withCommandLine(command.getCommandLine())
            .withAttributes(command.getAttributes());

    // add command to attrvalue
    attrValue.add(dtoFactory.toJson(commandDto));

    // Save attributes in new attributesList, the attributes in parameter is of type Map<String,
    // AttributeValue> and is empty anyway
    Map<String, List<String>> attributesList = new HashMap<>();
    attributesList.put("commands", attrValue);

    // Get the new project
    RegisteredProject project = (projectManager.get(projectPath)).get();

    try {
      System.out.println("Read project: " + project);

      // save new project configuration
      NewProjectConfigImpl projectConfig =
          new NewProjectConfigImpl(
              project.getPath(),
              project.getType(),
              project.getMixins(),
              project.getName(),
              project.getDescription(),
              attributesList,
              null,
              project.getSource());

      System.out.println("Projectconfig name: " + projectConfig.getName());
      System.out.println("Projectconfig attributes: " + projectConfig.getAttributes());

      // update project
      projectManager.update(projectConfig);

      for (RegisteredProject rpp : projectManager.getAll()) {
        System.out.println(rpp.getName());
        System.out.println(rpp.getAttributes());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }*/
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
