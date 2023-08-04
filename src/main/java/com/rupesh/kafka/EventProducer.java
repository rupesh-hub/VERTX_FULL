package com.rupesh.kafka;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import jakarta.inject.Inject;

import java.util.logging.Logger;

public class EventProducer implements IEventProducer {

  private final KafkaProducer<String, JsonObject> producer;
  private final Logger logger = Logger.getLogger(EventProducer.class.getName());

  @Inject
  public EventProducer(KafkaProducer<String, JsonObject> producer) {
    this.producer = producer;
  }

  @Override
  public Future<String> testEvent(JsonObject message) {
    var promise = Promise.<String>promise();

    message = message == null ? new JsonObject().put("key", "empty_message") : message;

    KafkaProducerRecord<String, JsonObject> recordAfterVoidSuccess =
      KafkaProducerRecord
        .create(BrokerTopic.TEST.getCode(),null, message);
    producer.
      send(recordAfterVoidSuccess)
      .onSuccess(recordMetadata -> promise
        .complete("Record sent::>>" + recordAfterVoidSuccess.value() + "::destination::>> "
          + recordMetadata.getTopic() + "::partition::>" + recordMetadata.getPartition() +
          "::offset::>>" + recordMetadata.getOffset()))
      .onFailure(throwable -> promise.fail("Failed to produce message::>>" + throwable.getMessage()));
    return promise.future();
  }


}
