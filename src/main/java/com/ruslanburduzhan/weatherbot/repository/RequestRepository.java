package com.ruslanburduzhan.weatherbot.repository;

import com.ruslanburduzhan.weatherbot.entity.mysql.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query(value = "select * from requests where chat_id = :chatId order by request_time desc limit 10", nativeQuery = true)
    List<Request> findLastRequests(long chatId);

    Optional<Request> findByCity(String city);

    List<Request> getAllByChatId(long chatId);

    void deleteById(int id);
}
