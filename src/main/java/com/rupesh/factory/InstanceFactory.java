package com.rupesh.factory;

import io.micronaut.context.BeanContext;
public class InstanceFactory<T> {

  public static <T> T getInstance(Class<T> clazz) {
    return BeanContext
      .run()
      .createBean(clazz);
  }

}
