package service;

import controller.AdministratorController;
import controller.ManagerController;
import controller.PersonController;
import controller.SubTextForToolBar;
import exception.InvalidSessionException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import model.Session;
import repository.SessionRepository;
import repository.SessionRepositoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Slf4j
public class SessionServiceImpl implements SessionService<Session> {
    private final SessionRepository<Session> sessionRepository = new SessionRepositoryImpl();
    private final FilmService<Film> filmService = new FilmServiceImpl();

    @Override
    public Session createSession(String nameFilm, String dateTimeSession, int cost, String status) {
        Session session = new Session();
        getValidCost(cost, status);

        int idSession = getIdSession();

        boolean anyMatch = filmService.giveAccessToFilmRepository().stream()
                .map(Film::getNameFilm)
                .anyMatch(e -> e.equals(nameFilm));

        if (anyMatch) {
            getValidDay(nameFilm, dateTimeSession, status);
            getValidDate(dateTimeSession, status);
            session.setIdSession(idSession);
            session.setDateTimeSession(dateTimeSession);
            session.setNameFilm(nameFilm);
            session.setTicketQuantity(25);
        } else {
            getException("Invalid session" + getLineSeparator() + "Try again", status);
        }
        return session;
    }

    @Override
    public List<Session> giveAccessToSessionRepository() {
        return sessionRepository.createSessionRepository();
    }

    @Override
    public void addSessionToRepository(Session session) {
        sessionRepository.addSessionToDb(session);
    }

    @Override
    public boolean checkValidSession(int choose, String status) {
        boolean containChoose = getAnyMatch(choose);
        if (!containChoose) {
            getException("Invalid id session" + getLineSeparator() + "Try again", status);
            return false;
        }
        return true;
    }

    @Override
    public Session giveAccessForGetSessionByIdSession(int id) {
        return sessionRepository.getSessionByIdSession(id);
    }

    @Override
    public void giveAccessForDecrement(int index) {
        sessionRepository.upDateSessionDecrement(index);
    }

    @Override
    public void giveAccessForIncrement(int index) {
        sessionRepository.upDateSessionIncrement(index);
    }

    @Override
    public void giveAccessForUpDateSession(int id, String name, String status) {
        boolean anyMatch = getAnyMatch(id);
        if (anyMatch) {
            sessionRepository.upDateSession(id, name);
        } else if (name.length() > 15) {
            getException("Invalid id session" + getLineSeparator() + "Try again", status);
        }
    }

    @Override
    public void giveAccessForDeleteSessionFromDb(int id, String status) {
        boolean anyMatch = getAnyMatch(id);
        if (anyMatch) {
            sessionRepository.deleteSessionFromDb(id);
        } else {
            getException("Invalid id" + getLineSeparator() + "Try again", status);
        }
    }

    @Override
    public void giveAccessForUpDateSessionDay(int id, String day, String status) {
        boolean anyMatch = getAnyMatch(id);
        if (anyMatch) {
            String nameFilm = sessionRepository.getSessionByIdSession(id).getNameFilm();
            getValidDay(nameFilm, day, status);
            sessionRepository.upDateSessionDay(id, day);
        } else {
            getException("Invalid date" + getLineSeparator() + "Try again", status);
        }
    }

    private boolean getAnyMatch(int id) {
        return sessionRepository.createSessionRepository().stream()
                .map(Session::getIdSession)
                .anyMatch(e -> e.equals(id));
    }

    private void getValidDate(String date, String status) {
        LocalDate localDate = LocalDate.now();
        String collect = Arrays.stream(String.valueOf(localDate.getYear())
                        .split("")).map(e -> "[" + e + "]")
                .collect(Collectors.joining());

        Pattern pattern = Pattern.compile("^" + collect + SubTextForToolBar.VALID_DATE.getText() +
                SubTextForToolBar.TIME.getText());
        Matcher matcher = pattern.matcher(date);
        if (!matcher.find()) {
            getException("Invalid date" + getLineSeparator() + "Try again", status);
        }
    }

    private void getException(String text, String status) {
        log.error("Invalid Session");
        try {
            throw new InvalidSessionException(text);
        } catch (InvalidSessionException e) {
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

    private int getIdSession() {
        return sessionRepository.createSessionRepository().stream()
                .map(Session::getIdSession)
                .max(Integer::compareTo)
                .get() + 1;
    }

    private String getLineSeparator() {
        return System.lineSeparator();
    }

    private void getValidDay(String nameFilm, String dateSession, String status) {
        boolean anyMatch = isAnyMatch(dateSession);

        int dayEnd = Integer.parseInt(filmService.giveAccessForGetFilm(nameFilm).getDateEnd().substring(8, 10));
        int dayStart = Integer.parseInt(filmService.giveAccessForGetFilm(nameFilm).getDateStart().substring(8, 10));
        int daySession = Integer.parseInt(dateSession.substring(8, 10));
        if (daySession > dayEnd || daySession < dayStart || anyMatch) {
            getException("Invalid date" + getLineSeparator() + "Try again", status);
        }
    }

    private boolean isAnyMatch(String dateSession) {
        return sessionRepository.createSessionRepository().stream()
                .map(Session::getDateTimeSession)
                .anyMatch(e -> e.equals(dateSession));
    }

    private void getValidCost(int cost, String status) {
        if (cost > 15 || cost < 5) {
            getException("Invalid cost" + getLineSeparator() + "Try again", status);
        }
    }
}
