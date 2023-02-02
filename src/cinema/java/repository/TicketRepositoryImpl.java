package repository;

import model.Ticket;
import utils.ConnectionManagerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketRepositoryImpl implements TicketRepository<Ticket> {
    @Override
    public List<Ticket> createTicketRepository() {
        List<Ticket> ticketsRepository = new ArrayList<>();

        try (Statement statement = ConnectionManagerUtil.openConnectionWithDB().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from ticket");

            while (resultSet.next()) {

                Ticket ticket = new Ticket();

                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setNameFilm(resultSet.getString("name_film"));
                ticket.setIdPerson(resultSet.getInt("id_person"));
                ticket.setIdSession(resultSet.getInt("id_session"));
                ticket.setCost(resultSet.getInt("cost"));
                ticket.setStatus(resultSet.getString("status"));

                ticketsRepository.add(ticket);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ticketsRepository;

    }

    @Override
    public Ticket getTicketFromDb(int id) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from ticket where id_ticket=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Ticket ticket = new Ticket();
            while (resultSet.next()) {
                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setNameFilm(resultSet.getString("name_film"));
                ticket.setIdPerson(resultSet.getInt("id_person"));
                ticket.setCost(resultSet.getInt("cost"));
                ticket.setIdSession(resultSet.getInt("id_session"));
                ticket.setStatus(resultSet.getString("status"));
            }
            return ticket;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, Ticket> createTicketRepository(int idSession) {
        Map<Integer, Ticket> ticketMap = new HashMap<>();
        int startKey = 0;

        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from ticket where id_session=?");
            statement.setInt(1, idSession);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Ticket ticket = new Ticket();

                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setNameFilm(resultSet.getString("name_film"));
                ticket.setIdPerson(resultSet.getInt("id_person"));
                ticket.setCost(resultSet.getInt("cost"));
                ticket.setIdSession(resultSet.getInt("id_session"));
                ticket.setStatus(resultSet.getString("status"));

                ticketMap.put(startKey = startKey + 1, ticket);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ticketMap;
    }

    @Override
    public void addTicketToDb(Ticket ticket) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "INSERT INTO ticket (name_film, id_session, cost) VALUES (?,?,?);");
            statement.setString(1, ticket.getNameFilm());
            statement.setInt(2, ticket.getIdSession());
            statement.setInt(3, ticket.getCost());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTicketFromDb(int index) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from ticket where id_ticket=?");
            statement.setInt(1, index);

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTicketFromDb(String nameFilm) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from ticket where name_film=?");
            statement.setString(1, nameFilm);

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> WatchBookTicKet(int idPerson) {
        List<Ticket> ticketsRepository = new ArrayList<>();
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from ticket where id_person=?");
            statement.setInt(1, idPerson);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Ticket ticket = new Ticket();

                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setNameFilm(resultSet.getString("name_film"));
                ticket.setIdPerson(resultSet.getInt("id_person"));
                ticket.setIdSession(resultSet.getInt("id_session"));
                ticket.setCost(resultSet.getInt("cost"));
                ticket.setStatus(resultSet.getString("status"));

                ticketsRepository.add(ticket);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ticketsRepository;
    }


    @Override
    public void updateTicketFromDb(int idPerson, String status, Ticket ticket) {
        int ticketId = ticket.getIdTicket();
        setIdPersonForTicket(idPerson, ticketId);
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update ticket set status=? where id_ticket=?");
            statement.setString(1, status);
            statement.setInt(2, ticketId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sitTicket(int ticketId) {

        setIdForSitTicket(ticketId);
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update ticket set status=null where id_ticket=?");

            statement.setInt(1, ticketId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setIdForSitTicket(int ticketId) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update ticket set id_person=null where id_ticket=?");
            statement.setInt(1, ticketId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setIdPersonForTicket(int idPerson, int ticketId) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update ticket set id_person=? where id_ticket=?");
            statement.setInt(1, idPerson);
            statement.setInt(2, ticketId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
