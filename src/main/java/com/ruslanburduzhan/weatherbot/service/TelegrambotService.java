package com.ruslanburduzhan.weatherbot.service;

import com.ruslanburduzhan.weatherbot.entity.Telegrambot;
import com.ruslanburduzhan.weatherbot.entity.api.Location;
import com.ruslanburduzhan.weatherbot.entity.api.Weather;
import com.ruslanburduzhan.weatherbot.entity.mysql.Request;
import com.ruslanburduzhan.weatherbot.entity.mysql.User;
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
    private Map<String, String> menuMap = new HashMap<>();

    public TelegrambotService(Telegrambot telegrambot, WeatherService weatherService,
                              UserRepository userRepository, RequestRepository requestRepository) {
        this.telegrambot = telegrambot;
        this.weatherService = weatherService;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        queue = new ArrayDeque<>();
        createCommandList();
        createMenuMap();
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

    private void createMenuMap() {
        menuMap.put("weather", "Получить погоду \uD83C\uDF26");
        menuMap.put("myInfo", "Обо мне \uD83D\uDC40");
        menuMap.put("settings", "Настройки ⚙️");
        menuMap.put("deleteRequests", "Удалить мои запросы ❌");
        menuMap.put("aboutBot", "О боте \uD83E\uDD16");
        menuMap.put("back", "Назад ⬅️");
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

            } else if (msgText.equals(menuMap.get("weather"))) {
                queue.add("/weather");
                getWeather(chatId);
            } else if (msgText.equals(menuMap.get("myInfo"))) {
                sendInfo(chatId, chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("settings"))) {
                settings(chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("aboutBot"))) {
                sendAboutBot(chatId);
                queue.clear();
            }else if (Objects.equals(queue.peek(), "/weather")) {
                sendWeatherMsg(chatId, msgText);
                queue.clear();
            } else if (msgText.equals(menuMap.get("deleteRequests"))) {
                deleteRequests(chatId);
            } else if (msgText.equals(menuMap.get("back"))) {
                showMenu(chatId, msg);
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
        if (requestRepository.findByCity(request).isEmpty())
            requestRepository.save(new Request(chatId, request, new Timestamp(System.currentTimeMillis())));
        else {
            var req = requestRepository.findByCity(request).get();
            req.setRequestTime(new Timestamp(System.currentTimeMillis()));
            req.setCounter(req.getCounter() + 1);
            requestRepository.save(req);
        }
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

    private void deleteRequests(long chatId) {
        List<Request> requestList = requestRepository.getAllByChatId(chatId);
        requestList.forEach(e -> requestRepository.deleteById(e.getId()));
        sendMessage(chatId, "Ваша история запросов удалена.");
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

    private void sendMessageWithKeyboardMarkup(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        SendMessage message = new SendMessage();
        message.setReplyMarkup(keyboardMarkup);
        message.setChatId(chatId);
        message.setText(text);
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
//            sendMessage(chatId, location.getName() +
//                    " - " + location.getCountry() + "\n" +
//                    "<strong>\n\uD83C\uDF21</strong> " + current.getTempC() + " °C" +
//                    "<strong>\n:🤔</strong> " + current.getFeelslikeC() + " °C" +
//                    "<strong>\n\uD83D\uDCA7</strong> " + current.getHumidity() + "%" +
//                    "<strong>\n\uD83D\uDCA8</strong> " + current.getWindMps() + " м/c" +
//                    "<strong>\n\uD83D\uDCC5</strong> " + location.getLocaltime().substring(0, 10) +
//                    "<strong>\n⌚</strong> " + location.getLocaltime().substring(11));
            sendMessage(chatId, location.getName() +
                    " - " + location.getCountry() +
                    "<strong>\n" + location.getLocaltime().substring(11) + " - "  + current.getCondition().getText() + "</strong>" +
                    "<strong>\n    " + current.getTempC() + " °C</strong>\n" +
                    "<strong>\nПо ощущениям:</strong>\n    " + current.getFeelslikeC() + " °C" +
                    "<strong>\nВероятность осадков:</strong>\n    " + current.getHumidity() + "%" +
                    "<strong>\nВлажность:</strong>\n    " + current.getHumidity() + "%" +
                    "<strong>\nВетер:</strong>\n    " + current.getWindMps() + " м/c" +
                    "<strong>\nДата:</strong>\n    " + location.getLocaltime().substring(0, 10));
            saveUserRequestHistory(chatId, location.getName());
        } else sendMessage(chatId, "Некорректное название города");
    }

    private void getWeather(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Введите название города:");
        List<Request> requestList = requestRepository.findLastRequests(chatId);
        if (!requestList.isEmpty()) {
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

        sendMessage(purposeId,
                "<strong>\nName:</strong> \n    " + name +
                        "<strong>\nUser_name:</strong> \n    " + user.getUserName() +
                        "<strong>\nMsg_counter:</strong> \n    " + user.getMsgCounter() +
                        "<strong>\nChat-id:</strong> \n    " + user.getChatId() +
                        "<strong>\nRegistered_at:</strong> \n    " +
                        user.getRegisteredAt().toString().substring(0, 16));
        List<Request> requestList = requestRepository.findLastRequests(chatId);
        if (!requestList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("<strong>Последние корректные запросы:</strong>\n");
            requestList.forEach(e -> stringBuilder.append("<strong>" + e.getCity() + "</strong> - " +
                    e.getCounter() + " раз(а)\n"));
            sendMessage(purposeId, stringBuilder.toString());
        }
    }

    private void sendAboutBot(long chatId){
        sendMessage(chatId,"Backend разработан на java ☕\n Стек: \n- Spring Boot" +
                "\n- Spring Data\n- Maven\n- Api погоды - weatherapi.com \n- БД MySQL\n- Docker\nРазработчик - Burdzuhan Ruslan \uD83D\uDC68\u200D\uD83D\uDCBB");
    }

    private void showMenu(long chatId, Message msg) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add(menuMap.get("weather"));
        row.add(menuMap.get("myInfo"));
        keyboardRowList.add(row);
        row2.add(menuMap.get("settings"));
        keyboardRowList.add(row2);
        keyboardMarkup.setKeyboard(keyboardRowList);
        sendMessageWithKeyboardMarkup(chatId, "Меню:", keyboardMarkup);
    }

    private void settings(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add(menuMap.get("deleteRequests"));
        row.add(menuMap.get("aboutBot"));
        keyboardRowList.add(row);
        row2.add(menuMap.get("back"));
        keyboardRowList.add(row2);
        keyboardMarkup.setKeyboard(keyboardRowList);
        sendMessageWithKeyboardMarkup(chatId, "Настройки:", keyboardMarkup);
    }
}
