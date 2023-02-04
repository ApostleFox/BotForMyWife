package com.apostleFox.demoFoxBot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public class User {
    private Menu menu = new Menu();

    public Menu getMenu() {
        return menu;
    }

    private Map<String, Integer> items = new HashMap<>();

    public void deleteM(){
        items.clear();
        this.items = items;
    }
    private String chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private Long registeredAt;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Long registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void clearItems(){
        items.clear();
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}