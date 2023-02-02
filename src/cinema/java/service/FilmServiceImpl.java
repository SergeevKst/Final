package service;

import controller.AdministratorController;
import controller.ManagerController;
import controller.SubTextForToolBar;
import exception.InvalidFilmException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import repository.FilmRepository;
import repository.FilmRepositoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Slf4j
public class FilmServiceImpl implements FilmService<Film> {
    private final FilmRepository<Film> filmRepository = new FilmRepositoryImpl();

    @Override
    public Film createFilm(String dateStart, String dateEnd, String nameFilm, String status) {
        boolean anyMatch = isAnyMatch(nameFilm);
        if (anyMatch || nameFilm.length() > 17) {
            getException("Invalid name film, name already exist" + getLineSeparator() + "Try again", status);
        } else {
            getValidDate(dateStart, status);
            getValidDate(dateEnd, status);
        }
        int end = Integer.parseInt(dateEnd.substring(8, 10));
        int start = Integer.parseInt(dateStart.substring(8, 10));
        checkPeriod(start, end, status);
        return new Film(dateStart, dateEnd, nameFilm);
    }

    @Override
    public void giveAccessForAddFilmToRepository(Film film) {
        filmRepository.addFilmToDb(film);
    }

    @Override
    public List<Film> giveAccessToFilmRepository() {
        return filmRepository.createFilmRepository();
    }

    @Override
    public void giveAccessForDeleteFilm(String name, String status) {
        boolean anyMatch = isAnyMatch(name);
        if (anyMatch) {
            filmRepository.deleteFilmByName(name);
        } else {
            getException("Invalid name film" + getLineSeparator() + "Try again", status);
        }
    }

    @Override
    public Film giveAccessForGetFilm(String name) {
        return filmRepository.getFilmFromDb(name);
    }

    @Override
    public void giveAccessForUpdateStartFilm(String start, String name, String status) {
        boolean anyMatch = isAnyMatch(name);
        if (anyMatch) {
            getValidDate(start, status);
            int dateEnd = getDayEnd(name);
            int dayStart = Integer.parseInt(start.substring(8, 10));
            checkPeriod(dayStart, dateEnd, status);
            filmRepository.updateStartFilm(start, name);
        } else {
            getException("Invalid name film" + getLineSeparator() + "Try again", status);
        }
    }

    @Override
    public void giveAccessForUpdateEndFilm(String end, String name, String status) {
        boolean anyMatch = isAnyMatch(name);
        if (anyMatch) {
            getValidDate(end, status);
            int dayStart = getDayStart(name);
            int dayEnd = Integer.parseInt(end.substring(8, 10));
            checkPeriod(dayStart, dayEnd, status);
            filmRepository.updateEndFilm(end, name);
        } else {
            getException("Invalid name film" + getLineSeparator() + "Try again", status);
        }
    }

    @Override
    public void giveAccessForUpdateNameFilm(String newName, String name, String status) {
        boolean anyMatch = isAnyMatch(newName);
        if (anyMatch || newName.length() > 17) {
            getException("Invalid name film, name already exist" + getLineSeparator() + "Try again", status);
        }
        boolean match = isAnyMatch(name);
        if (match) {
            filmRepository.updateNameFilm(newName, name);
        }
    }

    private boolean isAnyMatch(String name) {
        return giveAccessToFilmRepository().stream()
                .map(Film::getNameFilm)
                .anyMatch(e -> e.equals(name));
    }

    private void checkPeriod(int dayStart, int dateEnd, String status) {
        int period = dateEnd - dayStart;
        if (period != 14) {
            log.error("Period ought to be 14 days");
            getException("Invalid period, period ought to be 14 days" + getLineSeparator() + "Try again", status);
        }
    }

    private void getValidDate(String start, String status) {
        LocalDate localDate = LocalDate.now();
        String collect = Arrays.stream(String.valueOf(localDate.getYear())
                        .split("")).map(e -> "[" + e + "]")
                .collect(Collectors.joining());

        Pattern pattern = Pattern.compile("^" + collect + SubTextForToolBar.VALID_DATE.getText());
        Matcher matcher = pattern.matcher(start);
        if (!matcher.find()) {
            getException("Invalid data" + getLineSeparator() + "Try again", status);
        }
    }

    private void getException(String text, String status) {
        log.error("Invalid film");
        try {
            throw new InvalidFilmException(text);
        } catch (InvalidFilmException e) {
            System.err.println(e.getMessage());
        } finally {
            if (status.equals("Admin")) {
                AdministratorController.run();
            } else if (status.equals("Manager")) {
                ManagerController.run();
            }
        }
    }

    private int getDayEnd(String name) {
        String s = Arrays.toString(giveAccessToFilmRepository().stream()
                        .map(Film::getNameFilm)
                        .filter(e -> e.equals(name))
                        .map(this::giveAccessForGetFilm).map(Film::getDateEnd).collect(Collectors.joining()).split("-"))
                .substring(11, 13);
        return Integer.parseInt(s);
    }

    private int getDayStart(String name) {
        String s = Arrays.toString(giveAccessToFilmRepository().stream()
                        .map(Film::getNameFilm)
                        .filter(e -> e.equals(name))
                        .map(this::giveAccessForGetFilm).map(Film::getDateStart).collect(Collectors.joining()).split("-"))
                .substring(11, 13);
        return Integer.parseInt(s);
    }

    private String getLineSeparator() {
        return System.lineSeparator();
    }
}
