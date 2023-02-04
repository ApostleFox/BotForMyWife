package com.apostleFox.demoFoxBot.model;

import org.jvnet.hk2.annotations.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public User getUser(String telegramId){
        return users.get(telegramId);
    }

    public User save(User user){
        users.put(user.getChatId(), user);
        return user;
    }

}