package com.example.newClientWebservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Denna klass innehåller metoder som tar emot input från användaren.
 *
 * @author Fredrik
 */
public class UtilService {

    /**
     * Denna metod används för att läsa in String-input från användaren via konsolen.
     * @param prompt är en String som innehåller meddelandet som ska visas för användaren innan inmatning läses in.
     * @return en String som lästs in från användaren.
     */
    public static String getStringInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String input = scan.nextLine();

//        if (input.matches(".*\\d.*")) {
//            System.out.println("Invalid input. Please do not enter numbers.\n");
//            return getStringInput(prompt);
//        } else if (input.equals("")) {
//            System.out.println("Try again.");
//            return getStringInput(prompt);
//        }
        return input;
    }

    /**
     * Denna metod används för att läsa in int-input från användaren via konsolen.
     * @param prompt är en String som innehåller meddelandet som ska visas för användaren innan inmatning läses in.
     * @return ett heltal som lästs in från användaren.
     */
    public static int getIntInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);

        try {
            int input = scan.nextInt();

            if (input == 0) {
                System.out.println("Try again");
                return getIntInput(prompt);
            }

            return input;

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.\n");
            scan.next(); // discard the invalid input
            return getIntInput(prompt);
        }
    }

    /**
     * Denna metod används för att konvertera ett Java-objekt till en JSON-sträng och skapa en StringEntity som kan användas som payload i en HTTP-request.
     *
     * @param object är det Java-objekt som ska konverteras till en JSON-sträng.
     * @return en StringEntity som innehåller en JSON-sträng.
     * @throws JsonProcessingException kastar ett undantag om det blir problem med konverteringen.
     */
    public static StringEntity createPayload(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(object), ContentType.APPLICATION_JSON);

        return payload;
    }

    /**
     * Detta är en metod som specifikt används för att läsa in input från användaren när en PATCH request ska skickas.
     * Till skillnad från metoden getStringInput() så skrivs inget felmeddelande ut om användaren inte skriver in något.
     * @param prompt är en String som innehåller meddelandet som ska visas för användaren innan inmatning läses in.
     * @return en String som lästs in från användaren.
     */
    public static String getStringInputForHttpPatch(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String input = scan.nextLine();

        if (input.equals("")) {
            System.out.println("No changes made.");
        }
        return input;
    }

    /**
     * Detta är en metod som specifikt används för att läsa in input från användaren när en PATCH request ska skickas.
     * Till skillnad från metoden getIntInput() så skrivs inget felmeddelande ut om användaren inte skriver in något.
     * @param prompt är en String som innehåller meddelandet som ska visas för användaren innan inmatning läses in.
     * @return ett heltal som lästs in från användaren.
     */
    public static int getIntInputForHttpPatch(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String input = scan.nextLine();

        if (input.isEmpty()) {
            System.out.println("No changes made.");
            return 0;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getIntInputForHttpPatch(prompt);
        }
    }
}