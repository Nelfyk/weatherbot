package com.ruslanburduzhan.weatherbot.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity(name = "Users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
    private int msgCounter;
    @OneToOne(cascade = CascadeType.ALL)
    private Request request;

    public User() {
    }

    public User(long chatId, String firstName, String lastName, String userName, Timestamp registeredAt, int msgCounter) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.registeredAt = registeredAt;
        this.msgCounter = msgCounter;
    }
}
