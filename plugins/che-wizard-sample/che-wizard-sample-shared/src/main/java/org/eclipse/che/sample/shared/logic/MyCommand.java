package org.eclipse.che.sample.shared.logic;

import java.util.HashMap;

public class MyCommand {

  private String name;
  private String type;
  private String commandLine;
  HashMap<String, String> commandAttributes;

  public MyCommand(String name, String type, String commandLine) {
    this.name = name;
    this.type = type;
    this.commandLine = commandLine;
    commandAttributes = new HashMap<>();
    commandAttributes.put("goal", type);
    commandAttributes.put("previewUrl", "");
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getCommandLine() {
    return commandLine;
  }
}
