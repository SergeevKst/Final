package service;

import java.util.List;

public interface FilmService<E> {

    E createFilm(String dateStart, String dateEnd, String nameFilm, String status);

    List<E> giveAccessToFilmRepository();

    void giveAccessForAddFilmToRepository(E e);

    void giveAccessForDeleteFilm(String name, String status);

    void giveAccessForUpdateStartFilm(String start, String name, String status);

    void giveAccessForUpdateEndFilm(String end, String name, String status);

    void giveAccessForUpdateNameFilm(String newName, String name, String status);

    E giveAccessForGetFilm(String name);
}
