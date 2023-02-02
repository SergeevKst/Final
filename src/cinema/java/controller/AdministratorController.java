package controller;

import exception.InvalidChooseException;
import exception.InvalidLoginException;
import exception.InvalidRoleException;
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
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class AdministratorController {
    private final PersonService<Person> personService = new PersonServiceImpl();
    private final FilmService<Film> filmService= new FilmServiceImpl();
    private final SessionService<Session> sessionService=new SessionServiceImpl();
    private final TicketService<Ticket> ticketService = new TicketServiceImpl();
    private String personLoginForSeance;

    public static void run() {
        AdministratorController administratorController = new AdministratorController();
        administratorController.getStartAdminMenu();
    }

    private void getStartAdminMenu() {
        log.info("Admin choose");
        setPersonLoginForSeance();

        info(SubTextForToolBar.START_ADMIN.getText());
        String adminChoose=getScanner();
        checkValidChoose(adminChoose,"^[1-9][0-9]?$");
        int choose=Integer.parseInt(adminChoose);
        startChoose(choose > 25, "Invalid choose");

        switch (choose){
            case 1 ->  addPersonToRepository();
            case 2 ->  {personService.giveAccessForRepository().forEach(System.out::println);
            returnOrExit();}
            case 3 -> upDatePerson();
            case 4 -> upDateRole();
            case 5 -> deleteByIdPerson();
            case 6 -> {filmService.giveAccessToFilmRepository().forEach(System.out::println);
            returnOrExit();}
            case 7 ->  createFilm();
            case 8 ->  upDateNameFilm();
            case 9 ->  upDateDateStartFilm();
            case 10 -> upDateDateEndFilm();
            case 11 -> deleteFilm();
            case 12-> {sessionService.giveAccessToSessionRepository().forEach(System.out::println);
            returnOrExit();}
            case 13-> createNewSession();
            case 14-> upDateSession();
            case 15-> upDateSessionDay();
            case 16-> deleteSession();
            case 17-> {ticketService.giveAccessForCreateTicketsRepository().forEach(System.out::println);
            returnOrExit();}
            case 18-> createTicket();
            case 19-> bookTicket();
            case 20-> sitTicket();
            case 21-> deleteTicket();
            case 22-> {ticketService.giveAccessForCheckPersonTicket(personLoginForSeance,"Admin");
            returnOrExit();}
            case 23-> upDateTicket();
            case 24-> upDateNotEmptyTicket();
            case 25-> exit();
        }
    }

    private void upDateNotEmptyTicket() {
        log.info("Update sold ticket start");
        info("Write id not empty ticket");
        String id=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int tId = Integer.parseInt(id);

        boolean anyMatch = isAnyMatch(tId);
        startChoose(!anyMatch, "Invalid data");

        sessionService.giveAccessForIncrement(ticketService.giveAccessForGetTicket(tId).getIdSession());
        ticketService.giveAccessForSitTicket(tId);

        log.info("Update sold ticket end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateTicket() {
        log.info("Update ticket start");
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

        log.info("Update ticket end");
        sessionService.giveAccessForDecrement(ticketService.giveAccessForGetTicket(tId).getIdSession());
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void deleteTicket() {
        log.info("Delete ticket start");
        info("Write id ticket");
        String id=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int tId = Integer.parseInt(id);

        ticketService.giveAccessForDeleteFromRepository(tId,"Admin");

        log.info("Delete ticket end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void createTicket() {
        log.info("Create ticket start");
        info("Write name film");
        String name=getScanner();

        info("Write id session");
        String id=getScanner();

        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int tId = Integer.parseInt(id);

        info("Write cost on session");
        String cost=getScanner();
        checkValidChoose(cost,"^[1-9][0-5]?$");
        int tCost = Integer.parseInt(cost);
        ticketService.giveAccessForAddToRepository(ticketService.createTicket(name,tId,tCost,"Admin"));

        log.info("Create ticket end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void deleteSession() {
        log.info("Delete session start");
        info("Write id session");
        String id=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForDeleteSessionFromDb(i,"Admin");

        log.info("Delete session end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateSessionDay() {
        log.info("Update session day start");
        info("Write id session");
        String id=getScanner();

        info("Write new day and time session");
        String day=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForUpDateSessionDay(i,day,"Admin");

        log.info("Update session day end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }
    private void upDateSession() {
        log.info("Update session start");
        info("Write new name film");
        String name=getScanner();

        info("Write id session");
        String id=getScanner();
        checkValidChoose(id,"^[0-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(id);

        sessionService.giveAccessForUpDateSession(i,name,"Admin");

        log.info("Update session end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void createNewSession() {
        log.info("Create session start");
        info("Write name film");
        String name=getScanner();

        info("Write date and time session");
        String date=getScanner();

        info("Write cost on session");
        String cost=getScanner();
        checkValidChoose(cost,"^[1-9][0-5]?$");
        int i = Integer.parseInt(cost);

        Session session = sessionService.createSession(name, date, i,"Admin");
        sessionService.addSessionToRepository(session);
        ticketService.giveAccessForCreateFullPackTicketAndAddToDb(name,session.getIdSession(),i,"Admin");

        log.info("Create session end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void deleteFilm() {
        log.info("Delete film start");
        info("Write name film");
        String name=getScanner();

        filmService.giveAccessForDeleteFilm(name,"Admin");

        log.info("Delete film end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateDateEndFilm() {
        log.info("Choose update date end film");
        info("Write name film");
        String name=getScanner();

        info("Write new date end film");
        String dateEnd=getScanner();

        filmService.giveAccessForUpdateEndFilm(dateEnd,name,"Admin");

        log.info("Date is update");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateDateStartFilm() {
        log.info("Choose update date start film");
        info("Write name film");
        String name=getScanner();

        info("Write new date start film");
        String dateStart=getScanner();

        filmService.giveAccessForUpdateStartFilm(dateStart,name,"Admin");

        log.info("Date is update");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDateNameFilm() {
        log.info("Choose update name film");
        info("Write new name film");
        String newName=getScanner();

        info("Write name film from base");
        String name=getScanner();

        filmService.giveAccessForUpdateNameFilm(newName,name,"Admin");
        ticketService.giveAccessForDeleteFromRepository(newName);

        log.info("Name is update");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void createFilm() {
        log.info("Create film start");
        info("Write new name film");
        String name=getScanner();

        info("Write new date start film");
        String dateStart=getScanner();

        info("Write new date end film");
        String dateEnd=getScanner();

        Film film = filmService.createFilm(dateStart, dateEnd, name,"Admin");
        filmService.giveAccessForAddFilmToRepository(film);

        log.info("Create film end");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void deleteByIdPerson(){
        log.info("Delete person start");
        info("You should write person Id");
        String scanner= getScanner();
        checkValidChoose(scanner,"^[1-9][0-9]?[0-9]?$");
        int i = Integer.parseInt(scanner);

        boolean anyMatch = isMatch(personService.giveAccessForRepository().stream()
                .map(Person::getId), i);

        if (anyMatch){
            Person person = personService.giveAccessForGetPersonFromDB(i);
            checkPersonRole(person.getPersonRole());
            boolean equals = person.getPersonRole().equals(Role.MANAGER.getRole());
            getIncrementAfterDelete(i);
            getSubDelete(i, person, equals);
            log.info("Delete person end");
            info(SubTextForToolBar.VALID_DATA.getText());
            returnOrExit();
        }else getException("Invalid Id");
    }

    private void upDateRole() {
        log.info("Update role start");
        info("You should write person login");
        String login= getScanner();

        Optional<Person> optionalPerson = Optional.ofNullable(personService.giveAccessForGetPersonFromDB(login));
        if (optionalPerson.isEmpty()) getException("Invalid Login");

        String personRole = optionalPerson.get().getPersonRole();
        Person person = optionalPerson.get();
        checkPersonRole(personRole);

        info("You should write role");
        String newRole= getScanner();

        if (newRole.equals("Manager")) checkValidRoleManager(login);
        else if (newRole.equals("User")) getAnswerForUpdateRoleUser(person);
        else getException("Invalid role");

        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void upDatePerson() {
        log.info("Choose update person");
        info("You should write id person for set new login");
        String idPerson = getScanner();
        checkValidChoose(idPerson,"^[1-9][0-9]?[0-9]?$");
        int id = Integer.parseInt(idPerson);

        boolean anyMatch = isMatch(personService.giveAccessForRepository().stream()
                .map(Person::getId), id);

        if(anyMatch){
            info("You should write new login");
            String newLogin=getScanner();
            personService.checkValidNewLogin(newLogin,"Admin");
            personService.giveAccessForUpDatePerson(id,newLogin);
            log.info("Person is update");
            info(SubTextForToolBar.VALID_DATA.getText());
            returnOrExit();
        }else getException("Invalid Id");
    }

    private void sitTicket() {
        log.info("Sit ticket start");
        List<Ticket> tickets = ticketService.giveAccessForWatchBookTicKet(personLoginForSeance);

        if (tickets.isEmpty()) {
            getExceptionChoose("You don't have any ticket");
        } else {
            tickets.forEach(e -> System.out.println("{Id ticket: "
                    + e.getIdTicket() + ", name film: " + e.getNameFilm() + ", date session: "
                    + sessionService.giveAccessForGetSessionByIdSession(e.getIdSession()).getDateTimeSession()));
        }

        info("You should write id ticket witch you ought to sit");
        String idTicket = getScanner();
        checkValidChoose(idTicket, "^[1-9][0-9]?[0-9]?$");
        int id = Integer.parseInt(idTicket);

        boolean b = isMatch(tickets.stream().map(Ticket::getIdTicket), id);
        startChoose(!b, "Invalid ticket Id");

        getSit(tickets, id);

        log.info("Sit ticket end");
        log.info("Tickets are increase");
        getIncrement(tickets, id);
        returnOrExit();
    }

    private void bookTicket() {
        log.info("Choose book ticket");
        info("You should write session index");
        String sessionChoose=getScanner();
        checkValidChoose(sessionChoose,"^[1-9][0-9]?$");
        int choose = Integer.parseInt(sessionChoose);

        boolean serverAnswer = sessionService.checkValidSession(choose,"Admin");
        if (!serverAnswer){
            log.error("Invalid session");
            getStartAdminMenu();
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
            case 2 -> getStartAdminMenu();
            case 3 -> exit();
        }
    }

    private boolean isMatch(Stream<Integer> personService, int personId) {
        return personService
                .anyMatch(e -> e.equals(personId));
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

    private void getSit(List<Ticket> tickets, int id) {
        tickets.stream().map(Ticket::getIdTicket)
                .filter(e->e.equals(id))
                .forEach(ticketService::giveAccessForSitTicket);
    }

    private void buyTicket(int idSession) {
        log.info("Choose buy ticket");
        info("Write number of seat");
        String numberOfSeat=getScanner();
        checkValidChoose(numberOfSeat,"^[1-9][0-9]?$");
        int seat = Integer.parseInt(numberOfSeat);

        boolean serverAnswer = ticketService.checkValidNumberOfSeat(idSession, seat,"Admin");
        if (!serverAnswer){
            log.error("Invalid seat");
            getStartAdminMenu();
        }else {
            ticketService.giveAccessForBuyTicket(seat,idSession, personLoginForSeance);
            sessionService.giveAccessForDecrement(idSession);
        }

        log.info("Ticket is buy");
        info(SubTextForToolBar.VALID_DATA.getText());
        returnOrExit();
    }

    private void getException(String x) {
        log.error(x);
        try {
            throw new InvalidLoginException(x + getLineSeparator() +
                    "Try again");
        } catch (InvalidLoginException e) {
            System.err.println(e.getMessage());
        }finally {
            getStartAdminMenu();
        }
    }

    private void getExceptionChoose(String x) {
        try {
            log.error(x);
            throw new InvalidChooseException(x + getLineSeparator() +
                    "Try again");
        } catch (InvalidChooseException e) {
            System.err.println(e.getMessage());
        }finally {
            getStartAdminMenu();
        }
    }

    private void getSubDelete(int i, Person person, boolean equals) {
        if (equals){
            info("If you want do this you ought to create new manager in base at first");
            createNewManager(person);
        }else {
            personService.giveAccessForDeletePersonFromDB(i);
            getStartAdminMenu();
        }
    }

    private Person createAndAddPersonToRepository() {
        log.info("Create new person");
        Person person = new Person();
        info(SubTextForToolBar.CREATE_LOGIN.getText());
        String login = getScanner();
        boolean validNewLogin = personService.checkValidNewLogin(login, "Admin");

        info(SubTextForToolBar.CREATE_PASSWORD.getText());
        String password = getScanner();
        boolean validNewPassword = personService.checkValidNewPassword(password, "Admin");

        if (validNewLogin && validNewPassword) {
            info(SubTextForToolBar.VALID_DATA.getText());
            String newPassword = getHash(password);
            person.setLogin(login);
            person.setPassword(newPassword);
            person.setPersonRole(Role.USER.getRole());
        }

        log.info("New person is create");
        return person;
    }

    private boolean getIncrementAfterDelete(int id) {
        String login = personService.giveAccessForGetPersonFromDB(id).getLogin();
        List<Ticket> tickets = ticketService.giveAccessForWatchBookTicKet(login);
        if (!tickets.isEmpty()) {
            tickets.stream().map(Ticket::getIdTicket)
                    .forEach(ticketService::giveAccessForSitTicket);
            tickets.stream().map(Ticket::getIdSession)
                    .forEach(sessionService::giveAccessForIncrement);
            return true;
        }
        return false;
    }

    private void getIncrement(List<Ticket> tickets, int id) {
        tickets.stream().map(Ticket::getIdTicket)
                .filter(e->e.equals(id))
                .map(e->ticketService.giveAccessForGetTicket(e).getIdSession())
                .forEach(sessionService::giveAccessForIncrement);
    }

    private void checkPersonRole(String personRole) {
        if(personRole.equals(Role.ADMINISTRATOR.getRole())){
            try {
                log.error("Update Admin role");
                throw new InvalidRoleException("Invalid person, you can't update your role " + getLineSeparator() +
                        "Try again");
            } catch (InvalidRoleException e) {
                System.err.println(e.getMessage());
            }finally {
                getStartAdminMenu();
            }
        }
    }

    private void getAnswerForUpdateRoleUser(Person person) {
        info("User role you can set only manager, if you want do this you ought to create new manager in base at first"+getLineSeparator()+
                "1: I sure"+getLineSeparator()+
                "2: Return");

        String choose = getScanner();
        checkValidChoose(choose,"^[1-2]{1,1}$");
        int answer=Integer.parseInt(choose);

        switch (answer){
            case 1-> createNewManager(person);
            case 2->getStartAdminMenu();
        }
    }

    private void createNewManager(Person person) {
        log.info("Create manager");
        Person personManager = createAndAddPersonToRepository();
        personManager.setPersonRole(Role.MANAGER.getRole());

        info("Only one manger can be, Delete the other"+getLineSeparator()+
                "1: I sure"+getLineSeparator()+
                "2: Return");

        String choose = getScanner();
        checkValidChoose(choose,"^[1-2]{1,1}$");
        int answer=Integer.parseInt(choose);

        switch (answer){
            case 1-> {personService.giveAccessForAddPersonToDB(personManager);
                personService.giveAccessForDeletePersonFromDB(person);
                getStartAdminMenu();}
            case 2->getStartAdminMenu();
        }
    }

    private void checkValidRoleManager(String login) {
        boolean anyMatch = isAnyMatch();
        if (anyMatch){
            log.error("add Two manager");
            try {
                throw new InvalidRoleException("Invalid role, Manager can be only one " + getLineSeparator() +
                        "Try again");
            } catch (InvalidRoleException e) {
                System.err.println(e.getMessage());
            }finally {
                getStartAdminMenu();
            }
        }else personService.giveAccessForUpDatePerson(login,Role.MANAGER.getRole());
    }

    private boolean isAnyMatch() {
        return personService.giveAccessForRepository().stream()
                .map(Person::getPersonRole)
                .anyMatch(e -> e.equals(Role.MANAGER.getRole()));
    }

    private void addPersonToRepository() {
        log.info("Person add to base");
        Person person = createAndAddPersonToRepository();
        personService.giveAccessForAddPersonToDB(person);
        returnOrExit();
    }

    private void info(String info) {
        System.out.println(info);
    }

    private void startChoose(boolean choose, String Invalid_choose) {
        if (choose) {
            getExceptionChoose(Invalid_choose);
        }
    }

    private void returnOrExit() {
        info(SubTextForToolBar.RETURN_OR_EXIT.getText());
        String choose = getScanner();
        checkValidChoose(choose, "^[1-2]{1,1}$");

        int parsChoose = Integer.parseInt(choose);
        switch (parsChoose) {
            case 1 -> getStartAdminMenu();
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
        this.personLoginForSeance = personService.giveAccessForGetAdminOrManagerFromDB(Role.ADMINISTRATOR.getRole()).getLogin();
    }
    private String getHash(String password){
        log.info("Password is Hash");
        StringBuilder md=new StringBuilder();
        try {
            log.error("hash error");
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
