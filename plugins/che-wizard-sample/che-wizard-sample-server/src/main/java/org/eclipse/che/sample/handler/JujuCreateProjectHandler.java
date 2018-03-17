package org.eclipse.che.sample.handler;

import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.eclipse.che.api.project.server.ProjectManager;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.impl.NewProjectConfigImpl;
import org.eclipse.che.api.project.server.impl.RegisteredProject;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.api.workspace.shared.dto.CommandDto;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.sample.commander.FileCommander;
import org.eclipse.che.sample.shared.dao.TechnologyDAO;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public class JujuCreateProjectHandler extends MutableProjectConfig implements CreateProjectHandler {

  @Inject private FsManager fsManager;
  private final ProjectManager projectManager;
  protected static final DtoFactory dtoFactory = DtoFactory.getInstance();
  private TechnologyDAO dao;

  @Inject
  public JujuCreateProjectHandler(ProjectManager pm) {
    this.projectManager = pm;
  }

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
      configString = FileCommander.readFromInputStream(config);
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

    // adding commands to the project attributes
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
    }
    System.err.println("ready");
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }
}
