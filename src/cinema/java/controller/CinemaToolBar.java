package controller;

import exception.InvalidChooseException;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class CinemaToolBar {
    private final PersonController personController = new PersonController();

    public static void start(){
        CinemaToolBar cinemaToolBar = new CinemaToolBar();
        cinemaToolBar.setStartChoose();
    }

    private void setStartChoose(){
        log.info("App start");
        Pattern pattern = Pattern.compile("^[1-3]{1,1}$");

        info(SubTextForToolBar.START_TEXT.getText());
        String firstChoose= getScanner();
        Matcher matcher = pattern.matcher(firstChoose);

        if (!matcher.find()){
            try {
                throw new InvalidChooseException("Invalid choose" + getLineSeparator() +
                        "Try again");
            } catch (InvalidChooseException e) {
                System.err.println(e.getMessage());
                log.error("User has invalid choose");
            }finally {
                start();
            }
        }

        int choose = Integer.parseInt(firstChoose);
        switch (choose){
            case 1 -> personController.checkValidLoginAndPassword();
            case 2 -> personController.createNewPersonAndAddToDB();
            case 3 -> exit();
        }
    }

    private static void info(String info){
        System.out.println(info);
    }
    private String getScanner(){
       return new Scanner(System.in).nextLine();
    }
    private String getLineSeparator(){
        return System.lineSeparator();
    }
    private void exit(){
        System.exit(0);
    }
}
