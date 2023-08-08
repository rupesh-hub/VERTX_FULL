package com.rupesh.test;

import java.util.Arrays;

public class DiscountCalculator {

  enum ApplicableType {
    HIGHER("higher"),
    LOWER("lower"),
    COMBINATION("combination");

    private String code;

    ApplicableType(String code) {
      this.code = code;
    }

    public static ApplicableType byCode(final String code) {
      return Arrays.stream(ApplicableType.values()).filter(type -> code.equalsIgnoreCase(type.code)).findFirst().get();
    }
  }

  enum DiscountType {
    PERCENTAGE("percentage"),
    FLAT("flat"),
    BOTH("both");

    private String code;

    DiscountType(String code) {
      this.code = code;
    }

    public static DiscountType byCode(final String code) {
      return Arrays.stream(DiscountType.values()).filter(i -> code.equalsIgnoreCase(i.code)).findFirst().get();
    }

  }

  public double calculate(final double amount, final double percentage, final double flatDiscount, final String discountType, final String applicableType) {

    final DiscountType type = DiscountType.byCode(discountType);

    switch (type) {
      case PERCENTAGE:
        return percentageDiscount(amount, percentage);
      case FLAT:
        return amount - flatDiscount;
      case BOTH: {
        return combinedDiscount(amount, percentage, flatDiscount, ApplicableType.byCode(applicableType));
      }
      default:
        return amount;
    }

  }

  private double percentageDiscount(double amount, double percentage) {
    return amount - (percentage * amount) / 100;
  }

  private double combinedDiscount(final double amount, final double percentage, final double flatDiscount, final ApplicableType condition) {
    var byPercentage = (percentage * amount) / 100;
    var byFlat = flatDiscount;
    var byBoth = byPercentage + byFlat;

    switch (condition) {
      case HIGHER -> {
        return (byPercentage >= byFlat) ? amount - byPercentage : amount - byFlat;
      }
      case LOWER -> {
        return (byPercentage < byFlat) ? amount - byPercentage : amount - byFlat;
      }
      case COMBINATION -> {
        return amount - byBoth;
      }
      default -> {
        return amount;
      }
    }
  }

}
