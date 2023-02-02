package model;

import lombok.Data;

@Data
public class Film {
    private String dateStart;
    private String dateEnd;
    private String nameFilm;

    @Override
    public String toString() {
        return "Film{" +
                " name film:'" + nameFilm + '\'' +
                ", date start:" + dateStart +
                ", date end:" + dateEnd +
                '}';
    }

    public Film(String dateStart, String dateEnd, String nameFilm) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.nameFilm = nameFilm;
    }

    public Film() {}
}
