package repository;

import model.Session;
import utils.ConnectionManagerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionRepositoryImpl implements SessionRepository<Session> {
    @Override
    public void addSessionToDb(Session session) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement
                            ("INSERT INTO movie_session (id_session, name_film, date_session, ticket_quantity) VALUES (?,?,?,?);");
            statement.setInt(1, session.getIdSession());
            statement.setString(2, session.getNameFilm());
            statement.setString(3, session.getDateTimeSession());
            statement.setInt(4, session.getTicketQuantity());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Session> createSessionRepository() {
        List<Session> sessionList = new ArrayList<>();

        try (Statement statement = ConnectionManagerUtil.openConnectionWithDB().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from movie_session");

            while (resultSet.next()) {

                Session session = new Session();

                session.setIdSession(resultSet.getInt("id_session"));
                session.setDateTimeSession(resultSet.getString("date_session"));
                session.setNameFilm(resultSet.getString("name_film"));
                session.setTicketQuantity(resultSet.getInt("ticket_quantity"));

                sessionList.add(session);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sessionList;
    }

    @Override
    public Session getSessionByIdSession(int id) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from movie_session where id_session=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Session session = new Session();
            while (resultSet.next()) {
                session.setIdSession(resultSet.getInt("id_session"));
                session.setDateTimeSession(resultSet.getString("date_session"));
                session.setTicketQuantity(resultSet.getInt("ticket_quantity"));
                session.setNameFilm(resultSet.getString("name_film"));
            }
            return session;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteSessionFromDb(int id) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from movie_session where id_session=?");
            statement.setInt(1, id);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upDateSession(int id, String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update movie_session set name_film=? where id_session=?");
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upDateSessionDay(int id, String day) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update movie_session set date_session=? where id_session=?");
            statement.setString(1, day);
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upDateSessionDecrement(int index) {
        int ticketQuantity = getSessionByIdSession(index).getTicketQuantity();
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update movie_session set ticket_quantity=? where id_session=?");
            statement.setInt(1, ticketQuantity - 1);
            statement.setInt(2, index);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upDateSessionIncrement(int index) {
        int ticketQuantity = getSessionByIdSession(index).getTicketQuantity();
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update movie_session set ticket_quantity=? where id_session=?");
            statement.setInt(1, ticketQuantity + 1);
            statement.setInt(2, index);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
