package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denna klass används för att skapa objekt av typen Article.
 *
 * @author Fredrik
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private Long id;
    private String name;
    private int cost;
    private String description;
    private int quantity;

    /**
     * Denna konstruktor används för att skapa ett objekt av typen Article.
     * @param name är namnet på artikeln.
     * @param cost är priset på artikeln.
     * @param description är beskrivningen av artikeln.
     * @param quantity är antalet artiklar som finns i lager.
     */
    public Article(String name, int cost, String description, int quantity) {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.quantity = quantity;
    }

}
