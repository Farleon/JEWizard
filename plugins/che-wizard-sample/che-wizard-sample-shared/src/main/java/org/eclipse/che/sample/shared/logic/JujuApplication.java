package org.eclipse.che.sample.shared.logic;

public class JujuApplication {

  private String name;
  private String password;
  private String user;
  private String ip;
  private String port;

  public JujuApplication(String name, String password, String user, String ip, String port) {
    this.name = name;
    this.password = password;
    this.user = user;
    this.ip = ip;
    this.port = port;
  }

  public JujuApplication(String fromString) {
    String[] val = fromString.split(":::");

    this.name = val[0];
    this.password = val[1];
    this.user = val[2];
    this.ip = val[3];
    this.port = val[4];
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

  public String toString() {
    return this.name
        + ":::"
        + this.password
        + ":::"
        + this.user
        + ":::"
        + this.ip
        + ":::"
        + this.port;
  }
}
