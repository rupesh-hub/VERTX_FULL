package com.rupesh.kafka;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class EventProducer implements IEventProducer {

  private final KafkaProducer<String, JsonObject> producer;
  private static final Logger LOGGER = Logger.getLogger(EventProducer.class.getName());

  @Inject
  public EventProducer(KafkaProducer<String, JsonObject> producer) {
    this.producer = producer;
  }

  @Override
  public Future<String> eventPublish(final JsonObject message) {
    var promise = Promise.<String>promise();

    LOGGER.info("<<<<< message : " + message + " >>>>>");

    KafkaProducerRecord<String, JsonObject> result =
      KafkaProducerRecord
        .create("test", null, message);

    producer.
      send(result)
      .onSuccess(recordMetadata -> {
        promise
          .complete("Record sent::>> " + result.value() +
            " ::destination::>> " + recordMetadata.getTopic() +
            " ::partition::> " + recordMetadata.getPartition() +
            " ::offset::>> " + recordMetadata.getOffset());
      })
      .onFailure(throwable -> promise.fail("Failed to produce message::>>" + throwable.getMessage()));

    LOGGER.info("<<<<< after message sent : " + result + " >>>>>");
    return promise.future();
  }

}
