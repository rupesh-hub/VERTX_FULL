package com.rupesh.mqtt;

import static com.rupesh.constants.CustomConstants.MQTT_DEFAULT_PORT;
import static com.rupesh.constants.CustomConstants.MQTT_DEFAULT_HOST;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;

import java.nio.charset.StandardCharsets;

public class MqttClientConfiguration<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MqttClientConfiguration.class);

  public static void produceMessage(final Vertx vertx, final String topic, final String data) {
    final MqttClient client = MqttClient.create(vertx);

    client.connect(MQTT_DEFAULT_PORT,
      MQTT_DEFAULT_HOST,
      ch -> {
        if (ch.succeeded()) {
          LOGGER.info("CONNECTED TO MQTT SERVER !!!");

          client.publish(topic,
            Buffer.buffer(data),
            AT_MOST_ONCE,
            false,
            false,
            s -> client.disconnect(d -> LOGGER.info("DISCONNECTED FROM MQTT SERVER !!!.")));
        } else {
          LOGGER.error("FAIL TO CONNECT TO MQTT SERVER !!! " + ch.cause());
        }
      });
  }

  private static <T> byte[] serializeData(final T data) {
    return JsonObject
      .mapFrom(data)
      .encode()
      .getBytes(StandardCharsets.UTF_8);
  }

}

