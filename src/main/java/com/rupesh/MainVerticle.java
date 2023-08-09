package com.rupesh;

import com.rupesh.configuration.CassandraClientProvider;
import com.rupesh.configuration.CustomRetrieverConfiguration;
import com.rupesh.verticle.UserRestServer;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

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
    vertx.deployVerticle(new MainVerticle());

  }

}
