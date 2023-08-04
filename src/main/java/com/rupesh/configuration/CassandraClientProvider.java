package com.rupesh.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.tracing.TracingPolicy;
import jakarta.inject.Singleton;

@Singleton
public class CassandraClientProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraClientProvider.class);

  public static CassandraClient getConnection(final Vertx vertx) {
    LOGGER.info("CASSANDRA CLIENT CONNECTING...");

    return CassandraClient.create(vertx,
      new CassandraClientOptions(CqlSession.builder().withLocalDatacenter("datacenter1"))
        .addContactPoint("127.0.0.1", 9042)
        .setKeyspace("user")
        .setTracingPolicy(TracingPolicy.ALWAYS));
  }

  public static Future<String> pingCassandraConnection(final CassandraClient client) {
    Promise<String> promise = Promise.promise();

    client
      .execute("SELECT * FROM test LIMIT 1", (event) -> {
        if (event.succeeded()) {
          LOGGER.info("CASSANDRA CONNECTION SUCCESS");
          promise.complete(event.result().one().getFormattedContents());
        } else {
          LOGGER.error("CANNOT PING TO CASSANDRA CONNECTION");
          promise.fail(event.cause().getMessage());
        }
      });

    return promise.future();
  }

}
