package com.rupesh;

import com.rupesh.configuration.CassandraClientProvider;
import com.rupesh.configuration.CustomRetrieverConfiguration;
import com.rupesh.factory.InstanceFactory;
import com.rupesh.handler.user.list.AllUserHandler;
import com.rupesh.kafka.EventProducer;
import com.rupesh.kafka.IEventProducer;
import com.rupesh.kafka.KafkaConnectionProvider;
import com.rupesh.mqtt.MqttClientConfiguration;
import com.rupesh.mqtt.MqttServerConfiguration;
import com.rupesh.verticle.UserRestServer;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {

    final CassandraClient client = CassandraClientProvider.getConnection(vertx);

    CustomRetrieverConfiguration.init(vertx).onSuccess((success) -> {

      new EventProducer(KafkaConnectionProvider.getConnection(vertx, JsonObjectSerializer.class))
        .eventPublish(new JsonObject().put("message", "THIS IS TEST MESSAGE"))
        .onSuccess(eventSuccess -> {
          System.out.println("ON EVENT SUCCESS : " + eventSuccess);
        })
        .onFailure(error -> {
          System.out.println(error.getMessage());
        });

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

//  public static void main(String[] args) {
//    Vertx vertx = Vertx.vertx();
//    vertx.deployVerticle(new MainVerticle());
//  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Map<String, String> configMap = new HashMap<>();
    configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    KafkaProducer<String, String> producer = KafkaProducer.create(vertx, configMap);

    for (int i = 0; i < 5; i++) {
      producer.write(KafkaProducerRecord.create("test", "message_" + i));
    }
    vertx.close();
    producer.close();
  }

}
