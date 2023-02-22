package com.ruslanburduzhan.weatherbot.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Telegrambot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.key}")
    private String token;
    @Value("${bot.landlord}")
    private String landlord;
}
