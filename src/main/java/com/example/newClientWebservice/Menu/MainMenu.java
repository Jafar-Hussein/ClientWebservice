package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Service.UtilService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
/**
 * @Author Jafar Hussein
 * den här klassen är en hub som leder till andra klasser
 * @Method runMeny är en metod som kör alla andra metoder i en metod som beter sig som en hub för att leda till andra metoder
 * @Method printMainMenu skriver ut menyn
 * @Method userChoice är en metod för en switch case som leder till andra metoder
 **/

public class MainMenu {
   private static boolean isRunning = true; // global variabel så att vi kan använda den för att stänga av programmet

    /**
     * @Method runMeny är en metod som kör alla andra metoder i en metod som beter sig som en hub för att leda till andra metoder
     * @return void
     * @Error Exception e är för att fånga alla fel som kan uppstå
     */

    public static void runMeny() throws IOException, ParseException {
        while (isRunning) {
                printMainMenu();
            int choice = UtilService.getIntInput("Enter your choice: ");
            userChoice(choice);

        }
    }
    /**
     * @Method printMainMenu skriver ut menyn
     * @return void
     * @Array menuItems är en array av string som innehåller alla val i menyn
     * @for loop för att skriva ut alla val i menyn
     */
    private static void printMainMenu() {
        String [] menuItems = {"1. Login", "2. Register","3. View articles", "4. Exit"};
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    /**
     * @Method userChoice är en metod för en switch case som leder till andra metoder
     * @return void
     * @param choice är en int variabel som tar emot användarens val
     * @switch case för att leda till andra metoder
     */

    public static void userChoice(int choice) throws IOException, ParseException {
        switch (choice) {
            case 1:
                //login();
                Login.loginMenu();
                break;
            case 2:
                //register();
                Register.register();
                break;
            case 3:
                //viewArticles();
                ArticlesMenu.printArticlesMenu();
                break;
            case 4:
                System.out.println("Exiting...");
                isRunning = false;
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                break;
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        runMeny();
    }
}
