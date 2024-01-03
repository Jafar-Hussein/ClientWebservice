package com.example.newClientWebservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denna klass används för att representera en roll
 * som används för att ge användare olika behörigheter i applikationen.
 * @author Clara Brorson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Long roleId;
    private String authority;
}