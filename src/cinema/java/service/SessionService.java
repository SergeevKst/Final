package service;

import java.util.List;

public interface SessionService<E> {
    E createSession(String nameFilm, String dateTimeSession, int cost, String status);

    List<E> giveAccessToSessionRepository();

    void addSessionToRepository(E e);

    boolean checkValidSession(int choose, String status);

    E giveAccessForGetSessionByIdSession(int id);

    void giveAccessForDecrement(int index);

    void giveAccessForIncrement(int index);

    void giveAccessForUpDateSession(int id, String name, String status);

    void giveAccessForUpDateSessionDay(int id, String day, String status);

    void giveAccessForDeleteSessionFromDb(int id, String status);
}
