package com.rupesh.repository;

import com.rupesh.payload.User;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;

import java.util.List;

public interface UserRepository {

  Future<User> save(final User user, final CassandraClient client);
  Future<List<User>> findAll(final CassandraClient client);
  Future<String> update(final User user, final CassandraClient client);

}
