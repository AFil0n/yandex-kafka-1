package ru.practicum.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.model.Message;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class Utils {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();
    private static final String TOPIC_NAME = "project-1-topic";


    public static Message generateMessage() {
        Message message = Message.builder()
                .id(generateRandomID())
                .text(generateRandomString())
                .title(generateRandomString())
                .build();
        return message;
    }

    public static String generateRandomString() {
        int length = random.nextInt(100);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }

    public static Long generateRandomID() {
        return random.nextLong(999999L);
    }

    public static void publishingMessages(Producer<String, Message> producer) {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 900) + 100); // Ждем от 100 мс до 1000 мс перед публикацией следующего сообщения
            } catch (InterruptedException e) {
                log.error("Ошибка при публикации сообщения (InterruptedException)", e);
                continue; // Прерываем цикл, если поток был прерван
            }

            var value = generateMessage();

            log.info("Публикуем сообщение: {}", value);

            ProducerRecord<String, Message> record = new ProducerRecord<>(
                    TOPIC_NAME, UUID.randomUUID().toString(), value);

            try {
                producer.send(record);
            } catch (Exception e) {
                log.error("Ошибка при публикации сообщения", e);
            }
        }
    }

    public static void getMessages(Consumer<String, Message> consumer, boolean isBatch) {
        subscribe(consumer); // Подписываемся на топик

        while (true) {
            ConsumerRecords<String, Message> records;

            try {
                records = consumer.poll(Duration.ofMillis(1_000L)); // Получаем сообщения из топика (может вернуть пустые данные, если нет готовых сообщений)
            } catch (Exception e) {
                log.error("Ошибка при получении сообщений: {}", e.getMessage());
                continue; // Пропускаем итерацию, если произошла ошибка
            }

            if (!records.isEmpty() && records.count() > 0) {
                log.info("Получено {} сообщений", records.count());

                if (isBatch) {
                    for (var record : records) {
                        log.info("Получено сообщение: {}", record);
                    }

                    try {
                        consumer.commitSync(); // Коммитим оффсет после обработки всей пачки
                    } catch (Exception e) {
                        log.error("Ошибка при коммите оффсета: {}", e.getMessage());
                    }
                } else {
                    for (ConsumerRecord<String, Message> record : records) {
                        log.info("Получено сообщение: {}", record.value());
                    }
                }

            }
        }
    }

    private static void subscribe(Consumer<String, Message> consumer) {
        consumer.subscribe(List.of(TOPIC_NAME));
    }
}
