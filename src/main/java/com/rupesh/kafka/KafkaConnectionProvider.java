package com.rupesh.kafka;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConnectionProvider<T, U> {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConnectionProvider.class);

  public static <T, U> KafkaProducer<T, U> getConnection(final Vertx vertx, Class<? extends Serializer<U>> valueSerializer) {
    Map<String, String> configMap = new HashMap<>();
    configMap.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configMap.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configMap.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());
    configMap.put(ACKS_CONFIG, "1");
    return KafkaProducer.create(vertx, configMap);
  }

}
