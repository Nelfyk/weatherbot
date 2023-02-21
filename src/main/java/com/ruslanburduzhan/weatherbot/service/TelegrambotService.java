package com.ruslanburduzhan.weatherbot.service;

import com.ruslanburduzhan.weatherbot.entity.api.Telegrambot;
import com.ruslanburduzhan.weatherbot.entity.mysql.User;
import com.ruslanburduzhan.weatherbot.entity.api.Weather;
import com.ruslanburduzhan.weatherbot.model.WeatherbotRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TelegrambotService extends TelegramLongPollingBot {

    private final Telegrambot telegrambot;
    private final WeatherService weatherService;
    private final WeatherbotRepository repository;
    private Queue<String> queue;

    public TelegrambotService(Telegrambot telegrambot, WeatherService weatherService,WeatherbotRepository repository) {
        this.telegrambot = telegrambot;
        this.weatherService = weatherService;
        this.repository = repository;
        queue = new ArrayDeque<>();
        createCommandList();
    }

    private void createCommandList() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "показать меню"));
        botCommandList.add(new BotCommand("/weather", "Получить погоду по введённому городу"));
        try {
            execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return telegrambot.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegrambot.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();
            long chatId = msg.getChatId();
            String msgText = msg.getText();
            if (msgText.equals("/start")) {
                registerUser(msg);
                showMenu(chatId, msg);
                queue.clear();
            } else if (msgText.equals("/weather") || msgText.equals("Получить погоду")) {
                queue.add("/weather");
                sendMessage(chatId, "Введите название города");
            } else if (Objects.equals(queue.peek(), "/weather")) {
                sendWeatherMsg(msg);
            }
            msgCounterIncr(chatId);
        }
    }

    private void registerUser(Message msg) {
        if (repository.findByChatId(msg.getChatId()).isEmpty()) {
            var chat = msg.getChat();
            repository.save(
                    new User(msg.getChatId(), chat.getFirstName(), chat.getLastName(),
                            chat.getUserName(), new Timestamp(System.currentTimeMillis()),
                            1));
        }
    }

    private void msgCounterIncr(long chatId){
        User user = repository.findByChatId(chatId).get();
        user.setMsgCounter(user.getMsgCounter()+1);
        repository.save(user);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.MARKDOWN);
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWeatherMsg(Message msg) {
        String msgText = msg.getText();
        Weather weather = weatherService.getWeatherApi(msgText);
        if (weather.getError() == null) {
            var current = weather.getCurrent();
            var location = weather.getLocation();
            sendMessage(msg.getChatId(), location.getName() +
                    " - " + location.getRegion() +
                    " - " + location.getCountry() + "\n" +
                    "\n*темп.: " + current.getTempC() + " °C" +
                    "\nдата: " + location.getLocaltime().substring(0, 10) +
                    "\nвремя: " + location.getLocaltime().substring(11) + "*");
        } else sendMessage(msg.getChatId(), "Некорректное название города");
    }

    private void showMenu(long chatId, Message msg) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Получить погоду");
        row.add("Обо мне");
        keyboardRowList.add(row);
        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        SendMessage message = new SendMessage();

        message.setReplyMarkup(keyboardMarkup);

        message.setChatId(chatId);
        message.setText("Меню:");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
