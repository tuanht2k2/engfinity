package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findAllByMessengerIdOrderByCreatedAtAsc(String messengerId);
}
