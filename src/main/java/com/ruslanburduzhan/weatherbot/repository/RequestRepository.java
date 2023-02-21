package com.ruslanburduzhan.weatherbot.repository;

import com.ruslanburduzhan.weatherbot.entity.mysql.Request;
import com.ruslanburduzhan.weatherbot.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Integer> {
    @Query(value = "select* from requests where chat_id = :chatId" ,nativeQuery = true)
    Optional<Request> findByChatId(@Param("chatId") long chatId);
    @Query(value = "select* from requests where chat_id = :chatId LIMIT 20" ,nativeQuery = true)
    List<Request> findLastRequests(@Param("chatId") long chatId);
}
