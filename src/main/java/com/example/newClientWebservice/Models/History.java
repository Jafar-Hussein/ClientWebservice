package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
/**
 * @Author Jafar
 * Denna klass används för att skapa objekt av typen User.
 * det är en behållare för användarens historik
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private Long id;
    private Set<Article> purchasedArticles;
    private User user;
    private int totalCost;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id != null && id.equals(history.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
