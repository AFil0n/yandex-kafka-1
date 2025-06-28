package ru.practicum;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.practicum.model.Message;
import ru.practicum.serialization.JsonSerializer;
import ru.practicum.utils.Utils;

import java.util.Properties;

public class ProducerApplication {

	private static final Properties PROPERTIES;

	static {

		PROPERTIES = new Properties();
		PROPERTIES.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-0:9092,kafka-broker-1:9092,kafka-broker-2:9092"); // Адреса брокеров Kafka
		PROPERTIES.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Сериализатор ключа
		PROPERTIES.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName()); // Сериализатор значения
		PROPERTIES.put(ProducerConfig.ACKS_CONFIG, "all"); // Ждем подтверждения от всех реплик
		PROPERTIES.put(ProducerConfig.RETRIES_CONFIG, 3); // Количество попыток отправки сообщения в случае ошибки
	}

	public static void main(String[] args) {
		try (Producer<String, Message> producer = new KafkaProducer<>(PROPERTIES)) {
			Utils.publishingMessages(producer);
		}
	}

}
