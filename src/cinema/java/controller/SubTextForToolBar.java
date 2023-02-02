package controller;

public enum SubTextForToolBar {

    START_TEXT(
            "Hello dear user before you book a movie ticket could you answer some question" + getLineSeparator() +
                    "1: If you was registration before choose" + getLineSeparator() +
                    "2: If you are registering at now choose" + getLineSeparator() +
                    "3: Exit"),

    CHOOSE_AFTER_VALID_LOGIN_PASSWORD(
            "1: You can watch film list" + getLineSeparator() +
                    "2: If you want to book a ticket first look at the list session and remember the session index" + getLineSeparator() +
                    "3: You can book ticket on session" + getLineSeparator() +
                    "4: If you have ticket however you want sit it" + getLineSeparator() +
                    "5: If you want watch your tickets" + getLineSeparator() +
                    "6: Exit"),
    START_ADMIN("Hi administrator, you can see your options" + getLineSeparator() +
            "1: You can create new person and add him to DB" + getLineSeparator() +
            "2: You can watch person repository from base" + getLineSeparator() +
            "3: You can update person from base" + getLineSeparator() +
            "4: You can set new role for person" + getLineSeparator() +
            "5: You can delete person from base" + getLineSeparator() +
            "6: You can watch film repository from base" + getLineSeparator() +
            "7: You can create new film and add it to DB" + getLineSeparator() +
            "8: You can update name film from base" + getLineSeparator() +
            "9: You can update date start film from base" + getLineSeparator() +
            "10: You can update date end film from base" + getLineSeparator() +
            "11: You can delete film from base" + getLineSeparator() +
            "12: You can watch session repository from base" + getLineSeparator() +
            "13: You can create new Session and add it to DB" + getLineSeparator() +
            "14: You can update name film at session from base" + getLineSeparator() +
            "15: You can update date session from base" + getLineSeparator() +
            "16: You can delete session from base" + getLineSeparator() +
            "17: You can watch ticket repository from base" + getLineSeparator() +
            "18: You can create new ticket and add it to DB" + getLineSeparator() +
            "19: You can book ticket on session" + getLineSeparator() +
            "20: You can sit ticket on session" + getLineSeparator() +
            "21: You can delete ticket from base" + getLineSeparator() +
            "22: You can watch  your ticket" + getLineSeparator() +
            "23: You can update empty ticket from base" + getLineSeparator() +
            "24: You can sit person ticket by ticket id" + getLineSeparator() +
            "25: Exit"),
    VALID_DATE("-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|(((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$)"),

    START_MANAGER_MENU("Hi manager, you can see your options" + getLineSeparator() +
            "1: You can watch person repository from base" + getLineSeparator() +
            "2: You can watch film repository from base" + getLineSeparator() +
            "3: You can update name film from base" + getLineSeparator() +
            "4: You can update date start film from base" + getLineSeparator() +
            "5: You can update date end film from base" + getLineSeparator() +
            "6: You can watch session repository from base" + getLineSeparator() +
            "7: You can create new Session and add it to DB" + getLineSeparator() +
            "8: You can update name film at session from base" + getLineSeparator() +
            "9: You can update date session from base" + getLineSeparator() +
            "10: You can delete session from base" + getLineSeparator() +
            "11: You can watch ticket repository from base" + getLineSeparator() +
            "12: You can book ticket on session" + getLineSeparator() +
            "13: You can sit ticket on session" + getLineSeparator() +
            "14: You can watch  your ticket" + getLineSeparator() +
            "15: You can update empty ticket from base" + getLineSeparator() +
            "16: You can sit person ticket by ticket id" + getLineSeparator() +
            "17: Exit"),

    CHOOSE_AFTER_BOOK_TICKET("\u001B[32m" + "Success!" + "\u001B[0m" + getLineSeparator() +
            "1: Watch the ticket" + getLineSeparator() +
            "2: Return" + getLineSeparator() +
            "3: Exit"),
    RETURN_OR_EXIT(
            "1: Return" + getLineSeparator() +
                    "2: Exit"),
    TIME("(\s([1][4,7,9])):([0][0])$)"),
    WRITE_YOUR_LOGIN("Write your login"),

    WRITE_YOUR_PASSWORD("Write your password"),

    CREATE_PASSWORD("You should create password for example (11111)"),

    VALID_DATA("\u001B[32m" + "Success!" + "\u001B[0m"),
    CREATE_LOGIN("You should create login");

    private static String getLineSeparator() {
        return System.lineSeparator();
    }

    private final String text;

    public String getText() {
        return text;
    }

    SubTextForToolBar(String text) {
        this.text = text;
    }
}
