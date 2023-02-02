package role;

public enum Role {

    ADMINISTRATOR("\u001B[31m"+"Administrator"+"\u001B[0m"),
    MANAGER("\u001B[35m"+"Manager"+"\u001B[0m"),
    USER("\u001B[32m"+"User"+"\u001B[0m");

  private final String roleText;

  public String getRole(){
      return roleText;
  }

    Role(String roleText) {
        this.roleText = roleText;
    }
}
