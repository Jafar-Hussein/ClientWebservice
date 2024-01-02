package com.example.newClientWebservice.Menu;

import com.example.newClientWebservice.Models.Article;
import com.example.newClientWebservice.Models.Cart;
import com.example.newClientWebservice.Service.ArticleService;

import java.util.List;
/**
 * @Author Jafar Hussein
 * denna klass skriver ut alla artiklar från databasen
 * @Method printArticlesMenu() skriver ut alla artiklar från databasen med hjälp av getAllArticles() från ArticleService
 */
public class ArticlesMenu {

    /**
     * Skriver ut alla artiklar från databasen med hjälp av getAllArticles() från ArticleService
     * @return void
     * använde mig av en for loop för att skriva ut alla artiklar från databasen
     * skapar en int articleNumber för att kunna skriva ut artikel nummer för varje artikel
     * varje loop ökar articleNumber med 1
     */
    public static void printArticlesMenu() { // prints out the articles from articleService
        System.out.println("\nArticles Menu \n");
        Cart cart = new Cart();
        List<Article> articles = ArticleService.getAllArticles();
        int articleNumber = 0;
        for (Article article : articles) {
            articleNumber++;
            System.out.println(String.format("%d. Article id: %d\n Article: %s \n Price: %d \n Description: %s\n",
                    articleNumber, article.getId(), article.getName(), article.getCost(), article.getDescription()));
        }
    }


//    public static void main(String[] args) {
//        printArticlesMenu();
//    }
}
