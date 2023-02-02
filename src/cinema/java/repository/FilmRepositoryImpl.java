package repository;

import model.Film;
import utils.ConnectionManagerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmRepositoryImpl implements FilmRepository<Film> {
    @Override
    public void addFilmToDb(Film film) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement
                            ("INSERT INTO film (name_film, date_start, date_end) VALUES (?,?,?);");
            statement.setString(1, film.getNameFilm());
            statement.setString(2, film.getDateStart());
            statement.setString(3, film.getDateEnd());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Film> createFilmRepository() {
        List<Film> filmsRepository = new ArrayList<>();

        try (Statement statement = ConnectionManagerUtil.openConnectionWithDB().createStatement()) {

            ResultSet resultSet = statement.executeQuery("select * from film");

            while (resultSet.next()) {

                Film film = new Film();

                film.setDateStart(resultSet.getString("date_start"));
                film.setDateEnd(resultSet.getString("date_end"));
                film.setNameFilm(resultSet.getString("name_film"));

                filmsRepository.add(film);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return filmsRepository;
    }

    @Override
    public void deleteFilmByName(String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {

            PreparedStatement statement =
                    connection.prepareStatement("delete from film where name_film=?");

            statement.setString(1, name);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStartFilm(String start, String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update film set date_start=? where name_film=?");
            statement.setString(1, start);
            statement.setString(2, name);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEndFilm(String end, String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update film set date_end=? where name_film=?");
            statement.setString(1, end);
            statement.setString(2, name);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateNameFilm(String newName, String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("update film set name_film=? where name_film=?");
            statement.setString(1, newName);
            statement.setString(2, name);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Film getFilmFromDb(String name) {
        try (Connection connection = ConnectionManagerUtil.openConnectionWithDB()) {
            PreparedStatement statement =
                    connection.prepareStatement("select * from film where name_film=?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            Film film = new Film();
            while (resultSet.next()) {
                film.setDateStart(resultSet.getString("date_start"));
                film.setDateEnd(resultSet.getString("date_end"));
                film.setNameFilm(resultSet.getString("name_film"));

            }
            return film;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
