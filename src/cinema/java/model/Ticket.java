package model;

import lombok.Data;

@Data
public class Ticket {
    int idTicket;
    String nameFilm;
    int idPerson;
    int idSession;
    int cost;

    String status;
    int seat;

    public Ticket(String nameFilm, int idSession, int cost) {
        this.nameFilm = nameFilm;
        this.idSession = idSession;
        this.cost = cost;

    }

    public Ticket() {
    }
}
