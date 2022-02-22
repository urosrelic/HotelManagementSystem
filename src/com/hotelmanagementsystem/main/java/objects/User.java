package com.hotelmanagementsystem.main.java.objects;

public class User {
    private int id;
    private String name, email, password, security_question, answer, address;
    private int statusCode;

    public User(int id, String name, String email, String password, String security_question, String answer, String address, int statusCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.security_question = security_question;
        this.answer = answer;
        this.address = address;
        this.statusCode = statusCode;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurity_question() {
        return security_question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAddress() {
        return address;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", security_question='" + security_question + '\'' +
                ", answer='" + answer + '\'' +
                ", address='" + address + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
