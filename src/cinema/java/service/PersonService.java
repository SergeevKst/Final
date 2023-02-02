package service;

import java.util.List;

public interface PersonService<E> {

    E createNewPerson(String login, String password);

    void giveAccessForUpDatePerson(int index, String login);

    void giveAccessForUpDatePerson(String login, String role);

    void giveAccessForAddPersonToDB(E e);

    void giveAccessForDeletePersonFromDB(E e);

    void giveAccessForDeletePersonFromDB(int id);

    E giveAccessForGetPersonFromDB(int index);

    E giveAccessForGetPersonFromDB(String login);

    List<E> giveAccessForRepository();

    E giveAccessForGetAdminOrManagerFromDB(String role);

    boolean checkValidPersonFromDB(String login, String password);

    boolean checkValidNewLogin(String login, String role);

    boolean checkValidNewPassword(String password, String role);

}
