package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denna klass används för att skapa en loginsvar som innehåller en användare och en JWT.
 * User-objektet innehåller användarens id, användarnamn och roller.
 * JWT är en sträng som används för att autentisera användaren.
 * @author Clara Brorson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

        private User user;
        private String jwt;


}