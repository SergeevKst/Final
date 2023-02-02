package service;

import controller.AdministratorController;
import controller.ManagerController;
import controller.PersonController;
import exception.InvalidTicketException;
import model.Film;
import model.Person;
import model.Session;
import model.Ticket;
import repository.TicketRepository;
import repository.TicketRepositoryImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TicketServiceImpl implements TicketService<Ticket> {
    private final TicketRepository<Ticket> ticketRepository = new TicketRepositoryImpl();
    private final PersonService<Person> personService = new PersonServiceImpl();
    private final SessionService<Session> sessionService = new SessionServiceImpl();
    private final FilmService<Film> filmService = new FilmServiceImpl();

    @Override
    public Ticket createTicket(String nameFilm, int idSession, int cost,String status) {
        int size = ticketRepository.createTicketRepository(idSession).size();
        boolean filmContain = filmService.giveAccessToFilmRepository().stream()
                .map(Film::getNameFilm)
                .anyMatch(e -> e.equals(nameFilm));

        boolean idSessionContain = sessionService.giveAccessToSessionRepository().stream()
                .map(Session::getIdSession)
                .anyMatch(e -> e.equals(idSession));

        getValidCost(cost,status);
        if (size>=25 || !filmContain || !idSessionContain){
            getException("Invalid data" +getLineSeparator()+ "Try again",status);
        }

        return new Ticket(nameFilm, idSession, cost);
    }

    @Override
    public void giveAccessForReadTicketsRepository(int idSession) {
        Map<Integer, Ticket> ticketRepository1 = ticketRepository.createTicketRepository(idSession);
        System.out.println("\u001B[35m" + "     SCREEN" + "\u001B[0m");
        for (Map.Entry<Integer, Ticket> entry : ticketRepository1.entrySet()) {
            if (entry.getValue().getStatus() == null) {
                System.out.print("\u001B[32m" + getSpace(entry.getKey()) + "\u001B[0m" + " " + getEnter(entry.getKey()));
            } else {
                System.out.print("\u001B[31m" + getSpace(entry.getKey()) + "\u001B[0m" + " " + getEnter(entry.getKey()));
            }
        }
    }

    @Override
    public Ticket giveAccessForGetTicket(int id) {
        return ticketRepository.getTicketFromDb(id);
    }

    @Override
    public List<Ticket> giveAccessForWatchBookTicKet(String personLoginForSeance) {
        Person person = personService.giveAccessForGetPersonFromDB(personLoginForSeance);
        int id = person.getId();
        return ticketRepository.WatchBookTicKet(id);
    }

    @Override
    public void giveAccessForCheckPersonTicket(String personLoginForSeance,String status) {
        int id = personService.giveAccessForGetPersonFromDB(personLoginForSeance).getId();
        List<Ticket> tickets = ticketRepository.WatchBookTicKet(id);
        if (tickets.isEmpty()){
            getException("You have non ticket" +getLineSeparator()+ "Try again",status);
        }else {
        tickets.forEach(e-> System.out.println("{Id ticket: "
                +e.getIdTicket()+", name film: "+e.getNameFilm()+", date session: "
                +sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getDateTimeSession()+"}"));}
    }

    @Override
    public List<Ticket> giveAccessForCreateTicketsRepository() {
        return ticketRepository.createTicketRepository();
    }

    @Override
    public void giveAccessForAddToRepository(Ticket ticket) {
        ticketRepository.addTicketToDb(ticket);
    }

    @Override
    public void giveAccessForDeleteFromRepository(int index,String status) {
        boolean anyMatch = ticketRepository.createTicketRepository().stream().map(Ticket::getIdTicket).anyMatch(e -> e.equals(index));
        if (anyMatch){
            ticketRepository.deleteTicketFromDb(index);
        } else{
            getException("Invalid Id" +getLineSeparator()+ "Try again",status);
        }
    }

    @Override
    public void giveAccessForSitTicket(int ticketId) {
        ticketRepository.sitTicket(ticketId);
    }

    @Override
    public void giveAccessForBuyTicket(int seat, int idSession, String login) {
        String status = "Sold";
        int idPerson = personService.giveAccessForGetPersonFromDB(login).getId();

        Map<Integer, Ticket> ticketRepository1 = ticketRepository.createTicketRepository(idSession);
        Ticket ticket = ticketRepository1.get(seat);
        ticketRepository.updateTicketFromDb(idPerson, status, ticket);
    }

    @Override
    public void giveAccessForCreateFullPackTicketAndAddToDb(String nameFilm, int idSession, int cost,String status) {
        for (int i = 1; i <= 25; i++) {
            ticketRepository.addTicketToDb(createTicket(nameFilm, idSession, cost,status));
        }
    }

    @Override
    public void giveAccessForUpdateTicketFromDb(int idPerson, String status, Ticket ticket) {
        ticketRepository.updateTicketFromDb(idPerson,status,ticket);
    }

    @Override
    public void giveAccessForDeleteFromRepository(String nameFilm) {
       ticketRepository.deleteTicketFromDb(nameFilm);
    }

    @Override
    public boolean checkValidNumberOfSeat(int idSession, int numberOfSeat,String status) {
        Map<Integer, Ticket> ticketRepository1 = ticketRepository.createTicketRepository(idSession);
        Optional<Ticket> optionalTicket = Optional.ofNullable(ticketRepository1.get(numberOfSeat));
        Optional<String> statusNew = Optional.empty();
        if (optionalTicket.isPresent()) {
            statusNew = Optional.ofNullable(optionalTicket.get().getStatus());
        }
        boolean sold = statusNew.isPresent();
        if (sold) {
            getException("Ticket sold" +getLineSeparator()+ "Try again",status);
        } else if (numberOfSeat > 25 || numberOfSeat < 1) {
            getException("Invalid choose seat not found " +getLineSeparator()+ "Try again",status);
        }
        return true;
    }

    private String getEnter(int key) {
        if (key % 5 == 0) {
            return System.lineSeparator();
        }
        return "";
    }
    private String getSpace(int key) {
        if (key <10) {
            return " "+key;
        }
        return String.valueOf(key);
    }
    private void getException(String text,String status) {
        try {
            throw new InvalidTicketException(text);
        } catch (InvalidTicketException e) {
            System.err.println(e.getMessage());
        } finally {
            switch (status) {
                case "Admin" -> AdministratorController.run();
                case "Manager" -> ManagerController.run();
                case "User" -> {PersonController p = new PersonController();
                    p.getChooseAfterLog();}
            }
        }
    }
    private String getLineSeparator(){
        return System.lineSeparator();
    }
    private void getValidCost(int cost,String status){
        if(cost>15||cost<5){
            getException("Invalid cost" + getLineSeparator() + "Try again",status);
        }
    }
}
