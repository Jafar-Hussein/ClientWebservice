package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.Models.Article;
import com.example.newClientWebservice.Models.Cart;
import com.example.newClientWebservice.Models.History;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.newClientWebservice.Service.UserService.login;

/**
 * Denna klass används som en service för att utföra crud operationer.
 * Metoderna använder sig av HTTP-requests för att skicka förfrågningar till API:et.
 * Url:en specificeras i varje metod och överensstämmer med de endpoints som finns i API:et.
 * Metoderna returnerar ett svar från API:et i form av en String.
 * @author Clara Brorson
 */
public class CartService {

    /**
     * CloseableHttpClient används för att skicka HTTP-requests till API:et.
     * httpClient är en instans av CloseableHttpClient.
     */
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod används för att hämta alla carts från API:et.
     * @param jwt är en String som innehåller en JWT-token.
     */

    public static List<Cart> getAllCarts(String jwt) throws IOException, ParseException {

        HttpGet request = new HttpGet("http://localhost:8081/webshop/cart");

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Something went wrong");
                System.out.println(response.getCode());
                return null;
            }

            HttpEntity entity = response.getEntity();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<List<Cart>>() {});
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Denna metod används för att hämta en cart med ett specifikt id från API:et.
     * @param id är id:t för den cart som ska hämtas.
     * @param jwt är en String som innehåller en JWT-token.
     */

//    public static void getOneCartById(int id, String jwt) throws IOException, ParseException {
//        HttpGet request = new HttpGet(String.format("http://localhost:8081/webshop/cart/%d", id));
//        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            if (response.getCode() != 200) {
//                System.out.println("Something went wrong");
//                System.out.println(response.getCode());
//                return;
//            }
//
//            HttpEntity entity = response.getEntity();
//            String responseBody = EntityUtils.toString(entity);
//
//            ObjectMapper mapper = new ObjectMapper();
//            Cart cart = mapper.readValue(responseBody, new TypeReference<Cart>() {
//            });
//
//            System.out.println(String.format("Cart %s belongs to %s and contains:", cart.getId(), cart.getUsername()));
//
//            for (Article article : cart.getArticles()) {
//                System.out.println(String.format(" id: %d\n Article: %s\n Price: %d\n Description: %s\n Quantity: %d\n",
//                        article.getId(),   article.getName(), article.getCost(), article.getDescription(), article.getQuantity()));
//            }
//        }
//    }

    public static Cart getOneCartById(int id, String jwt) throws IOException, ParseException {
        HttpGet request = new HttpGet(String.format("http://localhost:8081/webshop/cart/%d", id));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Something went wrong");
                System.out.println("Error code: " + response.getCode());
                return null;
            }

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody, new TypeReference<Cart>() {});
        }
        // Handle exceptions or issues outside of try-catch block as necessary
    }


    /**
     * Denna metod används för att lägga till en artikel i en cart.
     * @param cartId är id:t för den cart som artikeln ska läggas till i.
     * @param articleId är id:t för den artikel som ska läggas till.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void addArticleToCart(int cartId, int articleId, String jwt) throws IOException, ParseException {
        HttpPost request = new HttpPost(String.format("http://localhost:8081/webshop/cart/%d", articleId));

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Something went wrong");
            System.out.println(response.getCode());
            return;
        }

        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Cart>() {});

        System.out.println(String.format("Article %s added to cart %s", articleId, cartId));
    }

    /**
     * Denna metod används för att uppdatera antalet artiklar i en cart.
     * @param cartId är id:t för den cart som artikeln ska uppdateras i.
     * @param articleId är id:t för den artikel som ska uppdateras.
     * @param quantity är det nya antalet artiklar.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void updateArticleCount(int cartId, int articleId, int quantity, String jwt) throws IOException, ParseException {

        HttpPatch request = new HttpPatch(String.format("http://localhost:8081/webshop/cart/%d/articles/%d?quantity=%d", cartId, articleId, quantity));

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Something went wrong");
            System.out.println(response.getCode());
            return;
        }

        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Cart>() {});

        System.out.println(String.format("Article %s in cart %s has updated its quantity to %d", articleId, cartId, quantity));
    }

    /**
     * Denna metod används för att ta bort en artikel från en cart.
     * @param cartId är id:t för den cart som artikeln ska tas bort från.
     * @param articleId är id:t för den artikel som ska tas bort.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void deleteArticleFromCart(int cartId, int articleId, String jwt) throws IOException, ParseException {

        HttpDelete request = new HttpDelete(String.format("http://localhost:8081/webshop/cart/%d/articles/%d", cartId, articleId));

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Something went wrong");
            System.out.println(response.getCode());
            return;
        }

        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Cart cart = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Cart>() {});

        //Skriv ut cart och artikel som har tagits bort
        System.out.println(String.format("Article %s has been deleted from cart %s", articleId, cartId));
    }

    /**
     * Denna metod används för att hämta en användares historik från API:et.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void purchaseArticles(String jwt) {
        try {
            HttpPost request = new HttpPost("http://localhost:8081/webshop/history/purchase");

            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getCode() != 200) {
                    System.out.println(response.getCode());
                    System.out.println("Error");
                    return;
                }

                System.out.println("Purchase completed successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean articleExistsInCart(int cartId, int articleId, String jwt) throws IOException, ParseException {
        // Fetch the cart based on cartId and authorization jwt
        Cart cart = getOneCartById(cartId, jwt); // This would be a method that fetches the cart

        // Check if the cart and its articles list are not null
        if(cart != null && cart.getArticles() != null) {
            for(Article article : cart.getArticles()) {
                if(article.getId() == articleId) {
                    return true; // Article found
                }
            }
        }
        return false; // Article not found or cart is null
    }

}