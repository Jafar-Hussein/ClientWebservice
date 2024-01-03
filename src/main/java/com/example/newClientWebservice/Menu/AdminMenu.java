package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Models.*;
import com.example.newClientWebservice.Service.CartService;
import com.example.newClientWebservice.Service.UtilService;
import org.apache.hc.core5.http.ParseException;


import java.io.IOException;
import java.util.List;

import static com.example.newClientWebservice.Menu.UserMenu.userMenu;
import static com.example.newClientWebservice.Service.ArticleService.*;
import static com.example.newClientWebservice.Service.HistoryService.getAllHistory;
import static com.example.newClientWebservice.Service.UserService.getUsers;
import static com.example.newClientWebservice.Service.UtilService.*;

/**
 * Den här klassen innehåller metoder för att visa menyer för en administratör.
 *
 * @author Fredrik
 */
public class AdminMenu {

    /**
     * Den här metoden visar en meny för en administratör.
     * @param jwt är en String som innehåller en JWT-token.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
    public static void adminMenu1(String jwt) throws IOException, ParseException {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. User menu");
            System.out.println("2. Admin menu");
            System.out.println("3. Exit.");

            int choice = getIntInput("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    userMenu(jwt);
                    break;
                case 2:
                    adminMenu2(jwt);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    adminMenu1(jwt);
                    break;
            }
        }
    }

    /**
     * Den här metoden visar en meny för en administratör.
     * @param jwt är en String som innehåller en JWT-token.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
    public static void adminMenu2(String jwt) throws IOException, ParseException {
        while (true) {
            System.out.println("\nAdmin menu:\n");
            System.out.println("1. View all carts");
            System.out.println("2. View all histories");
            System.out.println("3. View all users");
            System.out.println("4. Add article");
            System.out.println("5. Update article");
            System.out.println("6. Delete article");
            System.out.println("7. Go back");

           int choice = UtilService.getIntInput("\nEnter your choice: ");

            switch (choice) {
               case 1:
                     getAllCarts(jwt);
                    break;
               case 2:
                    /*getAllHistories(jwt);*/
                   break;
               case 3:
                   getAllUsers(jwt);
                    break;
                case 4:
                   addArticle(jwt);
                    break;
               case 5:
                   patchArticle(jwt);
                    break;
               case 6:
                    removeArticle(jwt);
                    break;
                case 7:
                    adminMenu1(jwt);
                    break;
               default:
                   System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    adminMenu2(jwt);
                  break;
            }
       }
   }

   /**
    * Den här metoden visar alla varukorgar.
    * @param jwt är en String som innehåller en JWT-token.
    * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
    * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
    */
   /*public static void getAllHistories(String jwt) throws IOException, ParseException {
       List<History> histories = getAllHistory(jwt);
       System.out.println("Histories:");
       for (History history : histories) {
           System.out.println("History ID: " + history.getId() + "\nUser: " + history.getUser().getUsername() + "\nTotal cost: " + history.getTotalCost());
           for (CartArticle cartArticle : history.getPurchasedArticles()) {
               Article article = cartArticle.getArticle();
               System.out.println(String.format(
                       "CartArticle ID: %d \n Article ID: %d \n name: %s \n cost: %d \n description: %s \n quantity: %d",
                       cartArticle.getId(), article.getId(), article.getName(), article.getCost(), article.getDescription(), cartArticle.getArticleQuantity()
               ));
           }
       }
   }*/

   /**
        * Den här metoden visar alla varukorgar.
        * @param jwt är en String som innehåller en JWT-token.
        * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
        * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
        */

    // Uppdaterad getAllCarts-metod i AdminMenu-klassen
    public static void getAllCarts(String jwt) throws IOException, ParseException {
        List<Cart> carts = CartService.getAllCarts(jwt);
        System.out.println("\nAll current carts:\n");
        for (Cart cart : carts) {
            if (cart != null) {
                System.out.println("Cart ID: " + cart.getId() + "\nUser: " + cart.getUsername());
                if (!cart.getCartArticles().isEmpty()) { // Uppdaterad till getCartArticles
                    for (CartArticle cartArticle : cart.getCartArticles()) { // Uppdaterad till CartArticle
                        Article article = cartArticle.getArticle();
                        System.out.println(String.format(
                                "Article ID: %d \n  name: %s \n  cost: %d \n  description: %s \n  quantity: %d",
                                article.getId(), article.getName(), article.getCost(), article.getDescription(), cartArticle.getArticleQuantity() // Uppdaterad till cartArticle.getArticleQuantity
                        ));
                    }
                } else {
                    System.out.println("The cart is empty.");
                }
            } else {
                System.out.println("No cart found.");
            }
        }
    }

        /**
        * Den här metoden visar alla användare.
        * @param jwt är en String som innehåller en JWT-token.
        * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
        * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
        */
   public static void getAllUsers(String jwt) throws IOException, ParseException {
        List<User> users = getUsers(jwt);
        for (User user : users) {
            System.out.println(String.format("Id: %d\n Username: %s",user.getId(), user.getUsername()));
        }
    }

    /**
     * Den här metoden lägger till en artikel.
     * @param jwt är en String som innehåller en JWT-token.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
   public static Void patchArticle(String jwt) throws IOException, ParseException {

        int id = getIntInput("Enter the id of the article you want to update: ");

        Article existingArticle = getOneArticle(id);

        if (existingArticle == null) {
            System.out.println("No article with that id exists.");
            patchArticle(jwt);

        } else {

            Article article = new Article();

            String newName = getStringInputForHttpPatch("If you want to change the name of the article. Enter the new name. Otherwise press enter:");
            if (!newName.isEmpty()) {
                article.setName(newName);
            }

            int newCost = getIntInputForHttpPatch("If you want to change the price of the article. Enter the new price. Otherwise press enter:");
            if (newCost != 0) {
                article.setCost(newCost);
            }

            String newDescription = getStringInputForHttpPatch("If you want to change the description of the article. Enter the new description. Otherwise press enter:");
            if (!newDescription.isEmpty()) {
                article.setDescription(newDescription);
            }

            return updateArticle(id, existingArticle, article, jwt);
        }
        return null;
    }

    /**
     * Den här metoden lägger till en artikel.
     * @param jwt är en String som innehåller en JWT-token.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
    public static void removeArticle(String jwt) throws IOException, ParseException {

        int id = getIntInput("Enter the id of the article you want to delete: ");

        Article articleAboutToBeDeleted = getOneArticle(id);

        if (articleAboutToBeDeleted == null) {
            System.out.println("No article with that id exists.");
        }
        else {
            System.out.println("Are you sure you want to delete " + articleAboutToBeDeleted.getName() + "?");
            System.out.println("1. Yes");
            System.out.println("2. No");

            if (getIntInput("Enter your choice: ") == 1) {
            deleteArticle(id, jwt);
        } else if (getIntInput("Enter your choice: ") == 2) {
                System.out.println("The article was not deleted.");
            adminMenu2(jwt);
        } else {
            System.out.println("Invalid input. Please enter a number between 1 and 2.");
            removeArticle(jwt);
        }
    }
}

}
