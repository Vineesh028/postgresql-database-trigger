package com.trigger.postgres.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trigger.postgres.listener.NotificationHandler;
import com.trigger.postgres.listener.NotificationListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotificationsListenerConfiguration {
    private final NotificationListener notificationListener;
    @Bean
    CommandLineRunner startListener(NotificationHandler handler) {
        return (args) -> {
            log.info("Listening for new notifications in the queue...");
            notificationListener.listenForNotifications(handler);
        };
    }
}