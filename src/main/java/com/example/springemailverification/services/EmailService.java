package com.example.springemailverification.services;

public interface EmailService {

    void sendSimpleMessage(String name, String to, String token);
    void sendMimeMessageWithAttchaments(String name, String to, String token);
    void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
    void sendHTMLEmail(String name, String to, String token);

    void sendHTMLEmailWithEmbeddedFiles(String name, String to, String token);

}
