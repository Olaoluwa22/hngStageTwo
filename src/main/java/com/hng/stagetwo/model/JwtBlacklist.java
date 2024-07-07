package com.hng.stagetwo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date invalidatedAt;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Date getInvalidatedAt() {
        return invalidatedAt;
    }
    public void setInvalidatedAt(Date invalidatedAt) {
        this.invalidatedAt = invalidatedAt;
    }

    @Override
    public String toString() {
        return "JwtBlacklist{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", invalidatedAt=" + invalidatedAt +
                '}';
    }
}
