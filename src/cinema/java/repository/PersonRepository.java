package repository;

import java.util.List;

public interface PersonRepository<E> {

    List<E> createPersonRepository();

    void addPersonRepository(E e);

    void deletePersonFromRepository(E e);

    void deletePersonFromRepository(int id);

    void upDatePerson(int index, String login);

    void upDatePerson(String login, String role);

    E getPersonFromDB(int index);

    E getPersonFromDB(String login);

    E getAdminOrManagerFromDB(String role);
}
