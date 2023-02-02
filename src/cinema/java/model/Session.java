package model;


import lombok.Data;

@Data
public class Session {
    private int idSession;
    private String dateTimeSession;
    private String nameFilm;
    private int ticketQuantity;

    public Session() {
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
