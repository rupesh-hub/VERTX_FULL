package com.rupesh.mqtt;

import static com.rupesh.constants.CustomConstants.MQTT_DEFAULT_PORT;
import static com.rupesh.constants.CustomConstants.MQTT_DEFAULT_HOST;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_LEAST_ONCE;
import static io.netty.handler.codec.mqtt.MqttQoS.EXACTLY_ONCE;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.ArrayList;
import java.util.List;

public class MqttServerConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqttServerConfiguration.class);

  public static void consumeMessage(final Vertx vertx, final String topic) {

    final MqttServerOptions options =
      new MqttServerOptions()
        .setPort(MQTT_DEFAULT_PORT)
        .setHost(MQTT_DEFAULT_HOST);

    final MqttServer server = MqttServer.create(vertx, options);

    server.endpointHandler(mqttEndpoint -> {
      LOGGER.info("MQTT CONNECTION CLIENT " + mqttEndpoint.clientIdentifier());

      mqttEndpoint.subscribeHandler(subscribe -> {
        for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
          LOGGER.info("SUBSCRIBED TO TOPIC [" + s.topicName() + "] WITH QOS [" + s.qualityOfService() + "]");
        }
        mqttEndpoint.accept(false);
      });

      mqttEndpoint.publishHandler(message -> {
        LOGGER.info("MESSAGE RECEIVE ON [" + message.topicName() + "] PAYLOAD [" + message.payload() + "] WITH QOS [" + message.qosLevel() + "]");
      });

      // Here, we add a subscription for the specified topic.
      // mqttEndpoint.subscribe(topic, AT_LEAST_ONCE.value());

      mqttEndpoint.accept(false);
    });

    server.listen(ar -> {
      if (ar.succeeded())
        LOGGER.info("MQTT SERVER STARTED LISTENING ON PORT " + server.actualPort());
      else
        LOGGER.error("MQTT SERVER ERROR " + ar.cause().getMessage());
    });

  }

  public static void start(final Vertx vertx) {

    final MqttServer mqttServer = MqttServer.create(vertx);

    mqttServer
      .endpointHandler(endpoint -> {
        LOGGER.info("MQTT CLIENT [" + endpoint.clientIdentifier() + "] REQUEST TO CONNECT, CLEAN SESSION = " + endpoint.isCleanSession());

        if (endpoint.auth() != null)
          LOGGER.info("[USERNAME = " + endpoint.auth().getUsername() + ", PASSWORD = " + endpoint.auth().getPassword() + "]");

        if (endpoint.will() != null) {
          LOGGER.info("[will flag = " + endpoint.will().isWillFlag() + " topic = " + endpoint.will().getWillTopic() + " msg = " + endpoint.will().getWillMessage() +
            " QoS = " + endpoint.will().getWillQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
        }
        LOGGER.info("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

        // accept connection from the remote client
        endpoint.accept(false);

        // handling requests for subscriptions
        endpoint.subscribeHandler(subscribe -> {

          List<MqttQoS> grantedQosLevels = new ArrayList<>();
          for (final MqttTopicSubscription s : subscribe.topicSubscriptions()) {
            LOGGER.info("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
            grantedQosLevels.add(s.qualityOfService());
          }
          // ack the subscriptions request
          endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

          // just as example, publish a message on the first topic with requested QoS
          endpoint
            .publish(subscribe.topicSubscriptions().get(0).topicName(),
              Buffer.buffer("Hello from the Vert.x MQTT server"),
              subscribe.topicSubscriptions().get(0).qualityOfService(),
              false,
              false);

          // specifying handlers for handling QoS 1 and 2
          endpoint
            .publishAcknowledgeHandler(messageId -> {
              LOGGER.info("Received ack for message = " + messageId);
            })
            .publishReceivedHandler(endpoint::publishRelease).publishCompletionHandler(messageId -> {
              LOGGER.info("Received ack for message = " + messageId);
            });
        });

        // handling requests for unsubscriptions
        endpoint.unsubscribeHandler(unsubscribe -> {

          for (String t : unsubscribe.topics()) {
            LOGGER.info("Un-subscription for " + t);
          }
          endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
        });

        // handling ping from client
        endpoint.pingHandler(v -> {
          LOGGER.info("Ping received from client");
        });

        // handling disconnect message
        endpoint.disconnectHandler(v -> {
          LOGGER.info("Received disconnect from client");
        });

        // handling closing connection
        endpoint.closeHandler(v -> {
          LOGGER.info("Connection closed");
        });

        // handling incoming published messages
        endpoint.publishHandler(message -> {
          LOGGER.info("Just received message on [" + message.topicName() + "] payload [" + message.payload() + "] with QoS [" + message.qosLevel() + "]");
          if (message.qosLevel() == AT_LEAST_ONCE) endpoint.publishAcknowledge(message.messageId());
          else if (message.qosLevel() == EXACTLY_ONCE) endpoint.publishReceived(message.messageId());
        }).publishReleaseHandler(endpoint::publishComplete);
      })
      .listen(ar -> {
        if (ar.succeeded())
          LOGGER.info("MQTT SERVER STARTED LISTENING ON PORT " + MQTT_DEFAULT_PORT);
        else
          LOGGER.error("MQTT SERVER ERROR " + ar.cause().getMessage());
      });
  }


}
