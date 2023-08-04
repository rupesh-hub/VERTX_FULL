package com.rupesh.payload;

public class User {
  private String id;
  private String name;
  private String email;
  private String phone;

  public User(){}

  public User(User user) {
    this.id = user.id;
    this.name = user.name;
    this.email = user.email;
    this.phone = user.phone;
  }

  private User(UserBuilder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.email = builder.email;
    this.phone = builder.phone;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone(){
    return phone;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  public static UserBuilder builder(User user) {
    return new UserBuilder(user);
  }

  public static class UserBuilder {
    private String id;
    private String name;
    private String email;
    private String phone;

    private UserBuilder() {}

    private UserBuilder(User user) {
      this.id = user.id;
      this.name = user.name;
      this.email = user.email;
      this.phone = user.phone;
    }

    public UserBuilder id(String id) {
      this.id = id;
      return this;
    }

    public UserBuilder name(String name) {
      this.name = name;
      return this;
    }

    public UserBuilder email(String email) {
      this.email = email;
      return this;
    }

    public UserBuilder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", email='" + email + '\'' +
      ", phone='" + phone + '\'' +
      '}';
  }
}
