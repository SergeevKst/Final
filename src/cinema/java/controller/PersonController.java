package controller;

import exception.InvalidChooseException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import model.Person;
import model.Session;
import model.Ticket;
import role.Role;
import service.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class PersonController {
    private final PersonService<Person> personService = new PersonServiceImpl();
    private final SessionService<Session> sessionService = new SessionServiceImpl();
    private final FilmService<Film> filmService = new FilmServiceImpl();
    private final TicketService<Ticket> ticketService = new TicketServiceImpl();
    private String personLoginForSeance;

    public void checkValidLoginAndPassword() {
        log.info("Authorization start");
        info(SubTextForToolBar.WRITE_YOUR_LOGIN.getText());
        String login = getScanner();

        info(SubTextForToolBar.WRITE_YOUR_PASSWORD.getText());
        String password = getScanner();

        boolean personFromDB = personService.checkValidPersonFromDB(login, password);
        if (personFromDB) {
            setPersonLoginForSeance(login);
            transitionToAdminOrManagerController(login);
            log.info("Authorization end");
            getChooseAfterLog();
        }

    }

    public void createNewPersonAndAddToDB() {
        log.info("Registration start");
        info(SubTextForToolBar.CREATE_LOGIN.getText());
        String login = getScanner();
        boolean validNewLogin = personService.checkValidNewLogin(login,"User");

        info(SubTextForToolBar.CREATE_PASSWORD.getText());
        String password = getScanner();
        boolean validNewPassword = personService.checkValidNewPassword(password,"User");

        if (validNewLogin&&validNewPassword){
            info(SubTextForToolBar.VALID_DATA.getText());
            String newPassword= getHash(password);
            Person newPerson = personService.createNewPerson(login, newPassword);
            personService.giveAccessForAddPersonToDB(newPerson);
        }

        log.info("Registration end");
        setPersonLoginForSeance(login);
        getChooseAfterLog();
    }

    public void getChooseAfterLog() {
        info(SubTextForToolBar.CHOOSE_AFTER_VALID_LOGIN_PASSWORD.getText());
        String personChoose = getScanner();
        checkValidChoose(personChoose,"^[1-6]{1,1}$");
        int choose = Integer.parseInt(personChoose);

        switch (choose){
            case 1 -> {filmService.giveAccessToFilmRepository().forEach(System.out::println);
           returnOrExit(); }
            case 2 -> {
                sessionService.giveAccessToSessionRepository().forEach(System.out::println);
            returnOrExit();}
            case 3 -> getMajorChoose();
            case 4 -> sitTicket();
            case 5 -> showBookTicket();
            case 6 -> exit();
        }
    }

    private void getMajorChoose() {
        log.info("Choose major branch");

        info("You should write session index");
        String sessionChoose=getScanner();
        checkValidChoose(sessionChoose,"^[1-9][0-9]?$");
        int choose = Integer.parseInt(sessionChoose);

        boolean serverAnswer = sessionService.checkValidSession(choose,"User");
        if (!serverAnswer){
            getChooseAfterLog();
        }

        ticketService.giveAccessForReadTicketsRepository(choose);
        info("1: Write number of seat"+getLineSeparator()+
                "2: Return"+getLineSeparator()+
                "3: Exit");

        String bayChoose = getScanner();
        checkValidChoose(bayChoose,"^[1-3]{1,1}$");
        int chooseBay = Integer.parseInt(bayChoose);

        switch (chooseBay){
            case 1 -> buyTicket(choose);
            case 2 -> getChooseAfterLog();
            case 3 -> exit();
        }
    }

    private void sitTicket() {
        log.info("Choose sit ticket");

        List<Ticket> tickets = ticketService.giveAccessForWatchBookTicKet(personLoginForSeance);
        if (tickets.isEmpty()) {
            getExceptionChoose("You don't have any ticket");
        } else showBook(tickets);

        info("You should write id ticket witch you ought to sit");
        String idTicket=getScanner();
        checkValidChoose(idTicket,"^[1-9][0-9]?[0-9]?$");
        int id = Integer.parseInt(idTicket);

        boolean anyMatch = tickets.stream().map(Ticket::getIdTicket)
                .anyMatch(e -> e.equals(id));
        if (!anyMatch) getExceptionChoose("Invalid ticket Id");

        tickets.stream().map(Ticket::getIdTicket)
                .filter(e->e.equals(id))
                .forEach(ticketService::giveAccessForSitTicket);

        getIncrement(tickets, id);
        log.info("Increase ticket quantity");
        getChooseAfterLog();
    }

    private void showBookTicket() {
        log.info("Choose watch ticket");
        List<Ticket> tickets = ticketService.giveAccessForWatchBookTicKet(personLoginForSeance);

        if (tickets.isEmpty()){
            log.error("Non ticket");
            getExceptionChoose("You have non ticket");
        }else showBook(tickets);
    }

    private void buyTicket(int idSession) {
        info("Write number of seat");
        String numberOfSeat=getScanner();
        checkValidChoose(numberOfSeat,"^[1-9][0-9]?$");
        int seat = Integer.parseInt(numberOfSeat);

        boolean serverAnswer = ticketService.checkValidNumberOfSeat(idSession, seat,"User");

        if (!serverAnswer){
            log.error("Choose invalid seat");
            getMajorChoose();
        }else ticketService.giveAccessForBuyTicket(seat,idSession, personLoginForSeance);

        log.info("Ticket is sold");
        chooseAfterBookTicket(seat);
    }

    private void chooseAfterBookTicket(int seat) {
        info(SubTextForToolBar.CHOOSE_AFTER_BOOK_TICKET.getText());
        String choose=getScanner();
        checkValidChoose(choose,"^[1-3]{1,1}$");
        int endChoose=Integer.parseInt(choose);

        getDecrement();
        log.info("Decrease ticket quantity");
        log.info("Cycle end");

        switch (endChoose){
            case 1 -> {show(seat);
                returnOrExit();}
            case 2 -> getChooseAfterLog();
            case 3 -> exit();
        }
    }

    private void showBook(List<Ticket> tickets) {
        tickets.forEach(e-> System.out.println("{Id ticket: "
                +e.getIdTicket()+", name film: "+e.getNameFilm()+", date session: "
                +sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getDateTimeSession()+"}"));
        returnOrExit();
    }

    private void transitionToAdminOrManagerController(String login) {
        boolean administrator = personService.giveAccessForGetPersonFromDB(login).getPersonRole().equals(Role.ADMINISTRATOR.getRole());
        boolean manager = personService.giveAccessForGetPersonFromDB(login).getPersonRole().equals(Role.MANAGER.getRole());

        if (administrator){
            log.info("Admin transition");
            AdministratorController.run();
        }else if (manager){
            log.info("Manager transition");
            ManagerController.run();
        }
    }

    private void getIncrement(List<Ticket> tickets, int id) {
        tickets.stream().map(Ticket::getIdTicket)
                .filter(e->e.equals(id))
                .map(e->ticketService.giveAccessForGetTicket(e).getIdSession())
                .forEach(sessionService::giveAccessForIncrement);
    }

    private void getDecrement() {
        ticketService.giveAccessForWatchBookTicKet(personLoginForSeance).stream()
                .map(e->sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getIdSession())
                .forEach(sessionService::giveAccessForDecrement);
    }

    private void show(int seat) {
        ticketService.giveAccessForWatchBookTicKet(personLoginForSeance)
                .forEach(e-> info("{Name film: "
                        +e.getNameFilm()+", date session: "
                        +sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getDateTimeSession()+", seat: "+ seat +"}"));
    }

    private void checkValidChoose(String choose,String regex){
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(choose);

    if (!matcher.find()) getExceptionChoose("Invalid choose");
}

    private void returnOrExit() {
    info(SubTextForToolBar.RETURN_OR_EXIT.getText());
    String choose = getScanner();
    checkValidChoose(choose, "^[1-2]{1,1}$");
    int parsChoose = Integer.parseInt(choose);

    switch (parsChoose) {
        case 1 -> getChooseAfterLog();
        case 2 -> exit();
    }
}

    private void getExceptionChoose(String x) {
        try {
            throw new InvalidChooseException(x + getLineSeparator() + "Try again");
        } catch (InvalidChooseException e) {
            System.err.println(e.getMessage());
        }finally {
            log.error("User has invalid choose");
            getChooseAfterLog();
        }
    }

    private void info(String info){
        System.out.println(info);
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

    private void setPersonLoginForSeance(String login) {
        this.personLoginForSeance = login;
    }
    private String getHash(String password){
        StringBuilder md=new StringBuilder();
        try {
            MessageDigest digest=MessageDigest.getInstance("MD5");
            byte[]bytes=digest.digest(password.getBytes());
            for (byte b:bytes) {
                md.append(String.format("%02X",b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md.toString();
    }
}
