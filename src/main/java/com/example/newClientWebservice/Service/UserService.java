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

    public static void register() {
        try {
            // Prompting user for username and password
            String username = getStringInput("Enter username ");
            String password = getStringInput("Enter your password ");

            // Creating a new user object with the provided details
            User newUser = new User(0L, username, password);

            // Setting up a new POST request to the server for registration
            HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/register");
            request.setEntity(createPayload(newUser)); // Attaching user details as payload

            // Sending the request and receiving the response
            CloseableHttpResponse response = httpClient.execute(request);

            // Handling response based on the status code
            if (response.getCode() == 200) {
                // If successful (200 OK), parse and print the user details from response
                HttpEntity payload = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});
                System.out.println(String.format("User %s has been created with the id %d", responseUser.getUsername(), responseUser.getId()));
            } else {
                // If not successful, handle different ranges of error codes
                if (response.getCode() >= 400 && response.getCode() < 500) {
                    System.out.println("Client error occurred: " + response.getCode());
                } else if (response.getCode() >= 500) {
                    System.out.println("Server error occurred: " + response.getCode());
                } else {
                    System.out.println("Unexpected status code: " + response.getCode());
                }
            }
        } catch (IOException | ParseException e) {
            // Handle network or parsing errors
            System.out.println("Network or parsing error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * @Method login loggar in en användare
     * ingen parameter eftersom att användaren inte har en token och måste logga in för att få en token
     *  @return loginResponse är ett objekt av LoginResponse klassen
     *  denna metoden är för att logga in en användare där alla registrerade användare kan använda för att logga in
     */
    public static LoginResponse login() {
        try {
            // Prompting user for username and password
            String username = getStringInput("Enter username ");
            String password = getStringInput("Enter your password ");

            // Creating a new user object with the provided details for login
            User loginUser = new User(0L, username, password);

            // Setting up a new POST request to the server for login
            HttpPost request = new HttpPost("http://localhost:8081/webshop/auth/login");
            request.setEntity(createPayload(loginUser)); // Attaching user details as payload

            // Sending the request and receiving the response
            CloseableHttpResponse response = httpClient.execute(request);

            // Handling response based on the status code
            if (response.getCode() == 200) {
                // If successful (200 OK), parse the login response details
                HttpEntity payload = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});

                // Checking if login was successful with a valid user returned
                if (loginResponse.getUser() != null) {
                    System.out.println(String.format("\nUser %s has logged in", loginResponse.getUser().getUsername()));
                    System.out.println(String.format("JWT token: %s", loginResponse.getJwt()));
                    return loginResponse; // Return the successful login response
                } else {
                    System.out.println("Incorrect username or password");
                    return null; // Login failed due to incorrect credentials
                }
            } else {
                // If not successful, handle different ranges of error codes
                if (response.getCode() >= 400 && response.getCode() < 500) {
                    System.out.println("Client error occurred: " + response.getCode());
                } else if (response.getCode() >= 500) {
                    System.out.println("Server error occurred: " + response.getCode());
                } else {
                    System.out.println("Unexpected status code: " + response.getCode());
                }
                return null; // Return null due to some error
            }
        } catch (IOException | ParseException e) {
            // Handle network or parsing errors
            System.out.println("Network or parsing error: " + e.getMessage());
            return null; // Return null due to exception
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
            return null; // Return null due to exception
        }
    }

}
