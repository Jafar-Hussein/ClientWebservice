package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.Models.Cart;
import com.example.newClientWebservice.Models.LoginResponse;
import com.example.newClientWebservice.Models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
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

import static com.example.newClientWebservice.Service.UtilService.createPayload;
import static com.example.newClientWebservice.Service.UtilService.getStringInput;

public class UserService {
    /**
     * @Author: Jafar Hussein
     * Denna klassen är för att hämta alla användare från databasen
     * Det finns tre metoder getUsers, register och login
     * */
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * @Method getUsers hämtar alla användare från databasen
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @Return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<User>>() {}) är en arraylist av User objekt
     * det här metoden är för admin för att kunna se alla användare
     * */
    public static List<User> getUsers(String jwt) throws IOException, ParseException { // för admin
    // skapa ett objekt av http get klassen
            HttpGet request = new HttpGet("http://localhost:8081/webshop/user");
    // inkludera en authorization metod till request
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    // exekvera request
            CloseableHttpResponse response = httpClient.execute(request);
            // visa upp response payload i console
            if (response.getCode() != 200) {
                System.out.println("Something went wrong");
                return null;
            }

            // visa upp response payload i console
            HttpEntity entity = response.getEntity();
        // skapa ett objekt av ObjectMapper klassen
            ObjectMapper mapper = new ObjectMapper();
            // skapar en arraylist av User objekt för att kunna loopa igenom och skriva ut alla users
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<User>>() {});
        // loopa igenom och skriv ut users
//        for (User user : users) {
//            System.out.println(String.format("Id: %d \n  Username: %s",user.getId(), user.getUsername()));
//        }
    }

    /**
     * @Method register skapar en ny användare
     * använder inte jwt token som parameter eftersom att det är en ny användare som inte har en token
     * @return void
     * denna metoden är för att skapa en ny användare där alla användare eller oregristrerade användare kan använda
     */
    public static void register()throws IOException, ParseException{
        // skapa ett username och password
        String username = getStringInput("Enter username ");
        String password = getStringInput("Enter your password ");
        // skapa ett user objekt och sparar username och password i det
        User newUser = new User(0L, username, password);
        // skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/register");
        // skapa en payload
        request.setEntity(createPayload(newUser));
        // skicka request
        CloseableHttpResponse response = httpClient.execute(request);
        // om response code inte är 200 så har något gått fel
        if (response.getCode() != 200){
            System.out.println("Something went wrong");
            return;
        }
        // hämta payload från response
        HttpEntity payload = response.getEntity();
        // skapa ett user objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        // skriv ut att användaren har skapats
        User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});

        System.out.println(String.format("User %s has been created with the id %d",responseUser.getUsername(), responseUser.getId()));
    }

    /**
     * @Method login loggar in en användare
     * ingen parameter eftersom att användaren inte har en token och måste logga in för att få en token
     *  @return loginResponse är ett objekt av LoginResponse klassen
     *  denna metoden är för att logga in en användare där alla registrerade användare kan använda för att logga in
     */

    public static LoginResponse login() throws IOException, ParseException{
        // skapa ett username och password
        String username = getStringInput("Enter username ");
        String password = getStringInput("Enter your password ");
        //Skapa user objekt
        User loginUser = new User(0L, username, password);

        //skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/login");
        //skapa en payload
        request.setEntity(createPayload(loginUser));

        //send request
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getCode() != 200){
            System.out.println("Something went wrong");
            return null;
        }

        //hämta Payload från response
        HttpEntity payload = response.getEntity();

        //skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});
        if (loginResponse.getUser() == null) {
            System.out.println("Felaktigt användarnamn eller lösenord");
            return null;
        }
        System.out.println(String.format("\nUser %s has logged in", loginResponse.getUser().getUsername()));
        System.out.println(String.format("JWT token: %s", loginResponse.getJwt()));

        return loginResponse;
    }

//    public static void main(String[] args) throws IOException, ParseException {
//        // Replace with your actual JWT token
//        String jwt = String.valueOf(login().getJwt());
////
////        getUsers(jwtToken);
//
////    register();
////   login();
//        List<User> users = getUsers(jwt);
//        for (User user : users) {
//            System.out.println(String.format("Id: %d\n Username: %s",user.getId(), user.getUsername()));
//        }
//    }
}
