package com.ruslanburduzhan.weatherbot.repository;

import com.ruslanburduzhan.weatherbot.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
