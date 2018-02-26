package org.eclipse.che.sample.handler;

import static org.eclipse.che.api.fs.server.WsPathUtils.resolve;
import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;

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
//test commit
/**
 * Generates a new project which contains a package.json with default content and a default
 * person.json file within an myJsonFiles folder.
 */
public class JujuCreateProjectHandler implements CreateProjectHandler {

  @Inject private FsManager fsManager;
  private static final String FILE_NAME = "package.json";

  @Override
  public void onCreateProject(
      String projectPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ConflictException, ServerException {

    try (InputStream packageJson = getClass().getClassLoader().getResourceAsStream("files/test");
        InputStream personJson = getClass().getClassLoader().getResourceAsStream("files/test")) {
      String projectWsPath = WsPathUtils.absolutize(projectPath);
      fsManager.createFile(resolve(projectWsPath, FILE_NAME), packageJson);

      String myJsonFilesWsPath = resolve(projectWsPath, "myJsonFiles");
      fsManager.createFile(resolve(myJsonFilesWsPath, "person.json"), personJson);
    } catch (IOException | NotFoundException e) {
      throw new ServerException(e.getLocalizedMessage(), e);
    }
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }
}
