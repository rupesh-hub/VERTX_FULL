package com.rupesh.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

public class GlobalResponseUtil {

  private GlobalResponseUtil() {
    throw new AssertionError();
  }

  public static <T> void createResponse(RoutingContext context, Future<T> data) {
    context.response()
      .putHeader("Content-Type", "application/json")
      .setStatusCode(201)
      .setStatusMessage("CREATED/UPDATE")
      .end(new JsonObject()
        .put("data", data.result())
        .encode());
  }

  private static <T> void successResponse(RoutingContext context, Future<T> data) {
    context.response()
      .putHeader("Content-Type", "application/json")
      .setStatusCode(200)
      .setStatusMessage("SUCCESS")
      .end(new JsonObject()
        .put("data", data.result())
        .encode());
  }

  public static void failureResponse(RoutingContext context, Throwable error) {
    context
      .response()
      .setStatusCode(context.statusCode())
      .setStatusMessage(error.getMessage())
      .end(new JsonObject()
        .put("data", error.getMessage())
        .encode());
  }

}
