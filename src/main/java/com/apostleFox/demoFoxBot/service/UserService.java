package com.apostleFox.demoFoxBot.service;

import com.apostleFox.demoFoxBot.model.User;
import com.apostleFox.demoFoxBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public User getUser(Message msg) {

        String chatId = String.valueOf(msg.getChatId());

        User user = repository.getUser(chatId);
        if (user == null) {
            var chat = msg.getChat();
            user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(System.currentTimeMillis() / 1000);

            repository.save(user);
        }
        return user;
    }
}
