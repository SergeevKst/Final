package model;

import lombok.Data;

@Data
public class Ticket {
    private int idTicket;
    private String nameFilm;
    private int idPerson;
    private int idSession;
    private int cost;
    private String status;

    public Ticket(String nameFilm, int idSession, int cost) {
        this.nameFilm = nameFilm;
        this.idSession = idSession;
        this.cost = cost;

    }

    public Ticket() {
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id Ticket=" + idTicket +
                ", name Film='" + nameFilm + '\'' +
                ", id Person=" + idPerson +
                ", id Session=" + idSession +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                '}';
    }
}
