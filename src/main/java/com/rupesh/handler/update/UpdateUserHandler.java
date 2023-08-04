package com.rupesh.handler.update;

import com.rupesh.factory.InstanceFactory;
import com.rupesh.payload.User;
import com.rupesh.repository.UserRepository;
import com.rupesh.util.GlobalResponseUtil;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class UpdateUserHandler implements Handler<RoutingContext> {

  private final UserRepository userRepository;
  private final CassandraClient client;

  @Inject
  public UpdateUserHandler(CassandraClient client){
    this.client = client;
    this.userRepository = InstanceFactory.getInstance(UserRepository.class);
  }

  @Override
  public void handle(final RoutingContext context) {
    final User user = context.body().asPojo(User.class);
    Future<String> usersFuture = userRepository.update(user, client);
    usersFuture
      .onSuccess(response -> GlobalResponseUtil.createResponse(context, usersFuture))
      .onFailure(error -> GlobalResponseUtil.failureResponse(context, error));
  }

}
