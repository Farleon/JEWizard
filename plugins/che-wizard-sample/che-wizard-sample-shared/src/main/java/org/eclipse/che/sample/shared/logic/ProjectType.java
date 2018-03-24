package org.eclipse.che.sample.shared.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectType {

  private String name;
  private String fileLocation;
  private HashMap<String, String> deploygoals; // map deployname on link with commands
  private ArrayList<String> folders;
  private HashMap<String, String> files;
  private HashMap<String, String> configFiles;
  private HashMap<String, String> configVariables;

  public ProjectType(String name, String fileLocation) {
    this.name = name;
    this.fileLocation = fileLocation;
    this.deploygoals = new HashMap<String, String>();
    this.folders = new ArrayList<>();
    this.files = new HashMap<>();
    this.configFiles = new HashMap<>();
    this.configVariables = new HashMap<>();
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

  public ArrayList<String> getFolders() {
    return folders;
  }

  public HashMap<String, String> getFiles() {
    return files;
  }

  public HashMap<String, String> getConfigFiles() {
    return configFiles;
  }

  public HashMap<String, String> getDeployGoals() {
    return deploygoals;
  }

  public HashMap<String, String> getConfigVariables() {
    return configVariables;
  }

  public void addDeploygoal(String goal, String location) {
    deploygoals.put(goal, location);
  }

  public void addFile(String file, String location) {
    files.put(file, location);
  }

  public void addConfigFile(String file, String location) {
    configFiles.put(file, location);
  }

  public void addConfigVariable(String variable, String value) {
    configVariables.put(variable, value);
  }

  public void addFolder(String goal) {
    folders.add(goal);
  }
}
