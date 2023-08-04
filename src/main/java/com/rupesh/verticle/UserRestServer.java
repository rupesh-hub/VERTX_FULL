package com.rupesh.verticle;

import com.rupesh.handler.update.UpdateUserHandler;
import com.rupesh.handler.user.list.AllUserHandler;
import com.rupesh.handler.user.register.UserRegisterHandler;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import jakarta.inject.Singleton;

@Singleton
public class UserRestServer extends AbstractVerticle {


  private static final Logger LOGGER = LoggerFactory.getLogger(UserRestServer.class);
  private static final int HTTP_PORT = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));
  private final CassandraClient client;
  public UserRestServer(CassandraClient client){
    this.client = client;
  }

  @Override
  public void start(Promise<Void> promise) {
    vertx
      .createHttpServer()
      .requestHandler(routePaths())
      .listen(HTTP_PORT)
      .onSuccess(success -> {
        System.out.println("HTTP SERVER RUNNING ON: http://localhost:" + HTTP_PORT);
        promise.complete();
      })
      .onFailure(promise::fail);
  }

  public Router routePaths() {
    Router router = Router.router(vertx);

    router.post()
      .handler(BodyHandler.create());

    router
      .route("/users/*")
      .subRouter(userRestApi());

    return router;
  }

  public Router userRestApi() {
    Router router = Router.router(vertx);

    router
      .get("/all")
      .handler(new AllUserHandler(client));

    router
      .post("/register")
      .handler(new UserRegisterHandler(client));

    router
      .post("/update")
      .handler(new UpdateUserHandler(client));

    router
      .post("/delete/{id}")
      .handler(new UpdateUserHandler(client));

    return router;
  }

}
