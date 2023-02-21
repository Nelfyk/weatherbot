package com.ruslanburduzhan.weatherbot.service;

import com.ruslanburduzhan.weatherbot.entity.api.Telegrambot;
import com.ruslanburduzhan.weatherbot.entity.mysql.Request;
import com.ruslanburduzhan.weatherbot.entity.mysql.User;
import com.ruslanburduzhan.weatherbot.entity.api.Weather;
import com.ruslanburduzhan.weatherbot.repository.RequestRepository;
import com.ruslanburduzhan.weatherbot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TelegrambotService extends TelegramLongPollingBot {

    private final Telegrambot telegrambot;
    private final WeatherService weatherService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private Queue<String> queue;

    public TelegrambotService(Telegrambot telegrambot, WeatherService weatherService,
                              UserRepository userRepository, RequestRepository requestRepository) {
        this.telegrambot = telegrambot;
        this.weatherService = weatherService;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        queue = new ArrayDeque<>();
        createCommandList();
    }

    private void createCommandList() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "показать меню"));
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
            } else if (msgText.equals("Получить погоду")) {
                queue.add("/weather");
                getWeather(chatId);
            } else if (Objects.equals(queue.peek(), "/weather")) {
                sendWeatherMsg(chatId, msgText);
                queue.clear();
            } else if (msgText.equals("Обо мне")) {
                sendInfo(chatId, chatId);
            }
            msgCounterIncr(chatId);
        } else if (update.hasCallbackQuery() && !queue.isEmpty()) {
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            sendWeatherMsg(chatId, callBackData);
            queue.clear();
        }
    }

    private void saveUserRequestHistory(long chatId, String request) {
        requestRepository.save(new Request(chatId, request, new Timestamp(System.currentTimeMillis())));
    }

    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chat = msg.getChat();
            userRepository.save(
                    new User(msg.getChatId(), chat.getFirstName(), chat.getLastName(),
                            chat.getUserName(), new Timestamp(System.currentTimeMillis()),
                            1));
        }
    }

    private void msgCounterIncr(long chatId) {
        User user = userRepository.findById(chatId).get();
        user.setMsgCounter(user.getMsgCounter() + 1);
        userRepository.save(user);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.HTML);
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWeatherMsg(long chatId, String city) {
        Weather weather = weatherService.getWeatherApi(city);
        if (weather.getError() == null) {
            var current = weather.getCurrent();
            var location = weather.getLocation();
            sendMessage(chatId, location.getName() +
//                    " - " + location.getRegion() +
                    " - " + location.getCountry() + "\n" +
                    "<strong>\nтемп.:</strong> " + current.getTempC() + " °C" +
                    "<strong>\nдата:</strong> " + location.getLocaltime().substring(0, 10) +
                    "<strong>\nвремя:</strong> " + location.getLocaltime().substring(11));
            saveUserRequestHistory(chatId, city);
        } else sendMessage(chatId, "Некорректное название города");
    }

    private void getWeather(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Введите название города:");
        List<Request> requestList = requestRepository.findLastRequests(chatId);
        System.out.println(requestList);
        if (requestList.size() > 0) {
            message.setText("Введите название города\nили выберите из списка:");
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
            for (int i = 0; i < requestList.size() && i < 5; i++) {
                var button = new InlineKeyboardButton();
                button.setText(requestList.get(i).getCity());
                button.setCallbackData(requestList.get(i).getCity());
                if (i > 2)
                    rowInLine2.add(button);
                else
                    rowInLine.add(button);
            }
            rowsInLine.add(rowInLine);
            rowsInLine.add(rowInLine2);
            markup.setKeyboard(rowsInLine);
            message.setReplyMarkup(markup);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendInfo(long chatId, long purposeId) {
        User user = userRepository.findById(chatId).get();
        String name;
        if (user.getLastName() == null) name = user.getFirstName();
        else name = user.getFirstName() + user.getLastName();

        sendMessage(purposeId, "<strong>Chat-id:</strong> \n    " + user.getChatId() +
                "<strong>\nName:</strong> \n    " + name +
                "<strong>\nRegistered_at:</strong> \n    " + user.getRegisteredAt().toString().substring(0,16) +
                "<strong>\nUser_name:</strong> \n    " + user.getUserName() +
                "<strong>\nMsg_counter:</strong> \n    " + user.getMsgCounter());
        List<Request> requestList = requestRepository.findLastRequests(chatId);
        if(requestList.size()>0){
            StringBuilder stringBuilder = new StringBuilder("<strong>Последние корректные запросы:</strong>\n");
            requestList.forEach(e-> stringBuilder.append(e.getCity()+"\n"));
            sendMessage(purposeId,stringBuilder.toString());
        }
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
