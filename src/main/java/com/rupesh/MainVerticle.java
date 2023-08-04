package com.rupesh;

import com.rupesh.configuration.CassandraClientProvider;
import com.rupesh.configuration.CustomRetrieverConfiguration;
import com.rupesh.mqtt.MqttClientConfiguration;
import com.rupesh.mqtt.MqttServerConfiguration;
import com.rupesh.verticle.UserRestServer;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {

    final CassandraClient client = CassandraClientProvider.getConnection(vertx);

    CustomRetrieverConfiguration.init(vertx).onSuccess((success) -> {

      MqttClientConfiguration.produceMessage(vertx, "test",new String("Hello"));
      MqttServerConfiguration.start(vertx);

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
