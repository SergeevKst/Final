package service;

import java.util.List;

public interface TicketService<E> {

    void giveAccessForReadTicketsRepository(int idSession);

    List<E> giveAccessForCreateTicketsRepository();

    E createTicket(String nameFilm, int idSession, int cost, String status);

    void giveAccessForUpdateTicketFromDb(int idPerson, String status, E e);

    void giveAccessForAddToRepository(E e);

    void giveAccessForDeleteFromRepository(int index, String status);

    void giveAccessForDeleteFromRepository(String nameFilm);

    boolean checkValidNumberOfSeat(int idSession, int numberOfSeat, String status);

    void giveAccessForBuyTicket(int seat, int idSession, String login);

    List<E> giveAccessForWatchBookTicKet(String personLoginForSeance);

    void giveAccessForCheckPersonTicket(String personLoginForSeance, String status);

    void giveAccessForCreateFullPackTicketAndAddToDb(String nameFilm, int idSession, int cost, String status);

    void giveAccessForSitTicket(int ticketId);

    E giveAccessForGetTicket(int id);
}
