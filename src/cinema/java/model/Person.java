package model;

import lombok.Data;
import role.Role;

@Data
public class Person {

    private int id;

    private String login;
    private String password;
    private String personRole;

    public Person(int id, String login, String password, String personRole) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.personRole = personRole;
    }

    public Person(String login, String password) {
        this.login = login;
        this.password = password;
        this.personRole= Role.USER.getRole();
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + "*****" + '\'' +
                ", person Role='" + personRole + '\'' +
                '}';
    }
}
