package com.ruslanburduzhan.weatherbot.model;

import com.ruslanburduzhan.weatherbot.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherbotRepository extends JpaRepository<User,Integer> {
    Optional<User> findByChatId(long chatId);
}
