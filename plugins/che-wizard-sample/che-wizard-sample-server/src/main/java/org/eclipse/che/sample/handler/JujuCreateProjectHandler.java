package org.eclipse.che.sample.handler;

import static org.eclipse.che.api.fs.server.WsPathUtils.resolve;
import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
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
  private static final String FILE_NAME = "package.json";
  TechnologyDAO dao;

  @Override
  public void onCreateProject(
      String projectPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ConflictException, ServerException {
    dao = TechnologyDAO.getInstance();
    for (String s : attributes.keySet()) {
      System.err.println(s + ": " + attributes.get(s).getString());
    }
    Technology t = dao.getTechnologies().get(attributes.get(TECHNOLOGY).getString());
    System.err.println(t.getName());
    ProjectType p = t.getProjectTypes().get(attributes.get(PROJECT_TYPE).getString());
    System.err.println(p.getName());
    String location = p.getFileLocation();
    System.err.println(location);
    try (InputStream packageJson = getClass().getClassLoader().getResourceAsStream(location);
        InputStream personJson = getClass().getClassLoader().getResourceAsStream("files/test")) {
      String projectWsPath = WsPathUtils.absolutize(projectPath);
      fsManager.createFile(resolve(projectWsPath, FILE_NAME), packageJson);

      String myJsonFilesWsPath = resolve(projectWsPath, "myJsonFiles");
      fsManager.createFile(resolve(myJsonFilesWsPath, "Main.java"), personJson);
    } catch (IOException | NotFoundException e) {
      throw new ServerException(e.getLocalizedMessage(), e);
    }
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }
}
