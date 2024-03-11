package com.trigger.postgres.listener;

import java.util.function.Consumer;

import org.postgresql.PGNotification;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationHandler implements Consumer<PGNotification>{


    @Override
    @Transactional
    public void accept(PGNotification pgNotification) {
        log.info("Notification with id: [{}] received :", pgNotification.getParameter());
        Long notificationId = getNotificationIdFromPgNotificationParameter(pgNotification.getParameter());

        if (notificationId == null) {
            return;
        }


    }

    private Long getNotificationIdFromPgNotificationParameter(String pgNotificationParameter) {
        try {
            return Long.parseLong(pgNotificationParameter);
        } catch (Exception exception) {
            log.error("Error occurred while parsing notification id from pgNotificationParameter : [{}]", pgNotificationParameter, exception);
            return null;
        }
    }
}