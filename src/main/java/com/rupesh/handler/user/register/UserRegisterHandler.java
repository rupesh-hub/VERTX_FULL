package com.rupesh.handler.user.register;

import com.rupesh.factory.InstanceFactory;
import com.rupesh.payload.User;
import com.rupesh.repository.UserRepository;
import com.rupesh.repository.UserRepositoryImpl;
import com.rupesh.util.GlobalResponseUtil;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class UserRegisterHandler implements Handler<RoutingContext> {

  private final UserRepository userRepository;
  private final CassandraClient client;

  @Inject
  public UserRegisterHandler(CassandraClient client) {
    this.userRepository = InstanceFactory.getInstance(UserRepositoryImpl.class);
    this.client = client;
  }

  @Override
  public void handle(RoutingContext context) {
    final User user = context.body().asPojo(User.class);
    final Future<User> usersFuture = userRepository.save(
      User
        .builder(user)
        .id(UUID.randomUUID().toString())
        .build(), client);
    usersFuture
      .onSuccess(response -> GlobalResponseUtil.createResponse(context, usersFuture))
      .onFailure(error -> GlobalResponseUtil.failureResponse(context, error));
  }

}
