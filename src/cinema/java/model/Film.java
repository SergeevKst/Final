package model;

import lombok.Data;

import java.util.Date;

@Data
public class Film {
    String dateStart;
    String dateEnd;
    String nameFilm;
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

//    public void setDateStart(String dateStart) {
//
//        this.dateStart = dateStart;
//    }
//
//    public void setDateEnd(String dateEnd) {
//        this.dateEnd = dateEnd;
//    }
}
