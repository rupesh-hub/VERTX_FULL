package com.rupesh.enums;

import java.util.Arrays;

public enum DiscountType {

  PERCENTAGE("percentage"),
  FLAT("flat"),
  BOTH("both"),
  NULL("null");

  private String code;

  DiscountType(String code) {
    this.code = code;
  }

  public static DiscountType byCode(final String code) {
    if (code == null) return DiscountType.NULL;

    return Arrays.stream(DiscountType.values()).filter(i -> code.equalsIgnoreCase(i.code)).findFirst().get();
  }

}
