package com.ruslaburduzhan.weatherbot.service;

import com.ruslaburduzhan.weatherbot.entity.Telegrambot;
import com.ruslaburduzhan.weatherbot.entity.Weather;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegrambotService extends TelegramLongPollingBot {

    Telegrambot telegrambot;
    WeatherService weatherService;

    public TelegrambotService(Telegrambot telegrambot, WeatherService weatherService) {
        this.telegrambot = telegrambot;
        this.weatherService = weatherService;
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
            System.out.println(msgText);
            if (msgText.startsWith("/weather ")) {
                sendWeatherMsg(msg);
            }
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
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
        botCommandList.add(new BotCommand("/start", "START"));
        try {
            execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWeatherMsg(Message msg) {
        String msgText = msg.getText();
        Weather weather = weatherService.getWeatherApi(msgText.substring(9));
        System.out.println(weather.getError());
        if (weather.getError() == null)
            sendMessage(msg.getChatId(), weather.toString());
        else sendMessage(msg.getChatId(), "Некорректное название города");
    }
}
