package com.rupesh.kafka;

public enum BrokerTopic {
  TEST("test");
  private final String code;

  BrokerTopic(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
