package com.example.springemailverification.util;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\n" +
                "Please click the link below to verify your email address:\n\n"+getVerificationUrl(host, token)+"\n"+
                "Thanks,\n" +
                "Your Company Name Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/user/confirm?token=" + token;
    }
}
