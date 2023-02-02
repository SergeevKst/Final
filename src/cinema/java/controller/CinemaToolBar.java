package cinematoolbar;

import comand.PersonComand;

import java.util.Scanner;

public class CinemaToolBar {

private   final   PersonComand personComand = new PersonComand();

    public static void start(){
        CinemaToolBar cinemaToolBar = new CinemaToolBar();
        cinemaToolBar.setStartChoose();

    }

    private void setStartChoose(){

        info(SubTextForToolBar.START_TEXT.getText());

        int firstChoose=getScenner();

        if (firstChoose<1||firstChoose>3){
            System.err.println("Uncorrected choose"+getLineSeparator()+
                               "Try again");
            start();
        }

        switch (firstChoose){
            case 1 -> personComand.checkValidLoginAndPassword();
            case 2 -> personComand.createNewPersonAndAddToDB();
            case 3 -> exit();
        }

    }

   public void getAllMenu(){
        info(SubTextForToolBar.CHOOSE_AFTER_VALID_LOGIN_PASSWORD.getText());
    }


    private static void info(String info){
        System.out.println(info);
    }

    private int getScenner(){
       return new Scanner(System.in).nextInt();

    }
    private String getLineSeparator(){
        return System.lineSeparator();
    }
    private void exit(){
        System.exit(0);
    }
}
