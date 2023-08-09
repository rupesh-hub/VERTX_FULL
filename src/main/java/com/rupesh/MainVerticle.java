package com.rupesh;

import com.rupesh.configuration.CassandraClientProvider;
import com.rupesh.configuration.CustomRetrieverConfiguration;
import com.rupesh.kafka.EventConsumer;
import com.rupesh.kafka.EventProducer;
import com.rupesh.kafka.KafkaConnectionProvider;
import com.rupesh.verticle.UserRestServer;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {

    final CassandraClient client = CassandraClientProvider.getConnection(vertx);

    CustomRetrieverConfiguration.init(vertx).onSuccess((success) -> {

      CassandraClientProvider
        .pingCassandraConnection(client)
        .onSuccess(suc -> {
          vertx.deployVerticle(new UserRestServer(client))
            .onFailure((error) -> {
              LOGGER.error(error.getMessage());
            });
        }).onFailure(throwable -> {
          LOGGER.info(throwable.getMessage());
        });

      startPromise.complete();
    }).onFailure((throwable) -> {
      LOGGER.error(throwable.getMessage());
    });

  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
//    vertx.deployVerticle(new MainVerticle());
    produce(vertx);
    consume(vertx);
  }

  private static <T> void produce(final Vertx vertx) {

    KafkaProducer<String, JsonObject> producer = KafkaConnectionProvider.getProducer(vertx,
      StringSerializer.class,
      JsonObjectSerializer.class);

    JsonArray users = new JsonArray()
      .add(new JsonObject()
        .put("first_name", "John")
        .put("last_name", "Doe")
        .put("email", "john.doe@example.com"))
      .add(new JsonObject()
        .put("first_name", "Jane")
        .put("last_name", "Smith")
        .put("email", "jane.smith@example.com"))
      .add(new JsonObject()
        .put("first_name", "Michael")
        .put("last_name", "Johnson")
        .put("email", "michael.johnson@example.com"));

    JsonObject message = new JsonObject().put("data", users.encode());
    EventProducer.publish(message, "my-topic", producer)
      .onSuccess(success -> {
        LOGGER.info(success);
      })
      .onFailure(failure -> LOGGER.error(failure.getMessage()));

    producer
      .close()
      .onSuccess(v -> System.out.println("Producer is now closed"))
      .onFailure(cause -> System.out.println("Close failed: " + cause));
  }

  private static <T> void consume(final Vertx vertx) {
    final KafkaConsumer<String, String> consumer = KafkaConnectionProvider.getConsumer(vertx,
      StringDeserializer.class,
      StringDeserializer.class);

    EventConsumer.consume("my-topic", consumer)
      .onSuccess(message -> parse(message))
      .onFailure(failure -> LOGGER.error(failure.getMessage()));
  }

  private static void parse(final String message){

    JsonObject jsonObject = new JsonObject(message);
    JsonArray dataArray = new JsonArray(jsonObject.getString("data"));

    for (Object object : dataArray) {
      JsonObject dataObject = (JsonObject) object;
      String firstName = dataObject.getString("first_name");
      String lastName = dataObject.getString("last_name");
      String email = dataObject.getString("email");

      System.out.println("First Name: " + firstName);
      System.out.println("Last Name: " + lastName);
      System.out.println("Email: " + email);
      System.out.println();
    }

  }


}
