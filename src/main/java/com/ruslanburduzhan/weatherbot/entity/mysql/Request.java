package com.ruslanburduzhan.weatherbot.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity(name = "requests")
@Data
public class Request {
    private long chatId;
    @Id
    private String city;
    private Timestamp requestTime;

    public Request() {
    }

    public Request(long chatId, String city, Timestamp requestTime) {
        this.chatId = chatId;
        this.city = city;
        this.requestTime = requestTime;
    }
}
