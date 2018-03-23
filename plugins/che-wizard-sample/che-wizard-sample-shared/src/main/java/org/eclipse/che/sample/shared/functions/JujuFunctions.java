package org.eclipse.che.sample.shared.functions;

import java.util.HashMap;
import org.eclipse.che.sample.shared.logic.ProjectType;
import org.eclipse.che.sample.shared.logic.Technology;

public class JujuFunctions {

  public static HashMap<String, Technology> createJujuSupportMap(String str) {
    try {
      HashMap<String, Technology> supportMap = new HashMap<>();
      String[] lines = str.split("\\r?\\n");
      for (String line : lines) {
        String split[] = line.split(":::");
        String tech = split[0];
        String projtype = split[1];
        String config = split[2];
        Technology t = new Technology(tech);
        if (supportMap.containsKey(tech)) {
          t = supportMap.get(tech);
        }
        ProjectType p = new ProjectType(projtype, config);
        t.addProjectType(p);
        supportMap.put(tech, t);
      }
      return supportMap;
    } catch (Exception e) {
      System.err.println("Could not create juju supported items map");
    }
    return null;
  }

  public static ProjectType extendProjectType(ProjectType p, String config) {
    try {
      if (config != null && config != "") {
        String[] lines = config.split("\\r?\\n");
        int i = 0;
        while (i < lines.length && !lines[i].startsWith("START_OF_FOLDERS") && i < lines.length)
          i++;
        i++;
        while (i < lines.length && !lines[i].startsWith("END_OF_FOLDERS") && i < lines.length) {
          p.addFolder(lines[i++]);
        }
        while (i < lines.length && !lines[i].startsWith("START_OF_FILES")) i++;
        i++;
        while (i < lines.length && !lines[i].startsWith("END_OF_FILES") && i < lines.length) {
          String[] parts = lines[i++].split(":::");
          p.addFile(parts[0], parts[1]);
        }
        while (i < lines.length && !lines[i].startsWith("START_OF_DEPLOYGOALS") && i < lines.length)
          i++;
        i++;
        while (i < lines.length && !lines[i].startsWith("END_OF_DEPLOYGOALS")) {
          String[] parts = lines[i++].split(":::");
          p.addDeploygoal(parts[0], parts[1]);
        }
        while (i < lines.length
            && !lines[i].startsWith("START_OF_CONFIG_FILES")
            && i < lines.length) i++;
        i++;
        while (i < lines.length
            && !lines[i].startsWith("END_OF_CONFIG_FILES")
            && i < lines.length) {
          String[] parts = lines[i++].split(":::");
          p.addConfigFile(parts[0], parts[1]);
        }
      }
    } catch (Exception e) {
      System.err.println("Could not extend project type with config: " + config);
    }

    return p;
  }
}
