package com.example.newClientWebservice.Service;

import com.example.newClientWebservice.Models.Article;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.newClientWebservice.Service.UtilService.*;

/**
 * Denna klass innehåller metoder för att utföra operationer på artiklar.
 * Metoderna är kopplade till olika endpoints i WebService-applikationen.
 *
 * @author Fredrik
 */
@Service
public class ArticleService {

    /**
     * Detta objekt används för att skicka HTTP requests till WebService-applikationen.
     */
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Denna metod används för att hämta alla artiklar från databasen.
     * Metoden skickar en GET request till endpoint: /webshop/articles i WebService-applikationen.
     * @return en lista med alla artiklar.
     */
    public static ArrayList<Article> getAllArticles() {
    try {
        HttpGet request = new HttpGet("http://localhost:8081/webshop/articles");
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred. HTTP response code: " + response.getCode());
            return null;
        }

        HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<Article>>() {});
    } catch (IOException | ParseException e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

    /**
     * Denna metod används för att hämta en artikel från databasen med ett specifikt id.
     * Metoden skickar en GET request till endpoint: /webshop/articles/{id} i WebService-applikationen.
     * @param id är id:t för den artikel som ska hämtas.
     * @return en artikel med det specifika id:t.
     */
    public static Article getOneArticle(int id) throws IOException, ParseException {

        HttpGet request = new HttpGet(String.format("http://localhost:8081/webshop/articles/%d", id));

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred. HTTP response code: " + response.getCode());
            return null;
        }
        HttpEntity entity = response.getEntity();

        ObjectMapper mapper = new ObjectMapper();
        Article article = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Article>() {});

        System.out.println(String.format("Article: %s \n Price: %d", article.getName(), article.getCost()));
        return article;
    }

    /**
     * Denna metod används för att skapa en artikel.
     * @return en artikel.
     */
    public static Article createArticle() {
        Article article = new Article();

        article.setName(getStringInput("Enter the name of the article:"));
        article.setCost(getIntInput("Enter the price of the article:"));
        article.setDescription(getStringInput("Enter the description of the article:"));
        return article;
    }

    /**
     * Denna metod används för att lägga till en artikel i databasen.
     * Metoden skickar en POST request till endpoint: /webshop/articles i WebService-applikationen.
     * @param jwt är en String som innehåller en JWT-token.
     * @throws IOException kastar ett undantag om det blir problem med inläsning från användaren.
     * @throws ParseException kastar ett undantag om det blir problem med parsning av JSON.
     */
    public static void addArticle(String jwt) throws IOException, ParseException {

       Article newArticle = createArticle();

        HttpPost request = new HttpPost("http://localhost:8081/webshop/articles");

        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(newArticle), ContentType.APPLICATION_JSON);

        request.setEntity(payload);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return;
        }

        HttpEntity entity = response.getEntity();

        Article responseArticle = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Article>() {});

        if (responseArticle.getName().equals(newArticle.getName()) && responseArticle.getCost() == newArticle.getCost()
                && responseArticle.getDescription().equals(newArticle.getDescription())) {
            System.out.println(String.format("The article: %s, has been added", responseArticle.getName()));
        } else {
            System.out.println("Something went wrong");
        }
    }

    /**
     * Denna metod används för att uppdatera en artikel i databasen.
     * Metoden skickar en PATCH request till endpoint: /webshop/articles/{id} i WebService-applikationen.
     * @param id är id:t för den artikel som ska uppdateras.
     * @param existingArticle är den artikel som ska uppdateras.
     * @param article är den artikel som innehåller den nya informationen.
     * @param jwt är en String som innehåller en JWT-token.
     * @return den uppdaterade artikeln.
     */
    public static Void updateArticle(int id, Article existingArticle, Article article, String jwt) throws IOException, ParseException {

        HttpPatch request = new HttpPatch(String.format("http://localhost:8081/webshop/articles/%d", id));

        // If a field in 'article' is null, set it to the corresponding value in 'existingArticle'
        if (article.getName() == null) {
            article.setName(existingArticle.getName());
        }
        if (article.getCost() == 0) {
            article.setCost(existingArticle.getCost());
        }
        if (article.getDescription() == null) {
            article.setDescription(existingArticle.getDescription());
        }

        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(article), ContentType.APPLICATION_JSON);

        request.setEntity(payload);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred: " + response.toString());
            return null;
        }

        HttpEntity entity = response.getEntity();

        Article responseArticle = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Article>() {});

        if ((responseArticle.getName() == null ? article.getName() == null : responseArticle.getName().equals(article.getName()))
                && responseArticle.getCost() == article.getCost()
                && (responseArticle.getDescription() == null ? article.getDescription() == null : responseArticle.getDescription().equals(article.getDescription()))) {
            System.out.println(String.format("The article: %s, has been updated", responseArticle.getName()));
        } else {
            System.out.println("Something went wrong");
        }
        return null;
    }

    /**
     * Denna metod används för att ta bort en artikel från databasen.
     * Metoden skickar en DELETE request till endpoint: /webshop/articles/{id} i WebService-applikationen.
     * @param id är id:t för den artikel som ska tas bort.
     * @param jwt är en String som innehåller en JWT-token.
     */
    public static void deleteArticle(int id, String jwt) throws IOException, ParseException {

        HttpDelete request = new HttpDelete(String.format("http://localhost:8081/webshop/articles/%d", id));

        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error occurred");
            return;
        }

        System.out.println("The article has been deleted");
    }
}
