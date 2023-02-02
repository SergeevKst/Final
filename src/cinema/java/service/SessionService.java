package service;

public interface SessionServer<E> {
    E createSession(String nameFilm, String dateTimeSession , int ticketQuantity);

    void readSessionRepository();

    void addSessionToRepository(E e);

    boolean checkValidSession(int choose);

}
