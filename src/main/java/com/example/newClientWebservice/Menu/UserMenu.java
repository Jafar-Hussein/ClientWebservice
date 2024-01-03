package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Models.Article;
import com.example.newClientWebservice.Models.Cart;
import com.example.newClientWebservice.Models.History;
import com.example.newClientWebservice.Service.ArticleService;
import com.example.newClientWebservice.Service.CartService;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.newClientWebservice.Menu.ArticlesMenu.printArticlesMenu;
import static com.example.newClientWebservice.Service.CartService.getOneCartById;
import static com.example.newClientWebservice.Service.HistoryService.getCurrentUserHistory;
import static com.example.newClientWebservice.Service.UtilService.getIntInput;

/**
 * Denna klass används för att skapa en meny för användare.
 * Här kan användaren välja att lägga till, ta bort och uppdatera artiklar i kundkorgen.
 * Den innefattar metoder som anropar metoder från olika Service-klasser.
 * @author Clara Brorson
 */
public class UserMenu {

    public static void userMenu(String jwt) throws IOException, ParseException {

        while (true) {
            System.out.println("\nWelcome to Fruit Haven!");
            System.out.println("1. View all fruits");
            System.out.println("2. Add a fruit to the basket");
            System.out.println("3. View basket");
            System.out.println("4. Remove a fruit from the basket");
            System.out.println("5. Want more fruits? Update the quantity of a fruit in the basket");
            System.out.println("6. History of purchases");
            System.out.println("7. Ready to checkout? Proceed to checkout");
            System.out.println("8. Back to Main Menu\n");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    printArticlesMenu();
                    break;
                case 2:
                    addFruitToCart(jwt);
                    break;
                case 3:
                    viewCart(jwt);
                    break;
                case 4:
                    deleteFruitFromCart(jwt);
                    break;
                case 5:
                    updateFruitQuantity(jwt);
                    break;
                case 6:
                    getHistory(jwt);
                    break;
                case 7:
                    purchaseCart(jwt);
                    break;
                case 8:
                    MainMenu.runMeny();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    userMenu(jwt);
                    break;
            }
        }
    }

    /**
     * Samtliga metoder nedanför anropas i userMenu-metoden.
     * @param jwt är en sträng som används för att autentisera användaren.
     * @throws IOException om det blir fel med inläsning av data.
     * @throws ParseException om det blir fel med parsning av data.
     */

    private static void addFruitToCart(String jwt) throws IOException, ParseException {
        printArticlesMenu();
        int articleNumber = getIntInput("\nEnter the article number of a fruit to add to the basket: ");

        List<Article> articles = ArticleService.getAllArticles();
        if (articleNumber > 0 && articleNumber <= articles.size()) {
            Article selectedArticle = articles.get(articleNumber - 1);
            int cartId = getIntInput("Enter the cart ID: ");

            CartService.addArticleToCart(cartId, Math.toIntExact(selectedArticle.getId()), jwt);
        } else {
            System.out.println("Invalid article number. Please try again.");
        }
    }

    private static void viewCart(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        Cart cart = getOneCartById(cartId, jwt);

        if (cart != null) {
            System.out.println(String.format("\nCart %d belongs to %s and contains:", cart.getId(), cart.getUsername()));
            for (Article article : cart.getArticles()) {
                System.out.println(String.format(" id: %d\n Article: %s\n Price: %d\n Description: %s\n Quantity: %d\n",
                        article.getId(), article.getName(), article.getCost(), article.getDescription(), article.getQuantity()));
            }
        } else {
            System.out.println("Cart not found or an error occurred.");
        }
    }

    private static void deleteFruitFromCart(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        int articleId = getIntInput("Enter the article ID: ");

        // Check if articleId exists in the cart before attempting to delete
        if(CartService.articleExistsInCart(cartId, articleId, jwt)) {
            // If the article exists, proceed with deletion
            CartService.deleteArticleFromCart(cartId, articleId, jwt);
            System.out.println("Article deleted successfully from the cart.");
        } else {
            // If the article does not exist, print an error message
            System.out.println("Error: Article does not exist in the cart.");
        }
    }

    private static void updateFruitQuantity(String jwt) throws IOException, ParseException {
        int cartId = getIntInput("Enter the cart ID: ");
        int articleId = getIntInput("Enter the article ID: ");
        int quantity = getIntInput("Enter the new quantity: ");
        CartService.updateArticleCount(cartId, articleId, quantity, jwt);
    }
    private static void getHistory(String jwt) throws IOException, ParseException {
        List<History> histories = getCurrentUserHistory(jwt);
        System.out.println("\nPurchased Articles:\n");
        for (History history : histories) {
            for (Article article : history.getPurchasedArticles()) {
                System.out.println(String.format(
                        "History id: %d\n User: %s\n article id: %d\n name: %s\n cost: %d\n description: %s\n quantity: %d\n Total cost: %d\n",
                        history.getId(), history.getUser().getUsername(),article.getId(), article.getName(), article.getCost(), article.getDescription(), article.getQuantity(), history.getTotalCost()
                ));
            }
        }
    }
    private static void purchaseCart(String jwt) throws IOException, ParseException {
        CartService.purchaseArticles(jwt);
    }
}