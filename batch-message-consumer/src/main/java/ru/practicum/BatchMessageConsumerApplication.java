package ru.practicum;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.practicum.model.Message;
import ru.practicum.serialization.JsonDeserializer;
import ru.practicum.utils.Utils;

import java.util.Properties;

public class BatchMessageConsumerApplication {
	private static final Properties PROPERTIES;

	static {
		PROPERTIES = new Properties();
		PROPERTIES.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-0:9092,kafka-broker-1:9092,kafka-broker-2:9092"); // Адреса брокеров Kafka
		PROPERTIES.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Десериализатор ключа
		PROPERTIES.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName()); // Десериализатор значения
		PROPERTIES.put(ConsumerConfig.GROUP_ID_CONFIG, "batch-group"); // Идентификатор группы потребителей
		PROPERTIES.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // Авто-коммит отключен
		PROPERTIES.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "6000"); // Ждёт минимум 6000 байт
		PROPERTIES.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "65000"); // Ждёт максимум 65000 мс
	}

	public static void main(String[] args) {
		try (Consumer<String, Message> consumer = new KafkaConsumer<>(PROPERTIES)) {
			Utils.getMessages(consumer, true);
		}
	}

}
