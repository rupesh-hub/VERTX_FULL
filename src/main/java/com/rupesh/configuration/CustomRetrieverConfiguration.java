package com.rupesh.configuration;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class CustomRetrieverConfiguration {

  public static Future<Void> init(Vertx vertx) {
    final Promise<Void> promise = Promise.<Void>promise();
    final ConfigStoreOptions env = new ConfigStoreOptions().setType("env");
    final ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
      .setScanPeriod(5000)
      .addStore(env);
    final ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
    configRetriever.getConfig(config -> {
      if (config.succeeded()) promise.complete();
      else promise.fail(config.cause());
    });
    return promise.future();
  }

}
