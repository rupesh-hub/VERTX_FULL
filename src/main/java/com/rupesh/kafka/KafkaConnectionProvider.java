package com.rupesh.kafka;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaConnectionProvider<T, U> {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConnectionProvider.class);
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String ACK = "1";
  private static final String GROUP = "kafka-group";

  public static <T, U> KafkaProducer<T, U> getProducer(final Vertx vertx,
                                                       Class<? extends Serializer<T>> keySerializer,
                                                       Class<? extends Serializer<U>> valueSerializer) {
    Map<String, String> config = new HashMap<>();
    config.put(BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    config.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer.getName());
    config.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());
    config.put(ACKS_CONFIG, ACK);

    return KafkaProducer.create(vertx, config);
  }

  public static <T, U> KafkaConsumer<T, U> getConsumer(final Vertx vertx,
                                                       Class<? extends Deserializer<T>> keyDSerializer,
                                                       Class<? extends Deserializer<U>> valueDSerializer) {
    Map<String, String> config = new HashMap<>();
    config.put(BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    config.put("key.deserializer", keyDSerializer.getName());
    config.put("value.deserializer", valueDSerializer.getName());
    config.put("auto.offset.reset", "latest");
    config.put("group.id", GROUP);

    return KafkaConsumer.create(vertx, config);
  }
}
