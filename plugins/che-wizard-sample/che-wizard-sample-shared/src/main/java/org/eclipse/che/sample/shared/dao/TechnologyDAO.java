package org.eclipse.che.sample.shared.dao;

import java.util.HashMap;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public final class TechnologyDAO {

  private static TechnologyDAO instance = new TechnologyDAO();
  private HashMap<String, Technology> technologies;

  protected TechnologyDAO() {
    technologies = new HashMap<>();
    createSupported();
  }

  private void createSupported() {
    Technology arangodb = new Technology("arangodb");
    ProjectType arangodb_java = new ProjectType("ArangoDb Java Project", "/files/arangodb_java");
    ProjectType arangodb_foxx =
        new ProjectType("ArangoDb Foxx Microservice", "/files/arangodb_foxx");
    arangodb.addProjectType(arangodb_foxx);
    arangodb.addProjectType(arangodb_java);
    technologies.put("arangodb", arangodb);
  }

  public static TechnologyDAO getInstance() {
    return instance;
  }

  public HashMap<String, Technology> getTechnologies() {
    return new HashMap<>(technologies);
  }
}
