package service;

import exception.InvalidChooseException;
import model.Session;
import repository.SessionRepository;
import repository.SessionRepositoryImpl;

public class SessionServerImpl implements SessionServer<Session> {
    private final SessionRepository<Session> sessionRepository = new SessionRepositoryImpl();

    @Override
    public Session createSession(String nameFilm, String dateTimeSession, int ticketQuantity) {
        return new Session( nameFilm, dateTimeSession, ticketQuantity);
    }

    @Override
    public void readSessionRepository() {
        sessionRepository.createSessionRepository().forEach(System.out::println);
    }

    @Override
    public void addSessionToRepository(Session session) {
sessionRepository.addSessionToDb(session);
    }

    @Override
    public boolean checkValidSession(int choose) {
        boolean containChoose = sessionRepository.createSessionRepository().stream()
                .map(Session::getIdSession)
                .anyMatch(e->e.equals(choose));
        if (!containChoose){
            try {
                throw new InvalidChooseException("Session not found" + System.lineSeparator() +
                        "Try again");
            } catch (InvalidChooseException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
