package org.eclipse.che.sample.shared.logic;

import java.util.HashMap;

public class Technology {

  private String name;
  private HashMap<String, ProjectType> projectTypes;

  public Technology(String name) {
    this.name = name;
    projectTypes = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addProjectType(ProjectType p) {
    this.projectTypes.put(p.getName(), p);
  }

  public HashMap<String, ProjectType> getProjectTypes() {
    return new HashMap<>(projectTypes);
  }
}
