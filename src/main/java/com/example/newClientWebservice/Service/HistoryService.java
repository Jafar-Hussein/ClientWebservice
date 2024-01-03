package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.Models.Article;
import com.example.newClientWebservice.Models.History;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
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
 * @author jafar
 *  det här klassen är för att hämta historik användarens köphistorik från databasen
 *  det finns två metoder getAllHistory och getCurrentUserHistory
 *
 * */
public class HistoryService {
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    /**
     * @Method getAllHistory hämtar alla köphistorik från databasen
     * @param jwt är en string som är en token som används för att autentisera användaren
     *  @return histories är en arraylist av History objekt
     *  det här metoden är för admin för att kunna se alla köphistorik från alla användare
     * */
    public static ArrayList<History> getAllHistory(String jwt) { //för admin
        //skapa ett objekt av http get klassen
        HttpGet request = new HttpGet("http://localhost:8081/webshop/history");

        // inkludera en authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        // Exekvera request
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error");
                return null;
            }

            // visa upp response payload i console
            HttpEntity entity = response.getEntity();

            // konvertera response payload till ett användbart objekt
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {
            });

//            System.out.println("Histories:");
//            // skriv ut alla köphistorik
//            for (History history : histories) {
//                System.out.println(String.format(
//                        "  History id: %d \n totalCost: %d \n article: %s \n user: %s",
//                        history.getId(), history.getTotalCost(), history.getPurchasedArticles(), history.getUser().getUsername()
//                ));
//            }

//            return histories;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @Method getCurrentUserHistory hämtar alla köphistorik från databasen av den aktuella användaren
     * @param jwt är en string som är en token som används för att autentisera användaren
     * @return articles är en arraylist av Article objekt, det är alla artiklar som köpts av den aktuella användaren
     * det här metoden är för att hämta alla köphistorik från den aktuella användaren där användaren och en aurhentiserad användare har tillgång till den
     * */
    public static ArrayList<History> getCurrentUserHistory(String jwt) {
        HttpGet request = new HttpGet("http://localhost:8081/webshop/history/currentUserHistory");

        // inkludera en authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        // Exekvera request
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Error");
                return null;
            }

            // visar upp response payload i console
            HttpEntity entity = response.getEntity();

            // konvertera response payload till ett användbart objekt
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<History>>() {
            });

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}