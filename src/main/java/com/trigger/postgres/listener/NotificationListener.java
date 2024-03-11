package com.trigger.postgres.listener;

import java.sql.Connection;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {
    @Value("${notificationListenerTimeout:10000}")
    private int listenerTimeout;

    private static final String NOTIFICATIONS_CHANNEL = "notifications";
    private final DataSource dataSource;
    
    @Async
    public void listenForNotifications(Consumer<PGNotification> consumer) {
        while (true) {
            try (Connection c = dataSource.getConnection()) {
                PGConnection pgconn = c.unwrap(PGConnection.class);
                c.createStatement().execute("LISTEN " + NOTIFICATIONS_CHANNEL);
                log.info("Connection established: Listening for notifications on channel: [{}]", NOTIFICATIONS_CHANNEL);
                log.info("Notified about hanging notifications in the queue...");
               
                while (true) {
                    PGNotification[] nts = pgconn.getNotifications(listenerTimeout);
                    if (nts == null) {
                        continue;
                    }
                    for (PGNotification nt : nts) {
                        consumer.accept(nt);
                    }
                }
            } catch (Exception e) {
                log.warn("Error occurred while listening for notifications, attempting to reconnect...", e);
            }
        }
    }
}