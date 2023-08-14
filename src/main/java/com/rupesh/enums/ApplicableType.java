package com.rupesh.enums;

import java.util.Arrays;

public enum ApplicableType {

  HIGHER("higher"),
  LOWER("lower"),
  COMBINATION("combination"),
  NULL("null");

  private String code;

  ApplicableType(String code) {
    this.code = code;
  }

  public static ApplicableType byCode(final String code) {
    if (code == null) return ApplicableType.NULL;

    return Arrays.stream(ApplicableType.values())
      .filter(type -> code.equalsIgnoreCase(type.code))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("No matching ApplicableType found for code: " + code));
  }


}
