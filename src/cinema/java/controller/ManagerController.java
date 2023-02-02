package controller;

import exception.InvalidChooseException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import model.Person;
import model.Session;
import model.Ticket;
import role.Role;
import service.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class ManagerController {
    private final PersonService<Person> personService = new PersonServiceImpl();
    private final FilmService<Film> filmService = new FilmServiceImpl();
    private final SessionService<Session> sessionService = new SessionServiceImpl();
    private final TicketService<Ticket> ticketService = new TicketServiceImpl();
    private String personLoginForSeance;

    public static void run() {
        ManagerController managerController = new ManagerController();
        managerController.getStartManagerMenu();
    }

    private void getStartManagerMenu() {
        log.info("Start manager menu");
        setPersonLoginForSeance();

        info(SubTextForToolBar.START_MANAGER_MENU.getText());
        String adminChoose = getScanner();
        checkValidChoose(adminChoose, "^[1-9][0-7]?$");
        int choose = Integer.parseInt(adminChoose);

        checkStartChoose(choose);
        switch (choose) {
            case 1 -> {
                personService.giveAccessForRepository().forEach(System.out::println);
                returnOrExit();
            }
            case 2 -> {
                filmService.giveAccessToFilmRepository().forEach(System.out::println);
                returnOrExit();
            }
            case 3 -> upDateNameFilm();
            case 4 -> upDateDateStartFilm();
            case 5 -> upDateDateEndFilm();
            case 6 -> {
                sessionService.giveAccessToSessionRepository().forEach(System.out::println);
                returnOrExit();
            }
            case 7 -> createNewSession();
            case 8 -> upDateSession();
            case 9 -> upDateSessionDay();
            case 10 -> deleteSession();
            case 11 -> {
                ticketService.giveAccessForCreateTicketsRepository().forEach(System.out::println);
                returnOrExit();
            }
            case 12 -> bookTicket();
            case 13 -> sitTicket();
            case 14 -> {
                ticketService.giveAccessForCheckPersonTicket(personLoginForSeance,"Manager");
                returnOrExit();
            }
            case 15 -> upDateTicket();
            case 16 -> upDateNotEmptyTicket();
            case 17 -> exit();
        }
    }

    private void upDateNameFilm() {
        log.info("Choose update name film");
        info("Write new name film");
        String newName = getScanner();

        info("Write name film from base");
        String name = getScanner();

        filmService.giveAccessForUpdateNameFilm(newName, name, "Manager");
        ticketService.giveAccessForDeleteFromRepository(newName);

        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("Name is update");
        returnOrExit();
    }

    private void upDateDateStartFilm() {
        log.info("Choose update start date film");
        info("Write name film");
        String name = getScanner();

        info("Write new date start film");
        String dateStart = getScanner();

        filmService.giveAccessForUpdateStartFilm(dateStart, name, "Manager");
        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("Start date is update");
        returnOrExit();
    }

    private void upDateDateEndFilm() {
        log.info("Choose update end date film");
        info("Write name film");
        String name = getScanner();

        info("Write new date end film");
        String dateEnd = getScanner();

        filmService.giveAccessForUpdateEndFilm(dateEnd, name, "Manager");
        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("End date is update");
        returnOrExit();
    }

    private void createNewSession() {
        log.info("Choose create new session");
        info("Write name film");
        String name = getScanner();

        info("Write date and time session");
        String date = getScanner();

        info("Write cost on session");
        String cost = getScanner();

        checkValidChoose(cost, "^[1-9][0-5]?$");
        int i = Integer.parseInt(cost);

        Session session = sessionService.createSession(name, date, i, "Manager");
        sessionService.addSessionToRepository(session);
        ticketService.giveAccessForCreateFullPackTicketAndAddToDb(name, session.getIdSession(), i,"Manager");

        log.info("Session is create and add, 25 tickets add to base");
        returnOrExit();
    }

    private void upDateSession() {
        log.info("Choose update session");
        info("Write new name film");
        String name = getScanner();

        info("Write id session");
        String id = getScanner();

        checkValidChoose(id, "^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForUpDateSession(i, name, "Manager");
        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("Session is update");
        returnOrExit();
    }

    private void upDateSessionDay() {
        log.info("Choose update session day");
        info("Write id session");
        String id = getScanner();

        info("Write new day and time session");
        String day = getScanner();

        checkValidChoose(id, "^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForUpDateSessionDay(i, day, "Manager");
        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("Day is update");
        returnOrExit();
    }

    private void deleteSession() {
        log.info("Choose delete session");
        info("Write id session");
        String id = getScanner();

        checkValidChoose(id, "^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForDeleteSessionFromDb(i, "Manager");
        info(SubTextForToolBar.VALID_DATA.getText());
        log.info("Session is delete");
        returnOrExit();
    }

    private void bookTicket() {
        log.info("Choose book ticket");
        info("You should write session index");
        String sessionChoose = getScanner();
        checkValidChoose(sessionChoose, "^[1-9][0-9]?$");
        int choose = Integer.parseInt(sessionChoose);

        boolean serverAnswer = sessionService.checkValidSession(choose, "Admin");
        if (!serverAnswer) {
            getStartManagerMenu();
        }

        ticketService.giveAccessForReadTicketsRepository(choose);
        info("1: Write number of seat" + getLineSeparator() +
                "2: Return" + getLineSeparator() +
                "3: Exit");

        String bayChoose = getScanner();

        checkValidChoose(bayChoose, "^[1-3]{1,1}$");

        int chooseBay = Integer.parseInt(bayChoose);

        switch (chooseBay) {
            case 1 -> buyTicket(choose);
            case 2 -> getStartManagerMenu();
            case 3 -> exit();
        }
    }

    private void buyTicket(int idSession) {
        info("Write number of seat");
        String numberOfSeat = getScanner();
        checkValidChoose(numberOfSeat, "^[1-9][0-9]?$");
        int seat = Integer.parseInt(numberOfSeat);

        boolean serverAnswer = ticketService.checkValidNumberOfSeat(idSession, seat,"Manager");
        if (!serverAnswer) {
            getStartManagerMenu();
        } else {
            log.info("Ticket is book");
            ticketService.giveAccessForBuyTicket(seat, idSession, personLoginForSeance);
            log.info("Ticket quantity is decrease");
            sessionService.giveAccessForDecrement(idSession);
        }

        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void sitTicket() {
        log.info("Choose sit ticket");
        List<Ticket> tickets = ticketService.giveAccessForWatchBookTicKet(personLoginForSeance);

        if (tickets.isEmpty()) {
            log.error("Non ticket");
            getExceptionChoose("You don't have any ticket");
        } else show(tickets);

        info("You should write id ticket witch you ought to sit");
        String idTicket = getScanner();
        checkValidChoose(idTicket, "^[1-9][0-9]?[0-9]?$");
        int id = Integer.parseInt(idTicket);

        boolean b = isMatch(tickets.stream().map(Ticket::getIdTicket), id);
        if (!b) getExceptionChoose("Invalid ticket Id");

        getSit(tickets, id);
        log.info("Ticket quantity is increase");
        getIncrement(tickets, id);
        returnOrExit();
    }

    private void upDateTicket() {
        log.info("Choose set person on ticket");
        info("Write id empty ticket");
        String id=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int tId = Integer.parseInt(id);

        info("Write id person witch you want set for empty ticket");
        String idP=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int personId = Integer.parseInt(idP);

        boolean anyMatch = isAnyMatch(tId);
        boolean match = isMatch(personService.giveAccessForRepository().stream()
                .map(Person::getId), personId);

        if (anyMatch&&match) getUp(tId, personId);
        else getExceptionChoose("Invalid data");

        sessionService.giveAccessForDecrement(ticketService.giveAccessForGetTicket(tId).getIdSession());
        log.info("Ticket quantity is decrease");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateNotEmptyTicket() {
        log.info("Choose delete person on ticket");
        info("Write id not empty ticket");
        String id=getScanner();

        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int tId = Integer.parseInt(id);

        boolean anyMatch = isAnyMatch(tId);
        if (!anyMatch) getExceptionChoose("Invalid data");

        sessionService.giveAccessForIncrement(ticketService.giveAccessForGetTicket(tId).getIdSession());
        log.info("Ticket quantity is increase");
        ticketService.giveAccessForSitTicket(tId);
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void getSit(List<Ticket> tickets, int id) {
        tickets.stream().map(Ticket::getIdTicket)
                .filter(e -> e.equals(id))
                .forEach(ticketService::giveAccessForSitTicket);
    }

    private boolean isMatch(Stream<Integer> personService, int personId) {
        return personService
                .anyMatch(e -> e.equals(personId));
    }

    private void show(List<Ticket> tickets) {
        tickets.forEach(e -> System.out.println("{Id ticket: "
                + e.getIdTicket() + ", name film: " + e.getNameFilm() + ", date session: "
                + sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getDateTimeSession()));
    }

    private boolean isAnyMatch(int tId) {
        return ticketService.giveAccessForCreateTicketsRepository().stream()
                .map(Ticket::getIdTicket)
                .anyMatch(e -> e.equals(tId));
    }

    private void getUp(int tId, int personId) {
        Optional<String> status = Optional.ofNullable(ticketService.giveAccessForGetTicket(tId).getStatus());
        if (status.isPresent()){
            getExceptionChoose("Invalid id ticket");
        }else {
            Ticket ticket=ticketService.giveAccessForGetTicket(tId);
            ticketService.giveAccessForUpdateTicketFromDb(personId,"Sold",ticket);
        }
    }

    private void getIncrement(List<Ticket> tickets, int id) {
        tickets.stream().map(Ticket::getIdTicket)
                .filter(e->e.equals(id))
                .map(e->ticketService.giveAccessForGetTicket(e).getIdSession())
                .forEach(sessionService::giveAccessForIncrement);
    }

    private void getExceptionChoose(String x) {
        log.error("Invalid choose");
        try {
            throw new InvalidChooseException(x + getLineSeparator() +
                    "Try again");
        } catch (InvalidChooseException e) {
            System.err.println(e.getMessage());
        }finally {
            getStartManagerMenu();
        }
    }

    private void info(String info){
        System.out.println(info);
    }

    private void returnOrExit() {
        info(SubTextForToolBar.RETURN_OR_EXIT.getText());
        String choose = getScanner();
        checkValidChoose(choose, "^[1-2]{1,1}$");
        int parsChoose = Integer.parseInt(choose);

        switch (parsChoose) {
            case 1 -> getStartManagerMenu();
            case 2 -> exit();
        }
    }

    private String getScanner(){
            return new Scanner(System.in).nextLine();
        }

    private String getLineSeparator(){
            return System.lineSeparator();
        }

    private void exit(){
            System.exit(0);
        }

    private void checkValidChoose(String choose,String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(choose);

        if (!matcher.find()) {
            log.error("Invalid choose");
            getExceptionChoose("Invalid choose");
        }
    }

    private void setPersonLoginForSeance() {
        this.personLoginForSeance = personService.giveAccessForGetAdminOrManagerFromDB(Role.MANAGER.getRole()).getLogin();
    }

    private void checkStartChoose(int choose) {
        if(choose>17||choose<1) getExceptionChoose("Invalid choose");
    }
}
