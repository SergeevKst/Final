package repository;

import model.Person;
import utils.ConnectionManagerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository<Person> {

    @Override
    public List<Person> createPersonRepository() {

        List<Person> repositoryPersonToLists = new ArrayList<>();

        try (Statement statement = ConnectionManagerUtil.openConnectionWithDB().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from person");

            while (resultSet.next()) {

                Person person = new Person();

                person.setId(resultSet.getInt("id_person"));
                person.setLogin(resultSet.getString("login"));
                person.setPassword(resultSet.getString("password"));
                person.setPersonRole(resultSet.getString("role"));

                repositoryPersonToLists.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return repositoryPersonToLists;

    }

    @Override
    public void upDatePerson(int index, String login) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update person set login=? where id_person=?");
            statement.setString(1, login);
            statement.setInt(2, index);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person getPersonFromDB(int index) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from person where id_person=?");
            statement.setInt(1, index);
            ResultSet resultSet = statement.executeQuery();
            Person person = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("id_person");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                person = new Person(id, login, password, role);
            }
            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person getPersonFromDB(String log) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from person where login=?");
            statement.setString(1, log);
            ResultSet resultSet = statement.executeQuery();
            Person person = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("id_person");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                person = new Person(id, login, password, role);
            }
            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person getAdminOrManagerFromDB(String personRole) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from person where role=?");
            statement.setString(1, personRole);
            ResultSet resultSet = statement.executeQuery();
            Person person = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("id_person");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                person = new Person(id, login, password, role);
            }
            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upDatePerson(String login, String role) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update person set role=? where login=?");
            statement.setString(1, role);
            statement.setString(2, login);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addPersonRepository(Person person) {

        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO person (login, password, role) VALUES (?,?,?);");
            statement.setString(1, person.getLogin());
            statement.setString(2, person.getPassword());
            statement.setString(3, person.getPersonRole());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePersonFromRepository(Person person) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from person where login=?");

            statement.setString(1, person.getLogin());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePersonFromRepository(int index) {

        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from person where id_person=?");
            statement.setInt(1, index);

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
