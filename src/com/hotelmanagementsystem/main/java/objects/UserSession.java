package com.hotelmanagementsystem.main.java.objects;

public final class UserSession {

    private static UserSession instance;

    private String email;

    private UserSession(String email) {
        this.email = email;
    }

    public static UserSession getInstace(String email) {
        if(instance == null) {
            instance = new UserSession(email);
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }


    public void cleanUserSession() {
        email = "";// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "email='" + email + '\'' +
                "}";
    }
}