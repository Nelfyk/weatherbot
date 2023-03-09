package com.ruslanburduzhan.weatherbot.service;

import com.ruslanburduzhan.weatherbot.entity.Telegrambot;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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
        botCommandList.add(new BotCommand("/start", "Начало работы"));
        botCommandList.add(new BotCommand("/menu", "Показать меню"));
        try {
            execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createMenuMap() {
        menuMap.put("weather", "Получить погоду \uD83C\uDF26");
        menuMap.put("myInfo", "Информация о пользователе \uD83D\uDC40");
        menuMap.put("myHistory", "Иcтория запросов \uD83C\uDF29️");
        menuMap.put("settings", "Настройки ⚙️");
        menuMap.put("deleteRequests", "Очистить историю запросов \uD83D\uDDD1️");
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
                startMsg(msg);
                queue.clear();
            } else if (msgText.equals("/menu")) {
                showMenu(chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("weather"))) {
                queue.add("/weather");
                getWeather(chatId);
            } else if (msgText.equals(menuMap.get("myInfo"))) {
                sendInfo(chatId, chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("myHistory"))) {
                sendHistory(chatId, chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("settings"))) {
                showSettings(chatId);
                queue.clear();
            } else if (msgText.equals(menuMap.get("aboutBot"))) {
                sendAboutBot(chatId);
                queue.clear();
            } else if (Objects.equals(queue.peek(), "/weather")) {
                sendWeatherMsg(chatId, msgText);
                queue.clear();
            } else if (msgText.equals(menuMap.get("deleteRequests"))) {
                acceptDeleteRequests(chatId);
            } else if (msgText.equals(menuMap.get("back"))) {
                showMenu(chatId);
            }
            msgCounterIncr(chatId);
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callBackData.equals("Ссылка github репозиторий")) {
                sendMessage(chatId, "https://github.com/Nelfyk/weatherbot");
            } else if (callBackData.startsWith("btn_")) {
                EditMessageText msg = new EditMessageText();
                msg.setChatId(chatId);
                msg.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                msg.setText("Удалить историю запросов?");
                try {
                    execute(msg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (callBackData.equals("btn_yes"))
                    deleteRequests(chatId);
            } else
                sendWeatherMsg(chatId, callBackData);
            queue.clear();
        }
    }

    private void saveUserRequestHistory(long chatId, String request) {
        if (requestRepository.findByCityAndChatId(request, chatId).isEmpty())
            requestRepository.save(new Request(chatId, request, new Timestamp(System.currentTimeMillis())));
        else {
            var req = requestRepository.findByCityAndChatId(request, chatId).get();
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
        sendMessage(chatId, "История запросов удалена ✔️");
    }

    private void acceptDeleteRequests(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Удалить историю запросов?");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var buttonYes = new InlineKeyboardButton();
        var buttonNo = new InlineKeyboardButton();
        buttonYes.setText("Да ✔️");
        buttonYes.setCallbackData("btn_yes");
        rowInLine.add(buttonYes);
        buttonNo.setText("Нет ❌");
        buttonNo.setCallbackData("btn_no");
        rowInLine.add(buttonNo);
        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);
        send(message);
    }

    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.HTML);
        message.setChatId(chatId);
        message.setText(textToSend);
        send(message);
    }

    private void sendMessageWithKeyboardMarkup(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        keyboardMarkup.setResizeKeyboard(true);
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

    private void startMsg(Message msg) {
        registerUser(msg);
        String name;
        if (msg.getChat().getLastName() == null) name = msg.getChat().getFirstName();
        else name = msg.getChat().getFirstName() + " " + msg.getChat().getLastName();
        sendMessage(msg.getChatId(), "Привет " + name + "! \uD83D\uDE09\n" +
                "Я WeatherBot, выдаю погоду по запрошенному городу \uD83E\uDD16\n" +
                "Так же храню информацию по последним запросам \uD83D\uDC40\n" +
                "Все команды находятся в меню \"/menu\" \uD83D\uDC48\n" +
                "Приятного пользования и хорошего дня! \uD83D\uDE3A");
    }

    private void sendWeatherMsg(long chatId, String city) {
        Weather weather = weatherService.getWeatherApi(city);
        if (weather.getError() == null) {
            var current = weather.getCurrent();
            var location = weather.getLocation();
            sendMessage(chatId, location.getName() +
                    " - " + location.getCountry() + "\n" +
                    "<strong>\n" + location.getLocaltime().substring(11) + " - " + current.getCondition().getText() + "</strong>" +
                    "<strong>\n    " + current.getTempC() + " °C</strong>" +
                    "<strong>\nПо ощущениям:</strong>\n    " + current.getFeelslikeC() + " °C" +
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
        send(message);
    }

    private void sendInfo(long chatId, long purposeId) {
        User user = userRepository.findById(chatId).get();
        String name;
        if (user.getLastName() == null) name = user.getFirstName();
        else name = user.getFirstName() + user.getLastName();

        sendMessage(purposeId,
                "<strong>\nИмя:</strong> \n    " + name +
                        "<strong>\nНикнейм:</strong> \n    " + user.getUserName() +
                        "<strong>\nКол-во сообщений:</strong> \n    " + user.getMsgCounter() +
                        "<strong>\nЗарегистрирован:</strong> \n    " +
                        user.getRegisteredAt().toString().substring(0, 16));

    }

    private void sendHistory(long chatId, long purposeId) {
        List<Request> requestList = requestRepository.findLastRequests(chatId);
        if (!requestList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("<strong>Последние корректные запросы:</strong>\n");
            requestList.forEach(e -> stringBuilder.append("<strong>" + e.getCity() + "</strong> - " +
                    e.getCounter() + " раз(а)\n"));
            sendMessage(purposeId, stringBuilder.toString());
        } else {
            sendMessage(purposeId, "История запросов пуста \uD83E\uDD37\u200D♂️");
        }
    }

    private void sendAboutBot(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Backend разработан на java ☕\n Стек: \n- Spring Boot " +
                "\n- Spring Data\n- Maven\n- Api погоды - weatherapi.com \n- БД MySQL" +
                "\n- Docker\nРазработчик - Burdzuhan Ruslan \uD83D\uDC68\u200D\uD83D\uDCBB");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("Ссылка на репозиторий git");
        button.setCallbackData("Ссылка github репозиторий");
        rowInLine.add(button);
        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);
        send(message);
    }

    private void showMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add(menuMap.get("weather"));
        row.add(menuMap.get("aboutBot"));
        keyboardRowList.add(row);
        row2.add(menuMap.get("settings"));
        keyboardRowList.add(row2);
        keyboardMarkup.setKeyboard(keyboardRowList);
        sendMessageWithKeyboardMarkup(chatId, "Меню:", keyboardMarkup);
    }

    private void showSettings(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row.add(menuMap.get("myHistory"));
        row.add(menuMap.get("myInfo"));
        keyboardRowList.add(row);
        row2.add(menuMap.get("deleteRequests"));
        keyboardRowList.add(row2);
        row3.add(menuMap.get("back"));
        keyboardRowList.add(row3);
        keyboardMarkup.setKeyboard(keyboardRowList);
        sendMessageWithKeyboardMarkup(chatId, "Настройки:", keyboardMarkup);
    }
}
