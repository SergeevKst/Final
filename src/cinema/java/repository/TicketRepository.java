package repository;

import java.util.List;
import java.util.Map;

public interface TicketRepository<E> {

    Map<Integer, E> createTicketRepository(int idSession);

    List<E> createTicketRepository();

    void addTicketToDb(E e);

    void deleteTicketFromDb(int index);

    void deleteTicketFromDb(String nameFilm);

    void updateTicketFromDb(int idPerson, String status, E e);

    void sitTicket(int ticketId);

    List<E> WatchBookTicKet(int idPerson);

    E getTicketFromDb(int id);
}
