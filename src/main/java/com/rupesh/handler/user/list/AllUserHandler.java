package com.rupesh.handler.user.list;

import com.rupesh.factory.InstanceFactory;
import com.rupesh.payload.User;
import com.rupesh.repository.UserRepository;
import com.rupesh.repository.UserRepositoryImpl;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AllUserHandler implements Handler<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllUserHandler.class);
  private final UserRepository userRepository;
  private final CassandraClient client;

  @Inject
  public AllUserHandler(CassandraClient client) {
    this.client = client;
    this.userRepository = InstanceFactory.getInstance(UserRepositoryImpl.class);
  }

  @Override
  public void handle(RoutingContext context) {
    Future<List<User>> future = userRepository.findAll(client);

    future
      .onSuccess((success) -> {
        context
          .response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject()
            .put("data", future.result())
            .toBuffer()
          );
      })
      .onFailure(error -> LOGGER.error(error.getMessage()));

  }

}
