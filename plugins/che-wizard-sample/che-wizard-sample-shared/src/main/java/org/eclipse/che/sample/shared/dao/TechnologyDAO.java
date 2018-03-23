package org.eclipse.che.sample.shared.dao;

import java.util.HashMap;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public final class TechnologyDAO {

  private static TechnologyDAO instance = new TechnologyDAO();
  private HashMap<String, Technology> technologies;
  private String test;

  protected TechnologyDAO() {
    technologies = new HashMap<>();
    createSupported();
  }

  private void createSupported() {
    // ArangoDB
    Technology arangodb = new Technology("ArangoDB");
    ProjectType arangodb_java = new ProjectType("Java Project", "/files/arangodb_java");
    ProjectType arangodb_foxx = new ProjectType("Foxx Microservice", "/files/arangodb_foxx");
    arangodb.addProjectType(arangodb_foxx);
    arangodb.addProjectType(arangodb_java);
    technologies.put("ArangoDB", arangodb);

    // Python Flask
    Technology flask = new Technology("Python Flask");
    ProjectType flask_webapp = new ProjectType("Web Application", "/files/flask_webapp");
    flask.addProjectType(flask_webapp);
    technologies.put("Python Flask", flask);
  }

  public void refillDao(HashMap<String, Technology> map) {
    this.technologies = map;
  }

  public static TechnologyDAO getInstance() {
    return instance;
  }

  public HashMap<String, Technology> getTechnologies() {
    return new HashMap<>(technologies);
  }

  public void addTechnology(String name, Technology t) {
    technologies.put(name, t);
  }

  public String test() {
    return test;
  }

  public void setTest(String test2) {
    test = test2;
  }
}
