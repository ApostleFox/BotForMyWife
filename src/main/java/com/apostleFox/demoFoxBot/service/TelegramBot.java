package com.apostleFox.demoFoxBot.service;

import com.apostleFox.demoFoxBot.config.BotConfig;
import com.apostleFox.demoFoxBot.model.User;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;
    final BotConfig config;

    static final String HELP_TEXT = "This bot is created for Kirill so that He could find out how many pieces of pizza each had";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start","get a welcome message"));
        listOfCommands.add(new BotCommand("/Свічки","get your data a store"));
        listOfCommands.add(new BotCommand("/Бомбочки", "delete your store"));
        listOfCommands.add(new BotCommand("/Акції", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/Про нас", "set your preferences"));
        listOfCommands.add(new BotCommand("/help", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            log.error("Error setting bot`s command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if(messageText.contains("/send")){
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
//                var users = userRepository.findAll();
//                for (User user : users){
//                    prepareAndSendMessage(user.getChatId(), textToSend);
//                }
            }

            String messageTwo = messageText;
            String[] words = messageTwo.split(" ");
            String command = words[0];
            Integer arg = null;
            int length = words.length;
            boolean isInt = false;
            try {
                arg = Integer.parseInt(words[length -1]);
                isInt = true;
                for(int i = 1; i <words.length-1; i++){
                    words[0] = words[0] + " " + words[i];
                }
                command = words[0];
            } catch (Exception e){
                isInt = false;
                for(int i = 1; i <words.length; i++){
                    words[0] = words[0] + " " + words[i];
                }
                command = words[0];
            }


            User user = getUser(update);
            Boolean isMenuPassed = false;
            switch (user.getMenu().getActiveMenuType()){
                case "proportion":

                        if (arg == null){
                            break;
                        }

                        user.getMenu().setNumber(arg);
                        final var result = user.getMenu().buildMenuResult();
                        user.getItems().put(user.getMenu().itemMenuResult(), arg);
                        prepareAndSendMessage(user.getChatId(), "You buy " + result);
                        isMenuPassed = true;
                        break;





            }
            if(isMenuPassed){
                return;
            }
            switch (command){

                case "/start":
//                      registerUser(update.getMessage());
                        sendMessage(String.valueOf(chatId), messageText);
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        user.getMenu().clearMenu();
                        break;

                case "/help":
                    prepareAndSendMessage(chatId, HELP_TEXT);
                    break;

                case "Свічки":
                    sendMessageCandles(chatId, messageText);
                    break;

                case "Тверді шампуні":
                    user.getMenu().setCategory("shampoo");
                    sendMessageSolidShampoo(chatId, messageText);
                    break;

                case "Для ослабленого волосся":
                    user.getMenu().setType("weak");
                    sendMessageWeakenedHair(chatId, messageText);
                    break;

                case "Для ослабленого волосся 60 грам":
                    user.getMenu().setProportion("60");
                    sendMessageWeakenedHair60(chatId, messageText);
                    break;


                case  "Твердий шампунь 60г для осл. вол.":
                    if (arg == null){
                        break;
                    }

                    user.getItems().put("Твердий шампунь 60г для осл. вол.", arg);
                    prepareAndSendMessage(user.getChatId(), "You buy " + arg);
                    //sendMessage(user.getChatId(), String.valueOf(user.getItems()));
                    break;


                case "Бомбочки":
                    sendMessageBombs(chatId, messageText);
                    break;

                case "Лісова Свічка":

                    if (arg == null){
                        //sendMessageTT(chatId, messageText);
                        break;
                    }

                    user.getItems().put("Лісова Свічка", arg);
                    prepareAndSendMessage(user.getChatId(), "You buy " + arg);
                    //sendMessage(user.getChatId(), String.valueOf(user.getItems()));
                    break;

                case "Дерево Свічі":

                    user.getItems().put("Дерево Свічі", arg);
                    prepareAndSendMessage(user.getChatId(), "You buy " + arg);
                    sendMessage(user.getChatId(), String.valueOf(user.getItems()));
                    break;



                case "Ароматезовані акції":
                    tabTab(chatId, messageText);
                    break;

                case "Акції для ванної":
                    register(chatId);
                    break;

                case "Стерти":
                    user.clearItems();
                    break;

                case "Назад":
                    sendMessage(String.valueOf(chatId), messageText);
                    break;
                default:

                        prepareAndSendMessage(chatId, "Sorry Bro");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if(callbackData.equals("YES_BUTTON")){
                String text = "Ти обрав купувати в нас";
                executeEditMessageText(text, chatId, messageId);

            } else if (callbackData.equals("NO_BUTTON")) {
                String text = "Ти вибрав не вірний варіант";
                executeEditMessageText(text, chatId, messageId);
                
            }
        }
    }

    private User getUser(Update update) {
        User userSS = userService.getUser(update.getMessage());
        return userSS;
    }

    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Ти дійсно бажаєш купити щось?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");

        var noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    private void startCommandReceived(long chatId, String name)  {

        String answer = EmojiParser.parseToUnicode("Hi, " + name + " nice to meet you!" + " :blush:");
        //log.info("Replied to user" + name);

        prepareAndSendMessage(chatId, answer);
    }

    private void sendMessage(String chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Свічки");
        row.add("Тверді шампуні");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Бомбочки");
        row.add("Про нас");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void sendMessageSolidShampoo(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Для ослабленого волосся");
        row.add("Для сухого та фарбованого волосся");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Для нормального волосся");
        row.add("Для жирного волосся");

        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Відновлюючий");
        row.add("Назад");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void sendMessageWeakenedHair(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Для ослабленого волосся 60 грам");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Для ослабленого волосся 100 грам");


        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Назад");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void sendMessageWeakenedHair60(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("1");
        row.add("2");
        row.add("3");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("4");
        row.add("5");
        row.add("6");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("7");
        row.add("8");
        row.add("9");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Назад");
        row.add("Завершити замовлення");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }
    private void sendMessageCandles(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Свічка номер один");
        row.add("Свічка номер два");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Свічка номер три");
        row.add("Свічка номер чотири");

        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Свічка номер п'ять");
        row.add("Назад");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int)messageId);
        try {
            execute(message);
        }
        catch (TelegramApiException e) {
            //log.error("Error occurred " + e.getMessage());
        }
    }

    private void sendMessageBombs(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Бомбочки для ванної один");
        row.add("Бомбочки для ванної два");
        row.add("Бомбочки для ванної три");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Бомбочки для ванної чотири");
        row.add("Бомбочки для ванної п'ять");
        row.add("Бомбочки для ванної шість");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        }
        catch (TelegramApiException e) {
            //log.error("Error occurred " + e.getMessage());
        }
    }

    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }
    private void prepareAndSendMessage(String chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }
        private void tabTab(long chatId, String textToSend) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(textToSend);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();


            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();

            row.add("1");
            row.add("2");
            row.add("3");

            keyboardRows.add(row);

            row = new KeyboardRow();

            row.add("4");
            row.add("5");
            row.add("6");

            keyboardRows.add(row);

            row = new KeyboardRow();

            row.add("7");
            row.add("8");
            row.add("9");

            keyboardRows.add(row);

            row = new KeyboardRow();

            row.add("Назад");
            row.add("10");
            row.add("Вперед");

            keyboardRows.add(row);

            keyboardMarkup.setKeyboard(keyboardRows);

            message.setReplyMarkup(keyboardMarkup);

            executeMessage(message);
        }



}




