package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Denna klass används för att representera en kundkorg.
 * @author Clara Brorson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

        private Long id;
        private User user;
        private Set<Article> articles;
        private String username;
        private int articleQuantity;
        public Cart(Long id) {
                this.id = id;
        }

        /**
         * Den här metoden tar emot ett LoginResponse-objekt och returnerar CartId
         * @param loginResponse LoginResponse-objekt som innehåller ett User-objekt som i sin tur innehåller ett Cart-objekt
         * @return CartId som är en Long eller null om CartId inte finns
         */
        public static Long getCartIdFromUser(LoginResponse loginResponse) {
                if (loginResponse != null && loginResponse.getUser() != null) {
                        return loginResponse.getUser().getCart().getId();
                }
                return null;
        }

        @Override
        public String toString() {
                return "Cart{" +
                        "id=" + id +
                        ", user=" + user +
                        ", articles=" + articles +
                        '}';
        }
}
