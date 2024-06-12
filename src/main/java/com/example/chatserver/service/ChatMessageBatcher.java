package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageBatcher {

    private final Lock lock = new ReentrantLock();
    private final ChatService chatService;
    private final RedisTemplate<String, ChatDto> redisTemplate;
    private final String QUEUE_KEY = "chat_message_queue";

    public void addMessage(ChatDto message) {
        lock.lock();
        try{
            redisTemplate.opsForList().leftPush(QUEUE_KEY, message);
        }finally {
            lock.unlock();
        }
    }

    @Scheduled(fixedRate = 5000)
    private void saveMessages() {
        List<ChatDto> messageToSave;
        lock.lock();
        log.info("scheduling save messages");
        try {
            if(!hasMessages()){
                return;
            }
            messageToSave = getAllMessages();
            chatService.saveMessages(messageToSave);
            log.info("save messages");
            removeMessages();
        }finally {
            lock.unlock();
        }
        chatService.saveMessages(messageToSave);
    }

    private boolean hasMessages() {
        List<ChatDto> range = redisTemplate.opsForList().range(QUEUE_KEY, 0, 0);
        return range != null && !range.isEmpty();
    }

    private List<ChatDto> getAllMessages() {
        return redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);
    }

    private void removeMessages() {
        redisTemplate.delete(QUEUE_KEY); // 모든 메시지 삭제
    }
}
