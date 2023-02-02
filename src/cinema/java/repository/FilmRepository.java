package repository;

import java.util.List;

public interface FilmRepository<E> {

    List<E> createFilmRepository();

    void addFilmToDb(E e);

    void deleteFilmByName(String name);

    void updateStartFilm(String start, String name);

    void updateEndFilm(String end, String name);

    void updateNameFilm(String newName, String name);

    E getFilmFromDb(String name);
}
