package service;

import controller.AdministratorController;
import controller.CinemaToolBar;
import exception.InvalidLoginException;
import exception.InvalidPassworException;
import model.Person;
import repository.PersonRepository;
import repository.PersonRepositoryImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonServiceImpl implements PersonService<Person> {

    private final PersonRepository<Person> personRepository = new PersonRepositoryImpl();

    @Override
    public Person createNewPerson(String login, String password) {
        return new Person(login, password);
    }

    @Override
    public void giveAccessForUpDatePerson(int index, String login) {
        personRepository.upDatePerson(index, login);
    }

    @Override
    public void giveAccessForUpDatePerson(String login, String role) {
        personRepository.upDatePerson(login, role);
    }

    public List<Person> giveAccessForRepository() {
        return personRepository.createPersonRepository();
    }

    @Override
    public void giveAccessForAddPersonToDB(Person person) {
        personRepository.addPersonRepository(person);
    }

    @Override
    public void giveAccessForDeletePersonFromDB(Person person) {
        personRepository.deletePersonFromRepository(person);
    }

    @Override
    public void giveAccessForDeletePersonFromDB(int id) {
        personRepository.deletePersonFromRepository(id);
    }

    @Override
    public Person giveAccessForGetPersonFromDB(int index) {
        return personRepository.getPersonFromDB(index);
    }

    @Override
    public Person giveAccessForGetPersonFromDB(String login) {
        return personRepository.getPersonFromDB(login);
    }

    @Override
    public boolean checkValidPersonFromDB(String login, String password) {
        List<Person> personRepositorylists = personRepository.createPersonRepository();

        boolean containsLogin = personRepositorylists.stream()
                .map(Person::getLogin)
                .anyMatch(e -> e.equals(login));

        if (containsLogin){
            Person personFromDB = personRepository.getPersonFromDB(login);
            String password1 = personFromDB.getPassword();
            if(password1.equals(getHash(password))) return true;
            else checkValidPassword();
        }else {
            checkValidLogin();
        }
        return false;
    }

    @Override
    public boolean checkValidNewLogin(String login, String role) {
        List<Person> personRepositorylists = personRepository.createPersonRepository();
        boolean correctLogin = personRepositorylists.stream()
                .map(Person::getLogin)
                .noneMatch(e -> e.equals(login));

        if (!correctLogin) {
            try {
                throw new InvalidLoginException("Invalid login" + getLineSeparator() +
                        "Try again");
            } catch (InvalidLoginException e) {
                System.err.println(e.getMessage());
            } finally {
                if (role.equals("User")) {
                    CinemaToolBar.start();
                } else if (role.equals("Admin")) {
                    AdministratorController.run();
                }
            }
        }
        return true;
    }

    @Override
    public Person giveAccessForGetAdminOrManagerFromDB(String role) {
        return personRepository.getAdminOrManagerFromDB(role);
    }

    @Override
    public boolean checkValidNewPassword(String password, String role) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(password);

        if (password.length() != 5 || !matcher.find()) {
            try {
                throw new InvalidPassworException("Invalid password" + getLineSeparator() +
                        "Try again");
            } catch (InvalidPassworException e) {
                System.err.println(e.getMessage());
            } finally {
                if (role.equals("User")) {
                    CinemaToolBar.start();
                } else if (role.equals("Admin")) {
                    AdministratorController.run();
                }
            }
        }
        return true;
    }


    private void checkValidLogin() {
            try {
                throw new InvalidLoginException("Invalid login" + getLineSeparator() +
                        "Try again");
            } catch (InvalidLoginException e) {
                System.err.println(e.getMessage());
            } finally {
                CinemaToolBar.start();
            }
    }

    private void checkValidPassword() {
            try {
                throw new InvalidPassworException("Invalid password" + getLineSeparator() +
                        "Try again");
            } catch (InvalidPassworException e) {
                System.err.println(e.getMessage());
            } finally {
                CinemaToolBar.start();
            }
    }

    private String getLineSeparator() {
        return System.lineSeparator();
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

