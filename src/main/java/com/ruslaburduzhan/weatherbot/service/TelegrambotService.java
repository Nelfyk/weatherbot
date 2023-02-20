package com.ruslaburduzhan.weatherbot.service;

import com.ruslaburduzhan.weatherbot.entity.Telegrambot;
import com.ruslaburduzhan.weatherbot.entity.Weather;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

@Service
public class TelegrambotService extends TelegramLongPollingBot {

    Telegrambot telegrambot;
    WeatherService weatherService;
    Stack<String> commands;

    public TelegrambotService(Telegrambot telegrambot, WeatherService weatherService) {
        this.telegrambot = telegrambot;
        this.weatherService = weatherService;
        commands = new Stack<>();
        createCommandList();
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
                menu(chatId, msg);
            } else if (msgText.equals("/weather") || msgText.equals("Получить погоду")) {
                commands.push("/weather");
                sendMessage(chatId, "Введите название города");
            } else if (Objects.equals(commands.peek(), "/weather")) {
                sendWeatherMsg(msg);
            }
        }
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

    private void sendWeatherMsg(Message msg) {
        String msgText = msg.getText();
        Weather weather = weatherService.getWeatherApi(msgText);
        if (weather.getError() == null) {
            var current = weather.getCurrent();
            var location = weather.getLocation();
            sendMessage(msg.getChatId(), location.getName()  +
                    " - " + location.getRegion() +
                    " - " + location.getCountry() + "\n" +
                    "\n*темп.: " + current.getTempC() + " °C" +
                    "\nдата: " + location.getLocaltime().substring(0,10) +
                    "\nвремя: " + location.getLocaltime().substring(11) + "*");
        } else sendMessage(msg.getChatId(), "Некорректное название города");
    }

    private void menu(long chatId, Message msg) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Получить погоду");
        row.add("Обо мне");
        keyboardRowList.add(row);
        keyboardMarkup.setKeyboard(keyboardRowList);

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
