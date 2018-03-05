package org.eclipse.che.sample.handler;

import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
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

    // Get info from DAO
    dao = TechnologyDAO.getInstance();
    Technology t = dao.getTechnologies().get(attributes.get(TECHNOLOGY).getString());
    ProjectType p = t.getProjectTypes().get(attributes.get(PROJECT_TYPE).getString());
    String location = p.getFileLocation();

    // Read extra info from config
    try (InputStream config =
        getClass().getClassLoader().getResourceAsStream(location + "/config.json")) {
      System.err.println(readFromInputStream(config));
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServerException(e.getLocalizedMessage(), e);
    }
  }

  @Override
  public String getProjectType() {
    return JUJU_PROJECT_TYPE_ID;
  }

  public String readFromInputStream(InputStream is) {
    System.err.println("try read");
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    try {
      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      buffer.flush();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    byte[] byteArray = buffer.toByteArray();

    String text = new String(byteArray);
    return text;
  }
}
