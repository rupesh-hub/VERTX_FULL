package com.rupesh;

import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
class TestMainVerticle {

  Calculator underTest = new Calculator();

  @Test
  void itShouldCalculate() {
    //given
    int num1 = 20;
    int num2 = 40;

    //when
    int result = underTest.add(num1, num2);

    //then
    int expected = 60;
    assertThat(result).isEqualTo(expected);
  }


  static class Calculator {

    public int add(int a, int b) {
      return a + b;
    }

  }

}
