package model;


import lombok.Data;

@Data
public class Session {
    int idSession;
    String dateTimeSession;
    String nameFilm;
    int ticketQuantity;

    public Session() {
    }

    public Session(String nameFilm,String dateTimeSession, int ticketQuantity) {
        this.dateTimeSession = dateTimeSession;
        this.nameFilm =nameFilm ;
        this.ticketQuantity = ticketQuantity;
    }
    public Session(String nameFilm,String dateTimeSession) {
        this.dateTimeSession = dateTimeSession;
        this.nameFilm =nameFilm ;
        this.ticketQuantity = 25;
    }

    @Override
    public String toString() {
        return "Session{" +
                "index session=" + idSession +
                ", name film='" + nameFilm + '\'' +
                ", date and time session='" + dateTimeSession + '\'' +
                ", ticket quantity=" + ticketQuantity +
                '}';
    }
}
