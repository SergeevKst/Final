package repository;

import java.util.List;

public interface SessionRepository<E> {

    List<E> createSessionRepository();

    void addSessionToDb(E e);

    E getSessionByIdSession(int idSession);

    void deleteSessionFromDb(int id);

    void upDateSession(int id, String nameFilm);

    void upDateSessionDay(int id, String day);

    void upDateSessionIncrement(int index);

    void upDateSessionDecrement(int index);
}
