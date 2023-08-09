package com.rupesh.kafka;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import jakarta.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class EventProducer<K, V> {
  private static final Logger LOGGER = Logger.getLogger(EventProducer.class.getName());

  public static <K,V> Future<String> publish(final V message, final String topic, KafkaProducer<K, V> producer) {
    var promise = Promise.<String>promise();

    LOGGER.info("<<<<< message : " + message + " >>>>>");

    KafkaProducerRecord<K, V> result = KafkaProducerRecord.create(topic, null, message);

    producer.send(result)
      .onSuccess(record -> {
        promise.complete("Record sent::>> " + result.value() +
          " ::destination::>> " + record.getTopic() +
          " ::partition::> " + record.getPartition() +
          " ::offset::>> " + record.getOffset());
      })
      .onFailure(throwable -> promise.fail("Failed to produce message::>>" + throwable.getMessage()));

    LOGGER.info("<<<<< after message sent : " + result + " >>>>>");
    return promise.future();
  }
}
