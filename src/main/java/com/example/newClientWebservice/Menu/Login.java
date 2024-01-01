package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Models.LoginResponse;
import com.example.newClientWebservice.Models.Role;
import com.example.newClientWebservice.Models.User;
import com.example.newClientWebservice.Service.UtilService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

import static com.example.newClientWebservice.Models.Cart.getCartIdFromUser;
import static com.example.newClientWebservice.Service.UserService.login;

/**
 * Denna klass används för att skapa en meny för inloggning.
 * @author Clara Brorson
 */

public class Login {

    /**
     * Denna metod används för att skapa en meny för inloggning.
     * Den anropar metoden login i UserService-klassen för att logga in användaren.
     * Användaren får tillbaka ett LoginResponse-objekt som innehåller ett JWT och en User.
     * LoginResponse-objektet skickas in i metoden getCartIdFromUser i Cart-klassen.
     * En if sats kontrollerar om användaren har admin-roll genom att anropa metoden isAdmin.
     * Beroende på om användaren har admin-roll eller inte, visas antingen admin-menyn eller användarmenyn.
     */
    public static void loginMenu() throws IOException, ParseException {
        while (true) {
            System.out.println("\nLogin Menu");
            System.out.println("1. Login");
            System.out.println("2. Back to Main Menu");

            int choice = UtilService.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    LoginResponse loginResponse = login();
                    Long cartId = getCartIdFromUser(loginResponse);

                    if (cartId != null) {
                        System.out.println("Ready to go shopping? Don't forget your Cart ID: " + cartId);

                        if (isAdmin(loginResponse.getUser())) {
                            AdminMenu.adminMenu1(loginResponse.getJwt());
                        } else {
                            UserMenu.userMenu(loginResponse.getJwt());
                        }

                    } else {
                        System.out.println("Something went wrong. Please try again.");
                    }
                    break;

                case 2:
                    MainMenu.runMeny();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    loginMenu();
                    break;
            }
        }
    }

    /**
     * Denna metod används för att kontrollera om en användare har admin-rollen.
     * @param user är en User som ska kontrolleras.
     * @return true om användaren har admin-rollen, annars false.
     */
    private static boolean isAdmin(User user) {
        if (user != null && user.getAuthorities() != null) {
            for (Role role : user.getAuthorities()) {
                if ("admin".equalsIgnoreCase(role.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }
}
