package controller;

import controller.SubTextForToolBar;
import model.Person;
import service.PersonService;
import service.PersonServiceImpl;

import java.util.Scanner;

public class PersonComand {
//  private  final CinemaToolBar cinemaToolBar =new  CinemaToolBar();
    private final PersonService<Person> personService = new PersonServiceImpl();



public void checkValidLoginAndPassword(){

    info(SubTextForToolBar.WRITE_YOUR_LOGIN.getText());

    String login =getScenner();

    info(SubTextForToolBar.WRITE_YOUR_PASSWORD.getText());

    int password = Integer.parseInt(getScenner());

    boolean validPersonFromDB = personService.checkValidPersonFromDB(login, password);

   // if (validPersonFromDB){
     //   cinemaToolBar.getAllMenu();
   // }
}

    public void createNewPersonAndAddToDB() {
    }


    private static void info(String info){
        System.out.println(info);
    }

    private String getScenner(){
        return new Scanner(System.in).nextLine();

    }
    private String getLineSeparator(){
        return System.lineSeparator();
    }
}
