package com.example.newClientWebservice.Models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Cart cart;
    private Set<Role> authorities;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;

    @JsonAlias("cartId")
    private void setCartId(Long cartId) {
        this.cart = new Cart(cartId);
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                ", cart=" + cart +
                ", authorities=" + authorities +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", accountNonExpired=" + accountNonExpired +
                '}';
    }
}