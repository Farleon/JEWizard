package org.eclipse.che.sample.commander;

public class JujuApplication {

  private String name;
  private String password;
  private String user;
  private String ip;
  private String port;
  private String supportedTechnologies;

  public JujuApplication(
      String name,
      String password,
      String user,
      String ip,
      String port,
      String supportedTechnologies) {
    this.name = name;
    this.password = password;
    this.user = user;
    this.ip = ip;
    this.port = port;
    this.supportedTechnologies = supportedTechnologies;
  }

  public String getPassword() {
    return password;
  }

  public String getUser() {
    return user;
  }

  public String getIp() {
    return ip;
  }

  public String getPort() {
    return port;
  }

  public String getName() {
    return name;
  }

  public String getSupportedTechnologies() {
    return supportedTechnologies;
  }

  public String getSupportString() {
    return "test";
  }
}
