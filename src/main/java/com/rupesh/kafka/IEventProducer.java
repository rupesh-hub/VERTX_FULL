package com.rupesh.kafka;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface IEventProducer {
  Future<String> eventPublish(final JsonObject message);

}
