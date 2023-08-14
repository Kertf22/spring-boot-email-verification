package com.example.springemailverification.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String verificationCode;
    private boolean enabled=false;

    public User() {
    }

    public User(String name, String email, String password, String verificationCode, boolean enabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
    }

    public User(String name, String email, String password, String verificationCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.enabled = false;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = false;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.enabled = false;
    }

    public User(String name) {
        this.name = name;
        this.enabled = false;
    }

    public User(String name, String email, boolean enabled) {
        this.name = name;
        this.email = email;
        this.enabled = enabled;
    }

    public User(String name, String email, String password, boolean enabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPassword() {
        return password;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
}


}
