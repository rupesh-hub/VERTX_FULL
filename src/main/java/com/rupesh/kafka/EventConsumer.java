package com.rupesh.kafka;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.logging.Logger;

public class EventConsumer<K, V> {

  private static final Logger LOGGER = Logger.getLogger(EventProducer.class.getName());

  public static <K, V> Future<V> consume(final String topic, KafkaConsumer<K, V> consumer) {
    var promise = Promise.<V>promise();

    consumer.handler(
      record -> {
          System.out.println("MESSAGE : "+record.value());
          promise.complete(record.value());
      }
    );

    consumer
      .subscribe(topic)
      .onSuccess(success -> {
        LOGGER.info("consumer topic " + topic + " ! subscribed success!!!");
      })
      .onFailure(
        failure -> {
          LOGGER.warning(failure.getMessage());
        }
      );

    return promise.future();
  }

}
