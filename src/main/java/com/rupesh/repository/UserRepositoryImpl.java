package com.rupesh.repository;

import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.rupesh.payload.User;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserRepositoryImpl implements UserRepository {

  private static final String SELECT_ALL_USER_QUERY = "SELECT * FROM USER";
  private static final String INSERT_USER_QUERY = "INSERT INTO user(id, name, email) VALUES(:id, :name, :email)";

  private static final String UPDATE_USER_QUERY = "UPDATE user SET name = :name, email = :email WHERE id = :id";

  @Override
  public Future<User> save(final User user, final CassandraClient client) {
    var promise = Promise.<User>promise();

    client.prepare(INSERT_USER_QUERY, preparedStatement -> {
      if (preparedStatement.succeeded()) {
        BoundStatementBuilder builder = preparedStatement.result().boundStatementBuilder();
        builder
          .setString("id", user.getId())
          .setString("name", user.getName())
          .setString("email", user.getEmail());

        client.execute(builder.build(), result -> {
          if (result.succeeded()) promise.complete(prepareResponse(user));
          else promise.fail(result.cause().getMessage());
        });
      } else promise.fail(preparedStatement.cause().getMessage());
    });

    return promise.future();
  }

  @Override
  public Future<String> update(final User user, CassandraClient client) {
    var promise = Promise.<String>promise();
    client.prepare(UPDATE_USER_QUERY, preparedStatement -> {
      if (preparedStatement.succeeded()) {
        BoundStatementBuilder statementBuilder = preparedStatement.result().boundStatementBuilder();

        client.execute(
          statementBuilder
            .setString("id", user.getId())
            .setString("name", user.getName())
            .setString("email", user.getEmail())
            .build(),
          resultSet -> {
            if (resultSet.succeeded()) promise.complete(user.getId());
            else promise.fail("Failed to update payment record");
          });
      } else promise.fail(preparedStatement.cause());
    });
    return promise.future();
  }

  @Override
  public Future<List<User>> findAll(final CassandraClient client) {
    var promise = Promise.<List<User>>promise();
    client.prepare(SELECT_ALL_USER_QUERY, preparedStatementResult -> {
      if (preparedStatementResult.succeeded()) {
        PreparedStatement preparedStatement = preparedStatementResult.result();
        client.execute(preparedStatement.bind(), done -> {
          if (done.succeeded()) promise.complete(prepareUserList(done.result().all().result()));
          else promise.fail(done.cause());
        });
      } else promise.fail(preparedStatementResult.cause());
    });
    return promise.future();
  }

  private List<User> prepareUserList(List<Row> rows) {
    List<User> users = new ArrayList<>();
    rows.forEach(row -> {
      users.add(User
        .builder()
        .id(row.getString("id"))
        .name(row.getString("name"))
        .email(row.getString("email"))
        .build());
    });
    return users;
  }

  private User prepareResponse(final User user) {
    return User
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .build();
  }

}
