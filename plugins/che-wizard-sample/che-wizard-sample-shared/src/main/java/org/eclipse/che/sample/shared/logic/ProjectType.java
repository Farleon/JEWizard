package org.eclipse.che.sample.shared.logic;

public class ProjectType {

  private String name;
  private String fileLocation;

  public ProjectType(String name, String fileLocation) {
    this.name = name;
    this.fileLocation = fileLocation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileLocation() {
    return fileLocation;
  }
}
