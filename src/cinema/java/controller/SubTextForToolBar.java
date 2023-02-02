package cinematoolbar;

public enum SubTextForToolBar {

    START_TEXT("Hello dear user before you book a movie ticket could you answer some question" + getLineSeparator() +
            "1: If you was registration before choose" + getLineSeparator() +
            "2: If you are registering at now choose" + getLineSeparator() +
            "3: Exit"),

    CHOOSE_AFTER_VALID_LOGIN_PASSWORD("1: You can watch film list" + getLineSeparator() +
            "2: If you are registering at now choose" + getLineSeparator() +
            "3: Exit"),

    WRITE_YOUR_LOGIN("Write your login"),

    WRITE_YOUR_PASSWORD("Write your password");

    private static String getLineSeparator() {
        return System.lineSeparator();
    }

    private final String text;

    public String getText(){
        return text;
    }

    SubTextForToolBar(String text) {
        this.text = text;
    }
}
